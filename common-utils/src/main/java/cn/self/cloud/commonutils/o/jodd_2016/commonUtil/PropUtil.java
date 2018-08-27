package cn.self.cloud.commonutils.o.jodd_2016.commonUtil;//package jodd_2016.commonUtil;
//import java.io.File;
//import java.io.IOException;
//import org.apache.log4j.Logger;
//import org.junit.Test;
//import jodd.props.Props;
///**
//* JODD操作properties文件
//* @author DJZHOU
//*
//*/
//public class PropUtil {
//private static Logger log = Logger.getLogger(PropUtil.class) ;
//@Test
//public void propExam(){
///*
//* 读取prop文件中的属性值
//*/
//Props p = new Props();
//log.warn(URLUtil.getClassPath(this.getClass())+"/a.properties") ;
//try {
//p.load(new File(URLUtil.getClassPath(this.getClass())+"/a.properties"));
//} catch (IOException e) {
//e.printStackTrace();
//}
//String story = p.getValue("a");
//log.warn(p.getBaseValue("a")) ;
//log.warn(story);
//log.warn(null == p.getValue("a")) ;
//log.warn(p.toString()) ;
//p.setValue("c", "cc") ;
//}
//}
