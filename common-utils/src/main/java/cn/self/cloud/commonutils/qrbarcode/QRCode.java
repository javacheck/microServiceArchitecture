package cn.self.cloud.commonutils.qrbarcode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 项目依赖：zxing里的core.jar、javase.jar
 * 一个servlet，可以完全取代chart.apis.google.com自行生成QR图片，
 * 有了这个servlet，不需要去访问google的服务器也可以在网页中内嵌URL在线显示QR图片了。
 */
@SuppressWarnings("serial")
public class QRCode extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int onColor = 0xFF000000;
        int offColor = 0xFFFFFFFF;
        int intX = 320;
        int intY = 320;
        String sErr = "L";
        String sEnc = "UTF-8";
        String content = "Nothing";
        String[] strarry = request.getQueryString().split("&");
        for (String s : strarry) {
            if (s.startsWith("chs=")) {
                String[] sDemi = s.substring(4).split("x");
                intX = Integer.parseInt(sDemi[0]);
                intY = Integer.parseInt(sDemi[1]);
            } else if (s.startsWith("chcl=")) {
                onColor = Integer.parseInt(s.substring(5, 11), 16) | 0xFF000000;
                offColor = Integer.parseInt(s.substring(11), 16) | 0xFF000000;
            } else if (s.startsWith("chld=")) {
                sErr = s.substring(5);
            } else if (s.startsWith("choe=")) {
                sEnc = s.substring(5);
            } else if (s.startsWith("chl=")) {
                content = URLDecoder.decode(s.substring(4), sEnc);
            }
        }

        Map hints = new HashMap();
        ErrorCorrectionLevel lErr;
        if (sErr.equals("H")) lErr = ErrorCorrectionLevel.H;
        else if (sErr.equals("Q")) lErr = ErrorCorrectionLevel.Q;
        else if (sErr.equals("M")) lErr = ErrorCorrectionLevel.M;
        else lErr = ErrorCorrectionLevel.L;
        hints.put(EncodeHintType.ERROR_CORRECTION, lErr);
        hints.put(EncodeHintType.CHARACTER_SET, sEnc);

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setContentType("image/png");
        ServletOutputStream rsp = response.getOutputStream();
        QRCodeWriter qrWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrWriter.encode(content, BarcodeFormat.QR_CODE, intX, intY, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", rsp, new MatrixToImageConfig(onColor, offColor));
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        rsp.flush();
        rsp.close();
        response.flushBuffer();
    }
}
