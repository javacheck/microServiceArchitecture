package cn.self.cloud.commonutils.id;
/**
 * ID生成根接口
 */
public interface IdService {
	/**
	 * 获得生成的唯一ID
	 * @return 唯一ID
	 */
	Long getId();
	Long getOrderId();
}
