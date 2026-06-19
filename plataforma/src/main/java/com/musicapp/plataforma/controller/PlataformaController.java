package com.musicapp.plataforma.controller;

import com.musicapp.plataforma.domain.Reproducao;
import com.musicapp.plataforma.repository.ReproducaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plataforma")
public class PlataformaController {

    @Autowired
    private ReproducaoRepository reproducaoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");

        if (email == null || senha == null) {
            return ResponseEntity.badRequest().body("E-mail e senha são obrigatórios.");
        }

        Map<String, String> response = new HashMap<>();
        response.put("status", "SUCESSO");
        response.put("token", "JWT-TOKEN-SIMULADO-" + email.hashCode());
        response.put("mensagem", "Autenticação realizada com sucesso.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reproduzir")
    public ResponseEntity<?> reproduzirMusica(@RequestBody Map<String, Long> request) {
        Long usuarioId = request.get("usuarioId");
        Long musicaId = request.get("musicaId");

        if (usuarioId == null || musicaId == null) {
            return ResponseEntity.badRequest().body("usuarioId e musicaId são obrigatórios.");
        }

        String statusAssinatura = "GRATUITO";
        try {
            ResponseEntity<Map> userResponse = restTemplate.getForEntity(
                    "http://localhost:8081/api/usuarios/assinaturas/" + usuarioId,
                    Map.class
            );
            if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null) {
                statusAssinatura = userResponse.getBody().getOrDefault("status", "GRATUITO").toString();
            }
        } catch (Exception e) {
            System.err.println("Erro ao comunicar com serviço Usuário: " + e.getMessage());
            return ResponseEntity.status(503).body("Serviço de Usuários temporariamente indisponível.");
        }

        if ("EXPIRADO".equals(statusAssinatura)) {
            return ResponseEntity.status(403).body("Sua assinatura expirou. Atualize seus dados de pagamento.");
        }
        if ("PAGAMENTO_PENDENTE".equals(statusAssinatura)) {
            return ResponseEntity.status(403).body("Pagamento pendente. Aguarde a confirmação da transação.");
        }

        Map<String, Object> musicaDetalhes;
        try {
            ResponseEntity<Map> catalogResponse = restTemplate.getForEntity(
                    "http://localhost:8083/api/catalogo/musicas/" + musicaId,
                    Map.class
            );
            if (!catalogResponse.getStatusCode().is2xxSuccessful() || catalogResponse.getBody() == null) {
                return ResponseEntity.badRequest().body("Música não encontrada no catálogo.");
            }
            musicaDetalhes = (Map<String, Object>) catalogResponse.getBody();
        } catch (Exception e) {
            System.err.println("Erro ao comunicar com serviço Catálogo: " + e.getMessage());
            return ResponseEntity.status(503).body("Serviço de Catálogo temporariamente indisponível.");
        }

        Reproducao reproducao = new Reproducao(usuarioId, musicaId, LocalDateTime.now(), "FINALIZADO");
        Reproducao reproducaoSalva = reproducaoRepository.save(reproducao);

        Map<String, Object> playbackResult = new HashMap<>();
        playbackResult.put("sessaoId", reproducaoSalva.getId());
        playbackResult.put("musicaId", musicaDetalhes.get("id"));
        playbackResult.put("titulo", musicaDetalhes.get("titulo"));
        playbackResult.put("urlStream", musicaDetalhes.get("urlStream"));
        playbackResult.put("duracao", musicaDetalhes.get("duracao"));
        playbackResult.put("assinaturaNivel", statusAssinatura);
        
        if ("GRATUITO".equals(statusAssinatura)) {
            playbackResult.put("modo", "ANUNCIO_ATIVADO");
            playbackResult.put("mensagem", "Reproduzindo com anúncios (Nível Grátis). Faça upgrade para Premium!");
        } else {
            playbackResult.put("modo", "PREMIUM_HI_FI");
            playbackResult.put("mensagem", "Reproduzindo em Alta Fidelidade (Nível Premium).");
        }

        return ResponseEntity.ok(playbackResult);
    }

    @GetMapping("/historico/{usuarioId}")
    public List<Reproducao> obterHistoricoReproducao(@PathVariable Long usuarioId) {
        return reproducaoRepository.findByUsuarioId(usuarioId);
    }
}
