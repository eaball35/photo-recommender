package com.starterproject.petrecommender.service;

import com.google.cloud.firestore.WriteResult;
import com.google.gson.Gson;
import com.starterproject.petrecommender.model.User;
import com.starterproject.petrecommender.repository.FirestoreService;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private FirestoreService firestoreService;

  @Value("${user.collection}")
  private String userCollection;

  private final Gson gson = new Gson();

  public String addUser(User user) {
    String newUser = serializeUser(user);

    Map<String, Object> userData = new HashMap<>();
    userData.put("user", newUser);

    WriteResult result = firestoreService.addDocument(userCollection, user.getUsername(), userData);
    return result.getUpdateTime().toString();
  }

  public User getUser(String username) throws FileNotFoundException {
    Map<String, Object> result = firestoreService.getDocument(userCollection, username);
    return deserializeUser(result.get("user").toString());
  }

  public String editUser(User updatedUser) {
    String user = serializeUser(updatedUser);
    Map<String, Object> editedUser = Map.of("user", user);
    WriteResult result =
        firestoreService.editData(userCollection, updatedUser.getUsername(), editedUser);
    return result.getUpdateTime().toString();
  }

  public String deleteUser(String userName) {
    WriteResult result = firestoreService.deleteData(userCollection, userName);
    return result.getUpdateTime().toString();
  }

  private String serializeUser(User user) {
    return gson.toJson(user);
  }

  private User deserializeUser(String user) {
    return gson.fromJson(user, User.class);
  }
}
