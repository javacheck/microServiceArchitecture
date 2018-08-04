package cn.lastmiles.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BeanUtils {
	
	public static <T> Map<String,Object> toMap(Object obj){
		Map<String,Object> map = new HashMap<String, Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get(obj));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return map;
	}

	public static <T> T toBean(Class<T> cl, Map<String, Object> map) {
		if (null == map || map.size() <= 0) {
			return null;
		}
		T instance = null;
		try {
			instance = cl.newInstance();
			Field[] fields = cl.getDeclaredFields();

			Set<String> keys = map.keySet();

			for (String key : keys) {
				for (Field field : fields) {
					field.setAccessible(true);
					String fieldName = field.getName();

					int index = key.indexOf(".");
					if (index != -1) {
						String beanName = key.substring(0, index);
						String attr = key.substring(index + 1);
						if (beanName.equalsIgnoreCase(fieldName)) {
							Object _cl = field.get(instance);
							if (_cl == null) {
								_cl = field.getType().newInstance();
							}
							Field[] _fields = field.getType()
									.getDeclaredFields();

							for (Field f : _fields) {
								f.setAccessible(true);
								String fn = f.getName();
								if (fn.equalsIgnoreCase(attr)) {
									Object value = map.get(key);
									if (value instanceof Number) {
										Number n = (Number) value;
										if (long.class.equals(f.getType())
												|| Long.class.equals(f
														.getType())) {
											f.set(_cl, n.longValue());
										} else if (int.class
												.equals(f.getType())
												|| Integer.class.equals(f
														.getType())) {
											f.set(_cl, n.intValue());
										} else if (short.class.equals(f
												.getType())
												|| Short.class.equals(f
														.getType())) {
											f.set(_cl, n.shortValue());
										} else if (byte.class.equals(f
												.getType())
												|| Byte.class.equals(f
														.getType())) {
											f.set(_cl, n.byteValue());
										} else if (double.class.equals(f
												.getType())
												|| Double.class.equals(f
														.getType())) {
											f.set(_cl, n.doubleValue());
										} else if (float.class.equals(f
												.getType())
												|| Float.class.equals(f
														.getType())) {
											f.set(_cl, n.floatValue());
										} else {
											f.set(_cl, value);
										}
									} else {
										f.set(_cl, value);
									}
									field.set(instance, _cl);
								}
							}
						}
					} else {
						if (key.equalsIgnoreCase(fieldName)) {
							Object value = map.get(key);
							if (value != null) {
								if (value instanceof Number) {
									Number n = (Number) value;
									if (long.class.equals(field.getType())
											|| Long.class.equals(field
													.getType())) {
										field.set(instance, n.longValue());
									} else if (int.class
											.equals(field.getType())
											|| Integer.class.equals(field
													.getType())) {
										field.set(instance, n.intValue());
									} else if (short.class.equals(field
											.getType())
											|| Short.class.equals(field
													.getType())) {
										field.set(instance, n.shortValue());
									} else if (byte.class.equals(field
											.getType())
											|| Byte.class.equals(field
													.getType())) {
										field.set(instance, n.byteValue());
									} else if (double.class.equals(field
											.getType())
											|| Double.class.equals(field
													.getType())) {
										field.set(instance, n.doubleValue());
									} else if (float.class.equals(field
											.getType())
											|| Float.class.equals(field
													.getType())) {
										field.set(instance, n.floatValue());
									} else {
										field.set(instance, value);
									}
								} else {
									field.set(instance, value);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return instance;
	}

	// public static <T> T toBean(Class<T> cl, Map<String, Object> map) {
	// if (null == map || map.size() <= 0) {
	// return null;
	// }
	// T instance = null;
	// try {
	// instance = cl.newInstance();
	// Field[] fields = cl.getDeclaredFields();
	//
	// for (Field field : fields) {
	// field.setAccessible(true);
	// String fieldName = field.getName();
	//
	// Object value = map.get(fieldName.toUpperCase());
	// value = (value == null ? map.get(fieldName) : value);
	// if (value != null) {
	// if (value instanceof Number) {
	// Number n = (Number) value;
	// if (long.class.equals(field.getType())
	// || Long.class.equals(field.getType())) {
	// field.set(instance, n.longValue());
	// } else if (int.class.equals(field.getType())
	// || Integer.class.equals(field.getType())) {
	// field.set(instance, n.intValue());
	// } else if (short.class.equals(field.getType())
	// || Short.class.equals(field.getType())) {
	// field.set(instance, n.shortValue());
	// } else if (byte.class.equals(field.getType())
	// || Byte.class.equals(field.getType())) {
	// field.set(instance, n.byteValue());
	// } else if (double.class.equals(field.getType())
	// || Double.class.equals(field.getType())) {
	// field.set(instance, n.doubleValue());
	// } else if (float.class.equals(field.getType())
	// || Float.class.equals(field.getType())) {
	// field.set(instance, n.floatValue());
	// } else {
	// field.set(instance, value);
	// }
	// } else {
	// field.set(instance, value);
	// }
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new RuntimeException(e);
	// }
	// return instance;
	// }

	public static <T> List<T> toList(Class<T> cl, List<Map<String, Object>> list) {
		List<T> ret = new ArrayList<T>();
		for (Map<String, Object> map : list) {
			ret.add(toBean(cl, map));
		}
		return ret;
	}
}
