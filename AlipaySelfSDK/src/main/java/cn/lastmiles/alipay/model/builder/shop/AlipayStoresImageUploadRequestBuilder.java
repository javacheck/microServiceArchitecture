/**
 * createDate : 2016年7月25日下午3:54:42
 * 口碑店铺
 */
package cn.lastmiles.alipay.model.builder.shop;

import org.apache.commons.lang.StringUtils;
import cn.lastmiles.alipay.config.Constants;
import cn.lastmiles.alipay.model.builder.RequestBuilder;
import com.alipay.api.FileItem;

public class AlipayStoresImageUploadRequestBuilder extends RequestBuilder {
	
	private BizContent bizContent = new BizContent();
	
	@Override
	public BizContent getBizContent() {
		return bizContent;
	}
		
	@Override
	public String validate() {
		 if ( StringUtils.isBlank(bizContent.imageName) ) {
	          return ("imageName should not be NULL!");
	     }
		 if( StringUtils.isNotBlank(bizContent.imageName) && bizContent.imageName.length() > 128 ){
			 return ("imageName length is very big , more than 128...");
		 }
		 if( StringUtils.isBlank(bizContent.imageType)){
			 return ("imageType should not be NULL!");
		 }
		 		 
		 if( null == bizContent.imageContent ){
			 return ("imageContent should not be NULL!");
		 }
		return Constants.VALIDATESUCCESS;
	}
	
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AlipayStoresImageUploadRequestBuilder { ");
        sb.append("bizContent=").append(bizContent);
        sb.append(", super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

	public static class BizContent {
        // <必填>图片格式(最大长度为8位)
        //@SerializedName("image_type")
        private String imageType;

        // <必填>图片名称(最大长度为128位)
        //@SerializedName("image_name")
        private String imageName;
        
        // <必填>图片二进制内容
        //@SerializedName("image_content")
        private FileItem imageContent;
        
        // <可选>用于显示指定图片所属的partnerId(支付宝内部使用，外部商户无需填写此字段,最大长度为16位)
        private String imagePid;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("AlipayStoresImageUploadRequestBuilder--->BizContent \r【");
            sb.append("imageType is : <").append(imageType).append(">\r");
            sb.append("imageName is : <").append(imageName).append(">\r");
            sb.append("imageContent is : <").append(imageContent).append(">\r");
            sb.append("imagePid is : <").append(imagePid).append(">\r");
            sb.append("】");
            return sb.toString();
        } 
	}
	
	public String getImageType() {
		return bizContent.imageType;
	}

	public void setImageType(String imageType) {
		bizContent.imageType = imageType;
	}

	public String getImageName() {
		return bizContent.imageName;
	}

	public void setImageName(String imageName) {
		bizContent.imageName = imageName;
	}

	public FileItem getImageContent() {
		return bizContent.imageContent;
	}

	public void setImageContent(FileItem imageContent) {
		bizContent.imageContent = imageContent;
	}

	public String getImagePid() {
		return bizContent.imagePid;
	}

	public void setImagePid(String imagePid) {
		bizContent.imagePid = imagePid;
	}
}