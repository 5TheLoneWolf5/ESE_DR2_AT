package com.musicapp.biblioteca.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Long usuarioId;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("playlist")
    private List<PlaylistItem> itens;

    public Playlist() {}

    public Playlist(String nome, Long usuarioId) {
        this.nome = nome;
        this.usuarioId = usuarioId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public List<PlaylistItem> getItens() { return itens; }
    public void setItens(List<PlaylistItem> itens) { this.itens = itens; }
}
