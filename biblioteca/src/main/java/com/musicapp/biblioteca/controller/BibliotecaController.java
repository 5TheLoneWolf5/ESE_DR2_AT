package com.musicapp.biblioteca.controller;

import com.musicapp.biblioteca.model.Favorito;
import com.musicapp.biblioteca.model.Playlist;
import com.musicapp.biblioteca.model.PlaylistItem;
import com.musicapp.biblioteca.service.BibliotecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {

    @Autowired
    private BibliotecaService bibliotecaService;

    @PostMapping("/usuarios/{usuarioId}/inicializar")
    public ResponseEntity<String> inicializarBiblioteca(@PathVariable Long usuarioId) {
        bibliotecaService.inicializarBiblioteca(usuarioId);
        return ResponseEntity.ok("Biblioteca inicializada com sucesso");
    }

    @PostMapping("/favoritos")
    public ResponseEntity<?> adicionarFavorito(@RequestBody Map<String, Long> request) {
        Long usuarioId = request.get("usuarioId");
        Long musicaId = request.get("musicaId");

        if (usuarioId == null || musicaId == null) {
            return ResponseEntity.badRequest().body("Parâmetros usuarioId e musicaId obrigatórios.");
        }

        try {
            Optional<Favorito> favorito = bibliotecaService.adicionarFavorito(usuarioId, musicaId);
            if (favorito.isEmpty()) {
                return ResponseEntity.ok("Música já está nos favoritos.");
            }
            return ResponseEntity.ok(favorito.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(503).body(e.getMessage());
        }
    }

    @GetMapping("/favoritos/usuario/{usuarioId}")
    public List<Favorito> obterFavoritos(@PathVariable Long usuarioId) {
        return bibliotecaService.obterFavoritos(usuarioId);
    }

    @PostMapping("/playlists")
    public Playlist criarPlaylist(@RequestBody Playlist playlist) {
        return bibliotecaService.criarPlaylist(playlist);
    }

    @PostMapping("/playlists/{playlistId}/adicionar")
    public ResponseEntity<?> adicionarMusicaNaPlaylist(@PathVariable Long playlistId, @RequestBody Map<String, Long> request) {
        Long musicaId = request.get("musicaId");
        if (musicaId == null) {
            return ResponseEntity.badRequest().body("Música ID obrigatória.");
        }

        try {
            PlaylistItem item = bibliotecaService.adicionarMusicaNaPlaylist(playlistId, musicaId);
            return ResponseEntity.ok(item);
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(503).body(e.getMessage());
        }
    }

    @GetMapping("/playlists/usuario/{usuarioId}")
    public List<Playlist> obterPlaylistsDoUsuario(@PathVariable Long usuarioId) {
        return bibliotecaService.obterPlaylistsDoUsuario(usuarioId);
    }

    @GetMapping("/playlists/{playlistId}")
    public ResponseEntity<?> obterDetalhesPlaylist(@PathVariable Long playlistId) {
        return bibliotecaService.obterDetalhesPlaylist(playlistId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
