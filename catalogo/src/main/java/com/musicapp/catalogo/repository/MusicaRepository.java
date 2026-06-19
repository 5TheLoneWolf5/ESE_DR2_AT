package com.musicapp.catalogo.repository;

import com.musicapp.catalogo.domain.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MusicaRepository extends JpaRepository<Musica, Long> {
    List<Musica> findByArtistaId(Long artistaId);
}
