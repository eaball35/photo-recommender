package com.starterproject.petrecommender.controller;

import com.google.cloud.recommendationengine.v1beta1.CatalogItem;
import com.starterproject.petrecommender.service.CodifyPhotoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codify")
public class CodifyController {

  @Autowired private CodifyPhotoService codifyPhotoService;

  @GetMapping
  public ResponseEntity<Object> codifyPhoto(@RequestParam String uri, @RequestParam String id) {
    try {
      CatalogItem response = codifyPhotoService.codifyPhoto(uri, id);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @GetMapping("/topColors")
  public ResponseEntity<Object> getTopImageColors(@RequestParam String uri) {
    try {
      List<String> response = codifyPhotoService.getTopImageColors(uri);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }
}
