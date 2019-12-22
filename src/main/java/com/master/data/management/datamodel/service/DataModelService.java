package com.master.data.management.datamodel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.master.data.management.datamodel.dto.DataModelResponse;
import java.io.IOException;

public interface DataModelService {

  DataModelResponse createDataModel(String collectionName, String indexId, JsonNode requestJson)
      throws IOException;

  void amendMasterTableRecord(String collectionName, JsonNode requestJson);
}
