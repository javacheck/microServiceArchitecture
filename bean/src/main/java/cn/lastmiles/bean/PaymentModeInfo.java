package cn.lastmiles.bean;

/**
 * 
 * @author zhangpengcheng
 *
 */
public class PaymentModeInfo {
	
	private Long id;//ID
	
	private Long associationId;//关联ID
	
	private Integer paymentMode;//支付方式
	
	private Integer amount;//次数
	
	private Double price;//总价
	
	public PaymentModeInfo(){
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAssociationId() {
		return associationId;
	}

	public void setAssociationId(Long associationId) {
		this.associationId = associationId;
	}

	public Integer getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(Integer paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	
	

}
