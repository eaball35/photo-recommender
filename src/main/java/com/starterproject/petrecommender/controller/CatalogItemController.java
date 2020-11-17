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

import com.google.cloud.recommendationengine.v1beta1.CatalogItem;
import com.starterproject.petrecommender.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendation")
public class CatalogItemController {

  @Autowired private RecommendationService recommendationService;

  @GetMapping("/items")
  public ResponseEntity<Object> getItem(@RequestParam String id) {
    try {
      CatalogItem item = recommendationService.getCatalogItem(id);
      return ResponseEntity.ok(item.getId());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @DeleteMapping("/items")
  public ResponseEntity deleteItem(@RequestParam String id) {
    try {
      recommendationService.deleteCatalogItem(id);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }
}
