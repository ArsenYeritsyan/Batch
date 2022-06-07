package com.example.batch.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class LineDto {
  private String firstName;

  private String lastName;

  private String date;

  public LineDto(String firstName, String lastName, String date) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.date = date;
  }
}
