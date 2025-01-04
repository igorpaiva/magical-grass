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
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
    }

    @Test
    void createPerson() {
        Person person = input.mockEntity(1);
        Person savedPerson = person;
        savedPerson.setId(1L);
        PersonDTO personDTO = input.mockDTO(1);
        personDTO.setKey(1L);
        when(personRepository.save(person)).thenReturn(savedPerson);

        PersonDTO result = personServices.createPerson(personDTO);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
    }

    @Test
    void updatePerson() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        Person updatedPerson = person;
        updatedPerson.setId(1L);

        PersonDTO personDTO = input.mockDTO(1);
        personDTO.setKey(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(updatedPerson);

        PersonDTO result = personServices.updatePerson(personDTO, updatedPerson.getId());
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
    }

    @Test
    void deletePerson() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        personServices.deletePerson(person.getId());
    }

    @Test
    void findAll() {
        fail("Not yet implemented");
    }
}