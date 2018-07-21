package cn.self.cloud.commonutils.password;

import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * 密码生成校验工具
 */
public final class PasswordUtils {

    /**
     * 密码加密
     *
     * @param password 明文密码
     */
    public static String encryptPassword(String password) {
        return new StrongPasswordEncryptor().encryptPassword(password);
    }

    /**
     * 密码校验比对
     *
     * @param password          明文密码
     * @param encryptedPassword 密文密码
     */
    public static boolean checkPassword(String password, String encryptedPassword) {
        return new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
    }
}