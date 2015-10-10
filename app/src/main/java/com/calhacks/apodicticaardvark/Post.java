package com.calhacks.apodicticaardvark;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by John on 10/10/2015.
 */
public class Post {
    @SerializedName("id")
    public long ID;
    public String title;
    public String author;
    public String url;
    @SerializedName("date")
    public Date dateCreated;
    public String body;
    public List tags;

    public Post(){

    }

    public class Tag{
        public String name;
        public String url;

        public Tag(){

        }
    }
}