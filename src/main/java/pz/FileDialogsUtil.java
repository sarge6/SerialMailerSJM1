package pz;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class FileDialogsUtil {
    public static final String PATTERN_DATE_DE = "dd.MM.yyyy";
    public static final String PATTERN_DATE_US = "MM-dd-yyyy";
    public static final String PATTERN_DATE_UK = "dd-MM-yyyy";
    public static final String PATTERN_DATETIME_DE = "dd.MM.yyyy'T'HH:mm:ss";
    public static final String PATTERN_DATE_PREFIX = "yyMMdd";
    public static final String PATTERN_DATETIME_PREFIX = "yyMMdd'T'HHmmss";
    public static final File PROJECT_DIR = new File(System.getProperty("user.dir"));
    public static final File USER_DIR = new File(System.getProperty("user.home"));
    public static final File USER_DOCS_DIR = new File(USER_DIR, "Documents");
    public static final FileFilter FILTER_JSON =
            new FileNameExtensionFilter("JSON-Files", "json", "JSON");
    public static final FileFilter FILTER_TXT =
            new FileNameExtensionFilter("TXT-Files", "txt", "TXT");
    public static final FileFilter FILTER_HTML =
            new FileNameExtensionFilter("HTML-Files", "html", "htm", "HTML", "HTM");
    public static final FileFilter FILTER_PDF =
            new FileNameExtensionFilter("PDF-Files", "pdf", "PDF");
    public static final int DIRS_AND_FILES = 2;
    public static final int DIRS_ONLY = 1;
    public static final int FILES_ONLY = 0;
    public static final int MSG_CODE_INFO = 1;
    public static final int MSG_CODE_QUEST = 3;
    public static final int MSG_CODE_ERROR = 0;
    public static final int MSG_CODE_PLAIN = -1;

    public static final boolean CHOOSE_MULTI_FILES = true;
    public static Optional<File> showOpenFileChooserDLG(String title){
        File chosenFile = null;
        Optional<File> chosenFileOpt = Optional.ofNullable(chosenFile);
        final JFileChooser fc = new JFileChooser();
        if(!title.isBlank()){ fc.setDialogTitle(title.toUpperCase()); }
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        //fc.addChoosableFileFilter(FILTER_TXT);
        fc.addChoosableFileFilter(FILTER_JSON);
        //fc.addChoosableFileFilter(FILTER_HTML);
        //fc.addChoosableFileFilter(FILTER_PDF);
        int choice = fc.showOpenDialog(null);
        if(choice == JFileChooser.APPROVE_OPTION) {
            chosenFileOpt = Optional.ofNullable(fc.getSelectedFile());
        }
        return chosenFileOpt;
    }
    public static Optional<File[]> showOpenFilesChooserDLG(String title){
        File[] chosenFilesArr = null;
        Optional<File[]> chosenFilesArrOpt = Optional.ofNullable(chosenFilesArr);
        final JFileChooser fc = new JFileChooser();
        if(!title.isBlank()){ fc.setDialogTitle(title.toUpperCase()); }
        fc.setMultiSelectionEnabled(CHOOSE_MULTI_FILES);
        fc.setFileSelectionMode(FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(FILTER_TXT);
        //fc.addChoosableFileFilter(FILTER_JSON);
        //fc.addChoosableFileFilter(FILTER_HTML);
        //fc.addChoosableFileFilter(FILTER_PDF);
        int choice = fc.showOpenDialog(null);
        if(choice == JFileChooser.APPROVE_OPTION) {
            chosenFilesArrOpt = Optional.ofNullable(fc.getSelectedFiles());
        }
        return chosenFilesArrOpt;
    }
    public static String showInputBoxDLG(String message, String defaultValue){
        String input = JOptionPane.showInputDialog(message, defaultValue);
        if( (input == null)||(input.isBlank()) ) { return ""; }
        return input;
    }
    public static void showMessageBoxDLG(String message, String title, int msgCode){
        if(message.isBlank()){ return; }
        if(title.isBlank()){ title = "MESSAGEBOX"; }
        JOptionPane.showMessageDialog(null, message, title, msgCode);
    }
    public static boolean showConfirmDLG(String message, String title){
        if( (title == null)||(title.isBlank()) ) title = "CONFIRMATION";
        boolean confirmed = false;
        int choice = JOptionPane.showConfirmDialog(null, message, title,
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (choice == JOptionPane.YES_OPTION) { confirmed = true; }
        return confirmed;
    }
    public static String buildDateTimeStr(String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }
    public static String[] getFilepathParts(File file){
        String[] parts = {"", "", ""};
        if(!file.exists()){ return parts; }
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if(pos == -1){ return parts; }
        String baseName = fileName.substring(0, pos);
        String extension = fileName.substring(pos+1);
        parts[0] = file.getParent();
        parts[1] = baseName;
        parts[2] = "." + extension;
        return parts;
    }
    public static int writeListToTextfile(File file, List<String> li){
        int counter = 0;
        if(li.isEmpty()){ return -1; }
        try (BufferedWriter writer = Files.newBufferedWriter(
                file.toPath(),
                StandardCharsets.UTF_8,
                //StandardOpenOption.APPEND)
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE) )
        {
            for(String item : li){
                writer.write(item);
                writer.newLine();
                counter+=1;
            }
        } catch (Exception e) { counter = -1; };
        return counter;
    }
    public String readClassPathPropertiesFileResourceStream(String propsFileName, String keyName){
        String value = "";
        if(propsFileName.isBlank()){ return ""; }
        try(InputStream in = this.getClass().getClassLoader().getResourceAsStream(propsFileName);) {
            Properties p = new Properties();
            if( (in == null)||(p ==null) ){
                throw new RuntimeException("InputStream/Properties null");
            }
            p.load(in);
            value = p.getProperty(keyName);
            if(value == null){ return ""; }
            return value;
        } catch (Exception e) { return ""; }
    }
    public static String readClassPathPropertiesFile(String propertyFileName, String propertyKey) {
        String propertyValue = "";
        Properties props = null;
        try {
            props = new Properties();
            String rootClassPath = Thread.currentThread()
                    .getContextClassLoader().getResource("").getPath(); //.../target/classes/
            String resourceFilePath = rootClassPath + propertyFileName;
            File propsFile = new File(resourceFilePath);
            if (!propsFile.isFile()) {
                return "";
            }
            //props.load(new FileInputStream(propsFile));
            props.load(new FileInputStream(resourceFilePath));
            if (props.containsKey(propertyKey)) {
                propertyValue = props.getProperty(propertyKey, "");
            }
        } catch (Exception e) {
            return "";
        }
        return propertyValue;
    }

    //propertyFile must be located in project rootDir!
    public static String readPropertiesFile(String propertyFileName, String propertyKey) {
        String propertyValue = "";
        Properties props = null;
        try {
            props = new Properties();
            File propsFile = new File(System.getProperty("user.dir"), propertyFileName);
            if (!propsFile.isFile()) {
                return "";
            }
            props.load(new FileInputStream(propsFile));
            //props.load(new FileInputStream(propertyFileName));
            if (props.containsKey(propertyKey)) {
                propertyValue = props.getProperty(propertyKey, "");
            }
        } catch (Exception e) {
            return "";
        }
        return propertyValue;
    }

}
