package com.example.moritzschuck.vinylz;

public class Vinyl {


    private String title;
    private String band;
    private String year;
    private String price;
    private int coverSrc;

    public Vinyl(String title, String band, String year, String price, int coverSrc) {
        super();
        this.band = band;
        this.title = title;
        this.year = year;
        this.price = price;
        this.coverSrc = coverSrc;
    }

    public String getTitle() {
        return title;
    }
    public String getYear() {
        return year;
    }
    public int getCoverSrc() {
        return coverSrc;
    }
    public String getPrice() {
        return price;
    }
    public String getBand(){
        return band;
    }
}
