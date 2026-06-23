package com.musicapp.biblioteca.repository;

import com.musicapp.biblioteca.model.PlaylistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaylistItemRepository extends JpaRepository<PlaylistItem, Long> {
    List<PlaylistItem> findByPlaylistId(Long playlistId);
    void deleteByPlaylistIdAndMusicaId(Long playlistId, Long musicaId);
}
