package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile){
        User user = spotifyRepository.createUser(name,mobile);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist=spotifyRepository.createArtist(name);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        List<String>list =spotifyRepository.artistList();
        if(!list.contains(artistName)){
            Artist artist=createArtist(artistName);
        }
        Album album=spotifyRepository.createAlbum(title, artistName);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception {


    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

    }

    public String mostPopularArtist() {

    }

    public String mostPopularSong() {

    }
}
