package cn.self.web;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

/**
 * Created by chen on 2016/1/13.
 */
public class CWSService extends HttpServlet {

    private PaodingAnalyzer analyzer = new PaodingAnalyzer();

    public String cws(String w) {
        String ret = "";
        if (StringUtils.isNotEmpty(w)) {
            StringBuilder sb = new StringBuilder();
            try {
                TokenStream tokenStream = analyzer.tokenStream("", new StringReader(w));
                boolean isFirst = true;
                Token token;
                while ((token = tokenStream.next()) != null) {
                    if (isFirst)
                        isFirst = false;
                    else
                        sb.append(" ");
                    sb.append(token.termText());
                }
                ret = sb.toString();
               System.out.println("w="+w+"\r\n分词结果："+ret);
                return ret;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }



    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        String sentence = request.getParameter("w");
        String ret="";
        if(StringUtils.isNotEmpty(sentence)){
            ret=cws(sentence);
        }
        PrintWriter printWriter=response.getWriter();
        printWriter.print(ret);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request, response);
    }

}
