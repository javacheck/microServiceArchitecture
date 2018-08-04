package cn.lastmiles.pay.pos.wxpay.service;

import cn.lastmiles.pay.pos.wxpay.common.Configure;
import cn.lastmiles.pay.pos.wxpay.protocol.refund_query_protocol.RefundQueryReqData;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 16:04
 */
public class RefundQueryService extends BaseService{

    public RefundQueryService(Configure configure) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.REFUND_QUERY_API,configure);
    }

    /**
     * 请求退款查询服务
     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public String request(RefundQueryReqData refundQueryReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(refundQueryReqData);

        return responseString;
    }




}
