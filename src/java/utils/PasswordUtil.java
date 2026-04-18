package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // hash mật khẩu
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    // check mật khẩu
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null) return false;
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}