package com.demo.uploader.service;

import java.util.List;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.Period;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.demo.uploader.client.AwsS3Client;
import com.demo.uploader.client.StorageClientInterface;
import com.demo.uploader.client.WorkerThread;
import com.demo.uploader.core.ProcessedPhoto;
import com.demo.uploader.dao.ProcessedPhotoDao;

public class ImageStorageService {

  public static final String NAME = "ImageStorageService";

  private final Logger LOG = LoggerFactory.getLogger(getClass());
  private final DBI jdbi;
  private final ProcessedPhotoDao processedPhotoDao;
  private Timer timer = new Timer("ImageStorageService", true);
  public static final int IMAGE_STORAGE_RUN_INTERVAL = 1000*60;
  public static final int INITIAL_DELAY = 0;
  public static final int LIMIT_VALUE = 10;
  private AwsS3Client storageClient;
  private String hostName = "localhost";

  public ImageStorageService(DBI jdbi, AwsS3Client awsClient, String hostName) {
    super();
    this.jdbi = jdbi;
    this.processedPhotoDao = jdbi.onDemand(ProcessedPhotoDao.class);
    this.storageClient = awsClient;
    this.hostName = hostName;
  }

  public static String currentDateTimeStr() {
    DateTime currentTime = DateTime.now(DateTimeZone.UTC);
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    return fmt.print(currentTime);
  }

  public void init() {
    scheduleImageStorageTask();
  }

  private void scheduleImageStorageTask() {

    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        uploadImages();
      }
    }, INITIAL_DELAY, IMAGE_STORAGE_RUN_INTERVAL);
  }

  // TODO: use a message queue. everytime image is uploaded, an message is added in the queue
  // workers listening to this queue should read the event and process it
  public void uploadImages() {
    String dtStr = ImageStorageService.currentDateTimeStr();
    LOG.info("Running " + this.getName());
    List<ProcessedPhoto> unProcessedPhoto = processedPhotoDao.getListOfUnProcessedPhoto(hostName, LIMIT_VALUE);
    LOG.info("count of unProcessedPhoto " + unProcessedPhoto.size());     
    if (unProcessedPhoto.size() > 0) {
      ExecutorService executor = Executors.newFixedThreadPool(unProcessedPhoto.size());
      for (int i = 0; i < unProcessedPhoto.size(); i++) {
        Runnable worker = new WorkerThread(storageClient, unProcessedPhoto.get(i), processedPhotoDao);  
        executor.execute(worker);
      }
      executor.shutdown();  
      while (!executor.isTerminated()) {   }
    }
  }

  public String getName() {
    return NAME;
  }

}
