package com.musicapp.pagamento.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cartoes")
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private boolean ativo;
    private String numero;
    private String validade;

    public Cartao() {}

    public Cartao(Long usuarioId, boolean ativo, String numero, String validade) {
        this.usuarioId = usuarioId;
        this.ativo = ativo;
        this.numero = numero;
        this.validade = validade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getValidade() { return validade; }
    public void setValidade(String validade) { this.validade = validade; }
}
