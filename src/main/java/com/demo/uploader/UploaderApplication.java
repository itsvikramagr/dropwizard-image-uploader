package com.demo.uploader;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.jdbi.DBIFactory;
import org.skife.jdbi.v2.DBI;

import com.demo.uploader.UploaderRootConfiguration;
import com.demo.uploader.resource.PhotoResource;
import com.demo.uploader.service.ImageStorageService;
import com.demo.uploader.client.AwsS3Client;
import com.demo.uploader.core.Photo;
import com.demo.uploader.dao.PhotoDao;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class UploaderApplication extends Application<UploaderRootConfiguration> {
  public static final String APPLICATION_NAME = "image-uploader";
  private ImageStorageService iss;

  public static void main(String[] args) throws Exception {
    new UploaderApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<UploaderRootConfiguration> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/assets", "/ui", "index.html"));
  }

  private void setUp(UploaderRootConfiguration configuration,
      Environment environment) throws ClassNotFoundException {
    DataSourceFactory databaseConf = configuration.getDataSourceFactory();
    DBIFactory factory = new DBIFactory();
    DBI jdbi = factory.build(environment, databaseConf, "database");   
    String hostName;
    try {
      hostName = java.net.InetAddress.getLocalHost().toString();
    } catch (Exception e) {
      //TODO - may be exit on exception
      hostName = "localhost";
    }
    hostName = hostName.split("/")[0].toLowerCase();
    environment.jersey().register(new PhotoResource(jdbi, hostName));
    // image storage service is a cron for now to pre-process and upload images to appropriate storage service
    // TODO: use a message queue. everytime image is uploaded, an message is added in the queue
    // workers listening to this queue should read the event and process it
    
    AWSCredentials awsCredentials = new BasicAWSCredentials(configuration.getAwsConfiguration().getAwsAccessId(), 
        configuration.getAwsConfiguration().getAwsSecretKey());
    AwsS3Client s3Client = new AwsS3Client(awsCredentials, configuration.getAwsConfiguration().getBucket(), 
        configuration.getAwsConfiguration().getPrefix());
 
    iss = new ImageStorageService(jdbi, s3Client, hostName);
    iss.init();  
        
  }
  
  @Override
  public void run(UploaderRootConfiguration configuration, Environment environment) throws Exception{
    setUp(configuration, environment);
  }
}