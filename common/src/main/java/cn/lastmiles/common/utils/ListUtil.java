package cn.lastmiles.common.utils;

import java.util.List;


public final class ListUtil {
	
	public static  boolean isNull(List<?> list ){
		return list==null||list.isEmpty();
	}
	public static  boolean isNotNull(List<?> list ){
		return list!=null&&!list.isEmpty();
	}
	
}
