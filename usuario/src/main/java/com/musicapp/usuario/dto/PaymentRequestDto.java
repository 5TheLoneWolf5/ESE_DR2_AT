package com.musicapp.usuario.dto;

public class PaymentRequestDto {
    private Long usuarioId;
    private String email;
    private String nome;
    private double valor;
    private String metodoPagamento;
    private String descricao;
    private String comerciante;

    public PaymentRequestDto() {}

    public PaymentRequestDto(Long usuarioId, String email, String nome, double valor, String metodoPagamento, String descricao, String comerciante) {
        this.usuarioId = usuarioId;
        this.email = email;
        this.nome = nome;
        this.valor = valor;
        this.metodoPagamento = metodoPagamento;
        this.descricao = descricao;
        this.comerciante = comerciante;
    }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getComerciante() { return comerciante; }
    public void setComerciante(String comerciante) { this.comerciante = comerciante; }
}
