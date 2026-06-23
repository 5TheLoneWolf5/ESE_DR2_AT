package com.musicapp.catalogo.service;

import com.musicapp.catalogo.model.Artista;
import com.musicapp.catalogo.model.Musica;
import com.musicapp.catalogo.repository.ArtistaRepository;
import com.musicapp.catalogo.repository.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogoService {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private MusicaRepository musicaRepository;

    public List<Artista> listarArtistas() {
        return artistaRepository.findAll();
    }

    public Optional<Artista> obterArtista(Long id) {
        return artistaRepository.findById(id);
    }

    public Artista criarArtista(Artista artista) {
        return artistaRepository.save(artista);
    }

    public List<Musica> listarMusicas() {
        return musicaRepository.findAll();
    }

    public Optional<Musica> obterMusica(Long id) {
        return musicaRepository.findById(id);
    }

    public List<Musica> listarMusicasPorArtista(Long artistaId) {
        return musicaRepository.findByArtistaId(artistaId);
    }

    public Musica criarMusica(Musica musica) {
        if (musica.getArtista() != null && musica.getArtista().getId() != null) {
            if (!artistaRepository.existsById(musica.getArtista().getId())) {
                throw new IllegalArgumentException("Artista associado não existe.");
            }
        }
        return musicaRepository.save(musica);
    }
}
