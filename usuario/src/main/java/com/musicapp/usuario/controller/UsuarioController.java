package com.musicapp.usuario.controller;

import com.musicapp.usuario.domain.Conta;
import com.musicapp.usuario.domain.Assinatura;
import com.musicapp.usuario.dto.PaymentRequestDto;
import com.musicapp.usuario.dto.PaymentResponseDto;
import com.musicapp.usuario.repository.ContaRepository;
import com.musicapp.usuario.repository.AssinaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private AssinaturaRepository assinaturaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/contas")
    public ResponseEntity<?> criarConta(@RequestBody Conta conta) {
        if (contaRepository.findByEmail(conta.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado");
        }
        conta.setStatus("ATIVO");
        Conta novaConta = contaRepository.save(conta);

        Assinatura novaAssinatura = new Assinatura(novaConta, "GRATUITO", "INDIVIDUAL");
        assinaturaRepository.save(novaAssinatura);

        try {
            restTemplate.postForEntity(
                "http://localhost:8084/api/biblioteca/usuarios/" + novaConta.getId() + "/inicializar",
                null,
                String.class
            );
        } catch (Exception e) {
            System.err.println("Erro ao inicializar biblioteca do usuário " + novaConta.getId() + ": " + e.getMessage());
        }

        return ResponseEntity.ok(novaConta);
    }

    @GetMapping("/contas/{id}")
    public ResponseEntity<?> obterConta(@PathVariable Long id) {
        return contaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/assinaturas/assinar")
    public ResponseEntity<?> assinarPremium(@RequestBody Map<String, Object> request) {
        Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
        String metodoPagamento = request.getOrDefault("metodoPagamento", "CREDIT_CARD").toString();
        double valor = Double.parseDouble(request.getOrDefault("valor", "19.90").toString());

        Optional<Conta> contaOpt = contaRepository.findById(usuarioId);
        if (contaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Conta de usuário não encontrada.");
        }
        Conta conta = contaOpt.get();

        Optional<Assinatura> assinaturaOpt = assinaturaRepository.findByContaId(usuarioId);
        Assinatura assinatura = assinaturaOpt.orElseGet(() -> new Assinatura(conta, "GRATUITO", "INDIVIDUAL"));

        if ("PREMIUM".equals(assinatura.getStatus())) {
            return ResponseEntity.badRequest().body("Usuário já possui um plano ativo.");
        }

        if (!"CREDIT_CARD".equalsIgnoreCase(metodoPagamento)) {
            return ResponseEntity.badRequest().body("O usuário deve ter um cartão de credito valido.");
        }

        assinatura.setStatus("PAGAMENTO_PENDENTE");
        assinaturaRepository.save(assinatura);

        PaymentRequestDto paymentRequest = new PaymentRequestDto(
                usuarioId,
                conta.getEmail(),
                conta.getNome(),
                valor,
                metodoPagamento,
                "Assinatura Premium MusicApp - Plano Mensal",
                "MusicApp"
        );

        try {
            ResponseEntity<PaymentResponseDto> paymentResponse = restTemplate.postForEntity(
                    "http://localhost:8082/api/pagamentos/processar",
                    paymentRequest,
                    PaymentResponseDto.class
            );

            PaymentResponseDto responseBody = paymentResponse.getBody();
            if (responseBody != null && "SUCESSO".equals(responseBody.getStatus())) {
                assinatura.setStatus("PREMIUM");
                assinaturaRepository.save(assinatura);
                
                Map<String, Object> successResult = new HashMap<>();
                successResult.put("status", "SUCESSO");
                successResult.put("assinatura", assinatura);
                successResult.put("transacaoId", responseBody.getTransacaoId());
                successResult.put("mensagem", "Assinatura ativada com sucesso.");
                return ResponseEntity.ok(successResult);
            } else {
                assinatura.setStatus("EXPIRADO");
                assinaturaRepository.save(assinatura);
                
                Map<String, Object> failResult = new HashMap<>();
                failResult.put("status", "FALHA");
                failResult.put("assinatura", assinatura);
                failResult.put("mensagem", responseBody != null ? responseBody.getMensagem() : "Erro desconhecido no pagamento.");
                return ResponseEntity.badRequest().body(failResult);
            }
        } catch (Exception e) {
            assinatura.setStatus("GRATUITO");
            assinaturaRepository.save(assinatura);
            return ResponseEntity.status(500).body("Erro de comunicação com o serviço de pagamentos: " + e.getMessage());
        }
    }

    @GetMapping("/assinaturas/{usuarioId}")
    public ResponseEntity<?> obterAssinatura(@PathVariable Long usuarioId) {
        return assinaturaRepository.findByContaId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
