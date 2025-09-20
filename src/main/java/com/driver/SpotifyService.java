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

    public Album createAlbum(String title, String artistName) throws Exception{
        List<Artist>list =spotifyRepository.artistList();
        boolean isContain=false;
        for (Artist artist:list){
            if(artist.getName().equals(artistName)){
                isContain=true;
                break;
            }
        }
        if(!isContain){
            createArtist(artistName);
        }
        Album album=spotifyRepository.createAlbum(title, artistName);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        List<Album> list = spotifyRepository.albumList();
        boolean isContain=false;
        for (Album album :list){
            if(album.getTitle().equals(albumName)){
                isContain=true;
                break;
            }
        }
        if(!isContain){
            throw new Exception("Album does not exist");
        }
        return spotifyRepository.createSong(title, albumName, length);
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        List<User>list=spotifyRepository.userList();
        boolean isContain=false;
        for (User user :list){
            if(user.getMobile().equals(mobile)){
                isContain=true;
                break;
            }
        }
        if(!isContain){
            throw new Exception("User does not exist");
        }
        return spotifyRepository.createPlaylistOnLength(mobile, title, length);
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        List<User> list= spotifyRepository.userList();
        boolean isContain=false;
        for (User user :list){
            if(user.getMobile().equals(mobile)){
                isContain=true;
                break;
            }
        }
        if(!isContain){
            throw new Exception("User does not exist");
        }
        Playlist playlist=spotifyRepository.createPlaylistOnName(mobile, title, songTitles);
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        List<User> list= spotifyRepository.userList();
        boolean isContain=false;
        for (User user :list){
            if(user.getMobile().equals(mobile)){
                isContain=true;
                break;
            }
        }
        if(!isContain){
            throw new Exception("User does not exist");
        }

        List<Playlist>playlistList=spotifyRepository.playlistList();
        isContain=false;
        for (Playlist playlist :playlistList){
            if(playlist.getTitle().equals(playlistTitle)){
                isContain=true;
                break;
            }
        }
        if(!isContain){
            throw new Exception("Playlist does not exist");
        }
        return spotifyRepository.findPlaylist(mobile, playlistTitle);
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        List<User> userList=spotifyRepository.userList();
        boolean isContain=false;
        for (User user:userList){
            if(user.getName().equals(mobile)){
                isContain=true;
                break;
            }
        }
        if(!isContain){
            throw new Exception("User does not exist");
        }

        List<Song> songList=spotifyRepository.songs;
        isContain=false;
        for (Song song :songList){
            if(song.getTitle().equals(songTitle)){
                isContain=true;
                break;
            }
        }
        if(!isContain){
            throw new Exception("Song does not exist");
        }
        return spotifyRepository.likeSong(mobile, songTitle);
    }

    public String mostPopularArtist() {
        return spotifyRepository.mostPopularArtist();
    }

    public String mostPopularSong() {
        return spotifyRepository.mostPopularSong();
    }
}
