package com.example.birdbsuser.helper;

public class Helper {
    public static String convertDate(String date){
        try {
            String[] a = date.split("/") ;
            return a[2]+a[1]+a[0];
        } catch (Exception e){
            return "";
        }
    }

    public static String setDatePickerNormal(int year , int month, int day) {
        String bln,hri ;
        if(month < 10){
            bln = "0"+ String.valueOf(month) ;
        } else {
            bln = String.valueOf(month) ;
        }

        if(day < 10){
            hri = "0"+ String.valueOf(day) ;
        } else {
            hri = String.valueOf(day) ;
        }

        return hri+"/"+bln+"/"+String.valueOf(year);
    }

    public static String dateToNormal(String date) {
        try {
            String b1 = date.substring(4);
            String b2 = b1.substring(2);

            String m = b1.substring(0, 2);
            String d = b2.substring(0, 2);
            String y = date.substring(0, 4);
            return d + "/" + m + "/" + y;
        } catch (Exception e) {
            return "Belum Selesai";
        }
    }
}
