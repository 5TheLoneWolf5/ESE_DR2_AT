package com.musicapp.pagamento.repository;

import com.musicapp.pagamento.domain.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByUsuarioId(Long usuarioId);
    List<Transacao> findByUsuarioIdAndDataHoraAfter(Long usuarioId, LocalDateTime dataHora);
}
