package com.test.magical_grass.services;

import com.test.magical_grass.exceptions.ResourceNotFoundException;
import com.test.magical_grass.dto.PersonDTO;
import com.test.magical_grass.mapper.DozerMapper;
import com.test.magical_grass.model.Person;
import com.test.magical_grass.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());
    @Autowired
    PersonRepository personRepository;

    public PersonDTO createPerson(PersonDTO person) {
        logger.info("Creating person: " + person);
        Person createdPerson = DozerMapper.parseObject(person, Person.class);
        return DozerMapper.parseObject(personRepository.save(createdPerson), PersonDTO.class);
    }

    public PersonDTO updatePerson(PersonDTO person, Long id) {
        logger.info("Updating person: " + person.getId());
        Person updatedPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        updatedPerson.setId(id);
        updatedPerson.setAddress(person.getAddress());
        updatedPerson.setFirstName(person.getFirstName());
        updatedPerson.setLastName(person.getLastName());

        return DozerMapper.parseObject(personRepository.save(updatedPerson), PersonDTO.class);
    }

    public void deletePerson(Long id) {
        logger.info("Deleting person: " + id);
        Person deletePerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        personRepository.delete(deletePerson);
    }

    public List<PersonDTO> findAll() {
        logger.info("Finding all people");
        return DozerMapper.parseListObject(personRepository.findAll(), PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding person by id: " + id);
        Person foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        return DozerMapper.parseObject(foundPerson, PersonDTO.class);
    }
}
