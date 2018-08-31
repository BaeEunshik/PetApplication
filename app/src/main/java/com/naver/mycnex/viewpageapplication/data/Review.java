package com.naver.mycnex.viewpageapplication.data;


import android.content.Intent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Review {
    private  float rating;//별점레이팅
    private long id;//아이디
    private Intent year;
    private Intent month;
    private Intent day;
    private String content;//리뷰
    private Integer TextPoint;//별점포인트

    public Review(float rating, long id, Intent year, Intent month, Intent day, String content, Integer textPoint) {
        this.rating = rating;
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = content;
        TextPoint = textPoint;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Intent getYear() {
        return year;
    }

    public void setYear(Intent year) {
        this.year = year;
    }

    public Intent getMonth() {
        return month;
    }

    public void setMonth(Intent month) {
        this.month = month;
    }

    public Intent getDay() {
        return day;
    }

    public void setDay(Intent day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTextPoint() {
        return TextPoint;
    }

    public void setTextPoint(Integer textPoint) {
        TextPoint = textPoint;
    }
}
