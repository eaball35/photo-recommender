package com.starterproject.petrecommender.service;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.ColorInfo;
import com.google.cloud.vision.v1.Feature;
import com.google.type.Color;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class CloudVisionService {

  @Autowired private ResourceLoader resourceLoader;
  @Autowired private CloudVisionTemplate cloudVisionTemplate;

  public List<Pair<String, Float>> getLabelDetection(String uri) {
    Resource imageResource = this.resourceLoader.getResource(uri);
    AnnotateImageResponse response =
        this.cloudVisionTemplate.analyzeImage(imageResource, Feature.Type.LABEL_DETECTION);

    List<Pair<String, Float>> imageLabels =
        response.getLabelAnnotationsList().stream()
            .map(
                labelAnnotation ->
                    Pair.of(labelAnnotation.getDescription(), labelAnnotation.getScore()))
            .collect(Collectors.toList());

    return imageLabels;
  }

  public Map<Color, Float> getImageProperties(String uri) {
    Resource imageResource = this.resourceLoader.getResource(uri);
    AnnotateImageResponse response =
        this.cloudVisionTemplate.analyzeImage(imageResource, Feature.Type.IMAGE_PROPERTIES);

    Map<Color, Float> imageColors =
        response.getImagePropertiesAnnotation().getDominantColors().getColorsList().stream()
            .collect(Collectors.toMap(ColorInfo::getColor, ColorInfo::getScore));

    return imageColors;
  }
}
