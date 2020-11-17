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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Value;

public class KGSearchService {

  @Value("${entity.search.url}")
  private String entitySearchUrl;

  @Value("${api.key}")
  private String apiKey;

  public String kgSearchCategory(String query) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    HttpTransport httpTransport = new NetHttpTransport();
    HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

    GenericUrl url = new GenericUrl(entitySearchUrl);
    url.put("query", query);
    url.put("limit", "1");
    url.put("indent", "true");
    url.put("key", apiKey);

    HttpRequest request = requestFactory.buildGetRequest(url);

    HttpResponse httpResponse = request.execute();
    String responseString = httpResponse.parseAsString();

    JsonNode node = mapper.readTree(responseString).get("itemListElement").get(0).get("result");
    return node.get("description").textValue();
  }
}
