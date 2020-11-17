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
