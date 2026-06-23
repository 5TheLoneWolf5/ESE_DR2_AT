package com.musicapp.pagamento.controller;

import com.musicapp.pagamento.acl.UsuarioPaymentRequest;
import com.musicapp.pagamento.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping("/processar")
    public ResponseEntity<Map<String, Object>> processarPagamento(@RequestBody UsuarioPaymentRequest request) {
        Map<String, Object> resultado = pagamentoService.processarPagamento(request);
        if ("SUCESSO".equals(resultado.get("status"))) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }

    @GetMapping("/transacoes/usuario/{usuarioId}")
    public ResponseEntity<?> obterTransacoesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagamentoService.obterTransacoesPorUsuario(usuarioId));
    }
}
