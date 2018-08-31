package com.naver.mycnex.viewpageapplication.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Mark {
    private long id;
    private long store_id;
    private double lat;
    private double lng;
}
