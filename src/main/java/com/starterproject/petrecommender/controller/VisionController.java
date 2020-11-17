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
