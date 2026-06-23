package com.musicapp.catalogo.repository;

import com.musicapp.catalogo.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
}
