package com.musicapp.plataforma.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reproducoes")
public class Reproducao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private Long musicaId;
    private LocalDateTime dataHora;
    private String status;

    public Reproducao() {}

    public Reproducao(Long usuarioId, Long musicaId, LocalDateTime dataHora, String status) {
        this.usuarioId = usuarioId;
        this.musicaId = musicaId;
        this.dataHora = dataHora;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getMusicaId() { return musicaId; }
    public void setMusicaId(Long musicaId) { this.musicaId = musicaId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
