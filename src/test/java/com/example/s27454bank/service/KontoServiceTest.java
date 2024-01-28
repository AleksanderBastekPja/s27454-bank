package com.example.s27454bank.service;

import com.example.s27454bank.exception.KontoNotFoundException;
import com.example.s27454bank.exception.ValidationError;
import com.example.s27454bank.model.konto.Konto;
import com.example.s27454bank.model.konto.Waluta;
import com.example.s27454bank.repository.KontoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KontoServiceTest {
    private static KontoService kontoService;
    private static KontoRepository kontoRepository;

    @BeforeAll
    static void setup() {
        kontoRepository = new KontoRepository();
        kontoService = new KontoService(kontoRepository);
    }

    @AfterEach
    void cleanUp() {
        kontoRepository.removeAll();
    }

    @Test
    void shouldCreateKontoReturnValidationErrorIfPeselLengthIsNotEleven() {
        Konto konto = new Konto(null, "123456789121", 1000.12, Waluta.USD, "Piotr", "Gąska");

        ValidationError result = assertThrows(ValidationError.class, () -> kontoService.createKonto(konto));

        assertEquals("pesel", result.getField());
        assertEquals("pesel should have 11 characters", result.getMessage());
    }

    @Test
    void shouldCreateKontoReturnValidationErrorIfKontoIsLowerOrEqualZero() {
        Konto konto = new Konto(null, "12345678912", -0.1, Waluta.USD, "Piotr", "Gąska");

        ValidationError result = assertThrows(ValidationError.class, () -> kontoService.createKonto(konto));

        assertEquals("saldo_poczatkowe", result.getField());
        assertEquals("saldo_poczatkowe should be higher than 0", result.getMessage());
    }

    @Test
    void shouldCreateKontoReturnCreatedKonto() {
        Konto konto = new Konto(0, "12345678912", 1000.12, Waluta.USD, "Piotr", "Gąska");

        Konto result = kontoService.createKonto(konto);

        assertEquals(konto, result);
    }

    @Test
    void shouldGetKontoByIdReturnKontoNotFoundExceptionIfKontoDoesNotExists() {
        assertThrows(KontoNotFoundException.class, () -> kontoService.getKontoById(0));
    }

    @Test
    void shouldGetKontoByIdCorrectlyReturnDemandedKonto() {
        Konto konto = new Konto(0, "12345678912", 1000.12, Waluta.USD, "Piotr", "Gąska");
        kontoService.createKonto(konto);
        Konto result = kontoService.getKontoById(0);
        assertEquals(konto, result);
    }

    @Test
    void shouldListBalanaceWithHigherAmountThanMinBalance() {
        Konto konto1 = new Konto(0, "12345678912", 1000.12, Waluta.USD, "Piotr", "Gąska");
        Konto konto2 = new Konto(0, "12345678912", 1000.11, Waluta.USD, "Piotr", "Gąska");

        kontoService.createKonto(konto1);
        kontoService.createKonto(konto2);

        List<Konto> kontoListHigherThanMinBalance = new ArrayList<>();
        kontoListHigherThanMinBalance.add(konto1);

        List<Konto> result = kontoService.listKontoWithHigherBalanceThan(1000.11);

        assertEquals(kontoListHigherThanMinBalance, result);
    }

}