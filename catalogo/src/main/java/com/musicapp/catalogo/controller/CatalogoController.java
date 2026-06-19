package com.musicapp.catalogo.controller;

import com.musicapp.catalogo.domain.Artista;
import com.musicapp.catalogo.domain.Musica;
import com.musicapp.catalogo.repository.ArtistaRepository;
import com.musicapp.catalogo.repository.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private MusicaRepository musicaRepository;

    @GetMapping("/artistas")
    public List<Artista> listarArtistas() {
        return artistaRepository.findAll();
    }

    @GetMapping("/artistas/{id}")
    public ResponseEntity<Artista> obterArtista(@PathVariable Long id) {
        return artistaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/artistas")
    public Artista criarArtista(@RequestBody Artista artista) {
        return artistaRepository.save(artista);
    }

    @GetMapping("/musicas")
    public List<Musica> listarMusicas() {
        return musicaRepository.findAll();
    }

    @GetMapping("/musicas/{id}")
    public ResponseEntity<Musica> obterMusica(@PathVariable Long id) {
        return musicaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/musicas/artista/{artistaId}")
    public List<Musica> listarMusicasPorArtista(@PathVariable Long artistaId) {
        return musicaRepository.findByArtistaId(artistaId);
    }

    @PostMapping("/musicas")
    public ResponseEntity<?> criarMusica(@RequestBody Musica musica) {
        if (musica.getArtista() != null && musica.getArtista().getId() != null) {
            if (!artistaRepository.existsById(musica.getArtista().getId())) {
                return ResponseEntity.badRequest().body("Artista associado não existe.");
            }
        }
        return ResponseEntity.ok(musicaRepository.save(musica));
    }
}
