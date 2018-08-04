package cn.lastmiles.pay.pos.wxpay.service;

import cn.lastmiles.pay.pos.wxpay.common.Configure;
import cn.lastmiles.pay.pos.wxpay.protocol.reverse_protocol.ReverseReqData;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 16:04
 */
public class ReverseService extends BaseService{

    public ReverseService(Configure configure) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.REVERSE_API,configure);
    }

    /**
     * 请求撤销服务
     * @param reverseReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public String request(ReverseReqData reverseReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(reverseReqData);

        return responseString;
    }

}
