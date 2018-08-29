package com.naver.mycnex.viewpageapplication.global;

public class Global {
    //예약가능 여부
    public static Integer RESERVATION_ABLE = 0;
    public static Integer RESERVATION_UNABLE = 1;

    //반려견 사이즈
    public static Integer PETSIZE_SMALL = 1;
    public static Integer PETSIZE_MIDIUM = 2;
    public static Integer PETSIZE_LARGE = 3;
    public static Integer PARKING_ABLE = 1;
    public static Integer PARKING_UNABLE = 2;
    public static Integer PARKING_VALET = 3;

    // 카테고리 관련 스태틱 변수
    public static int CATEGORY_GENERAL = 0; //동반입장
    public static int CATEGORY_SPECIAL = 1; //전용장소

    public static String[] CATEGORY_GENERAL_ARR = {"일반카페","일반식당","일반주점","일반숙박","공원","일반스튜디오"};
    public static int CATEGORY_GENERAL_LENGTH = CATEGORY_GENERAL_ARR.length;
    public static Integer CATEGORY_GENERAL_CAFE = 0;
    public static Integer CATEGORY_GENERAL_RESTAURANT = 1;
    public static Integer CATEGORY_GENERAL_BAR = 2;
    public static Integer CATEGORY_GENERAL_ACCOMMODATION = 3;
    public static Integer CATEGORY_GENERAL_PARK = 4;
    public static Integer CATEGORY_GENERAL_STUDIO = 5;
    public static Integer[] CATEGORY_GENENRAL_ID = {CATEGORY_GENERAL_CAFE,CATEGORY_GENERAL_RESTAURANT,CATEGORY_GENERAL_BAR,CATEGORY_GENERAL_ACCOMMODATION,CATEGORY_GENERAL_PARK,
                                                 CATEGORY_GENERAL_STUDIO};

    //전용장소
    public static String[] CATEGORY_SPECIAL_ARR = {"애견카페","펫샵","애견미용","동물병원","애견분양","애견교육","애견숙박","애견레저","애견장묘","애견놀이터","애견스튜디오"};
    public static int CATEGORY_SPECIAL_LENGTH = CATEGORY_SPECIAL_ARR.length;
    public static Integer CATEGORY_SPECIAL_CAFE = 100;
    public static Integer CATEGORY_SPECIAL_PETSHOP = 101;
    public static Integer CATEGORY_SPECIAL_BEAUTY = 102;
    public static Integer CATEGORY_SPECIAL_HOSPITAL = 103;
    public static Integer CATEGORY_SPECIAL_ADOPT = 104;
    public static Integer CATEGORY_SPECIAL_EDUCATION = 105;
    public static Integer CATEGORY_SPECIAL_ACCOMMODATION = 106;
    public static Integer CATEGORY_SPECIAL_LEISURE = 107;
    public static Integer CATEGORY_SPECIAL_FUNERAL = 108;
    public static Integer CATEGORY_SPECIAL_PLAYGROUND = 109;
    public static Integer CATEGORY_SPECIAL_STUDIO = 110;
    public static Integer[] CATEGORY_SPECIAL_ID = {CATEGORY_SPECIAL_CAFE,CATEGORY_SPECIAL_PETSHOP,CATEGORY_SPECIAL_BEAUTY,CATEGORY_SPECIAL_HOSPITAL,CATEGORY_SPECIAL_ADOPT,
                                                  CATEGORY_SPECIAL_EDUCATION,CATEGORY_SPECIAL_ACCOMMODATION,CATEGORY_SPECIAL_LEISURE,CATEGORY_SPECIAL_FUNERAL,CATEGORY_SPECIAL_PLAYGROUND,
                                                CATEGORY_SPECIAL_STUDIO};
}
