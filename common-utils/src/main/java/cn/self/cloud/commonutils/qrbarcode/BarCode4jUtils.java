package cn.self.cloud.commonutils.qrbarcode;

import java.awt.image.BufferedImage;
import java.io.*;

import cn.self.cloud.commonutils.basictype.StringUtils;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import javax.imageio.ImageIO;

public class BarCode4jUtils {
    public static String generateBarcode(Long barcode, File outputFile) {
        Code128Bean bean = new Code128Bean();
        final int dpi = 300;
        // module宽度
        //		final double moduleWidth = UnitConv.in2mm(1.0f / dpi);

        // 配置对象
//		bean.setModuleWidth(moduleWidth);
//        bean.setWideFactor(3);

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



    // ----------------------------------------------------------------
    /**
     * 生成文件
     *
     * @param msg
     * @param path
     * @return
     */
    public static File generateFile(String msg, String path) {
        File file = new File(path);
        try {
            generate(msg, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 生成字节
     *
     * @param msg
     * @return
     */
    public static byte[] generate(String msg) {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generate(msg, ous);
        return ous.toByteArray();
    }

    /**
     * 生成到流
     *
     * @param msg
     * @param ous
     */
    public static void generate(String msg, OutputStream ous) {
        if (StringUtils.isEmpty(msg) || ous == null) {
            return;
        }

        Code39Bean bean = new Code39Bean();
        // EAN13Bean bean = new EAN13Bean();

        //条形码类型
        /**
         ITF-14
         Code 128
         Codabar
         UPC-A
         UPC-E
         EAN-13
         EAN-8
         POSTNET
         PDF417
         等
        */
        // 精细度
        final int dpi = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);

        // 配置对象
        bean.setModuleWidth(moduleWidth);
        bean.setWideFactor(3);
        bean.doQuietZone(false);

        String format = "image/png";
        try {

            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // 生成条形码
            bean.generateBarcode(canvas, msg);

            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String msg = "123456789101";
        String path = "S:/barcode.png";
        generateFile(msg, path);
    }
}