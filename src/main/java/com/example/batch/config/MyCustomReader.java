package com.example.batch.config;

import com.example.batch.mapper.PersonFieldSetMapper;
import com.example.batch.model.Person;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.stereotype.Component;

@Component
public class MyCustomReader  extends FlatFileItemReader<Person> implements ItemReader<Person> {

    public MyCustomReader() {
        setLinesToSkip(1);
        setLineMapper(getDefaultLineMapper());
    }

    public DefaultLineMapper<Person> getDefaultLineMapper() {
        DefaultLineMapper<Person> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("firstName", "lastName", "date");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(new PersonFieldSetMapper());
        return defaultLineMapper;
    }
}
