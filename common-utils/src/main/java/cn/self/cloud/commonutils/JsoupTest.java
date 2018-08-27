package cn.self.cloud.commonutils;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
2017年9月3日 下午10:54:09
 */

public class JsoupTest {
	public static void main(String[] args) throws IOException {
		// 从 URL 直接加载 HTML 文档
		Document doc = Jsoup.connect("https://my.oschina.net/AaronCN/blog/299874").get(); 

		String title = doc.title(); 
		System.out.println(doc);
	}
}
