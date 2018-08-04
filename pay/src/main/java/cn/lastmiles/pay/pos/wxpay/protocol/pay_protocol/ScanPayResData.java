package cn.lastmiles.pay.pos.wxpay.protocol.pay_protocol;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * User: rizenguo
 * Date: 2014/10/22
 * Time: 16:42
 */

/**
 * 被扫支付提交Post数据给到API之后，API会返回XML格式的数据，这个类用来装这些数据
 */
@Table(name = "t_scanpay_resdata")
public class ScanPayResData {
    //协议层
	@Column
    private String return_code = "";
	@Column
    private String return_msg = "";

    //协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
	@Column
    private String appid = "";
	@Column
    private String mch_id = "";
	@Column
    private String nonce_str = "";
	@Column
    private String sign = "";
	@Column
    private String result_code = "";
	@Column
    private String err_code = "";
	@Column
    private String err_code_des = "";
	@Column
    private String device_info = "";

    //业务返回的具体数据（以下字段在return_code 和result_code 都为SUCCESS 的时候有返回）
	@Column
    private String openid = "";
	@Column
    private String is_subscribe = "";
	@Column
    private String trade_type = "";
	@Column
    private String bank_type = "";
	@Column
    private String total_fee = "";
	@Column
    private String coupon_fee = "";
	@Column
    private String fee_type = "";
	@Column
    private String transaction_id = "";
	@Column
    private String out_trade_no = "";
	@Column
    private String attach = "";
	@Column
    private String time_end = "";

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getCoupon_fee() {
        return coupon_fee;
    }

    public void setCoupon_fee(String coupon_fee) {
        this.coupon_fee = coupon_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
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

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

	@Override
	public String toString() {
		return "ScanPayResData [return_code=" + return_code + ", return_msg="
				+ return_msg + ", appid=" + appid + ", mch_id=" + mch_id
				+ ", nonce_str=" + nonce_str + ", sign=" + sign
				+ ", result_code=" + result_code + ", err_code=" + err_code
				+ ", err_code_des=" + err_code_des + ", device_info="
				+ device_info + ", openid=" + openid + ", is_subscribe="
				+ is_subscribe + ", trade_type=" + trade_type + ", bank_type="
				+ bank_type + ", total_fee=" + total_fee + ", coupon_fee="
				+ coupon_fee + ", fee_type=" + fee_type + ", transaction_id="
				+ transaction_id + ", out_trade_no=" + out_trade_no
				+ ", attach=" + attach + ", time_end=" + time_end + "]";
	}
    
}
