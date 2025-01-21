package com.test.magical_grass.services;

import com.test.magical_grass.controllers.PersonController;
import com.test.magical_grass.dto.PersonDTO;
import com.test.magical_grass.exceptions.RequiredObjectIsNullException;
import com.test.magical_grass.exceptions.ResourceNotFoundException;
import com.test.magical_grass.mapper.ModelMapperWrapper;
import com.test.magical_grass.model.Person;
import com.test.magical_grass.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());
    @Autowired
    PersonRepository personRepository;

    @Autowired
    PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler;

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all people");
        Page<Person> personPage = personRepository.findAll(pageable);
        Page<PersonDTO> personDTOPage = personPage.map(p -> ModelMapperWrapper.parseObject(p, PersonDTO.class));
        personDTOPage.map(personDTO -> personDTO.add(linkTo(methodOn(PersonController.class)
                .findById(personDTO.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getSort().toString(), pageable.getPageSize())).withSelfRel();
        return pagedResourcesAssembler.toModel(personDTOPage, link);
    }

    public PagedModel<EntityModel<PersonDTO>> findPersonByName(String firstName, Pageable pageable) {
        logger.info("Finding person by name: " + firstName);
        Page<Person> personPage = personRepository.findPersonByName(firstName, pageable);
        Page<PersonDTO> personDTOPage = personPage.map(p -> ModelMapperWrapper.parseObject(p, PersonDTO.class));
        personDTOPage.map(personDTO -> personDTO.add(linkTo(methodOn(PersonController.class)
                .findById(personDTO.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getSort().toString(), pageable.getPageSize())).withSelfRel();
        return pagedResourcesAssembler.toModel(personDTOPage, link);
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

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling person by id: " + id);

        personRepository.disablePerson(id);

        Person foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        PersonDTO personDTO = ModelMapperWrapper.parseObject(foundPerson, PersonDTO.class);
        personDTO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personDTO;
    }

    public void deletePerson(Long id) {
        logger.info("Deleting person: " + id);
        Person deletePerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        personRepository.delete(deletePerson);
    }
}
