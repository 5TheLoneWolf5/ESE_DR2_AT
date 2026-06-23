package com.musicapp.biblioteca.service;

import com.musicapp.biblioteca.model.Favorito;
import com.musicapp.biblioteca.model.Playlist;
import com.musicapp.biblioteca.model.PlaylistItem;
import com.musicapp.biblioteca.repository.FavoritoRepository;
import com.musicapp.biblioteca.repository.PlaylistRepository;
import com.musicapp.biblioteca.repository.PlaylistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BibliotecaService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistItemRepository playlistItemRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void inicializarBiblioteca(Long usuarioId) {
        System.out.println("Parceria (Partnership) ativada: Inicializando biblioteca do usuario ID: " + usuarioId);
        
        Playlist playlistPadrao = new Playlist("Minhas Curtidas", usuarioId);
        playlistRepository.save(playlistPadrao);
    }

    public Optional<Favorito> adicionarFavorito(Long usuarioId, Long musicaId) {
        validarMusicaNoCatalogo(musicaId);

        if (favoritoRepository.findByUsuarioIdAndMusicaId(usuarioId, musicaId).isPresent()) {
            return Optional.empty();
        }

        Favorito favorito = favoritoRepository.save(new Favorito(usuarioId, musicaId));
        return Optional.of(favorito);
    }

    public List<Favorito> obterFavoritos(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId);
    }

    public Playlist criarPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public PlaylistItem adicionarMusicaNaPlaylist(Long playlistId, Long musicaId) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(playlistId);
        if (playlistOpt.isEmpty()) {
            throw new NoSuchElementException("Playlist não encontrada.");
        }
        Playlist playlist = playlistOpt.get();

        validarMusicaNoCatalogoParaPlaylist(musicaId);

        return playlistItemRepository.save(new PlaylistItem(playlist, musicaId));
    }

    public List<Playlist> obterPlaylistsDoUsuario(Long usuarioId) {
        return playlistRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Map<String, Object>> obterDetalhesPlaylist(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .map(playlist -> {
                    List<PlaylistItem> itens = playlistItemRepository.findByPlaylistId(playlistId);
                    List<Long> musicasIds = itens.stream().map(PlaylistItem::getMusicaId).collect(Collectors.toList());
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("playlist", playlist);
                    result.put("musicasIds", musicasIds);
                    return result;
                });
    }

    private void validarMusicaNoCatalogo(Long musicaId) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    "http://localhost:8083/api/catalogo/musicas/" + musicaId,
                    Map.class
            );
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalArgumentException("Música não existe no catálogo.");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao validar música no catálogo: " + e.getMessage());
            throw new IllegalStateException("Serviço de catálogo indisponível. Tente novamente mais tarde.");
        }
    }

    private void validarMusicaNoCatalogoParaPlaylist(Long musicaId) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    "http://localhost:8083/api/catalogo/musicas/" + musicaId,
                    Map.class
            );
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalArgumentException("Música não existe no catálogo.");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Serviço de catálogo indisponível para validação.");
        }
    }
}
