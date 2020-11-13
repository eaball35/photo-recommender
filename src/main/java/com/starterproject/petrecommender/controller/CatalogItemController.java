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
