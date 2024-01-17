package com.nishant.smartkirana.auth.entities;

import com.nishant.smartkirana.annotations.NonBlank;
import com.nishant.smartkirana.annotations.Validated;

@lombok.Data
@Validated
public class Data {
  @NonBlank String data;
}
