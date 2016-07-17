package com.demo.uploader;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;


public class AWSS3Configuration {
  @Valid
  @NotNull
  private String awsAccessId = "xxxx";

  @Valid
  @NotNull
  private String awsSecretKey = "xxxxx";

  @Valid
  @NotNull
  private String bucket = "xxx";

  @Valid
  private String prefix = null;

  public String getAwsAccessId() {
    return awsAccessId;
  }

  @JsonProperty(("awsAccessId"))   
  public void setAwsAccessId(String awsAccessId) {
    this.awsAccessId = awsAccessId;
  }

  public String getAwsSecretKey() {
    return awsSecretKey;
  }

  @JsonProperty(("awsSecretKey"))
  public void setAwsSecretKey(String awsSecretKey) {
    this.awsSecretKey = awsSecretKey;
  }

  public String getBucket() {
    return bucket;
  }

  @JsonProperty(("bucket"))
  public void setBucket(String bucket) {
    this.bucket = bucket;
  }

  public String getPrefix() {
    return prefix;
  }

  @JsonProperty(("prefix"))
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

}
