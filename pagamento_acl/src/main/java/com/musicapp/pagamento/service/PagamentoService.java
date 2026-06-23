package com.musicapp.pagamento.service;

import com.musicapp.pagamento.acl.PagamentoAclService;
import com.musicapp.pagamento.acl.UsuarioPaymentRequest;
import com.musicapp.pagamento.model.Transacao;
import com.musicapp.pagamento.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoAclService pagamentoAclService;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public Map<String, Object> processarPagamento(UsuarioPaymentRequest request) {
        return pagamentoAclService.processarPagamentoUsuario(request);
    }

    public List<Transacao> obterTransacoesPorUsuario(Long usuarioId) {
        return transacaoRepository.findByUsuarioId(usuarioId);
    }
}
