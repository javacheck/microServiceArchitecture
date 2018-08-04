import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.common.utils.NumberUtils;
import jodd.http.HttpRequest;


public class IfTest {

	public static void main(String[] args) throws Exception {
		BigDecimal bigd = new BigDecimal(0.098);  
		double value = bigd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println(value);
	}

}
