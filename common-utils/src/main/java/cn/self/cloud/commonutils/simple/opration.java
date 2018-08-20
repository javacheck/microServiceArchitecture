package cn.self.cloud.commonutils.simple;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class opration extends HttpServlet {
	public List<Long> generate(int count) {
		System.out.println("OK :" + count);
		return null;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		String cnt = request.getParameter("count");
		// 这里可以调用自己写的程序方法，处理业务
		PrintWriter writer = response.getWriter();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(cnt);
		stringBuilder.append(" 返回给web页面 ");
		stringBuilder.append(" 哈哈哈哈哈哈哈 ");
		writer.print(stringBuilder.toString());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
