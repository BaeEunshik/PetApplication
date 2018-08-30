package com.naver.mycnex.viewpageapplication.data;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class StoreImage {
    private Store store;
    private ArrayList<ImageFile> image;
}
