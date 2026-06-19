package com.musicapp.biblioteca.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "favoritos")
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private Long musicaId;

    public Favorito() {}

    public Favorito(Long usuarioId, Long musicaId) {
        this.usuarioId = usuarioId;
        this.musicaId = musicaId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getMusicaId() { return musicaId; }
    public void setMusicaId(Long musicaId) { this.musicaId = musicaId; }
}
