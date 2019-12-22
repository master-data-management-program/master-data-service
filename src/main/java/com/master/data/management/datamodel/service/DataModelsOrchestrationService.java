package com.master.data.management.datamodel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.master.data.management.datamodel.dto.DataModelResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataModelsOrchestrationService {

  private final DataModelService dataModelService;

  public DataModelResponse createDataModel(String collectionName, String indexId,
      JsonNode requestJson) {
    //Verify and Add entry in Tables
    dataModelService.amendMasterTableRecord(collectionName, requestJson);
    //Verify and create new table
    try {
      return dataModelService.createDataModel(collectionName, indexId, requestJson);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }
}
