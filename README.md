# dropwizard-image-uploader
A demo project using dropwizard to upload images

setup Mysql: mysql -u [username] -p  < setup/init-mysql.sql 

compile:   mvn clean install

Start Application : java -jar target/photo-uploader-1.0.0-rc5-SNAPSHOT.jar server config/configs.json

To upload file: base-url:8000/file/ui

To get details of the image: base-url:8000/file/photo/<id>
