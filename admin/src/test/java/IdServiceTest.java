

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.lastmiles.bean.movie.Room;
import cn.lastmiles.cache.CacheService;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.config.RootConfig;
import cn.lastmiles.constant.MQTopic;
import cn.lastmiles.mq.MQService;
import cn.lastmiles.service.RoomService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RootConfig.class)
public class IdServiceTest {
	@Autowired
	private IdService idService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private FileService fileService;
	@Autowired
	private cn.lastmiles.dao.temporaryDao temporaryDao;
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private MQService mqService;
	
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

	@Test
	public void test() {
		System.out.println(roomService);
		Room room = new Room();
		room.setId(2961790L);
		
		Date date = DateUtils.parse("yyyy-MM-dd HH:mm", "2016-04-15 03:20");
//		double price = roomService.reckonRoomPrice(date, 40, 1146763L, room);
//		System.out.println(price);
	}

	public static void main(String[] args) {
		List<Long> list = new ArrayList<Long>();
		list.add(400L);
		list.add(800L);
		list.add(200L);
		// list.add(100L);

		if (list == null || list.size() <= 1) {
			System.out.println();
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			StringBuffer sf=new StringBuffer();
			for (int j = 0; j < list.size(); j++) {
				if (i == j) {
				} else {
					Long l = list.get(j);
					sf.append("-" + l + "-*");
				}
			}//J循环结束
			if (list.size()==2) {
				System.out.print("*"+sf.toString()+"\t");
				System.out.print(sf.substring(1)+"\t");
				System.out.println("*"+sf.substring(0,sf.length()-2));
			}else {
				System.out.print("*"+sf.toString()+"\t");
				System.out.print(sf.substring(1)+"\t");
				System.out.print("*"+sf.substring(0,sf.length()-2)+"\t");
				System.out.println(sf.substring(1,sf.length()-2));
			}
		}//I循环结束
	}
}
