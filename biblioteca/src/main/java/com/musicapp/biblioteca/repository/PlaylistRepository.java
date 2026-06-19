package com.musicapp.biblioteca.repository;

import com.musicapp.biblioteca.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUsuarioId(Long usuarioId);
}
