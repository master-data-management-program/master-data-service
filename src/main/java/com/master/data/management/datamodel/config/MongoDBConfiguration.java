package com.master.data.management.datamodel.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBConfiguration {

  private String mongodbURI;
  private String dbName;

  public MongoDBConfiguration(@Value("${mongodb.uri}") String mongodbURI,
      @Value("${mongodb.db_name}") String dbName) {
    this.mongodbURI = mongodbURI;
    this.dbName = dbName;
  }

  @Bean
  public MongoClient mongoClient() {
    return new MongoClient(new MongoClientURI(mongodbURI));
  }

  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongoClient(), dbName);
  }

  @Bean
  public MongoDatabase mongoDatabase() {
    MongoDatabase database = mongoClient().getDatabase(this.dbName);
    return database;
  }

}
