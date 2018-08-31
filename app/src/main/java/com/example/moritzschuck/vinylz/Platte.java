package com.example.moritzschuck.vinylz;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Platte {

    @NonNull
    @PrimaryKey (autoGenerate = true)
    private long plattenID;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "band")
    private String band;

    @ColumnInfo(name = "year")
    private String year;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "edition")
    private String edition;

    @ColumnInfo(name = "genre")
    private String genre;

    @ColumnInfo(name = "coverSrc")
    private String coverSrc;

    @NonNull
    @ColumnInfo(name = "fav")
    private boolean fav;


    @NonNull
    public long getPlattenID() {
        return plattenID;
    }
    public String getTitle() {
        return title;
    }
    public String getBand() {
        return band;
    }
    public String getYear() {
        return year;
    }
    public String getLocation() {
        return location;
    }
    public String getPrice() {
        return price;
    }
    public String getEdition() {
        return edition;
    }
    public String getGenre() {
        return genre;
    }
    public String getCoverSrc() {
        return coverSrc;
    }
    @NonNull
    public boolean getFav(){
        return fav;
    }


    public void setPlattenID(@NonNull long plattenID) {
        this.plattenID = plattenID;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBand(String band) {
        this.band = band;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setEdition(String edition) {
        this.edition = edition;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setCoverSrc(String coverSrc) {
        this.coverSrc = coverSrc;
    }
    public void setFav(@NonNull boolean fav){
        this.fav = fav;
    }

    public Platte(){

    }



}
