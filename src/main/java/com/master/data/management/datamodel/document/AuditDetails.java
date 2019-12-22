package com.master.data.management.datamodel.document;

public abstract class AuditDetails {

  abstract String getCreatedBy();

  abstract String getLastModifiedBy();

  abstract String getLastModifiedDateTime();

  abstract String getCreatedDateTime();

  abstract void setCreatedBy(String createdBy);

  abstract void setLastModifiedBy(String updatedBy);

  abstract void setCreatedDateTime(String createdDateTime);

  abstract void setLastModifiedDateTime(String lastModifiedDateTime);
}
