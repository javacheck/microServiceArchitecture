package cn.lastmiles.bean;

public class PayRecord {
	private Long orderId;
	private String queryId;
	private String respCode;
	private String respMsg;
	private String settleDate;
	private String traceNo;
	private String traceTime;
	private Integer txnAmt;
	private String txnTime;
	private int channel;//1表示银联，2微信
	private int status;//1成功，2失败
	
	public PayRecord(){super();};

	public PayRecord(Long orderId, String queryId, String respCode,
			String respMsg, String settleDate, String traceNo,
			String traceTime, Integer txnAmt, String txnTime, int channel,int status) {
		super();
		this.orderId = orderId;
		this.queryId = queryId;
		this.respCode = respCode;
		this.respMsg = respMsg;
		this.settleDate = settleDate;
		this.traceNo = traceNo;
		this.traceTime = traceTime;
		this.txnAmt = txnAmt;
		this.txnTime = txnTime;
		this.channel = channel;
		this.status = status;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}

	public Integer getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(Integer txnAmt) {
		this.txnAmt = txnAmt;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
