package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public List<Artist> artistList(){
        List<Artist> List =new ArrayList<>();
        for (Artist artist:artists){
            List.add(artist);
        }
        return List;
    }
    public List<Album> albumList() throws Exception{
        List<Album> albumList =new ArrayList<>();
        for (Album album:albums){
            albumList.add(album);
        }
        return albumList;
    }
    public List<User> userList(){
        return users;
    }
    public List<Playlist> playlistList(){
        return playlists;
    }

    public User createUser(String name, String mobile) {
        User newUser= new User(name,mobile);
        users.add(newUser);
        return newUser;
    }

    public Artist createArtist(String name) {
        Artist artist= new Artist(name);
        artists.add(artist);
        artistAlbumMap.put(artist,new ArrayList<>());
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Album album= new Album(title);
        albums.add(album);
        albumSongMap.put(album,new ArrayList<>());
        for(Artist artist:artistAlbumMap.keySet()){
            if(artist.getName().equals(artistName)){
                List<Album> albums1=artistAlbumMap.get(artist);
                albums1.add(album);
                break;
            }
        }
        return album;
    }


    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song= new Song(title, length);
        songs.add(song);
        for(Album album:albumSongMap.keySet()){
            if(album.getTitle().equals(albumName)){
                List<Song> songs1 =albumSongMap.get(album);
                songs1.add(song);
                break;
            }
        }

        return song;
    }


    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist playlist=new Playlist(title);
        playlists.add(playlist);
        List<Song>songList=new ArrayList<>();
        for (Song song: songs){
            if(length==song.getLength()){
                songList.add(song);
            }
        }
        playlistSongMap.put(playlist,songList);

        List<User> userList =new ArrayList<>();
        for (User user: users){
            if(user.getMobile().equals(mobile)){
                userList.add(user);
            }
        }
        playlistListenerMap.put(playlist,userList);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist= new Playlist(title);
        playlists.add(playlist);
        List<Song>songList=new ArrayList<>();
        for (Song song: songs){
            for (String song1:songTitles){
                if(song.getTitle().equals(song1)){
                    songList.add(song);
                }
            }
        }
        playlistSongMap.put(playlist,songList);

        List<User>userList= userList();
        for(User user: userList){
            if(user.getMobile().equals(mobile)){
                creatorPlaylistMap.put(user,playlist);
                break;
            }
        }
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        List<User>userList= userList();
        for(User user: userList){
            if(user.getMobile().equals(mobile)){

            }
        }
        Playlist playlist1;
        for (Playlist playlist:playlists){
            if(playlist.getTitle().equals(playlistTitle)){

            }
        }
        return new Playlist();
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        return new Song();
    }

    public String mostPopularArtist() {
        List<Artist>artistList=artistList();
        int maxLikes=Integer.MIN_VALUE;
        String artistName="";
        for (Artist artist:artistList){
            if(artist.getLikes()>=maxLikes){
                maxLikes= artist.getLikes();
                artistName=artist.getName();
            }
        }
        return  artistName;
    }

    public String mostPopularSong() {
        List<Song>songList=songs;
        int maxLikes=Integer.MIN_VALUE;
        String songName ="";
        for (Song song:songList){
            if(song.getLikes()>=maxLikes){
                maxLikes= song.getLikes();
                songName =song.getTitle();
            }
        }
        return songName;
    }
}
