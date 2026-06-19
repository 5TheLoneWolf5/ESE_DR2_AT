package com.musicapp.pagamento.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private double valor;
    private String status;
    private LocalDateTime dataHora;
    private String referenciaExterna;
    private String mensagem;
    private String comerciante;

    public Transacao() {}

    public Transacao(Long usuarioId, double valor, String status, LocalDateTime dataHora, String referenciaExterna, String mensagem, String comerciante) {
        this.usuarioId = usuarioId;
        this.valor = valor;
        this.status = status;
        this.dataHora = dataHora;
        this.referenciaExterna = referenciaExterna;
        this.mensagem = mensagem;
        this.comerciante = comerciante;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getReferenciaExterna() { return referenciaExterna; }
    public void setReferenciaExterna(String referenciaExterna) { this.referenciaExterna = referenciaExterna; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getComerciante() { return comerciante; }
    public void setComerciante(String comerciante) { this.comerciante = comerciante; }
}
