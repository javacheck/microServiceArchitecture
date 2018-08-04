package cn.lastmiles.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import jodd.io.FileUtil;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.StringUtils;

@Service
public class InitService {
	private Logger logger = LoggerFactory.getLogger(InitService.class);

	public InitService() {
		init();
	}
	
	public static void main(String[] args){
		
	}

	private void init() {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
//			Class.forName(ConfigUtils.getProperty("jdbc.driver"));
//			conn = DriverManager.getConnection(
//					ConfigUtils.getProperty("jdbc.url"),
//					ConfigUtils.getProperty("jdbc.username"),
//					ConfigUtils.getProperty("jdbc.password"));
//			rs = conn.getMetaData().getTables(null, null, "t_id", null);
//			stmt = conn.createStatement();
//			if (!rs.next()) {
//				logger.debug("init database .............. ");
//				String sql = FileUtil.readUTFString(ClassLoaderUtil
//						.getResourceAsStream("init.sql"));
//				String[] sqls = sql.split(";");
//
//				for (String s : sqls) {
//					if (StringUtil.isNotBlank(s)) {
//						stmt.addBatch(s);
//					}
//				}
//
//				stmt.executeBatch();
//			}

			logger.info("init service ..................... ");
			
			String sql = FileUtil.readUTFString(ClassLoaderUtil
					.getResourceAsStream("update.sql"));

			if (StringUtils.isNotBlank(sql)) {
				Class.forName(ConfigUtils.getProperty("jdbc.driver"));
				conn = DriverManager.getConnection(
						ConfigUtils.getProperty("jdbc.url"),
						ConfigUtils.getProperty("jdbc.username"),
						ConfigUtils.getProperty("jdbc.password"));
				stmt = conn.createStatement();
				String name = "sql_version";

				rs = stmt
						.executeQuery("select value from t_sys_config where name = '"
								+ name + "'");

				int version = 0;
				if (rs.next()) {
					version = Integer.valueOf(rs.getString(1));
				}

				String[] sqls = sql.split(";");
				int currentVersion = Integer.valueOf(sqls[0].trim());

				if (currentVersion > version) {
					for (int i = 1; i < sqls.length; i++) {
						if (StringUtil.isNotBlank(sqls[i])) {
							stmt.addBatch(sqls[i]);
						}
					}
					stmt.executeBatch();

					int ret = stmt
							.executeUpdate("update t_sys_config set value = '"
									+ currentVersion + "' where name = '"
									+ name + "'");
					if (ret == 0) {
						stmt.executeUpdate("insert into t_sys_config (name,value) values('"
								+ name + "','" + currentVersion + "')");
					}
				}
				
				logger.info("update database finished versio = {}..............  ",version);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeStatement(stmt);
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeConnection(conn);
		}
	}
	//
	// @Scheduled(fixedDelay = 1000 * 60)
	// public void updateSql() throws IOException {
	// // 修改数据库
	// String path = Config.getProperty("sql.dir") + "/admin.sql";
	// File file = new File(path);
	// if (file.exists()) {
	// logger.info("更新数据库 .............. ");
	// String sql = FileUtil.readUTFString(file);
	// String[] sqls = sql.split(";");
	// for (String s : sqls) {
	// logger.info(s);
	// if (StringUtil.isNotBlank(s)) {
	// jdbcTemplate.batchUpdate(s);
	// }
	// }
	// FileUtil.delete(file);
	// }
	// }
}
