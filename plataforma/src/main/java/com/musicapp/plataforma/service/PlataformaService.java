package com.musicapp.plataforma.service;

import com.musicapp.plataforma.model.Reproducao;
import com.musicapp.plataforma.exception.PaymentPendingException;
import com.musicapp.plataforma.exception.ServiceUnavailableException;
import com.musicapp.plataforma.exception.SubscriptionExpiredException;
import com.musicapp.plataforma.repository.ReproducaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlataformaService {

    @Autowired
    private ReproducaoRepository reproducaoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Map<String, String> login(String email, String senha) {
        if (email == null || senha == null) {
            throw new IllegalArgumentException("E-mail e senha são obrigatórios.");
        }

        Map<String, String> response = new HashMap<>();
        response.put("status", "SUCESSO");
        response.put("token", "JWT-TOKEN-SIMULADO-" + email.hashCode());
        response.put("mensagem", "Autenticação realizada com sucesso.");

        return response;
    }

    public Map<String, Object> reproduzirMusica(Long usuarioId, Long musicaId) {
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
            throw new ServiceUnavailableException("Serviço de Usuários temporariamente indisponível.");
        }

        if ("EXPIRADO".equals(statusAssinatura)) {
            throw new SubscriptionExpiredException("Sua assinatura expirou. Atualize seus dados de pagamento.");
        }
        if ("PAGAMENTO_PENDENTE".equals(statusAssinatura)) {
            throw new PaymentPendingException("Pagamento pendente. Aguarde a confirmação da transação.");
        }

        Map<String, Object> musicaDetalhes;
        try {
            ResponseEntity<Map> catalogResponse = restTemplate.getForEntity(
                    "http://localhost:8083/api/catalogo/musicas/" + musicaId,
                    Map.class
            );
            if (!catalogResponse.getStatusCode().is2xxSuccessful() || catalogResponse.getBody() == null) {
                throw new IllegalArgumentException("Música não encontrada no catálogo.");
            }
            musicaDetalhes = (Map<String, Object>) catalogResponse.getBody();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao comunicar com serviço Catálogo: " + e.getMessage());
            throw new ServiceUnavailableException("Serviço de Catálogo temporariamente indisponível.");
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

        return playbackResult;
    }

    public List<Reproducao> obterHistoricoReproducao(Long usuarioId) {
        return reproducaoRepository.findByUsuarioId(usuarioId);
    }
}
