package utilities.ReusableComponents;
import org.jboss.aerogear.security.otp.Totp;
import utilities.yamlReader.yamlReader;
public class TOTPGenerator {
    private static final yamlReader reader = new yamlReader();
    public static String getTwoFactorCode(){
        Totp totp = new Totp(reader.getAccessKey());
        return totp.now();
    }
}