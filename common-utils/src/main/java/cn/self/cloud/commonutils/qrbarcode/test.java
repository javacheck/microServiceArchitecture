package cn.self.cloud.commonutils.qrbarcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.sun.imageio.plugins.common.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static cn.self.cloud.commonutils.qrbarcode.MatrixUtil.toBufferedImage;

public class test {

    public static void getJabCode(String code) throws IOException, WriterException {
        int width = 200, height = 100;
        code="301010025000001877,301010025000001878";
        String[] args=code.split("\\,");
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        BufferedImage imageNew1 = new BufferedImage(width, height*args.length ,
                BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<args.length;i++){
            BitMatrix bitMatrix = new MultiFormatWriter().encode(args[i],
                    BarcodeFormat.CODE_128, width, height, null);
            BufferedImage tempimg = toBufferedImage(bitMatrix);
            int[] ImageArrayOne1 = new int[width * height];
            ImageArrayOne1 = tempimg.getRGB(0, 0, width, height-10, ImageArrayOne1, 0,
                    width);
            imageNew1.setRGB(0, 100*i, width, height-20, ImageArrayOne1, 0, width);
            MatrixToImageWriter
                    .writeToFile(bitMatrix, "png", new File("d:/test/zxing_01.png"));
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteOutputStream);

        }
        ImageIO.write(imageNew1, "png", new File("d:/test/zxing_03.png"));
    }


    // public static void aaa(){
    //     JBarcodeBean jBarcodeBean = new JBarcodeBean();
    //     jBarcodeBean.setCodeType(new Code128());
    //     jBarcodeBean.setCode("301010025000001877");
    //     BufferedImage img1 = new BufferedImage(300, 100,
    //             BufferedImage.TYPE_INT_RGB);
    //     img1 = jBarcodeBean.draw(img1);
    //     saveToPNG(img1, "4.png");
    // }
    //
    // static void saveToJPEG(BufferedImage paramBufferedImage, String paramString) {
    //     saveToFile(paramBufferedImage, paramString, "jpeg");
    // }
    // static void saveToFile(BufferedImage paramBufferedImage,
    //                        String paramString1, String paramString2) {
    //     try {
    //         FileOutputStream localFileOutputStream = new FileOutputStream(
    //                 "d:/test/" + paramString1);
    //         ImageUtil.encodeAndWrite(paramBufferedImage, paramString2,
    //                 localFileOutputStream, 100, 100);
    //         localFileOutputStream.close();
    //     } catch (Exception localException) {
    //         localException.printStackTrace();
    //     }
    // }
    //
    // public byte[] getJabCode(String code) throws IOException{
    //     //String code="301010025000001877,301010025000001878";
    //     String[] args=code.split("\\,");
    //     JBarcodeBean jBarcodeBean = new JBarcodeBean();
    //     jBarcodeBean.setLabelPosition(JBarcodeBean.LABEL_BOTTOM);
    //     // 条形码类型
    //     jBarcodeBean.setCodeType(new Code128());
    //     // jBarcodeBean.setCodeType(new Code39());
    //     //jBarcodeBean1.setLabelPosition(JBarcodeBean.LABEL_BOTTOM);
    //     int width1=200;
    //     int height1 =100;
    //     BufferedImage imageNew1 = new BufferedImage(width1, height1*args.length ,
    //             BufferedImage.TYPE_INT_RGB);
    //
    //     for(int i=0;i<args.length;i++){
    //         jBarcodeBean.setCode(args[i]);
    //         BufferedImage tempimg = new BufferedImage(width1, height1,
    //                 BufferedImage.TYPE_INT_RGB);
    //         tempimg = jBarcodeBean.draw(tempimg);
    //         int[] ImageArrayOne1 = new int[width1 * height1];
    //         ImageArrayOne1 = tempimg.getRGB(0, 0, width1, height1, ImageArrayOne1, 0,
    //                 width1);
    //         imageNew1.setRGB(0, 100*i, width1, height1, ImageArrayOne1, 0, width1);
    //
    //     }
    //     ByteArrayOutputStream out = new ByteArrayOutputStream();
    //     boolean flag = ImageIO.write(imageNew1, "gif", out);
    //     byte[] b = out.toByteArray();
    //     //saveToPNG(imageNew1, "imageNew1.png");
    //     return b;
    // }

}
