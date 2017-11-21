package com.rso.streaming.ententies;

import javax.persistence.*;
import java.util.List;

@Entity(name = "album")
@Table
@NamedQuery(name="Album.findAll", query="SELECT a FROM album a")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    private String title;
    private String artist;

    @OneToMany
    private List<Clip> clips;

    public Album(String title, List<Clip> clips) {
        super();
        this.clips = clips;
        this.title = title;
    }

    public Album() {
        super();
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Clip> getClips() {
        return clips;
    }

    public void setClips(List<Clip> clips) {
        this.clips = clips;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Album{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", clips=" + clips +
                '}';
    }
}
