package com.naver.mycnex.viewpageapplication.data;

import android.content.Intent;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Comment {
    private Integer id;
    private Intent year;
    private Intent month;
    private Intent day;
    private String comment;
    private Integer ComCount;//댓글수


    public Comment(Integer id, Intent year, Intent month, Intent day, String comment, Integer comCount) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.comment = comment;
        ComCount = comCount;
    }

    public Comment(Integer id, String comment, Integer ComCount) {
        this.id = id;
        this.comment = comment;
        this.ComCount = ComCount;

    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getComCount() {
        return ComCount;
    }

    public void setComCount(Integer comCount) {
        ComCount = comCount;
    }

}
