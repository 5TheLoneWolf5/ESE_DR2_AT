package com.musicapp.pagamento.acl;

import com.musicapp.pagamento.domain.Cartao;
import com.musicapp.pagamento.domain.Transacao;
import com.musicapp.pagamento.gateway.GatewaySimulado;
import com.musicapp.pagamento.repository.CartaoRepository;
import com.musicapp.pagamento.repository.TransacaoRepository;
import com.musicapp.pagamento.regras.ValidadorTransacao;
import com.musicapp.pagamento.regras.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PagamentoAclService {

    @Autowired
    private GatewaySimulado gatewaySimulado;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private List<ValidadorTransacao> validadores;

    public Map<String, Object> processarPagamentoUsuario(UsuarioPaymentRequest request) {
        double valor = request.getValor();
        String metodo = request.getMetodoPagamento();
        Long usuarioId = request.getUsuarioId();
        String comerciante = request.getComerciante() != null ? request.getComerciante() : "MusicApp";

        System.out.println("ACL traduzindo requisição para o usuário ID: " + usuarioId + " (" + request.getEmail() + ")");
        System.out.println("Processando transação com valor: R$ " + valor);

        Optional<Cartao> cartaoOpt = cartaoRepository.findByUsuarioId(usuarioId);

        try {
            for (ValidadorTransacao validador : validadores) {
                validador.validar(request, cartaoOpt.orElse(null));
            }
        } catch (ValidacaoException e) {
            Transacao transacaoFalha = new Transacao();
            transacaoFalha.setUsuarioId(usuarioId);
            transacaoFalha.setValor(valor);
            transacaoFalha.setDataHora(LocalDateTime.now());
            transacaoFalha.setStatus("FALHA");
            transacaoFalha.setReferenciaExterna(null);
            transacaoFalha.setMensagem(e.getMessage());
            transacaoFalha.setComerciante(comerciante);

            Transacao transacaoSalva = transacaoRepository.save(transacaoFalha);

            Map<String, Object> response = new HashMap<>();
            response.put("transacaoId", transacaoSalva.getId().toString());
            response.put("status", transacaoSalva.getStatus());
            response.put("referenciaExterna", transacaoSalva.getReferenciaExterna());
            response.put("mensagem", transacaoSalva.getMensagem());
            return response;
        }

        GatewaySimulado.GatewayResult result = gatewaySimulado.processarPagamentoExterno(valor, metodo);

        Transacao transacao = new Transacao();
        transacao.setUsuarioId(usuarioId);
        transacao.setValor(valor);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setStatus(result.isSuccess() ? "SUCESSO" : "FALHA");
        transacao.setReferenciaExterna(result.getTransactionId());
        transacao.setMensagem(result.getMessage());
        transacao.setComerciante(comerciante);

        Transacao transacaoSalva = transacaoRepository.save(transacao);

        Map<String, Object> response = new HashMap<>();
        response.put("transacaoId", transacaoSalva.getId().toString());
        response.put("status", transacaoSalva.getStatus());
        response.put("referenciaExterna", transacaoSalva.getReferenciaExterna());
        response.put("mensagem", transacaoSalva.getMensagem());

        return response;
    }
}
