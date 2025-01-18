package com.test.magical_grass.integrationtests.controller.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.magical_grass.configs.TestConfigs;
import com.test.magical_grass.integrationtests.dto.AccountCredentialsDTO;
import com.test.magical_grass.integrationtests.dto.PersonDTO;
import com.test.magical_grass.integrationtests.dto.TokenDTO;
import com.test.magical_grass.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

    private static TokenDTO tokenDTO;
    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setup(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    public void testAccess() throws JsonMappingException, JsonProcessingException {
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
        tokenDTO = objectMapper.treeToValue(body, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    public void testRefresh() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("admin123", "johndoe");

        TokenDTO newTokenDTO =
                given()
                        .basePath("/auth/refresh")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .pathParams("username", tokenDTO.getUsername())
                        .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                        .when()
                        .put("{username}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().as(TokenDTO.class);

        assertNotNull(newTokenDTO.getAccessToken());
        assertNotNull(newTokenDTO.getRefreshToken());
    }
}
