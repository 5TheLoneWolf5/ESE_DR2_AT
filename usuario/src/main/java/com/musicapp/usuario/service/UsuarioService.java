package com.musicapp.usuario.service;

import com.musicapp.usuario.model.Conta;
import com.musicapp.usuario.model.Assinatura;
import com.musicapp.usuario.dto.PaymentRequestDto;
import com.musicapp.usuario.dto.PaymentResponseDto;
import com.musicapp.usuario.repository.ContaRepository;
import com.musicapp.usuario.repository.AssinaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private AssinaturaRepository assinaturaRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Conta criarConta(Conta conta) {
        if (contaRepository.findByEmail(conta.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado");
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

        return novaConta;
    }

    public Optional<Conta> obterConta(Long id) {
        return contaRepository.findById(id);
    }

    public Map<String, Object> assinarPremium(Long usuarioId, String metodoPagamento, double valor) {
        Optional<Conta> contaOpt = contaRepository.findById(usuarioId);
        if (contaOpt.isEmpty()) {
            throw new NoSuchElementException("Conta de usuário não encontrada.");
        }
        Conta conta = contaOpt.get();

        Optional<Assinatura> assinaturaOpt = assinaturaRepository.findByContaId(usuarioId);
        Assinatura assinatura = assinaturaOpt.orElseGet(() -> new Assinatura(conta, "GRATUITO", "INDIVIDUAL"));

        if ("PREMIUM".equals(assinatura.getStatus())) {
            throw new IllegalStateException("Usuário já possui um plano ativo.");
        }

        if (!"CREDIT_CARD".equalsIgnoreCase(metodoPagamento)) {
            throw new IllegalArgumentException("O usuário deve ter um cartão de credito valido.");
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
                return successResult;
            } else {
                assinatura.setStatus("EXPIRADO");
                assinaturaRepository.save(assinatura);
                
                Map<String, Object> failResult = new HashMap<>();
                failResult.put("status", "FALHA");
                failResult.put("assinatura", assinatura);
                failResult.put("mensagem", responseBody != null ? responseBody.getMensagem() : "Erro desconhecido no pagamento.");
                return failResult;
            }
        } catch (Exception e) {
            assinatura.setStatus("GRATUITO");
            assinaturaRepository.save(assinatura);
            throw new RuntimeException("Erro de comunicação com o serviço de pagamentos: " + e.getMessage(), e);
        }
    }

    public Optional<Assinatura> obterAssinatura(Long usuarioId) {
        return assinaturaRepository.findByContaId(usuarioId);
    }
}
