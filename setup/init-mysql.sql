create database if not exists datastore;

use datastore;

CREATE TABLE if not exists `photo` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `filename` text,
    `width` int(11) DEFAULT NULL,
    `length` int(11) DEFAULT NULL,
    `size` int(11) DEFAULT NULL,
    `created_at` datetime DEFAULT NULL,
    `updated_at` datetime DEFAULT NULL,
     PRIMARY KEY (`id`));

CREATE TABLE if not exists `processed_photo` ( `id` int(11) NOT NULL AUTO_INCREMENT, 
  `photo_id` int(11) NOT NULL,
  `host_name` varchar(40) DEFAULT NULL,
  `image_local_location` varchar(255) DEFAULT NULL,
  `original_image_uri` varchar(255) DEFAULT NULL,
  `processed_image_uri` varchar(255) DEFAULT NULL,
  `is_processed` tinyint(1) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
   PRIMARY KEY (`id`),
   KEY `host_name` (`host_name`),
   KEY `photo_id` (`photo_id`) );

