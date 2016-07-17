package com.demo.uploader.client;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.demo.uploader.resource.PhotoResource;


public class AwsS3Client implements StorageClientInterface{

  private static final Logger LOG = LoggerFactory.getLogger(AwsS3Client.class);
  private AmazonS3Client s3Client;
  private final String bucket;
  private final String prefix;
  
  public AwsS3Client(AWSCredentials creds, String bucket, String prefix ) {
    this.s3Client = new AmazonS3Client(creds);
    this.bucket = bucket;
    this.prefix = prefix;    
  }

  @Override
  public Boolean preProcessImage(String originalFileLocation, String ProcessedImageLocation) {
    String localLocation = PhotoResource.uploadDir + File.separator + originalFileLocation;
    String modifiedLocalLocation = PhotoResource.uploadDir + File.separator + ProcessedImageLocation;
    // TODO we can compress the image or apply any processing        
    return true;
  }

  @Override
  public String upLoadImage(String src) {
    String localLocation = PhotoResource.uploadDir + File.separator + src;
    String uploadedLocation = src; 
    if (prefix != "" || prefix != null) {
      uploadedLocation = prefix + File.separator + src;
    }
    String uploadedLocationURI = "S3://"+bucket+File.separator+uploadedLocation;
    File f = new File(localLocation);
    try {
      if (f.exists()) {
        s3Client.putObject(bucket, uploadedLocation, f);
      }
      else {
        LOG.error("File doesnt exist");
        return null;
      }      
    } catch (Exception e) {
      LOG.error("Exception while uploading");
      return null;
    }
    return uploadedLocationURI;
  }

  @Override
  public Boolean downLoadImage(String src) {
    // TODO  use s3Client.getObject to download the file from s3
    return true;
  }

}
