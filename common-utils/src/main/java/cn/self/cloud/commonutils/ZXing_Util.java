package cn.self.cloud.commonutils;

/**
 * createDate : 2016年5月9日下午5:38:08
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class ZXing_Util {

    /**
     * 生成条形码和二维码
     * @param mode 1 代表条形码，其他的是二维码
     * @param str 内容
     * @param height 高度
     * @param file 生成地址
     * @return
     */
    public static BufferedImage encode(Integer mode,String str, Integer height,File file) {
        if (height == null || height < 100) {
            height = 200;
        }

        try {

            // 文字编码
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");


            BitMatrix bitMatrix = new MultiFormatWriter().encode(str, (mode == 1 ? BarcodeFormat.CODE_128 : BarcodeFormat.QR_CODE), height, height, hints);
            //MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(file));
            ImageIO.write(toBufferedImage(bitMatrix), "jpg", file);

            // 输出方式
            // 网页
            // ImageIO.write(image, "png", response.getOutputStream());

            // 文件
            // ImageIO.write(image, "png", file);
        } catch (Exception e)  {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 转换成图片
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int BLACK = 0xff000000;
        int WHITE = 0xFFFFFFFF;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }





    public static void main(String[] args) {
        File file = new File("F:/c.jpg");
        encode(2,"www.lastmiles.cn",500,file);
        System.out.println(decode(file));
    }




    /**
     * 解码(二维、一维均可)
     */
    public static String decode(File file) {
        BufferedImage image = null ;
        Result result = null;
        try{
            image = ImageIO.read(file);

            if (image == null) {
                return null;
            }
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            result = new MultiFormatReader().decode(bitmap,hints);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result.getText();
    }
}
