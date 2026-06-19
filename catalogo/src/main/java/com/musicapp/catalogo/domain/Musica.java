package com.musicapp.catalogo.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "musicas")
public class Musica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private int duracao;
    private String urlStream;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    @JsonIgnoreProperties("musicas")
    private Artista artista;

    public Musica() {}

    public Musica(String titulo, int duracao, String urlStream, Artista artista) {
        this.titulo = titulo;
        this.duracao = duracao;
        this.urlStream = urlStream;
        this.artista = artista;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getDuracao() { return duracao; }
    public void setDuracao(int duracao) { this.duracao = duracao; }
    public String getUrlStream() { return urlStream; }
    public void setUrlStream(String urlStream) { this.urlStream = urlStream; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }
}
