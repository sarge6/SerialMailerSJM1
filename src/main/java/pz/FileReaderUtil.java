package pz;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileReaderUtil {
    public static final String PATTERN_DT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_D = "yyyy-MM-dd";
    public static final String PATTERN_DT_DE = "dd.MM.yyyy HH:mm:ss";
    public static String getThisProjectPath() {
        return System.getProperty("user.dir");
    }
    public static String getUserDirPath() { return System.getProperty("user.home"); }
    public static String getUserDirDocumentsPath() {
        String dp = System.getProperty("user.home") +
                System.getProperty("file.separator") +
                "Documents";
        return dp;
    }
    public static String buildDateTimeStamp(){
        String dtStamp = "";
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern(PATTERN_DT_DE);
            dtStamp = now.format(formatter);
        } catch (Exception e) { return ""; }
        return dtStamp;
                
    }
    public static String buildDateStamp(){
        String dtStamp = "";
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(PATTERN_D);
            dtStamp = now.format(formatter);
        } catch (Exception e) { return ""; }
        return dtStamp;

    }
    public static String readFileIntoStringJ11(File file){
        String content = "";
        if(!file.isFile()) {return ""; }        
        String filepath = file.getAbsolutePath();
        try {        
            content = Files.readString(file.toPath());
        } catch (IOException e) { return "-1"; }        
        return content;
    }
    public static String readFileLinesIntoStringJ8(File file){
        String content = "";
        if(!file.isFile()) {return ""; }        
        String filepath = file.getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(
            Paths.get(filepath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> sb.append(s).append("\n"));
            content = sb.toString();
        } catch (IOException e) { return "-1"; }
        return content;
    }
    public static String readFileBytesToStringJ7(File file){
        String content = "";
        if(!file.isFile()) {return ""; }        
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            content = new String(bytes);
        } catch (IOException e) { return "-1"; }
        return content;
    }
    public static String readFileLinesToStringJ6(File file){
        String content = "";
        if(!file.isFile()) {return ""; } 
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
            new FileReader(file.getAbsolutePath()))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine).append("\n");
            }
            content = sb.toString();
        } catch (IOException e) { return "-1"; }
        return content;
    }
    public static String readhtmlFileLinesToTrimStringJ6(File file){
        String content = "";
        if(!file.isFile()) {return ""; }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new FileReader(file.getAbsolutePath(), StandardCharsets.UTF_8))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                if(!sCurrentLine.isBlank()){
                    sb.append(sCurrentLine.trim());
                    sb.append("\n");
                }
            }
            content = sb.toString();
        } catch (IOException e) { return ""; }
        return content.trim();
    }
    public static Map<String, String> readASPFileLinesIntoMap(File file, String fieldSep){
        //"Sehr geehrte(r) Herr XXX"; "xxx.xxx@mailer.com"
        if(fieldSep.isBlank()) {fieldSep = ";"; }
        Map<String, String> map = new HashMap<>();        
        if(!file.isFile()) {return map; }        
        String filepath = file.getAbsolutePath();
        try{
        List<String> list = Files.readAllLines(
            file.toPath(), StandardCharsets.UTF_8);
        if(list.isEmpty()){ return map; }        
        for(String item : list){
            if( (!item.isBlank())&(item.contains(fieldSep)) ){                
                String[]a = item.split(fieldSep);
                map.put(a[1].trim(), a[0].trim());
            }
        }      
        } catch (Exception e) { return map; }
        return map;
    }
    public static boolean createAndAppendLogTextFile(File dir, String filename, String line){
        boolean ok = false;
        if( (dir == null)||(!dir.isDirectory()) ){
            dir = new File(getUserDirDocumentsPath());
        }
        if(filename.isBlank()){
            filename = buildDateStamp() + "_" + "LOG.txt";
        }
        File file = new File(dir, filename);
        try (BufferedWriter writer = Files.newBufferedWriter(
                file.toPath(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND))
        {
            if( (line != null)&&(!line.isBlank()) ){
                writer.write(line);
                writer.newLine();
                ok = true;
            }
        } catch (Exception e) { ok = false; };
        return ok;
    }
    public static boolean killExistingLogTextFile(File dir, String filename){
        boolean ok = false;
        if( (dir == null)||(!dir.isDirectory()) ){ return false; }
        if(filename.isBlank()){ return false; }
        File file = new File(dir, filename);
        if(!file.isFile()) { return false; }
        try{ ok = file.delete(); }
        catch (Exception e) { ok = false; }
        return ok;
    }
}
