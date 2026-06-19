package com.musicapp.pagamento.gateway;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class GatewaySimulado {

    public static class GatewayResult {
        private final boolean success;
        private final String transactionId;
        private final String message;

        public GatewayResult(boolean success, String transactionId, String message) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getTransactionId() { return transactionId; }
        public String getMessage() { return message; }
    }

    public GatewayResult processarPagamentoExterno(double valor, String metodoPagamento) {
        if (metodoPagamento != null && metodoPagamento.toUpperCase().contains("FAIL")) {
            return new GatewayResult(false, null, "Cartão recusado pelo emissor.");
        }

        double rand = Math.random();
        if (rand > 0.05) {
            String txId = "GW-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            return new GatewayResult(true, txId, "Pagamento autorizado com sucesso.");
        } else {
            return new GatewayResult(false, null, "Saldo insuficiente ou falha temporária no processamento.");
        }
    }
}
