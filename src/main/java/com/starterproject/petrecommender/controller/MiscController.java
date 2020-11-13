package com.starterproject.petrecommender.controller;

import com.starterproject.petrecommender.service.KGSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MiscController {

  @Autowired private KGSearchService kgSearchService;

  //  use to quickly check service is working
  @GetMapping("/ping")
  public String pingPong() {
    return "PONG";
  }

  @GetMapping("/kgSearchCategory")
  private ResponseEntity<Object> kgSearchCategory(String query) {
    try {
      String response = kgSearchService.kgSearchCategory(query);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }
}
