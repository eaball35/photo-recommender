package com.starterproject.petrecommender.model;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Photo {

  @NotNull(message = "blobName cannot be null")
  private final String blobName;

  @NotNull(message = "id cannot be null")
  private final String id;

  @NotNull(message = "url cannot be null")
  private final String url;

  @Builder
  public Photo(String blobName, String id, String url) {
    this.blobName = blobName;
    this.id = id;
    this.url = url;
  }
}
