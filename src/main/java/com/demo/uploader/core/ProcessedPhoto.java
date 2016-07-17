package com.demo.uploader.core;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class ProcessedPhoto {


  private final Logger log = LoggerFactory.getLogger(getClass());

  private Long id;
  private Long photoId;
  private boolean isProcessed = false;
  private String hostName;
  private String imageLocalLocation;
  private String processedImageURI;
  private String originalImageURI;
  private DateTime createdAt;
  private DateTime updatedAt;

  public ProcessedPhoto()
  {

  }

  public ProcessedPhoto(Long photoId, String hostName, String imageLocalLocation) {
    this.setPhotoId(photoId);
    this.setHostName(hostName);
    this.setImageLocalLocation(imageLocalLocation);
    this.setCreatedAt(DateTime.now(DateTimeZone.UTC));
    this.setUpdatedAt(DateTime.now(DateTimeZone.UTC));
  }

  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Long getPhotoId() {
    return photoId;
  }
  public void setPhotoId(Long photoId) {
    this.photoId = photoId;
  }
  public boolean getIsProcessed() {
    return isProcessed;
  }
  public void setIsProcessed(boolean isProcessed) {
    this.isProcessed = isProcessed;
  }
  public String getHostName() {
    return hostName;
  }
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }
  public String getImageLocalLocation() {
    return imageLocalLocation;
  }
  public void setImageLocalLocation(String imageLocalLocation) {
    this.imageLocalLocation = imageLocalLocation;
  }
  public String getProcessedImageURI() {
    return processedImageURI;
  }
  public void setProcessedImageURI(String processedImageURI) {
    this.processedImageURI = processedImageURI;
  }
  public String getOriginalImageURI() {
    return originalImageURI;
  }
  public void setOriginalImageURI(String originalImageURI) {
    this.originalImageURI = originalImageURI;
  }
  public DateTime getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }
  public DateTime getUpdatedAt() {
    return updatedAt;
  }
  public void setUpdatedAt(DateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
  public Timestamp getCreatedAtTS() {return new Timestamp(createdAt.getMillis());}
  public Timestamp getUpdatedAtTS() {return new Timestamp(updatedAt.getMillis());}

}
