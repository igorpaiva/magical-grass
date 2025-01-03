package com.test.magical_grass.controllers;

import com.test.magical_grass.dto.PersonDTO;
import com.test.magical_grass.services.PersonServices;
import com.test.magical_grass.util.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {
    private AtomicLong counter = new AtomicLong();

    @Autowired
    private PersonServices personServices;

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<PersonDTO> findAll() {
        return personServices.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PersonDTO findById(@PathVariable(value = "id") Long id) {
        return personServices.findById(id);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PersonDTO createPerson(@RequestBody PersonDTO person) {
        return personServices.createPerson(person);
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PersonDTO updatePerson(@RequestBody PersonDTO person, @PathVariable Long id) {
        return personServices.updatePerson(person, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id) {
        personServices.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

}
