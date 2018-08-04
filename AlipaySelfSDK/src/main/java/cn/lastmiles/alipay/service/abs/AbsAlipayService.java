/**
 * createDate : 2016年7月25日下午4:46:09
 * 公共调用类
 */
package cn.lastmiles.alipay.service.abs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.lastmiles.alipay.config.Constants;
import cn.lastmiles.alipay.model.builder.RequestBuilder;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;

public abstract class AbsAlipayService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    // 验证bizContent对象
    protected String validateBuilder(RequestBuilder builder) {
        if ( builder == null ) {
            return "builder should not be NULL!";
        }
       return builder.validate();
    }

    protected boolean baseError(AlipayResponse response) {
		return null == response || Constants.ERROR.equals(response.getCode());
	}
    
    // 调用AlipayClient的execute方法，进行远程调用
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected AlipayResponse getResponse(AlipayClient client, AlipayRequest request) {
        try {
            AlipayResponse response = client.execute(request);
            if (response != null) {
                logger.info(response.getBody());
            }
            return response;

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}