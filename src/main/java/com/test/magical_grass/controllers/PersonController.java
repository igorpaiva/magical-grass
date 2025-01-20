package com.test.magical_grass.controllers;

import com.test.magical_grass.dto.PersonDTO;
import com.test.magical_grass.services.PersonServices;
import com.test.magical_grass.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for managing people")
public class PersonController {
    @Autowired
    private PersonServices personServices;

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Finds all people", description = "Finds all people",
            tags = {"People"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))}),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
    )
    public List<PersonDTO> findAll() {
        return personServices.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Finds a Person", description = "Finds a Person",
            tags = {"People"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "No content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
    )
    public PersonDTO findById(@PathVariable(value = "id") Long id) {
        return personServices.findById(id);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Creates a new Person",
            description = "Adds a new Person by passing a JSON or XML representation of the person",
            tags = {"People"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
    )
    public PersonDTO createPerson(@RequestBody PersonDTO person) {
        return personServices.createPerson(person);
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Updates an existing Person",
            description = "Updates a Person by passing a JSON or XML representation of the person",
            tags = {"People"}, responses = {
            @ApiResponse(description = "Updated", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
    )
    public PersonDTO updatePerson(@RequestBody PersonDTO person, @PathVariable Long id) {
        return personServices.updatePerson(person, id);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Disables a Person", description = "Disables a Person",
            tags = {"People"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "No content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
    )
    public PersonDTO disablePerson(@PathVariable(value = "id") Long id) {
        return personServices.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes an existing Person",
            description = "Deletes a Person by passing a JSON or XML representation of the person",
            tags = {"People"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content
            ),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
    )
    public ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id) {
        personServices.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

}
