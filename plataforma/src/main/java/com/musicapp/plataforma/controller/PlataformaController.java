package com.musicapp.plataforma.controller;

import com.musicapp.plataforma.model.Reproducao;
import com.musicapp.plataforma.exception.PaymentPendingException;
import com.musicapp.plataforma.exception.ServiceUnavailableException;
import com.musicapp.plataforma.exception.SubscriptionExpiredException;
import com.musicapp.plataforma.service.PlataformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plataforma")
public class PlataformaController {

    @Autowired
    private PlataformaService plataformaService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");

        try {
            Map<String, String> response = plataformaService.login(email, senha);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reproduzir")
    public ResponseEntity<?> reproduzirMusica(@RequestBody Map<String, Long> request) {
        Long usuarioId = request.get("usuarioId");
        Long musicaId = request.get("musicaId");

        if (usuarioId == null || musicaId == null) {
            return ResponseEntity.badRequest().body("usuarioId e musicaId são obrigatórios.");
        }

        try {
            Map<String, Object> playbackResult = plataformaService.reproduzirMusica(usuarioId, musicaId);
            return ResponseEntity.ok(playbackResult);
        } catch (SubscriptionExpiredException | PaymentPendingException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ServiceUnavailableException e) {
            return ResponseEntity.status(503).body(e.getMessage());
        }
    }

    @GetMapping("/historico/{usuarioId}")
    public List<Reproducao> obterHistoricoReproducao(@PathVariable Long usuarioId) {
        return plataformaService.obterHistoricoReproducao(usuarioId);
    }
}
