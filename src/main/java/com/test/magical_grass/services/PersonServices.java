package com.test.magical_grass.services;

import com.test.magical_grass.model.Person;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

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
