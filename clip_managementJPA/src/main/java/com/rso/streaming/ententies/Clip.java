package com.rso.streaming.ententies;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "clip")
@Table
@NamedQuery(name="Clip.findAll", query="SELECT c FROM clip c")
public class Clip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    private String author;
    private String title;
    private Date date;

    @ManyToOne
    private Album album;

    private int track_number;

    public Clip(String author, String title, Date date, Album album, int track_number) {
        super();
        this.author = author;
        this.title = title;
        this.date = date;
        this.album = album;
        this.track_number = track_number;
    }

    public Clip() {
        super();
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public int getTrack_number() {
        return track_number;
    }

    public void setTrack_number(int track_number) {
        this.track_number = track_number;
    }

    @Override
    public String toString() {
        return "Clip{" +
                "ID=" + ID +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", album=" + album +
                ", track_number=" + track_number +
                '}';
    }
}
