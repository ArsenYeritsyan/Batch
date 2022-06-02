package com.example.batch.config;

import com.example.batch.model.Person;
import com.example.batch.repo.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MyCustomWriter implements ItemWriter<Person> {

    private final PersonRepository repository;

    @Override
    public void write(List<? extends Person> list) throws Exception {
            repository.saveAll(list);
        }
    }
