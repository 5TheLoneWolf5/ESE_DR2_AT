package com.musicapp.usuario.dto;

public class PaymentResponseDto {
    private String transacaoId;
    private String status;
    private String referenciaExterna;
    private String mensagem;

    public PaymentResponseDto() {}

    public PaymentResponseDto(String transacaoId, String status, String referenciaExterna, String mensagem) {
        this.transacaoId = transacaoId;
        this.status = status;
        this.referenciaExterna = referenciaExterna;
        this.mensagem = mensagem;
    }

    public String getTransacaoId() { return transacaoId; }
    public void setTransacaoId(String transacaoId) { this.transacaoId = transacaoId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReferenciaExterna() { return referenciaExterna; }
    public void setReferenciaExterna(String referenciaExterna) { this.referenciaExterna = referenciaExterna; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
