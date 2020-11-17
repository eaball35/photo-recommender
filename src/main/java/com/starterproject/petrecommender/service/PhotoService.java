/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.starterproject.petrecommender.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.starterproject.petrecommender.model.Photo;
import com.starterproject.petrecommender.repository.CloudStorageService;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

  @Autowired private CloudStorageService storageService;

  @Value("${project.id}")
  private String projectId;

  private Map<String, ArrayList> photoDimensionsCache = new HashMap<>();

  public BlobId addPhoto(String filePath) {
    try {
      Blob responseBlob = storageService.createObject(filePath);

      // get dimensions and add to metadata
      setMetadataDimensions(responseBlob.getBucket(), responseBlob.getName());

      return responseBlob.getBlobId();
    } catch (IOException e) {
      System.err.println(e);
      throw new RuntimeException(e);
    }
  }

  public BlobId getPhoto(BlobId blobId) {
    Blob responseBlob = storageService.getObject(blobId);
    return responseBlob.getBlobId();
  }

  public BlobId deletePhoto(BlobId blobId) {
    storageService.deleteObject(blobId);
    return blobId;
  }

  public ArrayList<Photo> getAllPhotos() {
    Bucket bucket = storageService.getAllObjects();
    Page<Blob> blobs = bucket.list();

    ArrayList<Photo> photos = new ArrayList<>();
    for (Blob blob : blobs.iterateAll()) {
      String url = "http://storage.googleapis.com/" + blob.getBucket() + "/" + blob.getName();

      Photo photo =
          Photo.builder()
              .blobName(blob.getName())
              .bucket(blob.getBucket())
              .generationId(blob.getGeneratedId())
              .dimensions(blob.getMetadata().get("dimensions"))
              .url(url)
              .build();

      photos.add(photo);
    }
    //  randomizing return order to make front end more interesting for now
    Collections.shuffle(photos);
    return photos;
  }

  private void setMetadataDimensions(String bucketName, String objectName) throws IOException {
    String url = "http://storage.googleapis.com/" + bucketName + "/" + objectName;

    ArrayList dimensions = getDimensions(url);

    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    Map<String, String> newMetadata = new HashMap<>();
    newMetadata.put(
        "dimensions", dimensions.get(0).toString() + "/" + dimensions.get(1).toString());
    Blob blob = storage.get(bucketName, objectName);
    // Does an upsert operation, if the key already exists it's replaced by the new value, otherwise
    // it's added.
    blob.toBuilder().setMetadata(newMetadata).build().update();
  }

  private ArrayList getDimensions(String url) throws IOException {
    // Create a URL for the image's location
    URL imgUrl = new URL(url);

    // Get the image
    Image image = ImageIO.read(imgUrl);

    ArrayList response = new ArrayList<>();
    response.add(image.getHeight(null));
    response.add(image.getWidth(null));

    return response;
  }
}
