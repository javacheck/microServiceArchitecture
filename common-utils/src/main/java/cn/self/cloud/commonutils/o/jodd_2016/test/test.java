// package cn.self.cloud.commonutils.o.jodd_2016.test;
//
// import org.junit.Test;
// import jodd.bean.BeanUtil;
// import jodd.datetime.JDateTime;
// import jodd.typeconverter.TypeConverterManager;
//
// /**
//  * 网上对Jodd的描述如下：
// 	Jodd是一个普通开源Java包。你可以把Jodd想象成Java的"瑞士军刀",不仅小，锋利而且包含许多便利的功能。
// 	Jodd 提供的功能有：
// 	1. 提供操作Java bean，
// 	2. 可以从各种数据源加载Bean,
// 	3. 简化JDBC的接连与代码，
// 	4. 剖析SQL查询，
// 	5. 处理时间与日期，
// 	6. 操作与格式化String,
// 	7. 搜索本地硬盘上的文件，
// 	8. 帮助处理Servlet请求等。
// 	9.Madvoc - 一个简单的MVC框架，用CoC和注解的方式
// 	10. Petite：一个轻量级的DI（注入）框架
// 	11.Proxetta：一个高效的动态代理框架
// 	12. Db & DbOom：高效，轻量级的数据库处理框架
// 	13. Paramo：在运行时，非常简单的获取方法和构造方法的参数
// 	14. JTX：提供一个独立的，轻量级的事务管理器
// 	15.VTor：实用的验证框架，可以针对任何Java 对象
// 	16. Lagarto：高效，灵活的通用HTML解析器
// 	17.Decora：基于模板的页面装饰框架。
// 	18. Jerry：友好的jQuery java解析器，支持CSS选择器
//
// 	除此之外还包含一个很小，但实用的基于JSP的MVC框架。
// 	---------------------------------------------------
// 	不扯哪些没有的了，还是通过例子来说明问题比较好。
// 	去官方下载最新的jodd吧，我下载的版本是jodd-3.3.3  下载地址：http://jodd.org
//  *
//  * */
// public class test {
//
// 	/*BeanUtil的使用
// 	通过BeanUtil，可以对bean的属性进行控制（注入）；类型转换等
// 	*/
// 	    @Test
// 	    public void testBeanUtil() {
// 	        UserPojo userPojo = new UserPojo();
// 	        BeanUtil.setProperty(userPojo, "name", "zhaoyang"); //设置name属性值为zhaoyang
// 	        System.out.println(">>>>> " + BeanUtil.getProperty(userPojo, "name")); //获取name属性值
//
// 	        BeanUtil.setDeclaredProperty(userPojo, "add", "beijing");   //add是只读属性，如果用setProperty(~)就会报错，可以用声明式属性，强制属性赋值
// 	        System.out.println(">>>>> " + BeanUtil.getDeclaredProperty(userPojo, "add"));
//
// 	        int dd = TypeConverterManager.convertType("23", Integer.class);  //类型转换，将字符串“23”转换为Integer类型
// 	        System.out.println("@@ " + (dd - 3));
//
// //	        System.out.println("#### " + BeanTool.attributesToString(userPojo));  //打印出bean的结构
// 	    }
// /*
// 	BeanTool还有其他一些常用的方法，如
// 	copy(java.lang.Object source, java.lang.Object destination)  ：只copy公共属性
// 	copyFields(java.lang.Object source, java.lang.Object destination)  copy 所有的字段值到目标对象
// 	copyProperties(java.lang.Object source, java.lang.Object destination)
// 	load(java.lang.Object bean, java.lang.Object source)
// 	另外 BeanTemplateParser可以把Bean按照模版的显示方式进行解析，转换。官方例子
// 	// prepare template
// 	String template = "Hello ${user.name}. Today is ${dayName}.";
// 	...
//
// 	// prepare context
// 	Foo foo = newFoo();
// 	foo.getUser().setName("John Doe");
// 	foo.setDayName("Saturday");
// 	...
// 	// parse
// 	BeanTemplateParser btp = newBeanTemplateParser();
// 	String result = btp.parse(template, foo);
// 	// result == "Hello John Doe. Today is Saturday."
// 	*/
//
//
//
// 	    /*
// 	日期处理 - JDateTime
// 	日期设置。可以通过构造函数，或者set(~)进行
// 	JDateTime jdt = new JDateTime();        // set current date and time
// 	jdt = new JDateTime(2012, 12, 21):      // set 21st December 2012, midnight
// 	jdt = new JDateTime(System.currentTimeMillis());
// 	jdt = new JDateTime(2012, 12, 21, 11, 54, 22, 124)  // set 21st December 2012, 11:54:22.124
// 	jdt = new JDateTime("2012-12-21 11:54:22.124");     // -//-
// 	jdt = new JDateTime("12/21/2012", "MM/DD/YYYY");    // set 21st December 2012, midnight
// 	或者：
// 	JDateTime jdt = new JDateTime();            // set current date and time
// 	jdt.set(2012, 12, 21, 11, 54, 22, 124);     // set 21st December 2012, 11:54:22.124
// 	jdt.set(2012, 12, 21);                      // set 21st December 2012, midnight
// 	jdt.setDate(2012, 12, 21);                  // change date to 21st December 2012, do not change te time
// 	jdt.setCurrentTime();                       // set current date and time
// 	jdt.setYear(1973);                          // change the year
// 	jdt.setHour(22);                            // change the hour
// 	jdt.setTime(18, 00 12, 853);                // change just time, date remains the same
//
// 	读取日期和时间
// 	*/
// 	    @Test
// 	    public void testJDateTime() {
// 	        JDateTime jDateTime = new JDateTime();
// 	        int year = jDateTime.getYear();
// 	        System.out.println(">>>> " + year);
// 	        System.out.println("@@@@ " + JDateTime.JANUARY);
//
// 	        System.out.println("### " + jDateTime.getFormat());
// 	        System.out.println("### " + jDateTime.toString("YYYY-MM-DD hh")); //转换为字符串，可以自定义格式
//
// 	        jDateTime.convertToDate();  //日期转换为java.util.Date
// 	        jDateTime.convertToSqlDate(); //转换为java.sql.Date
//
// 	        jDateTime.add(-1,0,0);    //日期向后减去1年
// 	        System.out.println("$$$$ " + jDateTime.toString("YYYY-MM-DD hh")); //转换为字符串，可以自定义格式
// 	    }
// }
//
// /*
// 	JDateTime jdt = newJDateTime(1975, 1, 1);
// 	jdt.toString();                     // "1975-01-01 00:00:00.000"
// 	jdt.toString("YYYY.MM.DD");         // "1975.01.01"
// 	jdt.toString("MM: MML (MMS)");      // "01: January (Jan)"
// 	jdt.toString("DD is D: DL (DS)");   // "01 is 3: Wednesday (Wed)"
// 	JDateTime jdt = newJDateTime(1968, 9, 30);
// 	jdt.toString("'''' is a sign, W is a week number and 'W' is a letter");
// 	// "' is a sign, 5 is a week number and W is a letter"
//
// 	jdt.parse("2003-11-24 23:18:38.173");
// 	jdt.parse("2003-11-23");                // 2003-11-23 00:00:00.000
// 	jdt.parse("01.01.1975", "DD.MM.YYYY");  // 1975-01-01
// 	jdt.parse("2001-01-31", "YYYY-MM-***"); // 2001-01-01, since day is not parsed
//
// 	JDateTime jdt = newJDateTime();
// 	JdtFormatter fmt = newDefaultFormatter();
// 	fmt.convert(jdt, "YYYY-MM.DD");         // external conversion
//
// 	JdtFormat format = newJdtFormat(newDefaultFormatter(), "YYYY+DD+MM");
// 	jdt.toString(format);
// 	format.convert(jdt);
//
// 	DateFormat df = newSimpleDateFormat();
// 	df.format(jdt.convertToDate());         // date formatter
// */
//
//
//
//
//
// 	    /*
// 	属性读取 - Props
// 	属性文件如下：
// 	key1=value1
// 	key2 : value2
//
// 	[section1]
// 	key.with.macro = val_${key}
//
// 	[ ]
// 	key<profile1> = value A
// 	key<profile2> = value B
// 	默认 props文件是UTF8编码，无论是什么编码，Props加载properties文件用 ISO 8859-1这种编码格式
// 	基本使用方式：
// 	Props p = new Props();
// 	p.load(new File("example.properties"));
// 	...
// 	String stroy = p.getValue("story");
//
//
//
// 	----------- Sections 的获取-------------
// 	properties文件如下：
// 	[users.data]
// 	weitht = 49.5
// 	age=23
// 	获取方式：
// 	users.data.weitht = 49.5
// 	users.data.age = 63
// 	--------- Profiles的获取 -------------
// 	properties文件如下：
// 	db.port=3086
//
// 	db.url<develop>=localhost
// 	db.username<develop>=root
//
// 	db.url<deploy>=192.168.1.101
// 	db.username<deploy>=app2499
// 	或者是：
// 	db.port=3086
//
// 	[db<develop>]
// 	url=localhost
// 	username=root
//
// 	[db<deploy>]
// 	url=192.168.1.101
// 	username=app2499
// 	获取方法为：
// 	String url = props.getValue("db.url", "develop");
// 	String user = props.getValue("db.username", "develop");
// 	----------- 宏命令 -----------------
// 	key1=Something ${foo}
// 	...
// 	foo=nice
// 	key1的结果为 Something nice
// 	key1=**${key${key3}}**
// 	key3=2
// 	key2=foo
// 	key1的结果为：**foo**
// 	--------------------------------
//
//
// 	*/
//
//
//
//
//
//
//
//
// 	    /*
// 	放送和接收邮件 - Email
// 	官方有详尽的说明（http://jodd.org/doc/email.html）
// 	1.首先定义一个邮件（可以是HTML格式的，也可以是普通格式的）
// 	Email email = Email.create()
// 	    .from("...@jodd.org").to("...@jodd.org")
// 	    .subject("Hello HTML!")
// 	    .addHtml("<b>HTML</b> message...");
// 	2.添加一个带附件的邮件
// 	Email email = Email.create()
// 	                .from("weird@beotel.rs")
// 	                .to("weird@beotel.rs")
// 	                .subject("test5")
// 	                .addText("Здраво!")
// 	                .addHtml("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"+
// 	                        "<body><h1>Здраво!</h1><img src='cid:huh2.jpg'></body></html>")
// 	                .embedFile("d:\\huh2.jpg")
// 	                .attachFile("d:\\cover.jpg");
// 	3.定义邮件服务器，发送邮件
// 	SmtpServer smtpServer = newSmtpServer("mail.beotel.rs", newSimpleAuthenticator("user", "pass"));
// 	...
// 	SendMailSession session = smtpServer.createSession();
// 	session.open();
// 	session.sendMail(email1);
// 	session.sendMail(email2);
// 	session.close();
// 	4. 用SSL发送邮件
// 	SmtpServer smtpServer = newSmtpSslServer("smtp.gmail.com", "user@gmail.com", "password"));
// 	...
// 	SendMailSession session = smtpServer.createSession();
// 	session.open();
// 	session.sendMail(email);
// 	session.close();
// 	5.接收邮件
// 	Pop3Server popServer = newPop3Server("pop3.beotel.yu",
// 	        newSimpleAuthenticator("username", "password"));
// 	ReceiveMailSession session = popServer.createSession();
// 	session.open();
// 	System.out.println(session.getMessageCount());
// 	ReceivedEmail[] emails = session.receiveEmail(false);
// 	if(emails != null) {
// 	    for(ReceivedEmail email : emails) {
// 	        System.out.println("\n\n===["+ email.getMessageNumber() + "]===");
//
// 	        // common info
// 	        Printf.out("%0x", email.getFlags());
// 	        System.out.println("FROM:"+ email.getFrom());
// 	        System.out.println("TO:"+ email.getTo()[0]);
// 	        System.out.println("SUBJECT:"+ email.getSubject());
// 	        System.out.println("PRIORITY:"+ email.getPriority());
// 	        System.out.println("SENT DATE:"+ email.getSentDate());
// 	        System.out.println("RECEIVED DATE: "+ email.getReceiveDate());
//
// 	        // process messages
// 	        LinkedList<emailmessage> messages = email.getAllMessages();
// 	        for(EmailMessage msg : messages) {
// 	            System.out.println("------");
// 	            System.out.println(msg.getEncoding());
// 	            System.out.println(msg.getMimeType());
// 	            System.out.println(msg.getContent());
// 	        }
//
// 	        // process attachments
// 	        List<EmailAttachment> attachments = email.getAttachments();
// 	        if(attachments != null) {
// 	            System.out.println("+++++");
// 	            for(EmailAttachment attachment : attachments) {
// 	                System.out.println("name: "+ attachment.getName());
// 	                System.out.println("cid: "+ attachment.getContentId());
// 	                System.out.println("size: "+ attachment.getSize());
// 	                attachment.writeToFile(newFile("d:\\", attachment.getName()));
// 	            }
// 	        }
// 	    }
// 	}
// 	session.close();</emailmessage>
// 	类型转换 - Converter
// 	如图示，非常丰富：
//
// 	更加偏爱的一种转换方法：
// 	TypeConverterManager.convertType("173", Integer.class);
// 	就是使用 TypeConverterManager对象
//
// 	当然还可以自定义转换器
//
// 	查找文件和文件夹
// 	FindFile
// 	例子：
// 	FindFile ff = newWildcardFindFile("*")
// 	    .setRecursive(true)
// 	    .setIncludeDirs(true)
// 	    .searchPath("/some/path");
//
// 	File f;
// 	while((f = ff.nextFile()) != null) {
// 	    if(f.isDirectory() == true) {
// 	        System.out.println(". >"+ f.getName());
// 	    } else{
// 	        System.out.println(". "+ f.getName());
// 	    }
// 	}
// 	--------------------
// 	FindFile ff = newWildcardFindFile("*")
// 	    .setRecursive(true)
// 	    .setIncludeDirs(true)
// 	    .searchPath("/some/path");
//
// 	Iterator<File> iterator = ff.iterator();
//
// 	while(iterator.hasNext()) {
// 	    File f = iterator.next();
// 	    if(f.isDirectory() == true) {
// 	        System.out.println(". >"+ f.getName());
// 	    } else{
// 	        System.out.println(". "+ f.getName());
// 	    }
// 	}
// 	--------- FileScanner ------------------
// 	FileScanner fs = newFileScanner() {
// 	    @Override
// 	    protectedvoidonFile(File file) {
// 	        System.out.println(file.getName());
// 	    }
// 	}.includeDirs(true).recursive(true).includeFiles(false);
// 	fs.scan("d:\\temp\\");
//
// 	----------------- WildcardFileScanner ------------------------
// 	WildcardFileScanner wfs = newWildcardFileScanner("**file/**", true);
// 	List<File> files = wfs.list("/some/path");
// }
// */