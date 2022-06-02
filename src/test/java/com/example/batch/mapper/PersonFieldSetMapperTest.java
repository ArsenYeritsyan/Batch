package com.example.batch.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static com.example.batch.mapper.PersonFieldSetMapper.stringToDate;
import static org.junit.jupiter.api.Assertions.*;

class PersonFieldSetMapperTest {

  @Test
  void stringToDateTest() {
    try {
      Assertions.assertTrue(stringToDate("September 24th, 2020") instanceof Date);
      Assertions.assertTrue(stringToDate("10.08.2020") instanceof Date);
      Assertions.assertTrue(stringToDate("10-08-2020") instanceof Date);
      Assertions.assertTrue(stringToDate("10/08/2020") instanceof Date);
    } catch (Exception e) {
      Assertions.fail();
      e.printStackTrace();
    }

  }

  @Test
  void stringToDateTest1() {
    Exception thrown = Assertions.assertThrows(Exception.class, () -> {
      stringToDate("Septembe 24th, 2022");
    });

    Assertions.assertEquals("Septembe 24th, 2020 date format is incorrect!!!", thrown.getMessage());

  }
}