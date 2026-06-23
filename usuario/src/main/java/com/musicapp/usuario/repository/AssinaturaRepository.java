package com.musicapp.usuario.repository;

import com.musicapp.usuario.model.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {
    Optional<Assinatura> findByContaId(Long contaId);
}
