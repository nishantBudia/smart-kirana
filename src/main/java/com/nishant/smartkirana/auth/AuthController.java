package com.nishant.smartkirana.auth;

import com.nishant.smartkirana.annotations.Validated;
import com.nishant.smartkirana.auth.entities.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/auth")
public class AuthController {

  @GetMapping("home")
  @Validated
  public Object homeScreen(@RequestBody Data field) {
    return field;
  }

  @GetMapping("mome")
  public String mome() {
    return "yaya";
  }
}
