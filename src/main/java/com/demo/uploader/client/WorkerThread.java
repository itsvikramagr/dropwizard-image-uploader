package com.demo.uploader.client;

import java.awt.Stroke;
import java.io.File;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


import com.demo.uploader.core.ProcessedPhoto;
import com.demo.uploader.dao.ProcessedPhotoDao;
import com.demo.uploader.resource.PhotoResource;


public class WorkerThread implements Runnable {  

  private final Logger LOG = LoggerFactory.getLogger(getClass());
  private AwsS3Client storageClient;
  private ProcessedPhoto unProcessedImage;
  private final ProcessedPhotoDao processedPhotoDao;
  
  public WorkerThread(AwsS3Client storageClient, ProcessedPhoto p, ProcessedPhotoDao processedPhotoDao){  
    this.unProcessedImage=p;  
    this.storageClient = storageClient;
    this.processedPhotoDao = processedPhotoDao;
  }  
  
  public void run() {  
    String location = unProcessedImage.getImageLocalLocation();
    String modifiedImageLocation = location + "_" + "processed";
    LOG.info("Processing image with id " + unProcessedImage.getPhotoId().toString());
    if (storageClient.preProcessImage(location, modifiedImageLocation)) {
      String original_image_uri = storageClient.upLoadImage(location);
      if (original_image_uri == null) {
        return;
      }        
      String processed_image_uri = storageClient.upLoadImage(modifiedImageLocation);
      if (processed_image_uri != null){
        return;
      }
      processedPhotoDao.updateProcessedPhoto(unProcessedImage.getId(), original_image_uri, processed_image_uri,new Timestamp(DateTime.now(DateTimeZone.UTC).getMillis()));
    }
   
  }  
  
}  