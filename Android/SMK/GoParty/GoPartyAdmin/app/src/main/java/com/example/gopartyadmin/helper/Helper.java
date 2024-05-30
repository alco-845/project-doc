package com.example.gopartyadmin.helper;

import android.text.TextUtils;

import java.text.DecimalFormat;

public class Helper {
    public static String setDatePicker(int year , int month, int day) {
        String bln, hri ;
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

        return String.valueOf(year) + bln+hri;
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

    public static String numberFormat(String number){ // Rp. 1,000,000.00
        try{
            String hasil = "";
            String[] b = number.split("\\.") ;

            if(b.length == 1){
                String[] a = number.split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
            } else {
                String[] a = b[0].split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
                hasil+=","+b[1] ;
            }
            return  hasil ;
        }catch (Exception e){
            return  "" ;
        }
    }

    public static String removeE(double value){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return numberFormat(df.format(value)) ;
    }

    public static String removeE(String value){
        double hasil = Double.parseDouble(value) ;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return numberFormat(df.format(hasil)) ;
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
