package cn.self.cloud.commonutils.qrbarcode;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import javax.imageio.ImageIO;

public class BarCode4jUtils {
    public static String generateBarcode(Long barcode, File outputFile) {
        Code128Bean bean = new Code128Bean();
        final int dpi = 300;

        // 条码两端是否加空白
        bean.doQuietZone(true);
        bean.setFontName("Helvetica");
        OutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            String format = "image/png";
            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(bos, format, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            // 生成条形码
            bean.generateBarcode(canvas, barcode + "");
            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

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