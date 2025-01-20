package com.test.magical_grass.integrationtests.controller.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.magical_grass.configs.TestConfigs;
import com.test.magical_grass.integrationtests.dto.AccountCredentialsDTO;
import com.test.magical_grass.integrationtests.dto.PersonDTO;
import com.test.magical_grass.integrationtests.dto.TokenDTO;
import com.test.magical_grass.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static ObjectMapper objectMapper;

    private static PersonDTO personDTO;

    @BeforeAll
    public static void setup(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        personDTO = new PersonDTO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("admin123", "johndoe");

        var rawResponse =
                given()
                        .basePath("/auth/signin")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(user)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var body = objectMapper.readTree(rawResponse).get("body");
        var accessToken = objectMapper.treeToValue(body, TokenDTO.class).getAccessToken();

        requestSpecification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreatePerson() throws JsonMappingException, JsonProcessingException {
        mockPerson();
        var content =
            given().spec(requestSpecification)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(personDTO)
                    .when()
                    .post()
                    .then()
                        .statusCode(200)
                    .extract()
                        .body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Doe", createdPerson.getLastName());
        assertEquals("New York - US", createdPerson.getAddress());
    }

    @Test
    @Order(2)
    public void testUpdatePerson() throws JsonMappingException, JsonProcessingException {
        personDTO.setLastName("Dough");
        var content =
                given().spec(requestSpecification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(personDTO)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertTrue(createdPerson.getEnabled());

        assertEquals(personDTO.getId(), createdPerson.getId());

        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Dough", createdPerson.getLastName());
        assertEquals("New York - US", createdPerson.getAddress());
    }

    @Test
    @Order(3)
    public void testDisablePerson() throws JsonMappingException, JsonProcessingException {
        var content =
                given().spec(requestSpecification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MAGICAL_GRASS)
                        .pathParam("id", personDTO.getId())
                        .when()
                        .patch("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertFalse(createdPerson.getEnabled());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Dough", createdPerson.getLastName());
        assertEquals("New York - US", createdPerson.getAddress());
    }

    @Test
    @Order(4)
    public void testFindPerson() throws JsonMappingException, JsonProcessingException {
        mockPerson();
        var content =
                given().spec(requestSpecification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MAGICAL_GRASS)
                        .pathParam("id", personDTO.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertFalse(createdPerson.getEnabled());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Dough", createdPerson.getLastName());
        assertEquals("New York - US", createdPerson.getAddress());
    }

    @Test
    @Order(5)
    public void testDeletePerson() throws JsonMappingException, JsonProcessingException {
        mockPerson();
        given().spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", personDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    public void testFindAllPeople() throws JsonMappingException, JsonProcessingException {
        var content =
                given().spec(requestSpecification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        List<PersonDTO> foundPeople = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>(){});

        PersonDTO foundPerson = foundPeople.getFirst();

        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());

        assertEquals(2, foundPerson.getId());

        assertEquals("Johnny", foundPerson.getFirstName());
        assertEquals("Appleseed", foundPerson.getLastName());
        assertEquals("Austin - USA", foundPerson.getAddress());
    }

    @Test
    @Order(7)
    public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockPerson() {
        personDTO.setId(1L);
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("New York - US");
        personDTO.setEnabled(true);
    }
}
