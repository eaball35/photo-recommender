package com.starterproject.petrecommender.model;

import java.util.ArrayList;
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
