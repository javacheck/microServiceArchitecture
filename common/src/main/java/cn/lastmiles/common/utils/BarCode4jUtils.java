package cn.lastmiles.common.utils;
/**
 * createDate : 2016年3月1日下午3:06:38
 */
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

public class BarCode4jUtils {
	public static String generateBarcode(Long barcode,File outputFile){
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
	        BitmapCanvasProvider canvas = new BitmapCanvasProvider(bos, format, dpi,
	                BufferedImage.TYPE_BYTE_BINARY, false, 0);
	     // 生成条形码
	        bean.generateBarcode(canvas, barcode+"");
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
}