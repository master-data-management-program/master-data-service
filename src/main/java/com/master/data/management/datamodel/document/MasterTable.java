package com.master.data.management.datamodel.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.master.data.management.datamodel.dto.EntityStatus;
import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class MasterTable extends AuditDetails {

  @Id
  private ObjectId tableId;

  @NonNull
  @JsonProperty("tableName")
  private String tableName;

  @JsonProperty("fields")
  private String fields;

  @JsonProperty("status")
  private EntityStatus status;

  @JsonProperty("createdBy")
  private String createdBy;

  @JsonProperty("lastModifiedBy")
  private String lastModifiedBy;

  @JsonProperty("createdDateTime")
  private String createdDateTime;

  @JsonProperty("lastModifiedDateTime")
  private String lastModifiedDateTime;
}
