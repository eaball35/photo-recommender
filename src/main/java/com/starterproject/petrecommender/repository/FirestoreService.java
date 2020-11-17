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

package com.starterproject.petrecommender.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FirestoreService {

  @Autowired private Firestore firestoreDB;

  public WriteResult addDocument(String collection, String document, Map<String, Object> data) {
    DocumentReference docRef = firestoreDB.collection(collection).document(document);

    // asynchronously write data
    ApiFuture<WriteResult> result = docRef.set(data);

    try {
      // result.get() blocks on response
      return result.get();
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public Map<String, Object> getDocument(String collection, String document)
      throws FileNotFoundException {
    DocumentReference docRef = firestoreDB.collection(collection).document(document);

    DocumentSnapshot documentSnapshot;
    try {
      // asynchronously retrieve the document
      ApiFuture<DocumentSnapshot> future = docRef.get();

      // future.get() blocks on response
      documentSnapshot = future.get();
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    if (!documentSnapshot.exists()) {
      throw new FileNotFoundException();
    }

    return documentSnapshot.getData();
  }

  public WriteResult editData(String collection, String document, Map<String, Object> updates) {
    // Update an existing document
    DocumentReference docRef = firestoreDB.collection(collection).document(document);

    // (async) Update document
    ApiFuture<WriteResult> future = docRef.update(updates);

    try {
      return future.get();
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public WriteResult deleteData(String collection, String document) {
    // asynchronously delete a document
    ApiFuture<WriteResult> writeResult =
        firestoreDB.collection(collection).document(document).delete();

    try {
      return writeResult.get();
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
