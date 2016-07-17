package com.demo.uploader.client;

public interface StorageClientInterface {

  public Boolean preProcessImage(String fileLocation, String ProcessedImageLocation);
  public String upLoadImage(String src);
  public Boolean downLoadImage(String src);
    
}
