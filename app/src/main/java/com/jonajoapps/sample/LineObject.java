package com.jonajoapps.sample;

import java.util.Date;

/**
 * Created by SHAY on 11/23/2017.
 * <p>
 * Simple Object that represent how our model is look like
 * here we hae only title and date
 */

public class LineObject {
    public String title;
    public String imageUrl;
    public Date date;

    public LineObject(String title) {
        this.title = title;
        this.date = new Date();
    }

    public LineObject(String title, long dateInMillis, String imgUrl) {
        this.title = title;
        this.date = new Date(dateInMillis);
        this.imageUrl = imgUrl;
    }
}
