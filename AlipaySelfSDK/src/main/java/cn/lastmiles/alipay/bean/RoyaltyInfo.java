/**
 * createDate : 2016年7月27日下午4:44:54
 */
package cn.lastmiles.alipay.bean;

import java.util.Arrays;

import com.alipay.api.domain.RoyaltyDetailInfos;

public class RoyaltyInfo {
	/**
	 * <可选>
	 * 最大长度为150位
	 * 分账类型 卖家的分账类型，目前只支持传入ROYALTY（普通分账类型）。
	 */
	private String royaltyType;
	/**
	 * <必填>
	 * 分账明细的信息，可以描述多条分账指令，JSON数组。
	 */
	private RoyaltyDetailInfos[] royaltyDetailInfos;
	
	public RoyaltyInfo() {
		super();
	}

	public RoyaltyInfo(RoyaltyDetailInfos[] royaltyDetailInfos) {
		super();
		this.royaltyDetailInfos = royaltyDetailInfos;
	}

	public String getRoyaltyType() {
		return royaltyType;
	}

	public void setRoyaltyType(String royaltyType) {
		this.royaltyType = royaltyType;
	}

	public RoyaltyDetailInfos[] getRoyaltyDetailInfos() {
		return royaltyDetailInfos;
	}

	public void setRoyaltyDetailInfos(RoyaltyDetailInfos[] royaltyDetailInfos) {
		this.royaltyDetailInfos = royaltyDetailInfos;
	}

	@Override
	public String toString() {
		return "RoyaltyInfo [royaltyType=" + royaltyType
				+ ", royaltyDetailInfos=" + Arrays.toString(royaltyDetailInfos)
				+ "]";
	}
	
}