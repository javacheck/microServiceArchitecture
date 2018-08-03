package cn.self.cloud.commonutils.basictype;

/**
 * 对象操作类
 */
public abstract class ObjectUtils {

	/**
	 * 功能：对比两个对象是否相等
	 * @param obj1 Object
	 * @param obj2 Object
	 * @return 相等返回true
	 */
	public static boolean equals(Object obj1, Object obj2) {
		// 借用spring的对象对比方法
		return org.springframework.util.ObjectUtils.nullSafeEquals(obj1, obj2);
	}

	// 单元测试
    public static void main(String[] args) {
        System.out.println("对比传入的两个对象是否相等：" + equals("123",new String("123")));
    }
}
