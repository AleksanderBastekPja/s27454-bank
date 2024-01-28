package com.example.s27454bank.service;

import com.example.s27454bank.exception.KontoNotFoundException;
import com.example.s27454bank.exception.ValidationError;
import com.example.s27454bank.model.konto.Konto;
import com.example.s27454bank.repository.KontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KontoService {
    private final KontoRepository kontoRepository;

    public Konto createKonto(Konto konto) {

        if (konto.getPesel().length() != 11)
            throw new ValidationError("pesel", "pesel should have 11 characters");

        if (konto.getSaldo_poczatkowe() <= 0)
            throw new ValidationError("saldo_poczatkowe", "saldo_poczatkowe should be higher than 0");

        kontoRepository.create(konto);
        return konto;
    }

    public Konto getKontoById(Integer id) {
        Optional<Konto> konto = kontoRepository.getKontoById(id);
        return konto.orElseThrow(() -> new KontoNotFoundException("Kotno with id:" + id + "does not exists."));
    }

    public List<Konto> listKontoWithHigherBalanceThan(Double minBalance) {
        return kontoRepository
                .getAllKonto()
                .stream()
                .filter(it -> it.getSaldo_poczatkowe() > minBalance)
                .collect(Collectors.toList());
    }

}
