package com.musicapp.catalogo.controller;

import com.musicapp.catalogo.model.Artista;
import com.musicapp.catalogo.model.Musica;
import com.musicapp.catalogo.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/artistas")
    public List<Artista> listarArtistas() {
        return catalogoService.listarArtistas();
    }

    @GetMapping("/artistas/{id}")
    public ResponseEntity<Artista> obterArtista(@PathVariable Long id) {
        return catalogoService.obterArtista(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/artistas")
    public Artista criarArtista(@RequestBody Artista artista) {
        return catalogoService.criarArtista(artista);
    }

    @GetMapping("/musicas")
    public List<Musica> listarMusicas() {
        return catalogoService.listarMusicas();
    }

    @GetMapping("/musicas/{id}")
    public ResponseEntity<Musica> obterMusica(@PathVariable Long id) {
        return catalogoService.obterMusica(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/musicas/artista/{artistaId}")
    public List<Musica> listarMusicasPorArtista(@PathVariable Long artistaId) {
        return catalogoService.listarMusicasPorArtista(artistaId);
    }

    @PostMapping("/musicas")
    public ResponseEntity<?> criarMusica(@RequestBody Musica musica) {
        try {
            Musica novaMusica = catalogoService.criarMusica(musica);
            return ResponseEntity.ok(novaMusica);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
