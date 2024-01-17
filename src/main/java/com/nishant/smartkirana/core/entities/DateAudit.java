package com.nishant.smartkirana.core.entities;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class DateAudit {
  @CreatedDate Instant createdAt;

  @LastModifiedDate Instant updatedAt;
}
