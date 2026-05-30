package com.example.demo.endpoint;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.isNotNull;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entity.EstadoDenuncia;

@SpringBootTest
public class ServicioModeracionEndpointTest {

    // Caso de prueba CP2
    @Test 
    @DisplayName("Prueba endpoint POST /denuncias")
    public void testPostDenuncias() {
        given()
            .queryParam("idPost", notNullValue())
            .queryParam("motivo", notNullValue())
            .queryParam("idUsuarioDenunciante", notNullValue())
        .when()
            .post("/denuncias")
        .then()
            .statusCode(
                anyOf(
                    is(200),
                    is(201)
                )
            )
            .body("id", notNullValue())
            .body("usuarioDenunciante", isNotNull())
            .body("publicacion", isNotNull())
            .body("motivo", notNullValue())
            .body("estado", equalTo(EstadoDenuncia.PENDIENTE));
    }
}