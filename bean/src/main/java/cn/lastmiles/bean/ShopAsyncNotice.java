/**
 * createDate : 2016年10月12日下午5:08:59
 */
package cn.lastmiles.bean;
/**
 * 门店异步通知
 * @author wanglin
 *
 */
public class ShopAsyncNotice {
	private String request_id;
	private String biz_type;
	private String notify_id;
	private String notify_time;
	private String notify_type;
	private String apply_id;
	private String audit_status;
	private String is_online;
	private String is_show;
	private String version;
	private String sign_type;
	private String sign;
	private String shop_id;
	private String result_code;
	private String result_desc;
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public String getBiz_type() {
		return biz_type;
	}
	public void setBiz_type(String biz_type) {
		this.biz_type = biz_type;
	}
	public String getNotify_id() {
		return notify_id;
	}
	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}
	public String getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}
	public String getNotify_type() {
		return notify_type;
	}
	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}
	public String getApply_id() {
		return apply_id;
	}
	public void setApply_id(String apply_id) {
		this.apply_id = apply_id;
	}
	public String getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(String audit_status) {
		this.audit_status = audit_status;
	}
	public String getIs_online() {
		return is_online;
	}
	public void setIs_online(String is_online) {
		this.is_online = is_online;
	}
	public String getIs_show() {
		return is_show;
	}
	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getResult_desc() {
		return result_desc;
	}
	public void setResult_desc(String result_desc) {
		this.result_desc = result_desc;
	}
	@Override
	public String toString() {
		return "ShopAsyncNotice [request_id=" + request_id + ", biz_type="
				+ biz_type + ", notify_id=" + notify_id + ", notify_time="
				+ notify_time + ", notify_type=" + notify_type + ", apply_id="
				+ apply_id + ", audit_status=" + audit_status + ", is_online="
				+ is_online + ", is_show=" + is_show + ", version=" + version
				+ ", sign_type=" + sign_type + ", sign=" + sign + ", shop_id="
				+ shop_id + ", result_code=" + result_code + ", result_desc="
				+ result_desc + "]";
	}
	
}