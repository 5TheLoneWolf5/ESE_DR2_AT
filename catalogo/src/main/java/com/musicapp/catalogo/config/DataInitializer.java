package com.musicapp.catalogo.config;

import com.musicapp.catalogo.domain.Artista;
import com.musicapp.catalogo.domain.Musica;
import com.musicapp.catalogo.repository.ArtistaRepository;
import com.musicapp.catalogo.repository.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private MusicaRepository musicaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (artistaRepository.count() == 0) {
            Artista daftPunk = artistaRepository.save(new Artista("Daft Punk", "Electronic"));
            Artista queen = artistaRepository.save(new Artista("Queen", "Rock"));
            Artista billieEilish = artistaRepository.save(new Artista("Billie Eilish", "Pop"));

            musicaRepository.save(new Musica("Get Lucky", 369, "http://stream.musicapp.com/daftpunk/getlucky.mp3", daftPunk));
            musicaRepository.save(new Musica("One More Time", 320, "http://stream.musicapp.com/daftpunk/onemoretime.mp3", daftPunk));
            
            musicaRepository.save(new Musica("Bohemian Rhapsody", 354, "http://stream.musicapp.com/queen/bohemianrhapsody.mp3", queen));
            musicaRepository.save(new Musica("Don't Stop Me Now", 209, "http://stream.musicapp.com/queen/dontstopmenow.mp3", queen));

            musicaRepository.save(new Musica("Bad Guy", 194, "http://stream.musicapp.com/billie/badguy.mp3", billieEilish));
        }
    }
}
