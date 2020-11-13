package com.starterproject.petrecommender.controller;

import com.google.type.Color;
import com.starterproject.petrecommender.service.CloudVisionService;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class VisionController {

  @Autowired private CloudVisionService cloudVisionService;

  @RequestMapping("/vision/getLabelDetection")
  public ResponseEntity<Object> getLabelDetection(@RequestParam String file) {
    try {
      List<Pair<String, Float>> response = cloudVisionService.getLabelDetection(file);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @RequestMapping("/vision/getImageProperties")
  public ResponseEntity<Object> getImageProperties(@RequestParam String file) {
    try {
      Map<Color, Float> response = cloudVisionService.getImageProperties(file);
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }
}
