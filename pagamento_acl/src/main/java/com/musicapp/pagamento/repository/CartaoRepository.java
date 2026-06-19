package com.musicapp.pagamento.repository;

import com.musicapp.pagamento.domain.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    Optional<Cartao> findByUsuarioId(Long usuarioId);
}
