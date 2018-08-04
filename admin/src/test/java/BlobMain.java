import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jodd.util.Base64;

import org.apache.commons.io.IOUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;

public class BlobMain {
	public static void main(String[] args) throws IOException {
		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(com.mysql.jdbc.Driver.class);
		ds.setUrl("jdbc:mysql://192.168.80.58:3306/lastmiles?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8");
		ds.setUsername("lmuser");
		ds.setPassword("lmuser");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		
		final File blobIn = new File("C:\\cert\\apiclient_cert.p12");
		final InputStream blobIs = new FileInputStream(blobIn);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		IOUtils.copy(blobIs, output);
		byte[] arr = output.toByteArray();
		String str = Base64.encodeToString(arr);
		jdbcTemplate.execute("update t_pay_channel_info set  certIo = ? where id= 1",
				new AbstractLobCreatingPreparedStatementCallback(
						new DefaultLobHandler()) {
					@Override
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException,
							DataAccessException {
//						ps.setLong(1, 1L);
//						lobCreator.setBlobAsBinaryStream(ps, 1, blobIs,
//								(int) blobIn.length());
						
						lobCreator.setClobAsString(ps, 1, str);
					}
				});
		
		
//		jdbcTemplate.update("INSERT INTO t_blob_test (sdata) VALUES (?)", str);

		
//		final File blobIn = new File("C:\\cert\\apiclient_cert.p12");
//		final InputStream blobIs = new FileInputStream(blobIn);
//		jdbcTemplate.execute("update t_pay_channel_info set  certIo = ? where id= 1",
//				new AbstractLobCreatingPreparedStatementCallback(
//						new DefaultLobHandler()) {
//					@Override
//					protected void setValues(PreparedStatement ps,
//							LobCreator lobCreator) throws SQLException,
//							DataAccessException {
////						ps.setLong(1, 1L);
//						lobCreator.setBlobAsBinaryStream(ps, 1, blobIs,
//								(int) blobIn.length());
//					}
//				});
//		blobIs.close();
/*
		List<Map<String, Object>> list = jdbcTemplate.query(
				"select data from t_blob_test",
				new RowMapper<Map<String, Object>>() {
					public Map<String, Object> mapRow(ResultSet rs, int i)
							throws SQLException {
						Map<String, Object> results = new HashMap<String, Object>();
						DefaultLobHandler lobHandler = new DefaultLobHandler();
						
						results.put("BLOB", lobHandler.getBlobAsBinaryStream(rs, "data"));
						return results;
					}
				});
		
		System.out.println(((InputStream)list.get(0).get("BLOB")).available());
		
		IOUtils.copy((InputStream)list.get(0).get("BLOB"), new FileOutputStream("f://1.gif"));*/
	}
}
