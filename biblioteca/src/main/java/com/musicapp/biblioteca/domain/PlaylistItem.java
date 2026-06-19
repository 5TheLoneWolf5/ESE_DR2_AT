package com.musicapp.biblioteca.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "playlist_itens")
public class PlaylistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonIgnoreProperties("itens")
    private Playlist playlist;

    private Long musicaId;

    public PlaylistItem() {}

    public PlaylistItem(Playlist playlist, Long musicaId) {
        this.playlist = playlist;
        this.musicaId = musicaId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Playlist getPlaylist() { return playlist; }
    public void setPlaylist(Playlist playlist) { this.playlist = playlist; }
    public Long getMusicaId() { return musicaId; }
    public void setMusicaId(Long musicaId) { this.musicaId = musicaId; }
}
