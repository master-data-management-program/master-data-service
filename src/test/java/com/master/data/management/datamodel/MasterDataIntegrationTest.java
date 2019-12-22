package com.master.data.management.datamodel;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.data.management.datamodel.document.MasterTable;
import com.master.data.management.datamodel.dto.EntityStatus;
import java.time.ZonedDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MasterDataIntegrationTest {

  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  @LocalServerPort
  int randomServerPort;

  private TestRestTemplate testRestTemplate;

  @Before
  public void initialize() {
    RestTemplateBuilder customTemplateBuilder = restTemplateBuilder
        .rootUri("http://localhost:" + randomServerPort);
    this.testRestTemplate = new TestRestTemplate(customTemplateBuilder);
  }

  private String jsonBody = "{\"assetId\":\"number\"}";

  @Test
  public void shouldCreateEntryInMasterTableWithNewTable() throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.set("content-type", "application/json");

    MasterTable masterTable = MasterTable.builder()
        .tableName("mdmFields")
        .fields(jsonBody)
        .createdBy("Admin")
        .createdDateTime(ZonedDateTime.now().toString())
        .status(EntityStatus.ACTIVE)
        .build();

    HttpEntity<String> request = new HttpEntity<>(
        new ObjectMapper().writeValueAsString(masterTable), headers);

    ResponseEntity<String> result = testRestTemplate
        .postForEntity("/master/data/management/v1/models?collectionName=mdmFields&indexId=name",
            request,
            String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    result = testRestTemplate
        .postForEntity("/master/data/management/v1/models?collectionName=mdmFields&indexId=name",
            request,
            String.class);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}