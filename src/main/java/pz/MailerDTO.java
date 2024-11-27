package pz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/*
following props must explicit set with SETTER
String: fileContentPersSalut; fileContentSignature;
File: fileAttachment; filePersSalut; fileSignature;
*/
public class MailerDTO {
    public static final String NL = "\n";
    public static final File DIR = new File(System.getProperty("user.dir"));
    public static Logger LOG = LoggerFactory.getLogger(MailerDTO.class);
    private String resourceFileNameProps = "";
    private boolean allPropsSetYN = false;
    private int sendDelaySec = 1;
    private int sendDelaySecTest = 1;
    private int mailRateLimit = 30;
    private int mailRateLimitTest = 30;
    private String host = "";
    private String hostTest = "";
    private String port = "";
    private String portTest = "";
    private int portNr = 0;
    private int portTestNr = 0;
    private String user = "";
    private String userTest = "";
    private String password = "";
    private String passwordTest = "";
    private String from = "";
    private String fromTest = "";
    private String toTest = "";
    private String cc = "";
    private String ccTest = "";
    private String subject = "";
    private String subjectTest = "";
    private String placeholder = "";
    private String placeholderTest = "";
    private String fileContentPersSalut = "";
    private String fileContentSignature = "";
    private File fileAttachment = DIR;
    private File filePersSalut = DIR;
    private File fileSignature = DIR;
    private File filePrepReceivers = DIR;
    private Map<String, String> receiversMap = new HashMap<>();

    public MailerDTO() {}
    public MailerDTO(String resourceFileNameProps) {
        this.resourceFileNameProps = resourceFileNameProps;
        if(!resourceFileNameProps.isBlank()){
            this.allPropsSetYN =
                    this.readAllResourceFilePropsWithResourceStream(resourceFileNameProps);
        }
    }
    public String getResourceFileNameProps() {
        return resourceFileNameProps;
    }
    public void setResourceFileNameProps(String resourceFileNameProps) {
        this.resourceFileNameProps = resourceFileNameProps;
    }
    public boolean isAllPropsSetYN() {
        return allPropsSetYN;
    }
    public void setAllPropsSetYN(boolean allPropsSetYN) {
        this.allPropsSetYN = allPropsSetYN;
    }
    public int getSendDelaySec() { return sendDelaySec; }
    public void setSendDelaySec(int sendDelaySec) { this.sendDelaySec = sendDelaySec; }

    public int getSendDelaySecTest() { return sendDelaySecTest; }
    public void setSendDelaySecTest(int sendDelaySecTest) {
        this.sendDelaySecTest = sendDelaySecTest;
    }
    public int getMailRateLimit() { return mailRateLimit; }
    public void setMailRateLimit(int mailRateLimit) {
        this.mailRateLimit = mailRateLimit;
    }
    public int getMailRateLimitTest() {
        return mailRateLimitTest;
    }
    public void setMailRateLimitTest(int mailRateLimitTest) {
        this.mailRateLimitTest = mailRateLimitTest;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getHostTest() {
        return hostTest;
    }
    public void setHostTest(String hostTest) {
        this.hostTest = hostTest;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getPortTest() {
        return portTest;
    }

    public int getPortNr() { return portNr; }
    public void setPortNr(int portNr) { this.portNr = portNr; }
    public int getPortTestNr() { return portTestNr; }
    public void setPortTestNr(int portTestNr) { this.portTestNr = portTestNr; }

    public void setPortTest(String portTest) {
        this.portTest = portTest;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getUserTest() {
        return userTest;
    }
    public void setUserTest(String userTest) {
        this.userTest = userTest;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPasswordTest() {
        return passwordTest;
    }
    public void setPasswordTest(String passwordTest) {
        this.passwordTest = passwordTest;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getFromTest() {
        return fromTest;
    }
    public void setFromTest(String fromTest) {
        this.fromTest = fromTest;
    }
    public String getToTest() {
        return toTest;
    }
    public void setToTest(String toTest) {
        this.toTest = toTest;
    }
    public String getCc() {
        return cc;
    }
    public void setCc(String cc) {
        this.cc = cc;
    }
    public String getCcTest() {
        return ccTest;
    }
    public void setCcTest(String ccTest) {
        this.ccTest = ccTest;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSubjectTest() {
        return subjectTest;
    }
    public void setSubjectTest(String subjectTest) {
        this.subjectTest = subjectTest;
    }
    public String getPlaceholder() {
        return placeholder;
    }
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
    public String getPlaceholderTest() {
        return placeholderTest;
    }
    public void setPlaceholderTest(String placeholderTest) {
        this.placeholderTest = placeholderTest;
    }
    public String getFileContentPersSalut() {
        return fileContentPersSalut;
    }
    public void setFileContentPersSalut(String fileContentPersSalut) {
        this.fileContentPersSalut = fileContentPersSalut;
    }
    public String getFileContentSignature() {
        return fileContentSignature;
    }
    public void setFileContentSignature(String fileContentSignature) {
        this.fileContentSignature = fileContentSignature;
    }
    public File getFileAttachment() {
        return fileAttachment;
    }
    public void setFileAttachment(File fileAttachment) {
        this.fileAttachment = fileAttachment;
    }
    public File getFilePersSalut() {
        return filePersSalut;
    }
    public void setFilePersSalut(File filePersSalut) {
        this.filePersSalut = filePersSalut;
    }
    public File getFileSignature() {
        return fileSignature;
    }
    public File getFilePrepReceivers() {
        return filePrepReceivers;
    }
    public void setFilePrepReceivers(File filePrepReceivers) {
        this.filePrepReceivers = filePrepReceivers;
    }

    public void setFileSignature(File fileSignature) {
        this.fileSignature = fileSignature;
    }
    public Map<String, String> getReceiversMap() {
        return receiversMap;
    }
    public void setReceiversMap(Map<String, String> receiversMap) {
        this.receiversMap = receiversMap;
    }
    public boolean readAllResourceFilePropsWithResourceStream(String propsFileName){
        boolean isPropsSetYN = false;
        if(propsFileName.isBlank()){ return false; }
        try(InputStream in = this.getClass().getClassLoader().getResourceAsStream(propsFileName);) {
            Properties p = new Properties();
            if(in == null){
                isPropsSetYN = false;
                throw new RuntimeException("InputStream null");
            }
            p.load(in);
            //key not found - Null check?
            //this.setSendDelaySec(Integer.parseInt(p.getProperty("mail.delay.prod")));
            this.setSendDelaySec(this.getIntPropsVal(p, "mail.delay.prod"));
            //this.setSendDelaySecTest(Integer.parseInt(p.getProperty("mail.delay.test")));
            this.setSendDelaySecTest(this.getIntPropsVal(p, "mail.delay.test"));
            //this.setMailRateLimit(Integer.parseInt(p.getProperty("mail.rate.prod")));
            this.setMailRateLimit(this.getIntPropsVal(p, "mail.rate.prod"));
            //this.setMailRateLimitTest(Integer.parseInt(p.getProperty("mail.rate.test")));
            this.setMailRateLimitTest(this.getIntPropsVal(p, "mail.rate.test"));

            //this.setHost(p.getProperty("mail.host.prod"));
            this.setHost(this.getStrPropsVal(p, "mail.host.prod"));
            //this.setHostTest(p.getProperty("mail.host.test"));
            this.setHostTest(this.getStrPropsVal(p, "mail.host.test"));
            //this.setPort(p.getProperty("mail.port.prod"));
            this.setPort(this.getStrPropsVal(p, "mail.port.prod"));
            //this.setPortTest(p.getProperty("mail.port.test"));
            this.setPortTest(this.getStrPropsVal(p, "mail.port.test"));
            //this.setPortNr(Integer.parseInt(this.getPort()));
            this.setPortNr(this.getIntPropsVal(p, "mail.port.prod"));
            //this.setPortTestNr(Integer.parseInt(this.getPortTest()));
            this.setPortNr(this.getIntPropsVal(p, "mail.port.test"));

            //this.setUser(p.getProperty("mail.user.prod"));
            this.setUser(this.getStrPropsVal(p, "mail.user.prod"));
            //this.setUserTest(p.getProperty("mail.user.test"));
            this.setUserTest(this.getStrPropsVal(p, "mail.user.test"));
            //this.setPassword(p.getProperty("mail.password.prod"));
            this.setPassword(this.getStrPropsVal(p, "mail.password.prod"));
            //this.setPasswordTest(p.getProperty("mail.password.test"));
            this.setPasswordTest(this.getStrPropsVal(p, "mail.password.test"));

            //this.setFrom(p.getProperty("mail.from.prod"));
            this.setFrom(this.getStrPropsVal(p, "mail.from.prod"));
            //this.setFromTest(p.getProperty("mail.from.test"));
            this.setFromTest(this.getStrPropsVal(p, "mail.from.test"));
            //this.setToTest(p.getProperty("mail.to.test"));
            this.setToTest(this.getStrPropsVal(p, "mail.to.test"));
            //this.setCc(p.getProperty("mail.cc.prod"));
            this.setCc(this.getStrPropsVal(p, "mail.cc.prod"));
            //this.setCcTest(p.getProperty("mail.cc.test"));
            this.setCcTest(this.getStrPropsVal(p, "mail.cc.test"));

            this.setSubject(p.getProperty("mail.subject.prod"));
            this.setSubject(this.getStrPropsVal(p, "mail.subject.prod"));
            //this.setSubjectTest(p.getProperty("mail.subject.test"));
            this.setSubjectTest(this.getStrPropsVal(p, "mail.subject.test"));
            //this.setPlaceholder(p.getProperty("mail.placeholder.prod"));
            this.setPlaceholder(this.getStrPropsVal(p, "mail.placeholder.prod"));
            //this.setPlaceholderTest(p.getProperty("mail.placeholder.test"));
            this.setPlaceholderTest(this.getStrPropsVal(p, "mail.placeholder.test"));
            isPropsSetYN = true;

        } catch (Exception e) { isPropsSetYN = false; }
        return isPropsSetYN;
    }
    @Override
    public String toString() {
        return "MailerDTO{" + "resourceFileNameProps='" + resourceFileNameProps + '\'' + NL +
                ", allPropsSetYN=" + allPropsSetYN + NL +
                ", sendDelaySec=" + sendDelaySec +
                ", sendDelaySecTest=" + sendDelaySecTest + NL +
                ", mailRateLimit=" + mailRateLimit +
                ", mailRateLimitTest=" + mailRateLimitTest + NL +
                ", host='" + host + '\'' +
                ", hostTest='" + hostTest + '\'' + NL +
                ", port='" + port + '\'' +
                ", portTest='" + portTest + '\'' + NL +
                ", user='" + user + '\'' +
                ", userTest='" + userTest + '\'' + NL +
                ", password='" + password + '\'' +
                ", passwordTest='" + passwordTest + '\'' + NL +
                ", from='" + from + '\'' +
                ", fromTest='" + fromTest + '\'' + NL +
                ", toTest='" + toTest + '\'' +
                ", cc='" + cc + '\'' +
                ", ccTest='" + ccTest + '\'' + NL +
                ", subject='" + subject + '\'' + NL +
                ", subjectTest='" + subjectTest + '\'' + NL +
                ", placeholder='" + placeholder + '\'' +
                ", placeholderTest='" + placeholderTest + '\'' + NL +
                ", fileContentPersSalut='" + getContentLen(fileContentPersSalut) + " chars(trim)" + '\'' + NL +
                ", fileContentSignature='" + getContentLen(fileContentSignature) + " chars(trim)" + '\'' + NL +
                ", fileAttachment=" + fileAttachment + NL +
                ", filePersSalut=" + filePersSalut + NL +
                ", fileSignature=" + fileSignature + NL +
                ", filePrepReceivers=" + filePrepReceivers + NL +
                '}';
    }
    public String getContentLen(String content){
        if( (content == null)||(content.isBlank()) ){ return "0"; }
        int len = (content.isBlank()) ? 0 : content.length();
        NumberFormat nf = NumberFormat.getInstance();
        String s = nf.format(len);
        return s;
    }
    public int getIntPropsVal(Properties props, String key){
        int ret = 0;
        if(props == null) { return 0; }
        if( (key == null)||(key.isBlank()) ){ return 0; }
        String strVal = props.getProperty(key);
        if( (strVal == null)||(strVal.isBlank()) ){ return 0; }
        try {
            ret = Integer.parseInt(strVal);
        }catch(Exception e){ return 0; }
        return ret;
    }
    public String getStrPropsVal(Properties props, String key){
        String ret = "";
        if(props == null) { return ""; }
        if( (key == null)||(key.isBlank()) ){ return ""; }
        ret = props.getProperty(key);
        if( (ret == null)||(ret.isBlank()) ){ return ""; }
        return ret;
    }
}
