package com.demo.uploader.dao;

import java.util.List;

import com.demo.uploader.core.Photo;
import com.demo.uploader.core.PhotoMapper;

import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PhotoMapper.class)
public interface PhotoDao {

  @SqlUpdate("insert into photo (filename, width, length, size, created_at, updated_at) values (:fileName, :width, :length, :size, :createdAtTS, :updatedAtTS)")
  @GetGeneratedKeys
  public Long createPhoto(@BindBean Photo ph);

  @SqlQuery("select * from photo where id = :id")
  public Photo getPhotoById(@Bind("id") Long id);
  
  //Close the connection
  void close();

}
