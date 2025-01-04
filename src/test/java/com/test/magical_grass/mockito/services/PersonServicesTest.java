package com.test.magical_grass.mockito.services;

import com.test.magical_grass.dto.PersonDTO;
import com.test.magical_grass.model.Person;
import com.test.magical_grass.repositories.PersonRepository;
import com.test.magical_grass.services.PersonServices;
import com.test.magical_grass.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices personServices;
    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        PersonDTO result = personServices.findById(1L);
        assertNotNull(result);
        assertNotNull(result);
        assertNotNull(result);
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
    }

    @Test
    void createPerson() {
        fail("Not yet implemented");
    }

    @Test
    void updatePerson() {
        fail("Not yet implemented");
    }

    @Test
    void deletePerson() {
        fail("Not yet implemented");
    }

    @Test
    void findAll() {
        fail("Not yet implemented");
    }
}