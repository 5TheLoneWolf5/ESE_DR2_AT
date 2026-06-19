package com.musicapp.pagamento.config;

import com.musicapp.pagamento.domain.Cartao;
import com.musicapp.pagamento.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (cartaoRepository.count() == 0) {
            cartaoRepository.save(new Cartao(1L, true, "1234-5678-9012-3456", "12/30"));
            cartaoRepository.save(new Cartao(2L, false, "9876-5432-1098-7654", "08/29"));
        }
    }
}
