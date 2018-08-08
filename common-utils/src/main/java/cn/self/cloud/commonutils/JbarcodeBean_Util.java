package cn.self.cloud.commonutils;

/**
 * createDate : 2016年5月9日下午2:56:33
 */
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Ean13;

public class JbarcodeBean_Util {
    public static void generateJBarcodeBean(){
        JBarcodeBean jBarcodeBean = new JBarcodeBean();
        // 条形码类型
        jBarcodeBean.setCodeType(new Ean13());
//        jBarcodeBean.setCodeType(new Code39());
        // jBarcodeBean.setBarcodeHeight(200);
        jBarcodeBean.setCode("6922507005033");
        // 在条形码下面显示文字
        jBarcodeBean.setLabelPosition(JBarcodeBean.LABEL_BOTTOM);
        jBarcodeBean.setName("测试abc");
        OutputStream out;
        try {
            out = new FileOutputStream("F:/a.jpg");
            BufferedImage image = new BufferedImage(400, 400,
                    BufferedImage.TYPE_INT_RGB);
            image = jBarcodeBean.draw(image);
            ImageIO.write(image, "JPEG", out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generateJBarcodeBean();
    }
}
