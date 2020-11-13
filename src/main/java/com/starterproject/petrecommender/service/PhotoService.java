package com.starterproject.petrecommender.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.starterproject.petrecommender.repository.CloudStorageService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PhotoService {

  @Autowired private CloudStorageService storageService;

  public BlobId addPhoto(String filePath) {
    try {
      Blob responseBlob = storageService.createObject(filePath);
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

  public ArrayList<String> getAllPhotos() {
    Bucket bucket = storageService.getAllObjects();
    Page<Blob> blobs = bucket.list();

    ArrayList<String> photoUrls = new ArrayList<>();
    for (Blob blob : blobs.iterateAll()) {
      String url = "http://storage.googleapis.com/" + blob.getBucket() + "/" + blob.getName();
      photoUrls.add(url);
    }
    return photoUrls;
  }
}
