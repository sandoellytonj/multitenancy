package com.example.tenant.repository.api;

import com.example.tenant.model.api.Base;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseRepository extends JpaRepository<Base, Long> {

    Optional<Base> findByNome(String nome);

}
