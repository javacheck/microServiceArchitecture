package cn.lastmiles.bean;
/**
 * createDate : 2015年8月27日 上午10:32:46 
 */

public class StoreTerminal {
	private Long storeId;
	private String terminalId;
	
	public StoreTerminal() {
		super();
	}
	
	public StoreTerminal(Long storeId, String terminalId) {
		super();
		this.storeId = storeId;
		this.terminalId = terminalId;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
}