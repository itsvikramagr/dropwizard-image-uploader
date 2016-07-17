package com.demo.uploader.health;

import com.codahale.metrics.health.HealthCheck;

public class ServiceHeathCheck  extends HealthCheck{

  @Override
  protected Result check() throws Exception {
     return Result.healthy();
  }
}
