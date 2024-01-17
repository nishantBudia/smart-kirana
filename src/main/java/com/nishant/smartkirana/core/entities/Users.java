package com.nishant.smartkirana.core.entities;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@EqualsAndHashCode(callSuper = true)
public class Users extends DateAudit {
  @Id private String id;

  @Indexed(unique = true)
  private String emailId;

  @Indexed(unique = true)
  private String phoneNumber;

  private String password;
  private SignUpType signUpType;
  private List<Role> roles;

  public enum SignUpType {
    APP,
    GOOGLE,
    STORE
  }

  public enum Role {
    CUSTOMER,
    ADMIN,
    SALESMAN,
  }
}
