package pz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SerialMailerTest {
    public static final String RESOURCE_FILENAME = "mail.properties";
    public static final String DIRNAME_TESTFILES = "_TestKundenmails1";
    public static final String FILENAME_ASP3PZ = "TestASP3PZ.txt";
    public static final String FILENAME_EXPORT = "TestSentSimulation.txt";
    public static final String SEP = "-".repeat(40);
    public static MailerDTO mailerDto;
    public static Logger LOG = LoggerFactory.getLogger(SerialMailerTest.class);
    @BeforeAll
    public static void checkSetDtoFileProperties(){
        LOG.info("checkSetDtoFileProperties() is running");
        boolean printYN = false;
        mailerDto = new MailerDTO(RESOURCE_FILENAME);
        //Fetch Files
        File dirTestFiles = new File(FileDialogsUtil.USER_DOCS_DIR, DIRNAME_TESTFILES);
        File docHtmlContentFile = new File(dirTestFiles, "TestMailingPlaceholder.html");
        File docHtmlSignatureFile = new File(dirTestFiles, "TestSignaturAvale.htm");
        File docAttachmentFile = new File(dirTestFiles, "TestAttachment.pdf");
        File docPrepReceiversFile = new File(dirTestFiles, FILENAME_ASP3PZ);
        //Set Files
        mailerDto.setFilePersSalut(docHtmlContentFile);
        mailerDto.setFileSignature(docHtmlSignatureFile);
        mailerDto.setFileAttachment(docAttachmentFile);
        mailerDto.setFilePrepReceivers(docPrepReceiversFile);
        Map<String, String> prepReceiversMap =
            FileReaderUtil.readASPFileLinesIntoMap(mailerDto.getFilePrepReceivers(), ";");
        mailerDto.setReceiversMap(prepReceiversMap);
        //Set FilesContent
        String docHtmlSignature = FileReaderUtil.readhtmlFileLinesToTrimStringJ6(docHtmlSignatureFile);
        mailerDto.setFileContentSignature(docHtmlSignature);
        String docHtmlContent = FileReaderUtil.readhtmlFileLinesToTrimStringJ6(docHtmlContentFile);
        mailerDto.setFileContentPersSalut(docHtmlContent);
        int pos = docHtmlContent.indexOf(mailerDto.getPlaceholderTest());
        String docHtmlContentPart = docHtmlContent.substring(pos-25,pos+25);

        if(printYN == true){
            System.out.println(SEP);
            System.out.println(docHtmlContentPart);
            System.out.println(mailerDto);
            System.out.println(SEP);
        }
        Assertions.assertTrue(mailerDto.isAllPropsSetYN(), "SetAllMailDtoProperties failed");
    }
    @Test
    public void checkUserSalutBuild(){
        LOG.info("checkUserSalutBuild() is running");
        File dirTestFiles = new File(FileDialogsUtil.USER_DOCS_DIR, DIRNAME_TESTFILES);
        //SET prepared records for emailing file
        File docPrepReceiversFile = new File(dirTestFiles, FILENAME_ASP3PZ);
        SerialMailerTest.mailerDto.setFilePrepReceivers(docPrepReceiversFile);

        //Overwrite map in dto
        Map<String, String> prepReceiversMap =
                FileReaderUtil.readASPFileLinesIntoMap(mailerDto.getFilePrepReceivers(), ";");
        mailerDto.setReceiversMap(prepReceiversMap);

        //Overwrite template file content with placeholder in dto (email bodypart1)
        String docHtmlContent = FileReaderUtil.readhtmlFileLinesToTrimStringJ6(mailerDto.getFilePersSalut());
        mailerDto.setFileContentPersSalut(docHtmlContent);

        //Utility classes
        TestMail testMail = new TestMail();
        SimpleJavaMailUtil sjm = new SimpleJavaMailUtil(true);

        //Extract salutation placeholder from string
        String salutPlaceholder = mailerDto.getPlaceholderTest(); //#USER#
        String salutReplacement = "Sehr geehrte Frau Xxx";
        docHtmlContent = mailerDto.getFileContentPersSalut(); //full file content with placeholder
        //replace full file content placeholder with user salutation
        String docHtmlContentReplaced1 = testMail.replaceFullContent(
                docHtmlContent, salutPlaceholder, salutReplacement);
        String docHtmlContentReplaced2 = sjm.setHtmlContentReceiverSalutation(
                mailerDto.getFileContentPersSalut(), salutPlaceholder, salutReplacement);
        //get content part with replaced placeholder/user salutation
        String docHtmlContentReplacedSmall =
                testMail.buildPrunedContentPart(docHtmlContentReplaced1, salutReplacement);
        //get original content part with placeholder
        String docHtmlContentOriginalSmall =
                testMail.buildPrunedContentPart(docHtmlContent, salutPlaceholder);
        //display
        System.out.println(SEP);
        System.out.println("BEFORE REPLACEMENT: " + "\t\t" + docHtmlContentOriginalSmall);
        System.out.println("AFTER REPLACEMENT: " + "\t\t\t" + docHtmlContentReplacedSmall);
        System.out.println(SEP);
        //Utilty methods should return the same
        Assertions.assertEquals(docHtmlContentReplaced2, docHtmlContentReplaced1);

    }
    @Test
    public void checkUserEmailsFileExport(){
        LOG.info("checkUserEmailsFileExport() is running");

        //Exportfile for text comparison with prepared ASPs importfile
        String prefix = FileDialogsUtil.buildDateTimeStr(FileDialogsUtil.PATTERN_DATE_PREFIX) + "_";
        String exportFileName = prefix + FILENAME_EXPORT;
        File exportFileDir = FileDialogsUtil.USER_DOCS_DIR;

        File dirTestFiles = new File(FileDialogsUtil.USER_DOCS_DIR, DIRNAME_TESTFILES);
        //SET prepared records for emailing file
        File docPrepReceiversFile = new File(dirTestFiles, FILENAME_ASP3PZ);
        SerialMailerTest.mailerDto.setFilePrepReceivers(docPrepReceiversFile);

        //Overwrite map in dto
        Map<String, String> prepReceiversMap =
                FileReaderUtil.readASPFileLinesIntoMap(mailerDto.getFilePrepReceivers(), ";");
        mailerDto.setReceiversMap(prepReceiversMap);
        //Map of EmailAddresses contains no duplicates!!
        //LOG.info("Map of unique prepared mail receivers size: " + prepReceiversMap.size());

        TestMail testMail = new TestMail();
        for(String email : prepReceiversMap.keySet()){
            String userSalutation = prepReceiversMap.get(email);
            String replacedUserContent = testMail.replaceFullContent(
                    mailerDto.getFileContentPersSalut(),
                    mailerDto.getPlaceholderTest(), userSalutation);
            int pos = replacedUserContent.indexOf(userSalutation);
            String extractedUserSalutation = replacedUserContent.substring(pos, (pos+userSalutation.length()));
            String record = extractedUserSalutation + ";" + email;
            FileReaderUtil.createAndAppendLogTextFile(exportFileDir, exportFileName, record.trim());
        }
        File exportFile = new File(exportFileDir, exportFileName);
        System.out.println(SEP);
        System.out.println("File '" + exportFileName + "' created in Dir:\n" + exportFile.getParent());
        System.out.println(SEP);
        Assertions.assertTrue(exportFile.isFile());
    }
    @Test
    public void checkTestEmails(){
        LOG.info("checkTestEmails() is running");
        //SimpleJavaMailUtil mockSJM = Mockito.mock(SimpleJavaMailUtil.class);
        SimpleJavaMailUtil sjm = new SimpleJavaMailUtil(true);
        File dirTestFiles = new File(FileDialogsUtil.USER_DOCS_DIR, DIRNAME_TESTFILES);
        //SET prepared records for emailing file
        File docPrepReceiversFile = new File(dirTestFiles, FILENAME_ASP3PZ);
        SerialMailerTest.mailerDto.setFilePrepReceivers(docPrepReceiversFile);

        //Overwrite map in dto
        Map<String, String> prepReceiversMap =
                FileReaderUtil.readASPFileLinesIntoMap(mailerDto.getFilePrepReceivers(), ";");
        mailerDto.setReceiversMap(prepReceiversMap);
        //Map of EmailAddresses contains no duplicates!!
        System.out.println("Map of unique prepared mail receivers size: " + prepReceiversMap.size());
        String placeholder = mailerDto.getPlaceholderTest();
        for(String email : prepReceiversMap.keySet()){
            String userSalutation = prepReceiversMap.get(email);
            TestMail testMail = new TestMail();
            testMail.setHost(mailerDto.getHostTest());
            testMail.setPort(mailerDto.getPortTest());
            testMail.setFrom(mailerDto.getFromTest());
            testMail.setTo(email);
            testMail.setCc(mailerDto.getCcTest());
            testMail.setSubject(mailerDto.getSubjectTest() +
                    "  " + testMail.buildDateTimeStamp());
            String templateContent = mailerDto.getFileContentPersSalut();
            //String replacedContent = testMail.replaceFullContent(templateContent, placeholder, userSalutation);
            String replacedContent = sjm.setHtmlContentReceiverSalutation(templateContent, placeholder, userSalutation);
            String replacedContentPrune = testMail.buildPrunedContentPart(replacedContent, userSalutation);
            testMail.setBodyPartSalut(replacedContentPrune);
            testMail.setBodyPartSign(mailerDto.getFileContentSignature());
            testMail.setBodyPartSign(testMail.getPrunedBodyPartSign());
            testMail.setAttachmentFile(mailerDto.getFileAttachment());
            System.out.println(testMail.toString());
            System.out.println(SEP);
            testMail = null;
        }
    }
    @Disabled
    public void sendMailsFromHostExchangeDelayed(){
        LOG.info("sendMailsFromHostExchangeDelayed() is running");
        mailerDto = new MailerDTO(RESOURCE_FILENAME);
        //Fetch Files
        File dirTestFiles = new File(FileDialogsUtil.USER_DOCS_DIR, DIRNAME_TESTFILES);
        File docHtmlContentFile = new File(dirTestFiles, "TestMailingPlaceholder.html");
        File docHtmlSignatureFile = new File(dirTestFiles, "TestSignaturAvale.htm");
        File docAttachmentFile = new File(dirTestFiles, "TestAttachment.pdf");
        File docPrepReceiversFile = new File(dirTestFiles, FILENAME_ASP3PZ);
        //Set Files
        mailerDto.setFilePersSalut(docHtmlContentFile);
        mailerDto.setFileSignature(docHtmlSignatureFile);
        mailerDto.setFileAttachment(docAttachmentFile);
        mailerDto.setFilePrepReceivers(docPrepReceiversFile);

        //Set Delay -> mail.properties: "mail.delay.test"
        mailerDto.setSendDelaySecTest(1);
        mailerDto.setSendDelaySec(1);
        //Set MailRateLimit -> mail.properties: "mail.rate.prod"
        mailerDto.setMailRateLimitTest(30);
        mailerDto.setMailRateLimit(30);

        //Set Email Receivers with Salutations Map
        Map<String, String> prepReceiversMap =
                FileReaderUtil.readASPFileLinesIntoMap(mailerDto.getFilePrepReceivers(), ";");
        mailerDto.setReceiversMap(prepReceiversMap);
        if(prepReceiversMap.isEmpty()){
            System.out.println("Receivers map size is zero");
            return;
        }

        //Set FilesContent - with StandardCharsets.UTF8!!!
        //BufferedReader br = new BufferedReader(new FileReader(filepath, StandardCharsets.UTF_8))) {
        String docHtmlSignature = FileReaderUtil.readhtmlFileLinesToTrimStringJ6(docHtmlSignatureFile);
        mailerDto.setFileContentSignature(docHtmlSignature);
        String docHtmlContent = FileReaderUtil.readhtmlFileLinesToTrimStringJ6(docHtmlContentFile);
        mailerDto.setFileContentPersSalut(docHtmlContent);

        //Set test values explicitly in dto and pass dto as constructor arg
        //otherwise dto read properties file values will be used
        //if isMailTest == true then props.getXxTest() will be used
        mailerDto.setHostTest("smtp.office365.com");
        mailerDto.setPortTestNr(587);
        mailerDto.setUserTest("xxx@xxx.de");
        mailerDto.setPasswordTest("xxx"); //+++
        mailerDto.setFromTest("xxx@xxx.de");//+++
        System.out.println(mailerDto);

        //Email Sending -> SimpleJavaMailUtil -> SET MailerDTO Object OR pass as constructor parameter!
        boolean isMailTest = true;
        SimpleJavaMailUtil mailService = new SimpleJavaMailUtil(mailerDto, isMailTest);
        //int counter = mailService.sendEmailsWithMailerMain(); //OK
        int counter = mailService.sendEmailsRateLimitedWithMailerMain();
        System.out.println("Sending Counter: " + counter);
        System.out.println("List of failed: " + mailService.getFailedEmailsList());
        System.out.println("List of sent: " + mailService.getSentEmailsList());
        Assertions.assertTrue(counter > 0, "Emailing from Exchange failed");
    }

    @Disabled
    public void sendMailsFromHostGmx(){
        mailerDto = new MailerDTO(RESOURCE_FILENAME);
        //Fetch Files
        File dirTestFiles = new File(FileDialogsUtil.USER_DOCS_DIR, DIRNAME_TESTFILES);
        File docHtmlContentFile = new File(dirTestFiles, "TestMailingPlaceholder.html");
        File docHtmlSignatureFile = new File(dirTestFiles, "TestSignaturAvale.htm");
        File docAttachmentFile = new File(dirTestFiles, "TestAttachment.pdf");
        File docPrepReceiversFile = new File(dirTestFiles, FILENAME_ASP3PZ);
        //Set Files
        mailerDto.setFilePersSalut(docHtmlContentFile);
        mailerDto.setFileSignature(docHtmlSignatureFile);
        mailerDto.setFileAttachment(docAttachmentFile);
        mailerDto.setFilePrepReceivers(docPrepReceiversFile);

        //Set Delay -> mail.properties: "mail.delay.test"
        mailerDto.setSendDelaySecTest(1);
        mailerDto.setSendDelaySec(1);
        //Set MailRateLimit -> mail.properties: "mail.rate.prod"
        mailerDto.setMailRateLimitTest(30);
        mailerDto.setMailRateLimit(30);

        //Set Email Receivers with Salutations Map
        Map<String, String> prepReceiversMap =
                FileReaderUtil.readASPFileLinesIntoMap(mailerDto.getFilePrepReceivers(), ";");
        mailerDto.setReceiversMap(prepReceiversMap);

        //Set FilesContent - with StandardCharsets.UTF8!!!
        //BufferedReader br = new BufferedReader(new FileReader(filepath, StandardCharsets.UTF_8))) {
        String docHtmlSignature = FileReaderUtil.readhtmlFileLinesToTrimStringJ6(docHtmlSignatureFile);
        mailerDto.setFileContentSignature(docHtmlSignature);
        String docHtmlContent = FileReaderUtil.readhtmlFileLinesToTrimStringJ6(docHtmlContentFile);
        mailerDto.setFileContentPersSalut(docHtmlContent);

        //Set Test SMTP Server Data
        mailerDto.setHostTest("mail.gmx.net");
        mailerDto.setPortTestNr(587);
        mailerDto.setUserTest("zar@xxx.de");
        mailerDto.setPasswordTest(""); //+++
        System.out.println(mailerDto);

        //Email Sending -> SimpleJavaMailUtil -> SET MailerDTO Object OR pass as constructor parameter!
        boolean isMailTest = true;
        SimpleJavaMailUtil mailService = new SimpleJavaMailUtil(mailerDto, isMailTest);
        //int counter = mailService.sendEmailsWithMailerMain(); //OK
        int counter = mailService.sendEmailsRateLimitedWithMailerMain();
        System.out.println("Sending Counter: " + counter);
        System.out.println("List of failed: " + mailService.getFailedEmailsList());
        System.out.println("List of sent: " + mailService.getSentEmailsList());
    }

    @Disabled
    public void simulateMailSendWithDelay(){
        SimpleJavaMailUtil mailer = new SimpleJavaMailUtil(true);
        Map<String, String> map = mailerDto.getReceiversMap();
        String htmlContentTemplate = mailerDto.getFileContentPersSalut();
        mailerDto.setSendDelaySec(3);
        String htmlContentReplaced = "";
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy'T'HH:mm:ss");
        int pos =  htmlContentTemplate.indexOf(mailerDto.getPlaceholderTest());
        for(String receiver: map.keySet()){
            //System.out.println(receiver + "\t\t-> " + map.get(receiver));
            htmlContentReplaced = mailer.setHtmlContentReceiverSalutation(
                    htmlContentTemplate,
                    mailerDto.getPlaceholderTest(),
                    map.get(receiver));
            System.out.println(htmlContentReplaced.substring(pos-15, pos+70) + "\n" + df.format(new Date()));
            try {
                Thread.sleep(mailerDto.getSendDelaySec()*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Disabled
    public void readAllProps(){
        String resourcePropsFileName = "mail.properties";
        MailerDTO dto = new MailerDTO(resourcePropsFileName);
        boolean expectedAllPropsSetYN = true;
        boolean actualAllPropsSetYN = dto.isAllPropsSetYN();
        Assertions.assertEquals(expectedAllPropsSetYN,
                actualAllPropsSetYN, "Props not set");
        System.out.println(dto);
    }
}
