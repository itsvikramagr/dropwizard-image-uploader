package com.demo.uploader.resource;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.skife.jdbi.v2.DBI;

import com.demo.uploader.core.Photo;
import com.demo.uploader.core.ProcessedPhoto;
import com.demo.uploader.dao.PhotoDao;
import com.demo.uploader.dao.ProcessedPhotoDao;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/photo")
public class PhotoResource {
  private static final Logger LOG = LoggerFactory.getLogger(PhotoResource.class);
  public static final String uploadDir = "/media/ephemeral0/photo";
  private final PhotoDao photoDao;
  private final ProcessedPhotoDao processedPhotoDao;
  private final DBI jdbi;
  private String hostName;

  public PhotoResource(DBI jdbi, String hostName) {
    this.jdbi = jdbi;
    this.hostName = hostName;
    this.photoDao = jdbi.onDemand(PhotoDao.class);
    this.processedPhotoDao = jdbi.onDemand(ProcessedPhotoDao.class);
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPhoto(@PathParam("id") String id) {
    //TODO - download the image along with the metadata
    Photo photo= photoDao.getPhotoById(new Long(id));
    if (photo != null) {
      return Response.ok(photo).build();
    }
    else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)

  public Response uploadFile(
      @FormDataParam("file") final InputStream fileInputStream,
      @FormDataParam("file") final FormDataContentDisposition fileDetail) {

    String output="";
    Response.Status respStatus = Response.Status.OK;
    if (fileDetail == null) {
      respStatus = Response.Status.INTERNAL_SERVER_ERROR;
    } 
    else {
      String uniqueID=UUID.randomUUID().toString();
      DateTime currentTime = DateTime.now(DateTimeZone.UTC);
      Integer year = currentTime.year().get();
      Integer month = currentTime.monthOfYear().get();

      try {
        String fileName = hostName + "_" +  uniqueID+"_"+ fileDetail.getFileName();
        String relativeFileFolder = year.toString() + File.separator+ month.toString();
        String relativeFileLocation = relativeFileFolder + File.separator + fileName;

        String UploadedFileFolder =  uploadDir + File.separator + relativeFileFolder;
        String uploadedFileLocation = UploadedFileFolder + File.separator+ fileName; 

        File theDir = new File(UploadedFileFolder);
        theDir.mkdirs();
        writeToFile(fileInputStream, uploadedFileLocation);

        //Create a photo object so that we can keep metadata of uploaded image
        Photo p= createPhoto(fileDetail.getFileName(), 0, 0, 10);
        Long photoId=photoDao.createPhoto(p);
        p.setId(photoId);

        // Make an entry in processedphoto table so that cron can process it and upload to s3 etc
        // TODO - use message broker here instead of using cron
        ProcessedPhoto pp = new ProcessedPhoto(photoId, hostName, relativeFileLocation);
        Long processedPhotoId=processedPhotoDao.createProcessedPhoto(pp);
        pp.setId(processedPhotoId);
        LOG.info("info: {}", p.getId());        
        LOG.info("info: {}", fileDetail.toString());
        output = "Your Image has been uploaded with id =  " + photoId;
      } catch (Exception e) {
        e.printStackTrace();
        respStatus = Response.Status.INTERNAL_SERVER_ERROR;
      } 
    }
    return Response.status(respStatus).entity(output).build();
  }

  // save uploaded file to new location
  private void writeToFile(InputStream uploadedInputStream,
      String uploadedFileLocation) throws Exception{

    try {
      OutputStream out = new FileOutputStream(new File(
          uploadedFileLocation));
      int read = 0;
      final int BUFFER_LENGTH = 1024;
      final int UPLOAD_SIZE_LIMIT = 102400;

      byte[] bytes = new byte[BUFFER_LENGTH];
      int size = 0;
      out = new FileOutputStream(new File(uploadedFileLocation));
      while ((read = uploadedInputStream.read(bytes)) != -1) {
        out.write(bytes, 0, read);
        size = size + read;
        if (size > UPLOAD_SIZE_LIMIT){
          // TODO raise custom exception so that we can pass it as API response
          throw new Exception();
        }
      }
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private Photo createPhoto( String fileName, int width, int length, long size) {
    Photo ph = new Photo(fileName, width, length, size);
    return ph;
  }

}
