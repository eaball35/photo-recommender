package com.starterproject.petrecommender.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.recommendationengine.v1beta1.CatalogName;
import com.google.cloud.recommendationengine.v1beta1.CatalogServiceClient;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.starterproject.petrecommender.repository.FirestoreService;
import com.starterproject.petrecommender.service.CloudVisionService;
import com.starterproject.petrecommender.service.CodifyPhotoService;
import com.starterproject.petrecommender.service.GooglePetsService;
import com.starterproject.petrecommender.service.ItemService;
import com.starterproject.petrecommender.service.KGSearchService;
import com.starterproject.petrecommender.service.PhotoService;
import com.starterproject.petrecommender.service.RecommendationService;
import com.starterproject.petrecommender.service.UserService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
  @Value("${project.id}")
  private String projectId;

  @Bean
  public Firestore getFirestoreDB() throws IOException {
    FirestoreOptions firestoreOptions =
        FirestoreOptions.getDefaultInstance().toBuilder()
            .setProjectId(projectId)
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build();

    return firestoreOptions.getService();
  }

  @Bean
  public Storage getStorage() throws IOException {

    StorageOptions storageOptions =
        StorageOptions.getDefaultInstance().toBuilder()
            .setProjectId(projectId)
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build();
    return storageOptions.getService();
  }

  @Bean
  public FirestoreService getFirestoreService() {
    return new FirestoreService();
  }

  @Bean
  public ItemService getItemService() {
    return new ItemService();
  }

  @Bean
  public UserService getUserService() {
    return new UserService();
  }

  @Bean
  public PhotoService getPhotoService() {
    return new PhotoService();
  }

  @Bean
  public RecommendationService getRecommendationService() {
    return new RecommendationService();
  }

  @Bean
  public GooglePetsService getGooglePetsService() {
    return new GooglePetsService();
  }

  @Bean
  public CloudVisionService getCloudVisionService() {
    return new CloudVisionService();
  }

  @Bean
  public CodifyPhotoService getCodifyPhotoService() {
    return new CodifyPhotoService();
  }

  @Bean
  public KGSearchService getKGSearchService() {
    return new KGSearchService();
  }

  @Bean
  public CatalogServiceClient getCatalogServiceClient() throws IOException {

    //        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(
    //
    //
    // "/Users/emilyball/Desktop/starterproject/recommendation/turing-runway-293318-6b83d47bdffd.json"))
    //
    //     .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-platform"));
    //
    //        CredentialsProvider credProvider = FixedCredentialsProvider.create(credentials);
    //
    //        CatalogServiceSettings catalogServiceSettings = CatalogServiceSettings.newBuilder()
    //            .setCredentialsProvider(credProvider)
    //            .build();
    //
    //        return CatalogServiceClient.create(catalogServiceSettings);

    return CatalogServiceClient.create();
  }

  @Bean
  public CatalogName getCatalogName() {
    return CatalogName.of(projectId, "global", "default_catalog");
  }
}
