package com.demo.uploader.dao;

import java.sql.Timestamp;
import java.util.List;

import com.demo.uploader.core.ProcessedPhoto;
import com.demo.uploader.core.ProcessedPhotoMapper;

import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(ProcessedPhotoMapper.class)
public interface ProcessedPhotoDao {

  @SqlUpdate("insert into processed_photo (photo_id, host_name, image_local_location, original_image_uri, processed_image_uri, is_processed, created_at, updated_at) values (:photoId, :hostName, :imageLocalLocation, :originalImageURI, :processedImageURI, :isProcessed, :createdAtTS, :updatedAtTS)")
  @GetGeneratedKeys
  public Long createProcessedPhoto(@BindBean ProcessedPhoto ph);

  @SqlQuery("SELECT * FROM processed_photo where host_name = :host_name and is_processed = 0 order by id asc limit :limit")
  public List<ProcessedPhoto> getListOfUnProcessedPhoto(@Bind("host_name") String host_name, @Bind("limit") int limit);

  @SqlUpdate("update processed_photo set original_image_uri = :original_image_uri, processed_image_uri = :processed_image_uri, is_processed= 1, updated_at = :updated_at where id = :id" )
  public int updateProcessedPhoto(@Bind("id") Long id, @Bind("original_image_uri") String original_image_uri, @Bind("processed_image_uri") String processed_image_uri, @Bind("updated_at") Timestamp updated_at);

  //Close the connection
  void close();
  
}
