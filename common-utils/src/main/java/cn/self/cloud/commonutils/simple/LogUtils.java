package cn.self.cloud.commonutils.simple;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by yumei on 15-4-16.
 */
public class LogUtils {
    /**
     * 返回错误日志堆栈信息
     * @param e 异常对象
     * @return
     */
    public static final String getErrorInfo(Exception e){
        StringWriter stringWriter=  new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    public static void main(String[] args) {
        System.out.println(((15 >> 26) & 0x3F));
        System.out.println(((15 >> 13) & 0x1FFF));
        System.out.println((15 & 0x1FFF));
    }
}
