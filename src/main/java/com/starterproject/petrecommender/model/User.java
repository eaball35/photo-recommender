package com.starterproject.petrecommender.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;

@Getter
public class User implements Serializable {

  @NotNull
  @Size(min = 1, max = 50, message = "username must be between 1-50 characters")
  String username;
  @NotNull
  HashSet addedItems;
  @NotNull
  HashSet likedItems;
  @NotNull
  HashSet lovedItems;

  @Builder
  public User(String username, HashSet addedItems, HashSet likedItems, HashSet lovedItems) {
    this.username = username;
    this.addedItems = addedItems;
    this.likedItems = likedItems;
    this.lovedItems = lovedItems;
  }
}
