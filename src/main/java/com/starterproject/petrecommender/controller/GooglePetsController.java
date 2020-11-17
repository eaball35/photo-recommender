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

import com.starterproject.petrecommender.model.User;
import com.starterproject.petrecommender.service.GooglePetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/")
public class GooglePetsController {

  @Autowired private GooglePetsService googlePetsService;

  @PostMapping("/users")
  public ResponseEntity<Object> addNewUser(@RequestParam String username) {
    try {
      String response = googlePetsService.addNewUser(username);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @GetMapping("/users")
  public ResponseEntity<Object> getExistingUser(@RequestParam String username) {
    try {
      User user = googlePetsService.getExistingUser(username);
      return ResponseEntity.ok().body(user);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @DeleteMapping("/users")
  public ResponseEntity<Object> deleteExistingUser(@RequestParam String username) {
    try {
      String response = googlePetsService.deleteExistingUser(username);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @PostMapping("/pets")
  public ResponseEntity<Object> addNewPet(
      @RequestParam String photoFile, @RequestParam String username) {
    try {
      String response = googlePetsService.addNewPet(photoFile, username);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @PatchMapping("/pets-like")
  public ResponseEntity<Object> likeExistingPet(
      @RequestParam String username, @RequestParam String itemId) {
    try {
      String response = googlePetsService.likeExistingPet(username, itemId);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @PatchMapping("/pets-unlike")
  public ResponseEntity<Object> unlikeExistingPet(
      @RequestParam String username, @RequestParam String itemId) {
    try {
      String response = googlePetsService.unlikeExistingPet(username, itemId);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @PatchMapping("/pets-love")
  public ResponseEntity<Object> loveExistingPet(
      @RequestParam String username, @RequestParam String itemId) {
    try {
      String response = googlePetsService.loveExistingPet(username, itemId);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @PatchMapping("/pets-unlove")
  public ResponseEntity<Object> unloveExistingPet(
      @RequestParam String username, @RequestParam String itemId) {
    try {
      String response = googlePetsService.unloveExistingPet(username, itemId);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }
}
