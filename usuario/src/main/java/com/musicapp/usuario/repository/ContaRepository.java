package com.musicapp.usuario.repository;

import com.musicapp.usuario.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByEmail(String email);
}
