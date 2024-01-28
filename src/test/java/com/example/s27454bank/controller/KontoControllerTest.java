package com.example.s27454bank.controller;

import com.example.s27454bank.model.konto.Konto;
import com.example.s27454bank.model.konto.Waluta;
import com.example.s27454bank.repository.KontoRepository;
import com.example.s27454bank.service.KontoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class KontoControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private KontoRepository kontoRepository;

    @Autowired
    private KontoService kontoService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void cleanup(){
        kontoRepository.removeAll();
    }

    @Test
    void shouldCreateKonto() throws JsonProcessingException {
        Konto konto = new Konto(0, "12345678912", 1000.12, Waluta.USD, "Piotr", "Gąska");
        String kontoJson = objectMapper.writeValueAsString(konto);

        Konto result = webTestClient.post().uri("/konto/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(kontoJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Konto.class)
                .returnResult().getResponseBody();

        assertEquals(result, kontoRepository.getKontoById(result.getId()).get());
    }

    @Test
    void shouldReturnKontoByVariable() {
        Konto konto = new Konto(0, "12345678912", 1000.12, Waluta.USD, "Piotr", "Gąska");

        kontoRepository.create(konto);

        Konto result = webTestClient.get().uri("/konto/0")
                .exchange()
                .expectBody(Konto.class)
                .returnResult().getResponseBody();

        assertEquals(result, kontoRepository.getKontoById(result.getId()).get());
    }

    @Test
    void shouldReturnKontoWithBalanceMoreThan() {
        Double moreThan = 1000.11;

        Konto konto1 = new Konto(0, "12345678912", 1000.12, Waluta.USD, "Piotr", "Gąska");
        Konto konto2 = new Konto(1, "12345678911", 1000.11, Waluta.PLN, "Jan", "Walec");

        kontoRepository.create(konto1);
        kontoRepository.create(konto2);

        List<Konto> result = webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/konto/more_than")
                                .queryParam("minBalance", moreThan)
                                .build())
                .exchange()
                .expectBodyList(Konto.class)
                .returnResult().getResponseBody();

        assertEquals(result, kontoService.listKontoWithHigherBalanceThan(moreThan));
    }
}