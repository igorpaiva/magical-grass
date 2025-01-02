package com.test.magical_grass.controllers;

import com.test.magical_grass.dto.v1.PersonDTO;
import com.test.magical_grass.dto.v2.PersonDTOV2;
import com.test.magical_grass.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/person")
public class PersonController {
    private AtomicLong counter = new AtomicLong();

    @Autowired
    private PersonServices personServices;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonDTO> findAll() {
        return personServices.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO findById(@PathVariable(value = "id") Long id) {
        return personServices.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO createPerson(@RequestBody PersonDTO person) {
        return personServices.createPerson(person);
    }

    @PostMapping(value = "/v2", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTOV2 createPersonV2(@RequestBody PersonDTOV2 person) {
        return personServices.createPersonV2(person);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO updatePerson(@RequestBody PersonDTO person, @PathVariable Long id) {
        return personServices.updatePerson(person, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id) {
        personServices.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

}
