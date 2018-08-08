package com.alipay.api.response;

import com.alipay.api.internal.mapping.ApiField;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: alipay.offline.material.image.upload response.
 * 
 * @author auto create
 * @since 1.0, 2016-03-09 19:13:02
 */
public class AlipayOfflineMaterialImageUploadResponse extends AlipayResponse {

	private static final long serialVersionUID = 8195244468618967797L;

	/** 
	 * 图片在商家中心的唯一标识
	 */
	@ApiField("image_id")
	private String imageId;

	/** 
	 * 图片的访问地址（为了防止盗链，该地址不允许嵌在其他页面展示，只能在新页面展示）
	 */
	@ApiField("image_url")
	private String imageUrl;

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getImageId( ) {
		return this.imageId;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageUrl( ) {
		return this.imageUrl;
	}

}