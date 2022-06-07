package com.example.batch.model;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
class PersonProcessorTest {


    private final PersonProcessor systemUnderTest = new PersonProcessor();

    @Test
    void should_process_line_to_person() {
        LineDto lineDto= new LineDto("UserName", "LastName","January 26th, 2021");
        Person person = systemUnderTest.process(lineDto);
        assert person != null;
        assertThat(person.getCsvDate()).isEqualTo(LocalDate.of(2021,1,26));
    }
}