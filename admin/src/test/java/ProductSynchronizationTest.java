import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.config.RootConfig;
import cn.lastmiles.service.ProductSynchronizationService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class)
public class ProductSynchronizationTest {
	static {
		// 加载配置文件
		try {
			String filename = "common.properties";
			ConfigUtils.load(ClassLoaderUtil.getResourceAsStream(filename));
			
			String osname = System.getProperty("os.name").toLowerCase();
			String configDir = ConfigUtils.getProperty("config.dir");
			if (osname.startsWith("win")){
				configDir = ConfigUtils.getProperty("config.windows.dir");
			}
			if (StringUtil.isNotBlank(configDir)) {
				File file = new File(configDir + File.separatorChar + filename);
				if (file.exists()) {
					ConfigUtils.clearAndLoad(new FileInputStream(file));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Autowired
	private ProductSynchronizationService service ;
	
	@Test
	public void m(){
		service.sync(2968895L, 2969867L,null);
	}
}
