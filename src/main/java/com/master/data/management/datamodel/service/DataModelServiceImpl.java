package com.master.data.management.datamodel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.data.management.datamodel.config.ManageCollections;
import com.master.data.management.datamodel.document.MasterTable;
import com.master.data.management.datamodel.dto.DataModelResponse;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataModelServiceImpl implements DataModelService {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MongoDatabase database;

  @Autowired
  private ManageCollections manageCollections;

  @Value("${mongodb.db_name}")
  private String dbName;

  public DataModelResponse createDataModel(String collectionName, String indexId,
      JsonNode requestJson) throws IOException {
    MongoCollection<Document> databaseCollection = database.getCollection(collectionName);
    Document document = new ObjectMapper().convertValue(requestJson, Document.class);
    String fields = (String) document.get("fields");
    Document modelDocument = objectMapper
        .convertValue(objectMapper.readTree(fields), Document.class);
    DataModelResponse response = upsertDocument(databaseCollection, modelDocument,
        indexId);
    log.info("Total collections :: {}",
        IterableUtils.size(database.listCollectionNames()));
    return response;
  }

  private DataModelResponse upsertDocument(MongoCollection<Document> databaseCollection,
      Document document,
      String indexId) {
    HttpStatus status = HttpStatus.CREATED;
    String statusMessage = "Create Successful";

    BasicDBObject query = new BasicDBObject();
    query.append(indexId, document.get(indexId));

    FindIterable<Document> documentsIterable = databaseCollection.find(query);

    try {

      if (IterableUtils.size(documentsIterable) > 0) {
        query = new BasicDBObject();
        query.append("_id", documentsIterable.first().get("_id"));

        //Update the record in db
        UpdateResult result = updateCollection(databaseCollection, document, query, true);

        status = HttpStatus.OK;
        statusMessage = "Update successful.";
        log.info("Total document in collection:: {}", databaseCollection.countDocuments());

      } else {
        createCollection(databaseCollection, document, indexId);
      }

    } catch (MongoWriteException mwe) {
      log.error(mwe.getMessage());
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      statusMessage = mwe.getMessage();
    }

    return DataModelResponse.builder()
        .httpStatus(status)
        .statusMessage(statusMessage)
        .build();
  }

  public void amendMasterTableRecord(String collectionName, JsonNode requestJson) {
    String indexField = "tableName";
    BasicDBObject findTableSearch = new BasicDBObject().append(indexField, collectionName);
    MasterTable masterTable = objectMapper.convertValue(requestJson, MasterTable.class);

    MongoCollection<MasterTable> masterTableCollection = database
        .getCollection("MasterTable", MasterTable.class)
        .withCodecRegistry(manageCollections.getPojoCodecRegistry());

    FindIterable<MasterTable> existingMasterTableDocuments = masterTableCollection
        .find(findTableSearch);

    // Check does collection already exists in mdmFields Collections. If yes update otherwise create it.
    if (IterableUtils.size(existingMasterTableDocuments) > 0) {
      MasterTable existingMasterTable = existingMasterTableDocuments.first();
      BasicDBObject query = new BasicDBObject();
      query.append("tableId", existingMasterTable.getTableId());
      masterTable.setTableId(existingMasterTable.getTableId());
      updateCollection(masterTableCollection, masterTable, query, true);

    } else {
      createCollection(masterTableCollection, masterTable, indexField);
    }
  }

  private <T extends Object> UpdateResult updateCollection(MongoCollection<T> masterTableCollection,
      T document, BasicDBObject query, boolean replaceRecord) {
    //Update the record in db
    UpdateResult result = masterTableCollection
        .replaceOne(query, document,
            new ReplaceOptions().upsert(replaceRecord).bypassDocumentValidation(true));

    //Logs the Updated records stats here.
    log.info("Replace Matched Count....: "
        + result.getMatchedCount());
    log.info("Replace Modified Count...: "
        + result.getModifiedCount());
    log.info("Updated Successfully.");

    return result;
  }

  private <T> void createCollection(MongoCollection<T> databaseCollection, T document,
      String indexId) {
    databaseCollection.createIndex(new Document(indexId, 1), new IndexOptions().unique(false));
    databaseCollection.insertOne(document);
    log.info("Created Successfully.");
    log.info("Total document in collection:: {}", databaseCollection.countDocuments());
  }
}
