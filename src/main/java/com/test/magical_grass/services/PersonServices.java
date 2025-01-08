package com.test.magical_grass.services;

import com.test.magical_grass.controllers.PersonController;
import com.test.magical_grass.dto.PersonDTO;
import com.test.magical_grass.exceptions.RequiredObjectIsNullException;
import com.test.magical_grass.exceptions.ResourceNotFoundException;
import com.test.magical_grass.mapper.ModelMapperWrapper;
import com.test.magical_grass.model.Person;
import com.test.magical_grass.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());
    @Autowired
    PersonRepository personRepository;

    public List<PersonDTO> findAll() {
        logger.info("Finding all people");
        List<PersonDTO> personDTOList = ModelMapperWrapper.parseListObject(personRepository.findAll(), PersonDTO.class);
        personDTOList.stream().forEach(personDTO -> personDTO.add(linkTo(methodOn(PersonController.class)
                .findById(personDTO.getKey())).withSelfRel()));
        return personDTOList;
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding person by id: " + id);
        Person foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        PersonDTO personDTO = ModelMapperWrapper.parseObject(foundPerson, PersonDTO.class);
        personDTO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personDTO;
    }

    public PersonDTO createPerson(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException();
        logger.info("Creating person: " + person);
        Person createdPerson = ModelMapperWrapper.parseObject(person, Person.class);
        PersonDTO personDTO = ModelMapperWrapper.parseObject(personRepository.save(createdPerson), PersonDTO.class);
        personDTO.add(linkTo(methodOn(PersonController.class).findById(personDTO.getKey())).withSelfRel());
        return personDTO;
    }

    public PersonDTO updatePerson(PersonDTO person, Long id) {
        if (person == null) throw new RequiredObjectIsNullException();
        logger.info("Updating person: " + person.getKey());
        Person updatedPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        updatedPerson.setId(id);
        updatedPerson.setAddress(person.getAddress());
        updatedPerson.setFirstName(person.getFirstName());
        updatedPerson.setLastName(person.getLastName());

        PersonDTO personDTO = ModelMapperWrapper.parseObject(personRepository.save(updatedPerson), PersonDTO.class);
        personDTO.add(linkTo(methodOn(PersonController.class).findById(personDTO.getKey())).withSelfRel());
        return personDTO;
    }

    public void deletePerson(Long id) {
        logger.info("Deleting person: " + id);
        Person deletePerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        personRepository.delete(deletePerson);
    }
}
