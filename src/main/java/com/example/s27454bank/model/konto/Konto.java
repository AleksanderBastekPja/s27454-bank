package com.example.s27454bank.model.konto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Konto {
    Integer id;
    String pesel;
    Double saldo_poczatkowe;
    Waluta waluta;
    String imie;
    String nazwisko;
}
