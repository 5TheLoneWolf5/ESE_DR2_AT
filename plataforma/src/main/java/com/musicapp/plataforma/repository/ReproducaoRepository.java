package com.musicapp.plataforma.repository;

import com.musicapp.plataforma.model.Reproducao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReproducaoRepository extends JpaRepository<Reproducao, Long> {
    List<Reproducao> findByUsuarioId(Long usuarioId);
}
