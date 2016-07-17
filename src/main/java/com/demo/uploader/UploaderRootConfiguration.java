package com.demo.uploader;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.demo.uploader.AWSS3Configuration;

public class UploaderRootConfiguration extends Configuration {
  @Valid
  @NotNull
  private DataSourceFactory database = new DataSourceFactory();
  
  @NotNull
  private AWSS3Configuration awsConfiguration = new AWSS3Configuration();


  public DataSourceFactory getDataSourceFactory() {
    return database;
  }

  @JsonProperty("database")
  public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
    this.database = dataSourceFactory;
  }


  public AWSS3Configuration getAwsConfiguration() {
    return awsConfiguration;
  }
  
  @JsonProperty("aws-config")
  public void setAwsConiguration(AWSS3Configuration awsConfiguration) {
    this.awsConfiguration = awsConfiguration;
  }

}
