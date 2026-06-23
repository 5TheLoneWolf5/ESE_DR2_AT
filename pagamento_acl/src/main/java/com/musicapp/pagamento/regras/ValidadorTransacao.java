package com.musicapp.pagamento.regras;

import com.musicapp.pagamento.acl.UsuarioPaymentRequest;
import com.musicapp.pagamento.model.Cartao;

public interface ValidadorTransacao {
    void validar(UsuarioPaymentRequest request, Cartao cartao) throws ValidacaoException;
}
