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

import com.google.cloud.recommendationengine.v1beta1.CatalogItem;
import com.google.cloud.storage.BlobId;
import com.starterproject.petrecommender.model.Item;
import com.starterproject.petrecommender.model.User;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GooglePetsService {

  @Autowired private UserService userService;
  @Autowired private PhotoService photoService;
  @Autowired private ItemService itemService;
  @Autowired private CodifyPhotoService codifyPhotoService;
  @Autowired private RecommendationService recommendationService;

  public String addNewUser(String username) throws Exception {
    //  check if username already exists
    try {
      userService.getUser(username);
      System.err.println("Username already exist : " + username);
      throw new Exception("Username already exist : " + username);
    } catch (FileNotFoundException e) {
      //  If username doesn't already exist - add it
      User newUser =
          User.builder()
              .username(username)
              .addedItems(new HashSet())
              .likedItems(new HashSet())
              .lovedItems(new HashSet())
              .build();

      String updateTime = userService.addUser(newUser);

      return username + ": " + updateTime;
    }
  }

  public User getExistingUser(String username) throws Exception {
    //  get user by id
    try {
      User response = userService.getUser(username);
      return response;
      } catch (FileNotFoundException e) {
        throw new Exception("Username does not exist : " + username);
    }
  }

  public String editExistingUser(User editedUser) {
    //  edit user
    String updateTime = userService.editUser(editedUser);
    System.out.println("editExistingUser: " + updateTime);
    return editedUser.getUsername() + ": " + updateTime;
  }

  public String deleteExistingUser(String username) {
    //  delete user by username
    String deleteTime = userService.deleteUser(username);
    System.out.println("deleteExistingUser: " + deleteTime);
    return username + ": " + deleteTime;
  }

  public String addNewPet(String photoFile, String username) throws Exception {
    //  check user exists
    User user = userService.getUser(username);

    //  add photo to cloud storage
    BlobId blobId = photoService.addPhoto(photoFile);
    String photoUri = "gs://" + blobId.getBucket() + "/" + blobId.getName();
    //  using blobId to generate unique identifier for pet item
    String newId = generateBlobIdIdentifier(blobId);
    System.out.println("Added new photo blob to Cloud Storage" + newId);

    //  transform photo image to catalog item
    CatalogItem catalogItem = codifyPhotoService.codifyPhoto(photoUri, newId);

    //  add new catalog item to firestore
    Item newItem = Item.builder().id(newId).catalogItem(catalogItem).build();
    itemService.addItem(newItem);
    System.out.println("Added new catalog item to Firestore" + newId);

    //  add item to recommendation ai catalog
    recommendationService.createCatalogItem(catalogItem);
    System.out.println("Added new catalog item to Recommendation AI" + newId);

    //  add item to users addedItems
    HashSet updateItems = user.getAddedItems();
    updateItems.add(newId);
    User updatedUser =
        User.builder()
            .username(user.getUsername())
            .addedItems(updateItems)
            .likedItems(user.getLikedItems())
            .lovedItems(user.getLovedItems())
            .build();
    userService.editUser(updatedUser);
    System.out.println(username + "successfully added new item " + newId);

    //  return new unique identifier
    return newId;
  }

  public String editExistingPet(Item updateItem){
    //  update pet
    String editTime = itemService.editItem(updateItem);
    System.out.println("editExistingPet: " + editTime);
    return editTime;
  }

  public void deleteExistingPet(String itemId, String username) {
    //     check that user owns photo
    //     delete pet photo

    //     update pet data to deleted?
    //     think through more how you will remove petdata from users liked
  }

  public String likeExistingPet(String username, String itemId) throws Exception {
    //  get current user
    User user;
    try {
      user = getExistingUser(username);
    } catch (FileNotFoundException e) {
      System.err.println("Username does not exist");
      throw new Exception("Username does not exist");
    }

    //  update user likes by combing old + new
    HashSet updatedLikes = user.getLikedItems();
    updatedLikes.add(itemId);

    User editedUser =
        User.builder()
            .addedItems(user.getAddedItems())
            .likedItems(updatedLikes)
            .lovedItems(user.getLovedItems())
            .build();

    //  edit user
    String editTime = editExistingUser(editedUser);
    System.out.println("likeExistingPet: " + editTime);
    return username + ": " + editTime;
  }

  public String unlikeExistingPet(String username, String itemId) throws Exception {
    //  get current user
    User user;
    try {
      user = getExistingUser(username);
    } catch (NoSuchElementException e) {
      System.err.println("Username does not exist");
      throw new Exception("Username does not exist");
    }

    //  update user likes by removing requested
    HashSet updatedLikes = user.getLikedItems();
    updatedLikes.remove(itemId);

    User editedUser =
        User.builder()
            .addedItems(user.getAddedItems())
            .likedItems(updatedLikes)
            .lovedItems(user.getLovedItems())
            .build();

    //  edit user
    String editTime = editExistingUser(editedUser);
    System.out.println("unlikeExistingPet: " + editTime);
    return username + ": " + editTime;
  }

  public String loveExistingPet(String username, String itemId) throws Exception {
    //  get current user
    User user;
    try {
      user = getExistingUser(username);
    } catch (NoSuchElementException e) {
      System.err.println("Username does not exist");
      throw new Exception("Username does not exist");
    }

    //  update user loves by combing old + new
    HashSet updatedLoves = user.getLovedItems();
    updatedLoves.add(itemId);

    User editedUser =
        User.builder()
            .addedItems(user.getAddedItems())
            .likedItems(user.getLikedItems())
            .lovedItems(updatedLoves)
            .build();

    //  edit user
    String editTime = editExistingUser(editedUser);
    System.out.println("loveExistingPet: " + editTime);
    return username + ": " + editTime;
  }

  public String unloveExistingPet(String username, String itemId) throws Exception {
    //  get current user
    User user;
    try {
      user = getExistingUser(username);
    } catch (NoSuchElementException e) {
      System.err.println("Username does not exist");
      throw new Exception("Username does not exist");
    }

    //  update user loves by removing requested
    HashSet updatedLoves = user.getLovedItems();
    updatedLoves.remove(itemId);

    User editedUser =
        User.builder()
            .addedItems(user.getAddedItems())
            .likedItems(user.getLikedItems())
            .lovedItems(updatedLoves)
            .build();

    //  edit user
    String editTime = editExistingUser(editedUser);
    System.out.println("unloveExistingPet: " + editTime);
    return username + ": " + editTime;
  }

  private String generateBlobIdIdentifier(BlobId blobId) {
    return blobId.getBucket() + "/" + blobId.getName() + "/" + blobId.getGeneration();
  }
}
