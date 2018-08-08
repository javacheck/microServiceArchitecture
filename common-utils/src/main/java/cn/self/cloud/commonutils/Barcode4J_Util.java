package cn.self.cloud.commonutils;

/**
 * createDate : 2016年5月9日下午2:37:49
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

public class Barcode4J_Util {

    public static void getBarcode39Bean(int mode,String code,File file){
        Code39Bean bean = new Code39Bean();
        final int dpi = 150;
//         bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
        bean.setModuleWidth(0.2);
        bean.setHeight(15);
        bean.setWideFactor(3);
        bean.doQuietZone(true);
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            if (mode == 0) {
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(out,"image/jpeg", dpi, BufferedImage.TYPE_BYTE_GRAY, false,0);
                bean.generateBarcode(canvas, code);
                canvas.finish();
            } else {
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi,BufferedImage.TYPE_BYTE_GRAY, true, 0);
                bean.generateBarcode(canvas, code);
                canvas.finish();
                BufferedImage barcodeImage = canvas.getBufferedImage();
                ImageIO.write(barcodeImage, "jpg", out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void generateCode128Barcode(File file, String code) {
        Code128Bean bean = new Code128Bean();
        final int dpi = 150;
        bean.setModuleWidth(0.21);
        bean.setHeight(15);
        bean.doQuietZone(true);
        bean.setQuietZone(2);//两边空白区
        //human-readable
        bean.setFontName("Helvetica");
        bean.setFontSize(3);
        bean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out,"image/jpeg", dpi, BufferedImage.TYPE_BYTE_BINARY, true, 0);
            bean.generateBarcode(canvas, code);
            canvas.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
