package com.musicapp.catalogo.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "artistas")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String genero;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("artista")
    private List<Musica> musicas;

    public Artista() {}

    public Artista(String nome, String genero) {
        this.nome = nome;
        this.genero = genero;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public List<Musica> getMusicas() { return musicas; }
    public void setMusicas(List<Musica> musicas) { this.musicas = musicas; }
}
