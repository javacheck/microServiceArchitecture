package cn.self.cloud.commonutils.zother;

import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cp on 2017/10/30.
 */
public class ProduceEntity {
    public static final String DBName = "db_pinbao";
    public static final String url = "jdbc:mysql://rdscp5h7mz0uj29xeo6uo.mysql.rds.aliyuncs.com:3306/" + DBName + "?autoReconnect=true&amp;characterEncoding=utf8";
    public static final String user = "test1";
    public static final String password = "asdf20160225";
//    public static final String DBName = "db_notebook";
//    public static final String url = "jdbc:mysql://localhost:3306/" + DBName + "?autoReconnect=true&amp;characterEncoding=utf8";
//    public static final String user = "root";
//    public static final String password = "123456";


    public static void main(String[] args) throws Exception {
        Connection connection = null;
        Statement stmt = null;
        DatabaseMetaData m_DBMetaData;
        String tableNamePattern = "tb_%";
        String dirName = "ProduceEntity/produced/";
        File file = new File(dirName);
        if (!file.exists())
            file.mkdir();
        System.out.println(file.getAbsolutePath());
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            //2. 下面就是获取表的信息。
            m_DBMetaData = connection.getMetaData();
            ResultSet tableRet = m_DBMetaData.getTables(null, "%", tableNamePattern, new String[]{"TABLE"});
/*其中"%"就是表示*的意思，也就是任意所有的意思。其中m_TableName就是要获取的数据表的名字，如果想获取所有的表的名字，就可以使用"%"来作为参数了。*/

//3. 提取表的名字。
            while (tableRet.next()) {

                String tableName = tableRet.getString("TABLE_NAME");
                String tableRemark = getTableComment(connection, DBName, tableName);
                System.out.println("**** " + tableName + " ****");
                String className = produceClassName(tableName);
                String filePath = dirName + className + ".java";
                System.out.println("ClassName:" + className);
                System.out.println("filePath:" + filePath);
                StringBuilder content = new StringBuilder();
                content.append("import org.redkale.convert.json.JsonConvert;\r\n" +
                        "import org.redkale.source.FilterBean;\r\n" +
                        "import org.redkale.util.Comment; \r\n");
                content.append("import javax.persistence.Id;\r\n" +
                        "import javax.persistence.Table;\r\n" +
                        "import java.io.Serializable;\r\n");
                // content.append("/**\r\n  * Created by cp on " + DateFormatUtils.formatToday() + ".\r\n  */\r\n");

                content.append("@Table(name = \"" + tableName + "\") \r\n");
                content.append("@Comment(\"" + tableRemark + "\")\r\n");
                content.append("public class " + className + " implements Serializable, FilterBean {\r\n");
/*通过getString("TABLE_NAME")，就可以获取表的名字了。
从这里可以看出，前面通过getTables的接口的返回，JDBC是将其所有的结果，保存在一个类似table的内存结构中，而其中TABLE_NAME这个名字的字段就是每个表的名字。*/
                String primayrKey = "";
                ResultSet primaryKeyResultSet = m_DBMetaData.getPrimaryKeys("", null, tableName);
                while (primaryKeyResultSet.next()) {
                    primayrKey = primaryKeyResultSet.getString("COLUMN_NAME");
                    System.out.println("主键：" + primayrKey);
                }

                StringBuilder geterAndSetters = new StringBuilder();
//4. 提取表内的字段的名字和类型
                String columnName;
                String columnType;
                ResultSet colRet = m_DBMetaData.getColumns(null, "%", tableName, "%");
                while (colRet.next()) {
                    columnName = colRet.getString("COLUMN_NAME");
                    columnType = colRet.getString("TYPE_NAME");
                    int datasize = colRet.getInt("COLUMN_SIZE");
                    int digits = colRet.getInt("DECIMAL_DIGITS");
                    int nullable = colRet.getInt("NULLABLE");
                    String remark = colRet.getString("REMARKS");
                    remark = remark.replace("\r\n", "\"+\n\"");
                    System.out.println(columnName + " " + columnType + " " + datasize + " " + digits + " " + nullable);

                    String type = getTypeInTypeMap(columnType);
                    if (columnName.equals(primayrKey)) {
                        content.append(" @Id\r\n" +
                                "@Comment(\"主键\")\r\n" +
                                "private  "+type+" "+columnName+" ; \r\n");
                    } else {
                        content.append("@Comment(\"" + remark + "\")\r\n");
                        content.append("private " + type + " " + columnName + ";\r\n");
                    }

                    String columnNameFirstUp = getStringFirstUp(columnName);
                    geterAndSetters.append("public " + type + " get" + columnNameFirstUp + "() {\r\n" +
                            "    return " + columnName + ";\r\n" +
                            "}\r\n" +
                            "\r\n" +
                            "public void set" + columnNameFirstUp + "(" + type + " " + columnName + ") {\r\n" +
                            "    this." + columnName + " = " + columnName + ";\r\n" +
                            "}\r\n");
                }
                content.append("\r\n");
                content.append(geterAndSetters);
                content.append("@Override\r\npublic String toString() {\r\n         " +
                        "return JsonConvert.root().convertTo(this);\r\n}\r\n");
                content.append("}\r\n");
                System.out.println(content);
                System.out.println("**** " + className + " ****");
                FileUtils.writeStringToFile(new File(filePath), content.toString());
            }
/*JDBC里面通过getColumns的接口，实现对字段的查询。跟getTables一样，"%"表示所有任意的（字段），而m_TableName就是数据表的名字。

getColumns的返回也是将所有的字段放到一个类似的内存中的表，而COLUMN_NAME就是字段的名字，TYPE_NAME就是数据类型，比如"int","int unsigned"等等，COLUMN_SIZE返回整数，就是字段的长度，比如定义的int(8)的字段，返回就是8，最后NULLABLE，返回1就表示可以是Null,而0就表示Not Null。*/
        } finally {
            if (stmt != null)
                stmt.close();
            if (connection != null)
                connection.close();
        }
    }

    private static String getTableComment(Connection connection, String dbName, String tableName) throws SQLException {
        Statement statement = null;
        try {
            String sql = "Select table_name ,TABLE_COMMENT  from INFORMATION_SCHEMA.TABLES Where table_schema ='" + dbName + "' and table_name ='" + tableName + "'";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                String comment = resultSet.getString("TABLE_COMMENT");
                return comment;
            }
            return "";
        } finally {
            if (statement != null)
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public static Map<String, String> map = new HashMap();

    static {
        map.put("VARCHAR", "String");
        map.put("INT", "int");
        map.put("TINYINT", "int");
        map.put("BIGINT", "long");
        map.put("DOUBLE", "double");
        map.put("FLOAT", "float");
        map.put("DATETIME", "Date");
        map.put("DATE", "Date");
        map.put("SMALLINT", "int");
        map.put("DECIMAL", "double");
        map.put("TEXT","String");
    }

    private static String getTypeInTypeMap(String columnType) {
        return map.get(columnType);
    }

    private static String getStringFirstUp(String str) {
        if (StringUtils.isNullOrEmpty(str))
            return null;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 根据表名产生文件名，也就是类名
     *
     * @param tableName 类似 tb_account_bank 的格式
     * @return
     */
    private static String produceClassName(String tableName) {
        if (tableName.indexOf("tb_") < 0)
            return null;
        List<String> list = StringUtils.split(tableName, "_", true);
        String fileName = "";
        for (String str : list) {
            if (!str.equals("tb"))
                fileName += getStringFirstUp(str);
        }
        return fileName;
    }
}
