package com.musicapp.pagamento.regras;

import com.musicapp.pagamento.acl.UsuarioPaymentRequest;
import com.musicapp.pagamento.model.Cartao;
import com.musicapp.pagamento.model.Transacao;
import com.musicapp.pagamento.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ValidadorAltaFrequencia implements ValidadorTransacao {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Override
    public void validar(UsuarioPaymentRequest request, Cartao cartao) throws ValidacaoException {
        LocalDateTime limit = LocalDateTime.now().minusMinutes(2);
        List<Transacao> recent = transacaoRepository.findByUsuarioIdAndDataHoraAfter(request.getUsuarioId(), limit);
        if (recent.size() >= 3) {
            throw new ValidacaoException("alta-frequência-pequeno-intervalo");
        }
    }
}
