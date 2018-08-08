package cn.self.cloud.commonutils;

/**
 * createDate : 2016年5月9日下午3:45:24
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;
import com.swetake.util.Qrcode;

public class QrCode_Util {

    public static int createQRCode(String content, String imgPath,String logo, int size){
        BufferedImage bufImg = null;
        try {
            Qrcode qrcodeHandler = new Qrcode();

            //错误修正容量
            //L水平   7%的字码可被修正
            //M水平   15%的字码可被修正
            //Q水平   25%的字码可被修正
            //H水平   30%的字码可被修正
            //QR码有容错能力，QR码图形如果有破损，仍然可以被机器读取内容，最高可以到7%~30%面积破损仍可被读取。
            //相对而言，容错率愈高，QR码图形面积愈大。所以一般折衷使用15%容错能力。
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect('M');

            /* "N","A" or other */
            qrcodeHandler.setQrcodeEncodeMode('B');

            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
            qrcodeHandler.setQrcodeVersion(size);

            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");

            boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);

            // 图片尺寸
            int imgSize = 67 + 12 * (size - 1);
//             int imgSize = codeOut.length * 10;

            bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);

            Graphics2D gs = bufImg.createGraphics();

            // 设置背景颜色
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgSize, imgSize);

            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);

            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            if (contentBytes.length > 0 && contentBytes.length < 800) {


                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
//                             gs.fillRect(j*10, i*10, 10-1, 10-1);
                        }
                    }
                }
            } else {
                System.err.println("QRCode content bytes length = "+ contentBytes.length + " not in [ 0,800 ]. ");
                return -1;
            }

            if( null != logo && !"".equals(logo)){
                Image img = ImageIO.read(new File(logo));//实例化一个Image对象。

                gs.drawImage(img, 44, 55, 49, 30, null);

                gs.dispose();

                bufImg.flush();
            }

            // 生成二维码QRCode图片

            File imgFile = new File(imgPath);

            ImageIO.write(bufImg, "jpg", imgFile);

            gs.dispose();
            bufImg.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return -100;
        }
        return 0;
    }




    //----------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        createQRCode("www.baidu.com","F:/b.jpg","F:/picture.jpg",7);
        //System.out.println(decoderQRCode("F:/b.jpg"));

    }
    //----------------------------------------------------------------------------------------------------



    /**
     * QRCode不能解析中间有LOGO的二维码
     * @param imgPath
     * @return
     */
    public static String decoderQRCode(String imgPath) {
        File imageFile = new File(imgPath);
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(imageFile);
            QRCodeDecoder decoder = new QRCodeDecoder();
            TwoDimensionCodeImage t = new TwoDimensionCodeImage(bufImg);
            content = new String(decoder.decode(t), "utf-8");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return content;
    }

}