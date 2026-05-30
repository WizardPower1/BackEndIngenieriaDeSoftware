package com.example.demo.endpoint;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entity.EstadoSolicitudAmistad;

@SpringBootTest
public class ServicioSocialEndpointTest {
    
    // Caso de prueba CP1
    @Test
    @DisplayName("Prueba endpoint POST /solicitudAmistad/solicitar")
    public void testPostSolicitarAmistad() {
        given()
            .queryParam("idEmisor", notNullValue())
            .queryParam("idReceptor", notNullValue())
        .when()
            .post("/solicitudAmistad/solicitar")
        .then()
            .statusCode(
                anyOf(
                    is(200),
                    is(201)
                )
            )
            .body("id", notNullValue())
            .body("emisor", notNullValue())
            .body("receptor", notNullValue())
            .body("estado", equalTo(EstadoSolicitudAmistad.PENDIENTE));
    }

} 