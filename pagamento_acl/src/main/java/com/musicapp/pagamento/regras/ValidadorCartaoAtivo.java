package com.musicapp.pagamento.regras;

import com.musicapp.pagamento.acl.UsuarioPaymentRequest;
import com.musicapp.pagamento.model.Cartao;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCartaoAtivo implements ValidadorTransacao {

    @Override
    public void validar(UsuarioPaymentRequest request, Cartao cartao) throws ValidacaoException {
        if (cartao == null || !cartao.isAtivo()) {
            throw new ValidacaoException("cartão não ativo");
        }
    }
}
