package com.example.moritzschuck.vinylz;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by cip on 13.08.2018.
 */

@Entity
public class Platte {

    @NonNull
    @PrimaryKey (autoGenerate = true)

    private long plattenID;
    private String title, band, year, location, price, edition, genre, coverSrc;
    private boolean fav;

    public Platte(){

    }

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
    public void setFav(boolean fav){
        this.fav = fav;
    }



}
