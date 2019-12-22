package com.master.data.management.datamodel.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.master.data.management.datamodel.dto.DataModelResponse;
import com.master.data.management.datamodel.service.DataModelService;
import com.master.data.management.datamodel.service.DataModelsOrchestrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${data.endpoint}/models")
@RestController
@Slf4j
public class MasterDataController {

  @Autowired
  private DataModelsOrchestrationService orchestrationService;

  @PostMapping
  public ResponseEntity<String> addNewUserDetails(
      @RequestParam("collectionName") String collectionName,
      @RequestParam("indexId") String indexId,
      @RequestBody JsonNode requestJson) {

    DataModelResponse dataModelResponse = orchestrationService
        .createDataModel(collectionName, indexId, requestJson);

    return ResponseEntity
        .status(dataModelResponse.getHttpStatus())
        .body(dataModelResponse.getStatusMessage());
  }
}
