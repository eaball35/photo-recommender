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

import com.google.cloud.firestore.WriteResult;
import com.google.cloud.recommendationengine.v1beta1.CatalogItem;
import com.starterproject.petrecommender.model.Item;
import com.starterproject.petrecommender.repository.FirestoreService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  @Autowired private FirestoreService firestoreService;

  @Value("${item.collection}")
  private String itemCollection;

  public String addItem(Item item) {
    String catalogItem;
    try {
      catalogItem = serializeItem(item.getCatalogItem());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Map<String, Object> itemData = new HashMap<>();
    itemData.put("id", item.getId());
    itemData.put("catalogItem", catalogItem);

    WriteResult result = firestoreService.addDocument(itemCollection, item.getId(), itemData);
    return result.getUpdateTime().toString();
  }

  public Item getItem(String id) throws FileNotFoundException {
    Map<String, Object> result = firestoreService.getDocument(itemCollection, id);

    try {
      CatalogItem catalogItem = deserializeItem(result.get("catalogItem").toString());
      Item item = Item.builder().id(result.get("id").toString()).catalogItem(catalogItem).build();
      return item;
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Invalid catalog item");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String editItem(Item updatedItem){
    String catalogItem;
    try {
      catalogItem = serializeItem(updatedItem.getCatalogItem());
    } catch (IOException e) {
        throw new IllegalArgumentException(e);
    }

    Map<String, Object> editedItem = new HashMap<>();
    editedItem.put("id", updatedItem.getId());
    editedItem.put("catalogItem", catalogItem);

    WriteResult result =
        firestoreService.editData(itemCollection, updatedItem.getId(), editedItem);
    return result.getUpdateTime().toString();
  }

  public String deleteItem(String id){
    WriteResult result = firestoreService.deleteData(itemCollection, id);
    return result.getUpdateTime().toString();
  }

  private String serializeItem(CatalogItem item) throws IOException {
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    ObjectOutputStream so = new ObjectOutputStream(bo);
    so.writeObject(item);
    so.flush();
    return bo.toString();
  }

  private CatalogItem deserializeItem(String item) throws IOException, ClassNotFoundException {
    byte b[] = item.getBytes();
    ByteArrayInputStream bi = new ByteArrayInputStream(b);
    ObjectInputStream si = new ObjectInputStream(bi);
    return (CatalogItem) si.readObject();
  }
}
