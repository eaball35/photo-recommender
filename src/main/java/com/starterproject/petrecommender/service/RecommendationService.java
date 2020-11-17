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

import com.google.cloud.recommendationengine.v1beta1.CatalogInlineSource;
import com.google.cloud.recommendationengine.v1beta1.CatalogItem;
import com.google.cloud.recommendationengine.v1beta1.CatalogItemPathName;
import com.google.cloud.recommendationengine.v1beta1.CatalogName;
import com.google.cloud.recommendationengine.v1beta1.CatalogServiceClient;
import com.google.cloud.recommendationengine.v1beta1.ImportCatalogItemsResponse;
import com.google.cloud.recommendationengine.v1beta1.ImportErrorsConfig;
import com.google.cloud.recommendationengine.v1beta1.InputConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class RecommendationService {

  @Autowired private CatalogServiceClient catalogServiceClient;

  @Autowired private CatalogName parent;

  public CatalogItem createCatalogItem(CatalogItem catalogItem) {
    return catalogServiceClient.createCatalogItem(parent, catalogItem);
  }

  public ImportCatalogItemsResponse importCatalogItemsAsync(List<CatalogItem> catalogItems) {
    String requestId = UUID.randomUUID().toString();
    CatalogInlineSource catalogInlineSource =
        CatalogInlineSource.newBuilder().addAllCatalogItems(catalogItems).build();

    InputConfig inputConfig =
        InputConfig.newBuilder().setCatalogInlineSource(catalogInlineSource).build();

    ImportErrorsConfig errorsConfig = ImportErrorsConfig.getDefaultInstance();

    try {
      ImportCatalogItemsResponse response =
          catalogServiceClient
              .importCatalogItemsAsync(parent, requestId, inputConfig, errorsConfig)
              .get();

      return response;
    } catch (InterruptedException e) {
      System.err.println(e);
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      System.err.println(e);
      throw new RuntimeException(e);
    }
  }

  public CatalogItem getCatalogItem(String itemId) {
    CatalogItemPathName catalogItemPathName =
        CatalogItemPathName.of(
            parent.getProject(), parent.getLocation(), parent.getCatalog(), itemId);

    return catalogServiceClient.getCatalogItem(catalogItemPathName);
  }

  public void deleteCatalogItem(String itemId) {
    CatalogItemPathName catalogItemPathName =
        CatalogItemPathName.of(
            parent.getProject(), parent.getLocation(), parent.getCatalog(), itemId);

    catalogServiceClient.deleteCatalogItem(catalogItemPathName);
  }


}
