package com.test.magical_grass.mapper;

import com.test.magical_grass.dto.PersonDTO;
import com.test.magical_grass.model.Person;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.List;

public class ModelMapperWrapper {
    private static final ModelMapper mapper = new ModelMapper();

    static {
        mapper.createTypeMap(Person.class, PersonDTO.class)
                .addMapping(Person::getId, PersonDTO::setKey);
        mapper.createTypeMap(PersonDTO.class, Person.class)
                .addMapping(PersonDTO::getKey, Person::setId);
    }

    public static <S, D> D parseObject(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    public static <S, D> List<D> parseListObject(List<S> sourceList, Class<D> destinationClass) {
        List<D> destinationList = new ArrayList<>();
        for (S source : sourceList) {
            destinationList.add(parseObject(source, destinationClass));
        }
        return destinationList;
    }
}
