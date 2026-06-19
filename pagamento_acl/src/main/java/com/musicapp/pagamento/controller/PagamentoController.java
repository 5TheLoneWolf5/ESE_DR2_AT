package com.musicapp.pagamento.controller;

import com.musicapp.pagamento.acl.PagamentoAclService;
import com.musicapp.pagamento.acl.UsuarioPaymentRequest;
import com.musicapp.pagamento.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoAclService pagamentoAclService;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @PostMapping("/processar")
    public ResponseEntity<Map<String, Object>> processarPagamento(@RequestBody UsuarioPaymentRequest request) {
        Map<String, Object> resultado = pagamentoAclService.processarPagamentoUsuario(request);
        if ("SUCESSO".equals(resultado.get("status"))) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }

    @GetMapping("/transacoes/usuario/{usuarioId}")
    public ResponseEntity<?> obterTransacoesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(transacaoRepository.findByUsuarioId(usuarioId));
    }
}
