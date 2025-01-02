package com.test.magical_grass.mapper.custom;

import com.test.magical_grass.dto.v2.PersonDTOV2;
import com.test.magical_grass.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertPersonToDTO(Person person) {
        PersonDTOV2 personDTOV2 = new PersonDTOV2();
        personDTOV2.setId(person.getId());
        personDTOV2.setFirstName(person.getFirstName());
        personDTOV2.setLastName(person.getLastName());
        personDTOV2.setBirthDate(new Date());
        personDTOV2.setAddress(person.getAddress());
        return personDTOV2;
    }

    public Person convertDTOToPerson(PersonDTOV2 personDtoV2) {
        Person person = new Person();
        person.setId(personDtoV2.getId());
        person.setFirstName(personDtoV2.getFirstName());
        person.setLastName(personDtoV2.getLastName());
        person.setAddress(personDtoV2.getAddress());
        return person;
    }
}
