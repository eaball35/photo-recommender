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
import com.google.cloud.recommendationengine.v1beta1.FeatureMap;
import com.google.type.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class CodifyPhotoService {

  @Autowired private CloudVisionService cloudVisionService;

  @Autowired private RecommendationService recommendationService;

  @Autowired private KGSearchService kgSearchService;

  private HashMap<String, String> subcategoryMappingCache = new HashMap<>();

  public CatalogItem codifyPhoto(String uri, String id) {
    // I should think more about concurrency here

    //  get labels from vision ai
    List<Pair<String, Float>> imageLabels = getImageLabels(uri);

    //  get top colors from vision ai + color api
    List<String> topColors = getTopImageColors(uri);

    //  use labels to get categories from knowledge graph
    Map<String, Set<String>> categories = getCategories(imageLabels);
    //  add colors as category
    categories.put("colors", new HashSet(topColors));

    //  use categories to create category hierarchies and item attributes
    FeatureMap itemAttributes = mapToFeatureMap(categories);

    List<CatalogItem.CategoryHierarchy> categoryHierarchies = mapToCategoryHierarchies(categories);

    //  create tags from labels
    List<String> tags =
        imageLabels.stream()
            .map(label -> label.getLeft().toLowerCase())
            .collect(Collectors.toList());

    //  generate title with top descriptors
    String title = generateTitle(topColors, tags);

    //  create description by combing all labels
    String description = tags.toString();

    CatalogItem catalogItem =
        CatalogItem.newBuilder()
            .setId(id)
            .setTitle(title)
            .setDescription(description)
            .addAllCategoryHierarchies(categoryHierarchies)
            .setItemAttributes(itemAttributes)
            .addAllTags(tags)
            .build();

    return catalogItem;
  }

  private List<Pair<String, Float>> getImageLabels(String uri) {
    return cloudVisionService.getLabelDetection(uri);
  }

  private Map<Color, Float> getImageProperties(String uri) {
    return cloudVisionService.getImageProperties(uri);
  }

  private String generateTitle(List<String> topColors, List<String> tags) {
    //  create title from top colors + top 3 labels
    String title = "";
    if (topColors.size() > 0) {
      title += topColors.get(0);
    }
    if (tags.size() > 0) {
      title += " " + tags.get(0);
    }
    if (tags.size() > 1) {
      title += " " + tags.get(1);
    }
    if (tags.size() > 2) {
      title += " " + tags.get(2);
    }

    return title;
  }

  private Map<String, Set<String>> getCategories(List<Pair<String, Float>> imageLabels) {
    Map<String, Set<String>> categories = new HashMap<>();

    for (Pair<String, Float> label : imageLabels) {
      String currSubcategory = label.getLeft().toLowerCase().trim();
      String currCategory = null;
      // check if we already know the category
      if (subcategoryMappingCache.containsKey(currSubcategory)) {
        currCategory = subcategoryMappingCache.get(currSubcategory);
        // otherwise get the category and add to cache
      } else {
        try {
          currCategory = kgSearchService.kgSearchCategory(currSubcategory).toLowerCase().trim();
          subcategoryMappingCache.put(currSubcategory, currCategory);
        } catch (Exception e) {
          System.out.println("unable to get category for subcategory " + currSubcategory);
          //  set value to null in cache so we don't check again
          subcategoryMappingCache.put(currSubcategory, null);
        }
      }

      //  if able to get a category - add it to result
      if (currCategory != null) {
        Set<String> currSubcategories = categories.get(currCategory);
        if (currSubcategories == null) {
          currSubcategories = new HashSet<>();
        }
        currSubcategories.add(currSubcategory);

        categories.put(currCategory, currSubcategories);
      }
    }

    return categories;
  }

  private List<CatalogItem.CategoryHierarchy> mapToCategoryHierarchies(
      Map<String, Set<String>> categories) {
    List<CatalogItem.CategoryHierarchy> categoryHierarchies = new ArrayList<>();

    for (Map.Entry<String, Set<String>> categoryLine : categories.entrySet()) {
      String category = categoryLine.getKey();
      for (String subcategory : categoryLine.getValue()) {

        CatalogItem.CategoryHierarchy categoryHierarchy =
            CatalogItem.CategoryHierarchy.newBuilder()
                .addCategories(category)
                .addCategories(subcategory)
                .build();

        categoryHierarchies.add(categoryHierarchy);
      }
    }

    return categoryHierarchies;
  }

  private FeatureMap mapToFeatureMap(Map<String, Set<String>> itemAttributes) {
    FeatureMap.Builder builder = FeatureMap.newBuilder();

    itemAttributes.entrySet().stream()
        .forEach(
            item -> {
              FeatureMap.StringList stringList =
                  FeatureMap.StringList.newBuilder().addAllValue(item.getValue()).build();

              builder.putCategoricalFeatures(item.getKey(), stringList);
            });
    return builder.build();
  }

  public List<String> getTopImageColors(String uri) {
    Map<Color, Float> imageProperties = getImageProperties(uri);

    //  I should make these call parallel
    //  converts each rgb value to colorName
    List<String> colors = new ArrayList<>();
    for (Map.Entry<Color, Float> entry : imageProperties.entrySet()) {
      try {
        String color =
            getColorFromRGB(
                (int) entry.getKey().getRed(),
                (int) entry.getKey().getGreen(),
                (int) entry.getKey().getBlue());

        colors.add(color);
      } catch (Exception e) {
        System.err.println("Couldn't get color for current rgb" + entry.getKey().toString());
      }
    }
    return colors;
  }

  private String getColorFromRGB(int r, int g, int b) throws IOException, JSONException {
    JSONObject response = getColorRequest("http://thecolorapi.com/id?rgb=" + r + "," + g + "," + b);
    JSONObject name = (JSONObject) response.get("name");
    Object color = name.get("value");

    return color.toString().toLowerCase();
  }

  private JSONObject getColorRequest(String url) throws IOException, JSONException {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(url).get().build();
    Response response = client.newCall(request).execute();
    return jsonToMap(response.body().string());
  }

  private static JSONObject jsonToMap(String t) throws JSONException {
    HashMap<String, String> map = new HashMap<>();
    JSONObject jObject = new JSONObject(t);
    Iterator<?> keys = jObject.keys();

    while (keys.hasNext()) {
      String key = (String) keys.next();
      String value = jObject.getString(key);
      map.put(key, value);
    }

    return jObject;
  }
}
