package com.test.magical_grass.integrationtests.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.test.magical_grass.configs.TestConfigs;
import com.test.magical_grass.integrationtests.dto.PersonDTO;
import com.test.magical_grass.integrationtests.dto.wrappers.PersonDTOWrapper;
import com.test.magical_grass.integrationtests.testcontainers.AbstractIntegrationTest;
import com.test.magical_grass.model.Person;
import com.test.magical_grass.repositories.PersonRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    PersonRepository personRepository;
    private static Person person;

    public static void setup() {
             person = new Person();
    }

    @Test
    @Order(1)
    public void testFindPersonByFirstName() throws JsonMappingException, JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "firstName"));

        person = personRepository.findPersonByName("lano", pageable).getContent().getFirst();

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());

        assertEquals(3, person.getId());

        assertEquals("Fulano", person.getFirstName());
        assertEquals("de Tal", person.getLastName());
        assertEquals("Rio de Janeiro - Brazil", person.getAddress());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    public void testDisablePerson() throws JsonMappingException, JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "firstName"));

        personRepository.disablePerson(person.getId());
        person = personRepository.findPersonByName("lano", pageable).getContent().getFirst();

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());

        assertEquals(3, person.getId());

        assertEquals("Fulano", person.getFirstName());
        assertEquals("de Tal", person.getLastName());
        assertEquals("Rio de Janeiro - Brazil", person.getAddress());
        assertFalse(person.getEnabled());
    }
}
