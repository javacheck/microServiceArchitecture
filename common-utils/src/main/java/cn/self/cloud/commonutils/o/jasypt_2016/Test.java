package cn.self.cloud.commonutils.o.jasypt_2016;

/**
 * 提高加密数据的安全性 - Jasypt 初试 
 */
import java.math.BigDecimal;
import org.apache.commons.codec.binary.Base64;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.jasypt.util.digest.Digester;
import org.jasypt.util.numeric.BasicDecimalNumberEncryptor;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/*
* 在应用 WebLogic、学习 Maven 的时候都碰到过设置密码的情况，且发现他们对同样密码的
* 2次加密结果还都不一致，但却能验证成功，看过了 Maven 使用的加密的源码。但是作为
* 可独立使用类库就有点牵强了。于是，就用 google 百度了一下，发现了类似组件，先按照
* 官网上的例子给写出个 sample 吧。
* 
* 注意：虽然该组件可以提高加密数据被破解的几率，但是对于系统密码的保护仍需要重视。
* 系统密码被泄露后，更新系统密码引发的加密数据需要使用原密码解密、再使用新密码加密
* 也是一个问题（即，加密密码的版本管理问题）。 
*/
public class Test {

    public static void main(String[] args) {
        testGeneralDigest();
        testPasswordEncryption();
        testTextEncryption();
        testNumberEncryption();
        testBinaryEncryption();
    }

    /**
     * 进行正常的摘要。称不上为加密，只能用于检测是否数据被改窜。
     */
    public static void testGeneralDigest() {
        System.out.println("========= test general digesting");

        Digester digester = new Digester();
        digester.setAlgorithm("SHA-1");

        // 第一次
        String msg = "zhang3";
        byte[] digest = digester.digest(msg.getBytes());
        String digestBase64 = Base64.encodeBase64String(digest).trim();

        // 第二次
        msg = "zhang3";
        digest = digester.digest(msg.getBytes());
        digestBase64 = Base64.encodeBase64String(digest).trim();
        System.out.println();
    }

    /**
     * 对密码进行加密。一般都使用单向加密（其实也摘要的一种）。
     */
    public static void testPasswordEncryption() {
        System.out.println("========= test password encryption (digesting).");
        ConfigurablePasswordEncryptor encryptor = new ConfigurablePasswordEncryptor();
        encryptor.setAlgorithm("SHA-1");
        encryptor.setPlainDigest(false);

        // 第一次
        String userPassword = "123456";
        String encryptedPassword = encryptor.encryptPassword(userPassword);
        String inputPassword = userPassword;
        boolean matched = encryptor.checkPassword(inputPassword,
                encryptedPassword);

        // 第二次
        userPassword = "123456";
        encryptedPassword = encryptor.encryptPassword(userPassword);
        inputPassword = userPassword;
        matched = encryptor.checkPassword(inputPassword, encryptedPassword);

        System.out.println();
    }

    /**
     * 对文本进行加密。可对大量数据进行加密。
     */
    public static void testTextEncryption() {
        System.out.println("========= test text encryption.");

        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        String password = "123456";
        encryptor.setPassword(password);

        // 第一次
        String text = "zhang3";
        String encryptedText = encryptor.encrypt(text);
        String plainText = encryptor.decrypt(encryptedText);

        // 第二次
        text = "zhang3";
        encryptedText = encryptor.encrypt(text);
        plainText = encryptor.decrypt(encryptedText);

        System.out.println();
    }

    /**
     * 对数值型 BasicDecimal 进行加密。加密前后均为 BasicDecimal 类型。
     */
    public static void testNumberEncryption() {
        System.out.println("========= test number encryption.");

        BasicDecimalNumberEncryptor encryptor = new BasicDecimalNumberEncryptor();

        String password = "123456";
        encryptor.setPassword(password);

        // 第一次
        BigDecimal number = new BigDecimal(123456789.123D);
        BigDecimal encryptedNumber = encryptor.encrypt(number);
        BigDecimal plainNumber = encryptor.decrypt(encryptedNumber);

        // 第二次
        number = new BigDecimal(123456789.123D);
        encryptedNumber = encryptor.encrypt(number);
        plainNumber = encryptor.decrypt(encryptedNumber);

        System.out.println();
    }

    /**
     * 对二进制进行加密。
     */
    public static void testBinaryEncryption() {
        System.out.println("========= test binary encryption.");

        BasicBinaryEncryptor encryptor = new BasicBinaryEncryptor();

        String password = "123456";
        encryptor.setPassword(password);

        // 第一次
        String binaryStr = "zhang3";
        byte[] encryptedBinary = encryptor.encrypt(binaryStr.getBytes());
        String encryptedBinaryBase64 = Base64.encodeBase64String(
                encryptedBinary).trim();
        byte[] plainBinary = encryptor.decrypt(encryptedBinary);
        String plainBinaryStr = new String(plainBinary);

        // 第二次
        binaryStr = "zhang3";
        encryptedBinary = encryptor.encrypt(binaryStr.getBytes());
        encryptedBinaryBase64 = Base64.encodeBase64String(encryptedBinary)
                .trim();
        plainBinary = encryptor.decrypt(encryptedBinary);
        plainBinaryStr = new String(plainBinary);

        System.out.println();
    }
}
