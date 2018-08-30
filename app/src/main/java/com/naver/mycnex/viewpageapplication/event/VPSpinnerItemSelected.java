package com.naver.mycnex.viewpageapplication.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class VPSpinnerItemSelected {
    private int location_idx;
    private int size_idx;
    private int general_idx;
    private int special_idx;
}
