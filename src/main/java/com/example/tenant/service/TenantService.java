package com.example.tenant.service;

import com.example.tenant.model.api.Base;
import com.example.tenant.model.emissor.Ciclo;
import com.example.tenant.multitenancy.TenantContext;
import com.example.tenant.repository.api.BaseRepository;
import com.example.tenant.repository.emissor.CicloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    @Autowired
    private BaseRepository baseRepository;

    @Autowired
    private CicloRepository cicloRepository;

    public Base retornaCiclos(String nome) {
        Base base = baseRepository
                .findByNome(nome)
                .orElseThrow();

        TenantContext.set(base);

        Base base2 = baseRepository
                .findByNome(nome)
                .orElseThrow();

        System.out.println(base2);

        List<Ciclo> ciclo = cicloRepository.findAll();
        return base;
    }
}
