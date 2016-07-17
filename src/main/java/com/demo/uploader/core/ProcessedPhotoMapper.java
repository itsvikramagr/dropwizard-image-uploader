package com.demo.uploader.core;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProcessedPhotoMapper implements ResultSetMapper<ProcessedPhoto>{

  public ProcessedPhoto map(int index, ResultSet r, StatementContext ctx)
  throws SQLException {
    ProcessedPhoto p = new ProcessedPhoto();

    p.setId(r.getLong("id"));
    p.setPhotoId(r.getLong("photo_id"));
    p.setHostName(r.getString("host_name"));
    p.setImageLocalLocation(r.getString("image_local_location"));
    p.setOriginalImageURI(r.getString("original_image_uri"));
    p.setProcessedImageURI(r.getString("processed_image_uri"));
    p.setIsProcessed(r.getBoolean("is_processed"));
    p.setCreatedAt(convertToDateTime(r.getTimestamp("created_at")));
    p.setUpdatedAt(convertToDateTime(r.getTimestamp("updated_at")));
    return p;
  }

  private DateTime convertToDateTime(Timestamp ts) {
    return ts == null ? null : new DateTime(ts).withZone(DateTimeZone.UTC);
  }
}
