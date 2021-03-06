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

package com.starterproject.petrecommender.controller;

import com.google.cloud.storage.BlobId;
import com.starterproject.petrecommender.model.Photo;
import com.starterproject.petrecommender.service.PhotoService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/photos")
public class PhotoController {

  @Autowired private PhotoService photoService;

  @PostMapping
  public ResponseEntity<Object> addPhoto(@RequestParam String filePath) {
    try {
      BlobId response = photoService.addPhoto(filePath);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @GetMapping("/{bucketName}/{objectName}/{generationId}")
  public ResponseEntity<Object> getPhoto(
      @PathVariable String bucketName,
      @PathVariable String objectName,
      @PathVariable Long generationId) {

    try {
      BlobId blobId = BlobId.of(bucketName, objectName, generationId);
      BlobId response = photoService.getPhoto(blobId);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @DeleteMapping("/{bucketName}/{objectName}/{generationId}")
  public ResponseEntity<Object> deletePhoto(
      @PathVariable String bucketName,
      @PathVariable String objectName,
      @PathVariable Long generationId) {

    try {
      BlobId blobId = BlobId.of(bucketName, objectName, generationId);
      BlobId response = photoService.deletePhoto(blobId);

      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @GetMapping()
  public ResponseEntity<Object> getAllPhotos() {
    ArrayList<Photo> photos;
    try {
      photos = photoService.getAllPhotos();
      return ResponseEntity.ok().body(photos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }
}
