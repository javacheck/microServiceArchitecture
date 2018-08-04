import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Main {
	public static void main(String[] args) {
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			Connection conn = DriverManager
//			.getConnection(
//					"jdbc:mysql://192.168.80.58:3306/lastmiles?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8",
//					"lmuser", "lmuser");
//			System.out.println(conn);
//			ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
//			while (rs.next()){
//				String tbname = rs.getString("TABLE_NAME");
//				System.out.println(tbname);
//				ResultSet rrs = conn.getMetaData().getColumns(null, "%", tbname, "%");
//				while (rrs.next()){
//					System.out.println(rrs.getString("COLUMN_NAME"));
//					System.out.println(rrs.getString("TYPE_NAME"));
//				}
//				rrs = conn.getMetaData().getPrimaryKeys(null, null, tbname);
//				while (rrs.next()){
//					System.err.println("TABLE_NAME : "+rrs.getObject(3)); 
//					System.err.println("COLUMN_NAME: "+rrs.getObject(4)); 
//					System.err.println("KEY_SEQ : "+rrs.getObject(5)); 
//					System.err.println("PK_NAME : "+rrs.getObject(6)); 
//				}
//				System.out.println("--------------------------");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println((0.09 * 100 + 1.05 * 100) / 100);
	}
}
