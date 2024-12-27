package com.test.magical_grass.services;

import com.test.magical_grass.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setAddress("Marseille - France");
        person.setFirstName("John");
        person.setLastName("Doe");
        return person;
    }

    public Person createPerson(Person person) {
        logger.info("Creating person: " + person);
        return person;
    }

    public Person updatePerson(Person person) {
        logger.info("Updating person: " + person.getId());
        return person;
    }

    public void deletePerson(String id) {
        logger.info("Deleting person: " + id);
    }

    public List<Person> findAll() {
        logger.info("Finding all people");
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Person person = mockPerson(i);
            persons.add(person);
        }
        return persons;
    }

    public Person findById(String id) {
        logger.info("Finding person by id: " + id);

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setAddress("Marseille - France");
        person.setFirstName("John");
        person.setLastName("Doe");

        return person;
    }
}
