package com.gulles.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gulles/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return songRepository.save(song);
    }

    @GetMapping("/{id}")
    public Song getSongById(@PathVariable Long id) {
        Optional<Song> song = songRepository.findById(id);
        return song.orElse(null); // Postman expects direct object
    }

    @DeleteMapping("/{id}")
    public String deleteSong(@PathVariable Long id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return "Song with ID " + id + " deleted.";
        } else {
            return "Song with ID " + id + " not found.";
        }
    }

    @PutMapping("/{id}")
    public Song updateSong(@PathVariable Long id, @RequestBody Song updatedSong) {
        Optional<Song> optionalSong = songRepository.findById(id);

        if (optionalSong.isPresent()) {
            Song song = optionalSong.get();
            song.setTitle(updatedSong.getTitle());
            song.setArtist(updatedSong.getArtist());
            song.setAlbum(updatedSong.getAlbum());
            song.setGenre(updatedSong.getGenre());
            song.setUrl(updatedSong.getUrl());

            return songRepository.save(song);
        } else {
            return null;
        }
    }

    @GetMapping("/search/{keyword}")
    public List<Song> searchSongs(@PathVariable String keyword) {
        return songRepository
                .findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCaseOrGenreContainingIgnoreCase(
                        keyword, keyword, keyword, keyword);
    }
}