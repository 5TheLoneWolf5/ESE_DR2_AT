package com.musicapp.biblioteca.controller;

import com.musicapp.biblioteca.domain.Favorito;
import com.musicapp.biblioteca.domain.Playlist;
import com.musicapp.biblioteca.domain.PlaylistItem;
import com.musicapp.biblioteca.repository.FavoritoRepository;
import com.musicapp.biblioteca.repository.PlaylistRepository;
import com.musicapp.biblioteca.repository.PlaylistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistItemRepository playlistItemRepository;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/usuarios/{usuarioId}/inicializar")
    public ResponseEntity<String> inicializarBiblioteca(@PathVariable Long usuarioId) {
        System.out.println("Parceria (Partnership) ativada: Inicializando biblioteca do usuario ID: " + usuarioId);
        
        Playlist playlistPadrao = new Playlist("Minhas Curtidas", usuarioId);
        playlistRepository.save(playlistPadrao);

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
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    "http://localhost:8083/api/catalogo/musicas/" + musicaId,
                    Map.class
            );
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.badRequest().body("Música não existe no catálogo.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao validar música no catálogo: " + e.getMessage());
            return ResponseEntity.status(503).body("Serviço de catálogo indisponível. Tente novamente mais tarde.");
        }

        if (favoritoRepository.findByUsuarioIdAndMusicaId(usuarioId, musicaId).isPresent()) {
            return ResponseEntity.ok("Música já está nos favoritos.");
        }

        Favorito favorito = favoritoRepository.save(new Favorito(usuarioId, musicaId));
        return ResponseEntity.ok(favorito);
    }

    @GetMapping("/favoritos/usuario/{usuarioId}")
    public List<Favorito> obterFavoritos(@PathVariable Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId);
    }

    @PostMapping("/playlists")
    public Playlist criarPlaylist(@RequestBody Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    @PostMapping("/playlists/{playlistId}/adicionar")
    public ResponseEntity<?> adicionarMusicaNaPlaylist(@PathVariable Long playlistId, @RequestBody Map<String, Long> request) {
        Long musicaId = request.get("musicaId");
        if (musicaId == null) {
            return ResponseEntity.badRequest().body("Música ID obrigatória.");
        }

        Optional<Playlist> playlistOpt = playlistRepository.findById(playlistId);
        if (playlistOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Playlist playlist = playlistOpt.get();

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    "http://localhost:8083/api/catalogo/musicas/" + musicaId,
                    Map.class
            );
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.badRequest().body("Música não existe no catálogo.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(503).body("Serviço de catálogo indisponível para validação.");
        }

        PlaylistItem item = playlistItemRepository.save(new PlaylistItem(playlist, musicaId));
        return ResponseEntity.ok(item);
    }

    @GetMapping("/playlists/usuario/{usuarioId}")
    public List<Playlist> obterPlaylistsDoUsuario(@PathVariable Long usuarioId) {
        return playlistRepository.findByUsuarioId(usuarioId);
    }

    @GetMapping("/playlists/{playlistId}")
    public ResponseEntity<?> obterDetalhesPlaylist(@PathVariable Long playlistId) {
        return playlistRepository.findById(playlistId)
                .map(playlist -> {
                    List<PlaylistItem> itens = playlistItemRepository.findByPlaylistId(playlistId);
                    List<Long> musicasIds = itens.stream().map(PlaylistItem::getMusicaId).collect(Collectors.toList());
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("playlist", playlist);
                    result.put("musicasIds", musicasIds);
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
