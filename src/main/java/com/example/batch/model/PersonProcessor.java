package com.example.batch.model;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PersonProcessor implements ItemProcessor<LineDto, Person> {

  private static final String DATE_ORDINAL_SUFFIX = "(?<=\\d)(st|nd|rd|th)";
  private static final String DATE_ORDINAL_SUFFIX_MATCHER = "(.*th.*|.*st.*|.*rd.*|.*nd.*)";

  @Override
  public Person process(LineDto person) {
    Person person1 = new Person();
    person1.setFirstName(person.getFirstName());
    person1.setLastName(person.getLastName());
    person1.setCsvDate(parseDate(person.getDate()));
    return person1;
  }

  private LocalDate parseDate(String date) {
    if (date.matches(DATE_ORDINAL_SUFFIX_MATCHER)) {
      String correctDate = date.replaceAll(DATE_ORDINAL_SUFFIX + ",", "");
      return LocalDate.parse(correctDate, DateTimeFormatter.ofPattern("MMMM d yyyy"));
    }
    return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }
}
