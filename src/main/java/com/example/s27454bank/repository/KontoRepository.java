package com.example.s27454bank.repository;

import com.example.s27454bank.model.konto.Konto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class KontoRepository {
    List<Konto> kontoList = new ArrayList<>();

    public Konto create(Konto konto) {
        konto.setId(kontoList.size());
        kontoList.add(konto);

        return konto;
    }

    public Optional<Konto> getKontoById(Integer id) {
        return kontoList
                .stream()
                .filter(konto -> konto.getId().equals(id))
                .findFirst();
    }

    public List<Konto> getAllKonto() { return kontoList; }

    public void removeAll() { kontoList = new ArrayList<>(); }

}