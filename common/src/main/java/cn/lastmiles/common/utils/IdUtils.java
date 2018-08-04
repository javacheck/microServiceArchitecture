package cn.lastmiles.common.utils;

import com.relops.snowflake.Snowflake;

public class IdUtils {
	final static Snowflake snowflake = new Snowflake(1);
	
	public static Long getId(){
		return snowflake.next();
	}
}
