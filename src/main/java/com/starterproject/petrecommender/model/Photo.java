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

package com.starterproject.petrecommender.model;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Photo {

  @NotNull(message = "blobName cannot be null")
  private final String blobName;

  @NotNull(message = "bucket cannot be null")
  private final String bucket;

  @NotNull(message = "generationId cannot be null")
  private final String generationId;

  @NotNull(message = "url cannot be null")
  private final String url;

  @NotNull(message = "dimensions cannot be null")
  private final String dimensions;

  @Builder
  public Photo(String blobName, String bucket, String generationId, String url, String dimensions) {
    this.blobName = blobName;
    this.bucket = bucket;
    this.generationId = generationId;
    this.url = url;
    this.dimensions = dimensions;
  }
}
