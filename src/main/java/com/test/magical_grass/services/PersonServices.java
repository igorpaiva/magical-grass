package com.test.magical_grass.services;

import com.test.magical_grass.exceptions.ResourceNotFoundException;
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

    public Person createPerson(Person person) {
        logger.info("Creating person: " + person);
        return personRepository.save(person);
    }

    public Person updatePerson(Person person, Long id) {
        logger.info("Updating person: " + person.getId());
        Person updatedPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        updatedPerson.setId(id);
        updatedPerson.setAddress(person.getAddress());
        updatedPerson.setFirstName(person.getFirstName());
        updatedPerson.setLastName(person.getLastName());

        return personRepository.save(updatedPerson);
    }

    public void deletePerson(Long id) {
        logger.info("Deleting person: " + id);
        Person deletePerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        personRepository.delete(deletePerson);
    }

    public List<Person> findAll() {
        logger.info("Finding all people");
        return personRepository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Finding person by id: " + id);
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
    }
}
