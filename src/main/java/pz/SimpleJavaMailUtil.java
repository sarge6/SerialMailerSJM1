package pz;

import jakarta.activation.FileDataSource;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.io.File;
import java.util.*;

public class SimpleJavaMailUtil {
    private boolean isMailTest = true;
    private MailerDTO dto; //src/main/resources/mail.properties
    private List<String> failedEmailsList = new ArrayList<>();
    private List<String> sentEmailsList = new ArrayList<>();
    //constructors
    public SimpleJavaMailUtil() {}
    public SimpleJavaMailUtil(MailerDTO mailerDto, boolean isMailTest) {
        this.dto = mailerDto;
        this.isMailTest = isMailTest;
    }
    public SimpleJavaMailUtil(boolean isMailTest) {
        this.isMailTest = isMailTest;
    }
    //getter/setter
    public boolean isMailTest() {
        return isMailTest;
    }
    public void setMailTest(boolean mailTest) {
        isMailTest = mailTest;
    }
    public MailerDTO getDto() {
        return dto;
    }
    public void setDto(MailerDTO dto) {
        this.dto = dto;
    }
    public List<String> getFailedEmailsList() {
        return failedEmailsList;
    }
    public void setFailedEmailsList(List<String> failedEmailsList) {
        this.failedEmailsList = failedEmailsList;
    }
    public List<String> getSentEmailsList() { return this.sentEmailsList; }
    public void setSentEmailList(List<String> sentEmailsList) {
        this.sentEmailsList = sentEmailsList;
    }
    //main method for jave mail send
    public int sendEmailsWithMailerMain(){
        int counter = 0;
        Optional<Mailer> optMailer = this.buildMailer();
        if( (optMailer.isEmpty()) ){
            counter = -1;
            throw new RuntimeException("Mailer is empty");
        }
        if( (this.dto.getReceiversMap() == null)||(this.dto.getReceiversMap().isEmpty()) ){
            counter = -1;
            throw new RuntimeException("ReceiversMap is empty");
        }
        Mailer mailer = optMailer.get();
        Optional<Email> optEmail;
        Set<String> keySet = this.dto.getReceiversMap().keySet();
        if( (keySet.isEmpty()) ){
            counter = -1;
            throw new RuntimeException("KeySet is empty");
        }
        long sendDelayMS = (this.dto.getSendDelaySec() > 0) ? (this.dto.getSendDelaySec()*1000) : 0;
        for(String keyEmailAddr : keySet){
            String salutMarker = (this.isMailTest)
                    ? this.dto.getPlaceholderTest()
                    : this.dto.getPlaceholder();
            String persSalut = this.dto.getReceiversMap().get(keyEmailAddr); //map value
            String htmlPersSalut = this.setHtmlContentReceiverSalutation(
                    this.dto.getFileContentPersSalut(), salutMarker, persSalut);
            if(!htmlPersSalut.isBlank()){
                optEmail = this.buildHtmlEmailWithAttachmentFile(keyEmailAddr, htmlPersSalut);
                if(optEmail.isPresent()){
                    Email email = optEmail.get();
                    //boolean validEmailAddr = mailer.validate(email);
                    try {
                        mailer.sendMail(email);
                        this.sentEmailsList.add(keyEmailAddr);
                        counter+=1;
                        if(sendDelayMS > 0){ Thread.sleep(sendDelayMS); }
                    }
                    catch (Exception e) {
                        this.failedEmailsList.add(keyEmailAddr);
                    }
                }
            }
        }
        return counter;
    }
    public int sendEmailsRateLimitedWithMailerMain(){
        int counter = 0;
        int keyMailCounter = 0;
        Optional<Mailer> optMailer = this.buildMailer();
        if( (optMailer.isEmpty()) ){
            counter = -1;
            throw new RuntimeException("Mailer is empty");
        }
        if( (this.dto.getReceiversMap() == null)||(this.dto.getReceiversMap().isEmpty()) ){
            counter = -1;
            throw new RuntimeException("ReceiversMap is empty");
        }
        Mailer mailer = optMailer.get();
        Optional<Email> optEmail;
        Set<String> keySet = this.dto.getReceiversMap().keySet();
        if( (keySet.isEmpty()) ){
            counter = -1;
            throw new RuntimeException("KeySet is empty");
        }
        int mailRateLimit = 0;
        if(this.isMailTest){ mailRateLimit = this.dto.getMailRateLimitTest(); }
        else{ mailRateLimit = this.dto.getMailRateLimit(); }
        int sendDelaySec = 0;
        long sendDelayMS = 0;
        if(this.isMailTest) { sendDelaySec = this.dto.getSendDelaySecTest(); } //test
        else { sendDelaySec = this.dto.getSendDelaySec(); } //prod
        sendDelayMS = (sendDelaySec > 0) ? (sendDelaySec*1000) : 0; //milliseconds
        for(String keyEmailAddr : keySet){
            String salutMarker = (this.isMailTest)
                    ? this.dto.getPlaceholderTest()
                    : this.dto.getPlaceholder();
            String persSalut = this.dto.getReceiversMap().get(keyEmailAddr); //map value
            String trailer = this.dto.getFileContentSignature();
            //replace placeholder with individual receiver salutation
            String htmlPersSalut = this.setHtmlContentReceiverSalutation(
                    this.dto.getFileContentPersSalut(), salutMarker, persSalut);
            if(!htmlPersSalut.isBlank()){
                if(!trailer.isBlank()) { htmlPersSalut = htmlPersSalut + trailer; }
                optEmail = this.buildHtmlEmailWithAttachmentFile(keyEmailAddr, htmlPersSalut);
                if(optEmail.isPresent()){
                    Email email = optEmail.get();
                    //boolean validEmailAddr = mailer.validate(email);
                    try {
                        mailer.sendMail(email);
                        keyMailCounter+=1;
                        this.sentEmailsList.add(keyEmailAddr);
                        counter+=1;
                        //Delay due to Message Rate Limit per Minute
                        if( (keyMailCounter > 0)&((keyMailCounter%mailRateLimit)==0) ) {
                            if(sendDelayMS > 0){ Thread.sleep(sendDelayMS); }
                        }
                    }
                    catch (Exception e) {
                        this.failedEmailsList.add(keyEmailAddr);
                    }
                }
            }
        }
        return counter;
    }
    //Loop: ReceiverEmailAddr + ReceiverIndividualSalutation
    //ReceiverIndividualSalutation = HTML Placeholder-Replacement final String
    //Constant: Subject, Sender, Files, SMTP-Server Data ...
    public Optional<Email> buildHtmlEmailWithAttachmentFile(
    String receiverAddr, String bodyPartHtmlReceiverSalut) {
        Optional<Email> optNullRet = Optional.ofNullable(null);
        Email email = null;
        if( (receiverAddr.isBlank())||(bodyPartHtmlReceiverSalut.isBlank()) ){
            return optNullRet;
        }
        File attachmentFile = this.dto.getFileAttachment();
        if(!attachmentFile.isFile()){
            return optNullRet;
        }
        try {
            if(this.isMailTest){
                email = EmailBuilder.startingBlank()
                        .withSubject(this.dto.getSubjectTest())
                        .withAttachment(attachmentFile.getName(),
                                new FileDataSource(attachmentFile.getAbsolutePath()))
                        .from(this.dto.getFromTest())
                        .to(receiverAddr)
                        .withHTMLText(bodyPartHtmlReceiverSalut)
                        .buildEmail();
            }
            else{
                email = EmailBuilder.startingBlank()
                        .withSubject(this.dto.getSubject())
                        .withAttachment(attachmentFile.getName(),
                                new FileDataSource(attachmentFile.getAbsolutePath()))
                        .from(this.dto.getFrom())
                        .to(receiverAddr)
                        .withHTMLText(bodyPartHtmlReceiverSalut)
                        .buildEmail();
            }
        } catch (Exception e) { return optNullRet; }
        return Optional.ofNullable(email);
    }
    public Optional<Mailer> buildMailer(){
        Mailer mailer = null;
        try {
            if(isMailTest){
                mailer = MailerBuilder
                        .withSMTPServer(
                                this.dto.getHostTest(),
                                this.dto.getPortTestNr(),
                                this.dto.getUserTest(),
                                this.dto.getPasswordTest())
                        .buildMailer();
            }
            else{
                mailer = MailerBuilder
                        .withSMTPServer(
                                this.dto.getHost(),
                                this.dto.getPortNr(),
                                this.dto.getUser(),
                                this.dto.getPassword())
                        .buildMailer();
            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
        return Optional.ofNullable(mailer);
    }
    //Loop: replace HTML content placeholder with individual salutation
    public String setHtmlContentReceiverSalutation(String htmlContentTemplate,
    String salutationMarker, String userSalutation) {
        if( (htmlContentTemplate.isBlank()
                ||(htmlContentTemplate.compareTo("-1") == 0))
                ||(salutationMarker.isBlank())
                ||(userSalutation.isBlank())) {
            //System.out.println("HTML-ContentTemplate is empty");
            return "";
        }
        int pos = htmlContentTemplate.indexOf(salutationMarker);
        if(pos == -1){
            //System.out.println("HTML-ContentTemplate Placeholder not found");
            return "";
        }
        return htmlContentTemplate.replace(salutationMarker, userSalutation);
    }

}
