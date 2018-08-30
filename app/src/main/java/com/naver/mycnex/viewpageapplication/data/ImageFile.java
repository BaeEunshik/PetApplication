package com.naver.mycnex.viewpageapplication.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Data
public class ImageFile {
    private Long id;
    private String originName;
    private String savedName;
    private Long store_id;
}
