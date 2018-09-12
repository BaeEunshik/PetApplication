package com.naver.mycnex.viewpageapplication.data;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StoreData {
    private Store store;
    private ArrayList<ImageFile> images;
    private ArrayList<Review> reviews;
    private ArrayList<Member> members;
    private Bookmark bookmarks;

    public StoreData(Store store, ArrayList<ImageFile> images, ArrayList<Review> reviews, ArrayList<Member> members, Bookmark bookmarks) {
        this.store = store;
        this.images = images;
        this.reviews = reviews;
        this.members = members;
        this.bookmarks = bookmarks;
    }

    public StoreData(Store store, ArrayList<ImageFile> images, ArrayList<Review> reviews) {
        this.store = store;
        this.images = images;
        this.reviews = reviews;
    }

    public StoreData(Store store, ArrayList<ImageFile> images, ArrayList<Review> reviews, ArrayList<Member> members) {
        this.store = store;
        this.images = images;
        this.reviews = reviews;
        this.members = members;
    }
}
