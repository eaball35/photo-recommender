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

package com.starterproject.petrecommender.repository;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Repository
public class CloudStorageService {

  @Value("${bucket.name}")
  private String bucketName;

  @Autowired private Storage storage;

  public Blob createObject(String filePath) throws IOException {
    BlobId blobId = BlobId.of(bucketName, generateObjectName());
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

    return storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
  }

  public Blob getObject(BlobId blobId) {
    return storage.get(blobId);
  }

  public void deleteObject(BlobId blobId) {
    storage.delete(blobId);
  }

  public Bucket getAllObjects() {
    return storage.get(bucketName);
  }

  private String generateObjectName() {
    return UUID.randomUUID().toString();
  }
}
