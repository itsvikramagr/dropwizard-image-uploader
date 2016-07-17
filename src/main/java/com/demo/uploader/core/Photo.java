package com.demo.uploader.core;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.lang.Long;

public class Photo {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private Long id;
  private String fileName;
  private int width;
  private int length;
  private long size;
  private DateTime createdAt;
  private DateTime updatedAt;


  public Photo()
  {

  }

  public Photo(String fileName, int width, int length, long size) {
    this.setLength(length);
    this.setWidth(width);
    this.setSize(size);
    this.setFileName(fileName);
    this.setCreatedAt(DateTime.now(DateTimeZone.UTC));
    this.setUpdatedAt(DateTime.now(DateTimeZone.UTC));
  }

  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public int getWidth() {
    return width;
  }
  public void setWidth(int width) {
    this.width = width;
  }
  public int getLength() {
    return length;
  }
  public void setLength(int length) {
    this.length = length;
  }
  public long getSize() {
    return size;
  }
  public void setSize(long size) {
    this.size = size;
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
