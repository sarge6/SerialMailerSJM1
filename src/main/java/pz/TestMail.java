package pz;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestMail {
    private String host = "";
    private String port = "";
    private String from = "";
    private String to = "";
    private String cc = "";
    private String subject = "";
    private String bodyPartSalut = "";
    private String bodyPartSign = "";
    private File attachmentFile = new File(".");
    public TestMail() {}
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getCc() {
        return cc;
    }
    public void setCc(String cc) {
        this.cc = cc;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getBodyPartSalut() {
        return bodyPartSalut;
    }
    public void setBodyPartSalut(String bodyPartSalut) {
        this.bodyPartSalut = bodyPartSalut;
    }
    public String getBodyPartSign() {
        return bodyPartSign;
    }
    public void setBodyPartSign(String bodyPartSign) {
        this.bodyPartSign = bodyPartSign;
    }
    public String getPrunedBodyPartSign(){
        return bodyPartSign.substring(0, 100);
    }
    public File getAttachmentFile() {
        return attachmentFile;
    }
    public void setAttachmentFile(File attachmentFile) {
        this.attachmentFile = attachmentFile;
    }
    @Override
    public String toString() {
        return "TestMail{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' + "\n" +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", cc='" + cc + '\'' + "\n" +
                ", subject='" + subject + '\'' +"\n" +
                ", bodyPartSalut='" + bodyPartSalut + '\'' + "\n" +
                ", bodyPartSign='" + bodyPartSign + '\'' + "\n" +
                ", attachmentFile'" + attachmentFile + '\'' + "\n" +
                ", attachmentFileName'" + attachmentFile.getName() + '\'' + "\n" +
                '}';
    }
    public String buildDateTimeStamp(){
        String dtStamp = "";
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            dtStamp = now.format(formatter);
        } catch (Exception e) { return ""; }
        return dtStamp;
    }
    public String buildPrunedContentPart(String content, String searchPattern){
        String returnPart = "";
        int shift = 5;
        if( (content == null)||(content.isBlank()) ){ return "#"; }
        if( (searchPattern == null)||(searchPattern.isBlank()) ){ return "#"; }
        int len = content.length();
        if(len < 1){ return "#"; }
        int pos = content.indexOf(searchPattern);
        if(pos == -1){ return "#"; }
        int subLenR = (pos+searchPattern.length()+shift);
        int subLenL = (pos-shift);
        if( (len >= subLenR)&(subLenL > 0) ){
            returnPart = content.substring(subLenL, subLenR);
        }
        return returnPart;
    }
    public String replaceFullContent(String content, String placeholder, String replacement) {
        if( (content == null)
                ||(content.isBlank())
                ||(placeholder.isBlank())
                ||(replacement.isBlank())) {
            return "#";
        }
        int pos = content.indexOf(placeholder);
        if(pos == -1){ return "#"; }
        return content.replace(placeholder, replacement);
    }

}
