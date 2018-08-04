package cn.lastmiles.pay.pos.wxpay.protocol.pay_query_protocol;

import cn.lastmiles.pay.pos.wxpay.common.RandomStringGenerator;
import cn.lastmiles.pay.pos.wxpay.protocol.BaseReqData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import jodd.util.ReflectUtil;

/**
 * User: rizenguo
 * Date: 2014/10/25
 * Time: 13:54
 */
public class ScanPayQueryReqData extends BaseReqData{

    //每个字段具体的意思请查看API文档
    private String transaction_id = "";
    private String out_trade_no = "";
    private String nonce_str = "";

    /**
     * 请求支付查询服务
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
     * @param outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @return API返回的XML数据
     * @throws Exception
     */
    public ScanPayQueryReqData(String transactionID, String outTradeNo){

        //--------------------------------------------------------------------
        //以下是测试数据，请商户按照自己的实际情况填写具体的值进去
        //--------------------------------------------------------------------


        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        setTransaction_id(transactionID);

        //商户系统自己生成的唯一的订单号
        setOut_trade_no(outTradeNo);

        //随机字符串，不长于32 位
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));

        //根据API给的签名规则进行签名
        //String sign = Signature.getSign(toMap());
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    @Override
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields =ReflectUtil.getSupportedFields( this.getClass());
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
