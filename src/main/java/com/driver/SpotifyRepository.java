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
        if(albums.contains(album)){
             return album;
        }
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
                playlistListenerMap.put(playlist, new ArrayList<>(Arrays.asList(user)));
                break;
            }
        }
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        User user =getUserByMobile(mobile);
        if(user ==null){
            throw new Exception("User does not exist");
        }
        Playlist playlist=getPlaylistByTitle(playlistTitle);
        if(playlist==null){
            throw new Exception("Playlist does not exist");
        }
        List<Playlist> userPlaylists = userPlaylistMap.getOrDefault(user, new ArrayList<>());
        if (!userPlaylists.contains(playlist)) {
            userPlaylists.add(playlist);
            userPlaylistMap.put(user, userPlaylists);
        }

        List<User> listeners = playlistListenerMap.getOrDefault(playlist, new ArrayList<>());
        if (!listeners.contains(user)) {
            listeners.add(user);
            playlistListenerMap.put(playlist, listeners);
        }
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

        User user= getUserByMobile(mobile);
        if(user==null){
            throw new Exception("User does not exist");
        }
        Song song=getSongByTitle(songTitle);
        if(song==null){
            throw new Exception("Song does not exist");
        }

        List<User> userList1 =songLikeMap.getOrDefault(song,new ArrayList<>());
        if(userList1.contains(user)){
            return song;
        }
        userList1.add(user);
        songLikeMap.put(song,userList1);
        song.setLikes(song.getLikes()+1);

        Album album=null;
        for (Album album1 :albumSongMap.keySet()){
            List<Song> songList=albumSongMap.get(album1);
            if(songList.contains(song)){
                album=album1;
            }
        }
        if(album==null){
            throw  new Exception("Album does not exist");
        }

        Artist artist=null;
        for(Artist artist1 :artistAlbumMap.keySet()){
            List<Album>albumList=artistAlbumMap.get(artist1);
            if(albumList.contains(album)){
                artist=artist1;
            }
        }
        if(artist==null){
            throw  new Exception("Artist does not exist");
        }
        artist.setLikes(artist.getLikes()+1);

        //public HashMap<Album, List<Song>> albumSongMap;
       //public HashMap<Artist, List<Album>> artistAlbumMap;

        return song;
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

    ///////////////////////////////////////////methods///////////////////////////////////////////////////////////////

    public  Playlist getPlaylistByTitle(String playlistTitle){
        Playlist playlist=null;
        for (Playlist playlist2 :playlists){
            if(playlist2.getTitle().equals(playlistTitle)){
                playlist= playlist2;
            }
        }
        return playlist;
    }
    public Song getSongByTitle(String songTitle){
        List<Song>songList=songs;
        Song song=null;
        for (Song song1:songList){
            if(song1.getTitle().equals(songTitle)){
                song=song1;
            }
        }
        return song;
    }
    public User getUserByMobile(String mobile){
        List<User>userList= users;
        User user= null;
        for (User user1 : userList){
            if(user1.getMobile().equals(mobile)){
                user=user1;
            }
        }
        return user;
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
}
