package com.musicapp.usuario.controller;

import com.musicapp.usuario.model.Conta;
import com.musicapp.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/contas")
    public ResponseEntity<?> criarConta(@RequestBody Conta conta) {
        try {
            Conta novaConta = usuarioService.criarConta(conta);
            return ResponseEntity.ok(novaConta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/contas/{id}")
    public ResponseEntity<?> obterConta(@PathVariable Long id) {
        return usuarioService.obterConta(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/assinaturas/assinar")
    public ResponseEntity<?> assinarPremium(@RequestBody Map<String, Object> request) {
        Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
        String metodoPagamento = request.getOrDefault("metodoPagamento", "CREDIT_CARD").toString();
        double valor = Double.parseDouble(request.getOrDefault("valor", "19.90").toString());

        try {
            Map<String, Object> result = usuarioService.assinarPremium(usuarioId, metodoPagamento, valor);
            if ("SUCESSO".equals(result.get("status"))) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (java.util.NoSuchElementException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/assinaturas/{usuarioId}")
    public ResponseEntity<?> obterAssinatura(@PathVariable Long usuarioId) {
        return usuarioService.obterAssinatura(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
