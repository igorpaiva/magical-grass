package com.test.magical_grass.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.test.magical_grass.configs.TestConfigs;
import com.test.magical_grass.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void shouldDisplaySwaggerUiPage() {
        var content =
            given()
                    .basePath("/swagger-ui/index.html")
                    .port(TestConfigs.SERVER_PORT)
                    .when()
                        .get()
                    .then()
                        .statusCode(200)
                    .extract()
                        .body().asString();
        assertTrue(content.contains("<title>Swagger UI</title>"));
    }
}
