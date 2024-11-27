package pz;

import javax.swing.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SimpleEncryption {
    private static Map<String, String> cube = new HashMap<>();
    static{
        //http://www.brianveitch.com/websites/cryptography/random.html
        cube.put("A", "S");
        cube.put("B", "B");
        cube.put("C", "X");
        cube.put("D", "E");
        cube.put("E", "M");
        cube.put("F", "U");
        cube.put("G", "G");
        cube.put("H", "R");
        cube.put("I", "C");
        cube.put("J", "P");
        cube.put("K", "Z");
        cube.put("L", "D");
        cube.put("M", "H");
        cube.put("N", "L");
        cube.put("O", "K");
        cube.put("P", "Y");
        cube.put("Q", "W");
        cube.put("R", "T");
        cube.put("S", "V");
        cube.put("T", "A");
        cube.put("U", "J");
        cube.put("V", "I");
        cube.put("W", "Q");
        cube.put("X", "O");
        cube.put("Y", "F");
        cube.put("Z", "N");
        cube.put("a", "s");
        cube.put("b", "b");
        cube.put("c", "x");
        cube.put("d", "e");
        cube.put("e", "m");
        cube.put("f", "u");
        cube.put("g", "g");
        cube.put("h", "r");
        cube.put("i", "c");
        cube.put("j", "p");
        cube.put("k", "z");
        cube.put("l", "d");
        cube.put("m", "h");
        cube.put("n", "l");
        cube.put("o", "k");
        cube.put("p", "y");
        cube.put("q", "w");
        cube.put("r", "t");
        cube.put("s", "v");
        cube.put("t", "a");
        cube.put("u", "j");
        cube.put("v", "i");
        cube.put("w", "q");
        cube.put("x", "o");
        cube.put("y", "f");
        cube.put("z", "n");
    }
    public static String encodeB64(String plainText){
        byte[] bytes = plainText.getBytes();
        String encString = Base64.getEncoder().encodeToString(bytes);
        return encString;
    }
    public static String decodeB64(String cypherText){
        byte[] bytes = Base64.getDecoder().decode(cypherText);
        String decString = new String(bytes);
        return decString;
    }
    public static String scrambleEncryption(String plainText){
        if(plainText.isBlank()){ return ""; }
        StringBuilder sb = new StringBuilder();
        String plainLetter = "", cypherLetter = "";
        Character c = null;
        for (int i = 0; i < plainText.length(); ++i) {
            c = plainText.charAt(i);
            plainLetter = String.valueOf(c);
            if(isSimpleLetter(c)) {
            //if (cube.containsKey(plainLetter.toUpperCase())) {}
                cypherLetter = cube.get(plainLetter);
                sb.append(cypherLetter);
            }
            else{
                sb.append(plainLetter);
            }
        }
        return sb.toString();
    }
    public static String scrambleDecryption(String cypherText){
        if(cypherText.isBlank()){ return ""; }
        StringBuilder sb = new StringBuilder();
        String plainLetter = "", cypherLetter = "";
        Character c = null;
        for (int i = 0; i < cypherText.length(); ++i) {
            c = cypherText.charAt(i);
            cypherLetter = String.valueOf(c);
            if(isSimpleLetter(c)) {
            //if(Character.isLetter(c)){} - ÄÖÜß are Letters!
                plainLetter = getMapKeyByValue(cube, cypherLetter);
                sb.append(plainLetter);
            }
            else{
                sb.append(cypherLetter);
            }
        }
        return sb.toString();
    }
    public static String getMapKeyByValue(Map<String, String> map, String value) {
        if( (value.isBlank())||(map.isEmpty()) ) { return ""; }
        String valueFound = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                valueFound = entry.getKey();
            }
        }
        return valueFound;
    }
    public static boolean hasOnlyLetters(String str) {
        return ((str != null) && (!str.equals(""))
                && (str.matches("^[a-zA-Z]*$")));
    }
    public static boolean isSimpleLetter(Character c){
        if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') return true;
        else return false;
    }
    public static void dialogEncodeWithBase64(){
        String result = "";
        String input = JOptionPane.showInputDialog("TextInput to encrypt");
        if( (input != null)&&(!input.isBlank()) ){ result = encodeB64(input); }
        input = JOptionPane.showInputDialog("Decryption Result:", result);
    }

}
