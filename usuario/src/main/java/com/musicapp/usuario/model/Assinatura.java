package com.musicapp.usuario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "assinaturas")
public class Assinatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "conta_id", referencedColumnName = "id")
    private Conta conta;
    
    private String status;
    private String tipo;

    public Assinatura() {}

    public Assinatura(Conta conta, String status, String tipo) {
        this.conta = conta;
        this.status = status;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Conta getConta() { return conta; }
    public void setConta(Conta conta) { this.conta = conta; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
