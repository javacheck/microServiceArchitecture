package cn.lastmiles.pay.pos.wxpay;

import cn.lastmiles.pay.pos.wxpay.common.Util;
import cn.lastmiles.pay.pos.wxpay.protocol.pay_protocol.ScanPayReqData;
import cn.lastmiles.pay.pos.wxpay.protocol.pay_protocol.ScanPayResData;
import cn.lastmiles.pay.pos.wxpay.protocol.pay_query_protocol.ScanPayQueryReqData;
import cn.lastmiles.pay.pos.wxpay.protocol.pay_query_protocol.ScanPayQueryResData;




public class Main {
	/**签名算法需要用到的秘钥*/
	private static final String key = "lastmiles20160128lastmiles201601";
	/**公众账号ID*/
	private static final String appID = "wx651d06c7183eb1bc";
	/**商户ID*/
	private static final String mchID = "1253257201";
	/**子商户ID，受理模式必填*/
	private static final String sdbMchID = "";
	/**HTTP证书在服务器中的路径，用来加载证书用*/
	private static final String certLocalPath = "C:\\cert\\apiclient_cert.p12";
	/**HTTP证书的密码，默认等于MCHID*/
	private static final String certPassword = "1253257201";

    public static void main(String[] args) {

        try {
//        	//WXPay.initSDKConfiguration(key, appID, mchID, sdbMchID, certLocalPath, certPassword);
//        	ScanPayReqData scanPayReqData = new ScanPayReqData("131188177339311124", "饺子", "fujianshuju", "oderTest8", 1, "0001", "8.8.8.8", "20161228000000", "20161229000000", "goodsTag");
//        	String msg =WXPay.requestScanPayService(scanPayReqData);
//        	System.out.println("支付返回结果："+msg);
//        	ScanPayResData scanPayResData = (ScanPayResData) Util.getObjectFromXML(msg, ScanPayResData.class);
//        	System.out.println("支付返回结果Bean："+scanPayResData);
//        	ScanPayQueryReqData scanPayQueryReqData = new ScanPayQueryReqData(scanPayResData.getTransaction_id(), scanPayResData.getOut_trade_no());
//        	msg =WXPay.requestScanPayQueryService(scanPayQueryReqData);
//        	System.out.println("支付查询返回结果："+msg);
//        	ScanPayQueryResData scanPayQueryResData = (ScanPayQueryResData) Util.getObjectFromXML(msg, ScanPayQueryResData.class);
//        	System.out.println("支付查询返回结果Bean："+scanPayQueryResData);
        	//WXPay.requestScanPayService(scanPayReqData);
            //--------------------------------------------------------------------
            //温馨提示，第一次使用该SDK时请到com.tencent.common.Configure类里面进行配置
            //--------------------------------------------------------------------



            //--------------------------------------------------------------------
            //PART One:基础组件测试
            //--------------------------------------------------------------------

            //1）https请求可用性测试
            //HTTPSPostRquestWithCert.test();

            //2）测试项目用到的XStream组件，本项目利用这个组件将Java对象转换成XML数据Post给API
            //XStreamTest.test();


            //--------------------------------------------------------------------
            //PART Two:基础服务测试
            //--------------------------------------------------------------------

            //1）测试被扫支付API
            //PayServiceTest.test();

            //2）测试被扫订单查询API
            //PayQueryServiceTest.test();

            //3）测试撤销API
            //温馨提示，测试支付API成功扣到钱之后，可以通过调用PayQueryServiceTest.test()，将支付成功返回的transaction_id和out_trade_no数据贴进去，完成撤销工作，把钱退回来 ^_^v
            //ReverseServiceTest.test();

            //4）测试退款申请API
            //RefundServiceTest.test();

            //5）测试退款查询API
            //RefundQueryServiceTest.test();

            //6）测试对账单API
            //DownloadBillServiceTest.test();


            //本地通过xml进行API数据模拟的时候，先按需手动修改xml各个节点的值，然后通过以下方法对这个新的xml数据进行签名得到一串合法的签名，最后把这串签名放到这个xml里面的sign字段里，这样进行模拟的时候就可以通过签名验证了
           // Util.log(Signature.getSignFromResponseString(Util.getLocalXMLString("/test/com/tencent/business/refundqueryserviceresponsedata/refundquerysuccess2.xml")));

            //Util.log(new Date().getTime());
            //Util.log(System.currentTimeMillis());

        } catch (Exception e){
        	e.printStackTrace();
            Util.log(e.getMessage());
        }

    }

}
