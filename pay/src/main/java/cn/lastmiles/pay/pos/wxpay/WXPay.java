package cn.lastmiles.pay.pos.wxpay;


import cn.lastmiles.pay.pos.wxpay.business.DownloadBillBusiness;
import cn.lastmiles.pay.pos.wxpay.business.RefundBusiness;
import cn.lastmiles.pay.pos.wxpay.business.RefundQueryBusiness;
import cn.lastmiles.pay.pos.wxpay.business.ScanPayBusiness;
import cn.lastmiles.pay.pos.wxpay.common.Configure;
import cn.lastmiles.pay.pos.wxpay.protocol.downloadbill_protocol.DownloadBillReqData;
import cn.lastmiles.pay.pos.wxpay.protocol.pay_protocol.ScanPayReqData;
import cn.lastmiles.pay.pos.wxpay.protocol.pay_query_protocol.ScanPayQueryReqData;
import cn.lastmiles.pay.pos.wxpay.protocol.refund_protocol.RefundReqData;
import cn.lastmiles.pay.pos.wxpay.protocol.refund_query_protocol.RefundQueryReqData;
import cn.lastmiles.pay.pos.wxpay.protocol.reverse_protocol.ReverseReqData;
import cn.lastmiles.pay.pos.wxpay.service.*;

/**
 * SDK总入口
 */
public class WXPay {
	/*
	 /**
     * 初始化SDK依赖的几个关键配置
     * @param key 签名算法需要用到的秘钥
     * @param appID 公众账号ID
     * @param mchID 商户ID
     * @param sdbMchID 子商户ID，受理模式必填
     * @param certLocalPath HTTP证书在服务器中的路径，用来加载证书用
     * @param certPassword HTTP证书的密码，默认等于MCHID
    public static void initSDKConfiguration(PayMerchant payMerchant){
        Configure.setKey(payMerchant.getKey());
        Configure.setAppID(payMerchant.getAppID());
        Configure.setMchID(payMerchant.getMchID());
        Configure.setSubMchID(payMerchant.getSubMchID());
        Configure.setCertLocalPath(payMerchant.getCertLocalPath());
        Configure.setCertPassword(payMerchant.getCertPassword());
    }
*/
    /**
     * 初始化SDK依赖的几个关键配置
     * @param key 签名算法需要用到的秘钥
     * @param appID 公众账号ID
     * @param mchID 商户ID
     * @param sdbMchID 子商户ID，受理模式必填
     * @param certLocalPath HTTP证书在服务器中的路径，用来加载证书用
     * @param certPassword HTTP证书的密码，默认等于MCHID
     * @param certIo HTTP证书的流
     */
    public static Configure initSDKConfiguration(String key,String appID,String mchID,String sdbMchID,String certLocalPath,String certPassword,String certIo){
    	Configure configure = new Configure();
    	configure.setKey(key);
        configure.setAppID(appID);
        configure.setMchID(mchID);
        configure.setSubMchID(sdbMchID);
        configure.setCertLocalPath(certLocalPath);
        configure.setCertPassword(certPassword);
        configure.setCertIo(certIo);
        return configure;
    }

    /**
     * 请求支付服务
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public static String requestScanPayService(ScanPayReqData scanPayReqData,Configure configure) throws Exception{
        return new ScanPayService(configure).request(scanPayReqData);
    }

    /**
     * 请求支付查询服务
     * @param scanPayQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
	public static String requestScanPayQueryService(ScanPayQueryReqData scanPayQueryReqData,Configure configure) throws Exception{
		return new ScanPayQueryService(configure).request(scanPayQueryReqData);
	}

    /**
     * 请求退款服务
     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public static String requestRefundService(RefundReqData refundReqData,Configure configure) throws Exception{
        return new RefundService(configure).request(refundReqData);
    }

    /**
     * 请求退款查询服务
     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
	public static String requestRefundQueryService(RefundQueryReqData refundQueryReqData,Configure configure) throws Exception{
		return new RefundQueryService(configure).request(refundQueryReqData);
	}

    /**
     * 请求撤销服务
     * @param reverseReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
	public static String requestReverseService(ReverseReqData reverseReqData,Configure configure) throws Exception{
		return new ReverseService(configure).request(reverseReqData);
	}

    /**
     * 请求对账单下载服务
     * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public static String requestDownloadBillService(DownloadBillReqData downloadBillReqData,Configure configure) throws Exception{
        return new DownloadBillService(configure).request(downloadBillReqData);
    }

    /**
     * 直接执行被扫支付业务逻辑（包含最佳实践流程）
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    public static void doScanPayBusiness(ScanPayReqData scanPayReqData, ScanPayBusiness.ResultListener resultListener,Configure configure) throws Exception {
        new ScanPayBusiness(configure).run(scanPayReqData, resultListener);
    }

    /**
     * 调用退款业务逻辑
     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 业务逻辑可能走到的结果分支，需要商户处理
     * @throws Exception
     */
    public static void doRefundBusiness(RefundReqData refundReqData, RefundBusiness.ResultListener resultListener,Configure configure) throws Exception {
        new RefundBusiness(configure).run(refundReqData,resultListener);
    }

    /**
     * 运行退款查询的业务逻辑
     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    public static void doRefundQueryBusiness(RefundQueryReqData refundQueryReqData,RefundQueryBusiness.ResultListener resultListener,Configure configure) throws Exception {
        new RefundQueryBusiness(configure).run(refundQueryReqData,resultListener);
    }

    /**
     * 请求对账单下载服务
     * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @return API返回的XML数据
     * @throws Exception
     */
    public static void doDownloadBillBusiness(DownloadBillReqData downloadBillReqData,DownloadBillBusiness.ResultListener resultListener,Configure configure) throws Exception {
        new DownloadBillBusiness(configure).run(downloadBillReqData,resultListener);
    }


}
