package com.example.solfamidasback.utilities;

public class Utilities {

    public static String createLink(){
        String link = java.util.UUID.randomUUID().toString();
        link.replaceAll("-", "");
        link.substring(0, 32);
        return link;
    }
}
