package pz;

public class SerialMailer {
    //must be located in src/main/resources!
    public static final String RESOURCE_PROPS_FILENAME = "mail.properties";

    public static void main(String[] args) {
        MailerDTO dto = new MailerDTO(RESOURCE_PROPS_FILENAME);
        boolean isAllPropsSet = dto.isAllPropsSetYN();
        FileDialogsUtil.showMessageBoxDLG(
                "All Properties read/set [MailerDTO]: " + isAllPropsSet,
                "SERIALMAILER MAIN",
                FileDialogsUtil.MSG_CODE_PLAIN);
        System.out.println(dto);
    }
}
