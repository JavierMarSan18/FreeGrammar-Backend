package edu.jarkvin.freegrammar.util;

import java.util.Random;

public class StringUtils {

    private StringUtils(){

    }
    //Elimina el primero y último caracter indicado.
    public static String trimByChar(String str, String c){
        int minLength = 2;
        //Valida si la longitud de la cadena es mayor a 2 y si contiene a c.
        if(str.length() > minLength && str.contains(c)){
            //Valida si la cadena comienza con c y recorta
            if(str.substring(0, 1).equals(c)){
                str = str.substring(1);
            }
//            //Valida si la cadena termina con c y recorta.
            if(str.substring(str.length()-1).equals(c)){
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }
}
