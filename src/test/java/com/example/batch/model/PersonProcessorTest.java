package com.example.batch.model;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
class PersonProcessorTest {

    @InjectMocks
    PersonProcessor systemUnderTest;

    @Test
    void should_process_line_to_Person() {
        LineDto lineDto= new LineDto("UserName", "LastName","January 26th, 2021");
        Person person = systemUnderTest.process(lineDto);
        assert person != null;
        assertThat(person.getCsvDate()).isEqualTo(LocalDate.of(2021,1,26));
    }
}