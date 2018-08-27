package cn.self.cloud.commonutils.qrbarcode;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Random;

/**
 * zxing 是由google开源的1D/2D编解码类库。目标是能够对QR编码、Data Matrix、UPC的1D条形码进行解码。
 * 二维码相对于条形码的优势：数据容量更大；超越了字母数字的限制；具有抗损毁能力
 */
public class QRCodeUtil {
    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "png";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    private static BufferedImage createImage(String content, String imgPath, boolean needCompress, Integer logoWidth, Integer logoHeight, Integer qrcodeSize, String charset) throws Exception {
        qrcodeSize = qrcodeSize == null ? QRCODE_SIZE : qrcodeSize;//如果未传递二维码长度 则默认
        charset = charset == null ? CHARSET : charset;//如果未传递编码
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, charset);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, qrcodeSize, qrcodeSize, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        QRCodeUtil.insertImage(image, imgPath, needCompress, logoWidth, logoHeight, qrcodeSize);
        return image;
    }

    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress, Integer logoWidth, Integer logoHeight, Integer qrcodeSize) throws Exception {
        logoWidth = logoWidth == null ? WIDTH : logoWidth;//如果未传递LOGO宽度则默认
        logoHeight = logoHeight == null ? HEIGHT : logoHeight;//如果未传递LOGO长度则默认
        qrcodeSize = qrcodeSize == null ? QRCODE_SIZE : qrcodeSize;//如果未传递二维码长度 则默认
        Image src = null;
        if (imgPath.startsWith("http") || imgPath.startsWith("https")) {//判断是否为网络图片
            src = ImageIO.read(new URL(imgPath));
        } else {
            File file = new File(imgPath);
            if (!file.exists()) {
                System.err.println("" + imgPath + "   该文件不存在！");
                return;
            }
            src = ImageIO.read(new File(imgPath));
        }
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            width = width > logoWidth ? logoWidth : width;
            height = height > logoHeight ? logoHeight : height;
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (qrcodeSize - width) / 2;
        int y = (qrcodeSize - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        //Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        //graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress, Integer logoWidth, Integer logoHeight, Integer qrcodeSize, String charset) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress, logoWidth, logoHeight, qrcodeSize, charset);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999) + ".jpg";
        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress, null, null, null, null);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999) + ".jpg";
        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
    }

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 存放目录
     * @author lanyuan
     * Email: mmm333zzz520@163.com
     * @date 2013-12-11 上午10:16:36
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content  内容
     * @param imgPath  LOGO地址
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, Integer logoWidth, Integer logoHeight, Integer qrcodeSize, String charset) throws Exception {
        QRCodeUtil.encode(content, imgPath, destPath, false, logoWidth, logoHeight, qrcodeSize, charset);
    }

    /**
     * 生成二维码
     *
     * @param content      内容
     * @param destPath     存储地址
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String destPath, boolean needCompress, Integer logoWidth, Integer logoHeight, Integer qrcodeSize, String charset) throws Exception {
        QRCodeUtil.encode(content, null, destPath, needCompress, logoWidth, logoHeight, qrcodeSize, charset);
    }

    /**
     * 生成二维码
     *
     * @param content  内容
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String destPath, Integer logoWidth, Integer logoHeight, Integer qrcodeSize, String charset) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false, logoWidth, logoHeight, qrcodeSize, charset);
    }

    /**
     * 生成二维码
     *
     * @param content  内容
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String destPath) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false, null, null, null, null);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress, null, null, null, null);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress, Integer logoWidth, Integer logoHeight, Integer qrcodeSize, String charset) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress, logoWidth, logoHeight, qrcodeSize, charset);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress, Integer logoWidth, Integer logoHeight, Integer qrcodeSize) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress, logoWidth, logoHeight, qrcodeSize, null);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress, Integer logoSize, Integer qrcodeSize) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress, logoSize, logoSize, qrcodeSize, null);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress, Integer qrcodeSize) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress, null, null, qrcodeSize, null);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param output  输出流
     * @throws Exception
     */
    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);

        return result.getText();
    }

    public static String decode(InputStream input) throws Exception {
        BufferedImage image;
        image = ImageIO.read(input);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);

        return result.getText();
    }

    /**
     * 解析二维码
     *
     * @param path 二维码图片地址
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }

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
        // File file = new File("F:/c.jpg");
        // encode(2,"www.lastmiles.cn",500,file);
        // System.out.println(decode2(file));
        try {
            encode("http://www.baidu.com", "S:/56.jpg", "S:/MyWorkDoc", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 解码(二维、一维均可)
     */
    public static String decode2(File file) {
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
            // 注意要使用 utf-8，因为刚才生成二维码时，使用了utf-8
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            result = new MultiFormatReader().decode(bitmap,hints);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result.getText();
    }
}
