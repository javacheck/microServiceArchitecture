package cn.self.cloud.commonutils.jdbc;

import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import jodd.io.FileNameUtil;
import jodd.io.StreamUtil;
import jodd.io.findfile.ClassScanner;
import jodd.lagarto.dom.Document;
import jodd.lagarto.dom.Element;
import jodd.lagarto.dom.LagartoDOMBuilder;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.ReflectionUtils;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

public class JdbcUtils {
    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static StringTemplateLoader templateLoader = new StringTemplateLoader();
    private final static Configuration cfg = new Configuration(new Version("2.3.23"));

    public static void init(NamedParameterJdbcTemplate jt) {
        try {
            namedParameterJdbcTemplate = jt;
            cfg.setTemplateLoader(templateLoader);
            // 加载sql
            URL url = ClassLoaderUtil.getResourceUrl("/user_sql");
            if (url != null) {
                ClassScanner classScanner = new ClassScanner() {
                    @Override
                    protected void onEntry(EntryData entryData)
                            throws Exception {
                        String filename = entryData.getName();
                        if (filename.endsWith(".xml")) {
                            InputStream inputStream = entryData.openInputStream();
                            byte[] bytes = StreamUtil.readAvailableBytes(inputStream);

                            String content = new String(bytes, "utf-8");

                            LagartoDOMBuilder domBuilder = new LagartoDOMBuilder();
                            domBuilder.enableXmlMode();
                            Document doc = domBuilder.parse(content);
                            Element root = doc.getFirstChildElement("root");
                            if (root != null) {
                                Element[] sqlElement = root.getChildElements();
                                for (Element e : sqlElement) {
                                    String id = FileNameUtil.getBaseName(filename) + "." + e.getAttribute("id");
                                    String sql = e.getTextContent();
                                    if (templateLoader.findTemplateSource(id) != null) {
                                        throw new RuntimeException("id = " + id + " exist");
                                    }
                                    templateLoader.putTemplate(id, sql);
                                }
                            }
                        }
                    }
                };

                classScanner.setIncludeResources(true);
                classScanner.scan(url);
            }
        } catch (Exception e) {
            logger.debug("load sql error ");
        }
    }

    public static void loadSql(String id) {
        logger.info("load sql = {}", id);
        try {
            String filename = StringUtil.split(id, ".")[0] + ".xml";
            InputStream is = ClassLoaderUtil.getResourceAsStream("/user_sql/" + filename);
            byte[] bytes = StreamUtil.readAvailableBytes(is);

            String content = new String(bytes, "utf-8");

            LagartoDOMBuilder domBuilder = new LagartoDOMBuilder();
            domBuilder.enableXmlMode();
            Document doc = domBuilder.parse(content);
            Element root = doc.getFirstChildElement("root");
            if (root != null) {
                Element[] sqlElement = root.getChildElements();
                for (Element e : sqlElement) {
                    id = FileNameUtil.getBaseName(filename) + "." + e.getAttribute("id");
                    if (templateLoader.findTemplateSource(id) == null) {
                        String sql = e.getTextContent();
                        templateLoader.putTemplate(id, sql);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private final static Logger logger = LoggerFactory
            .getLogger(JdbcUtils.class);

    public static void main(String[] args) {
    }

    public static List<Map<String, Object>> queryForList(String sqlId, Map<String, Object> parameters) {
        String sql = getSql(sqlId, parameters);
        return namedParameterJdbcTemplate.queryForList(sql, parameters);
    }

    public static <T> List<T> queryForList(String sqlId, Map<String, Object> parameters, Class<T> cl) {
        String sql = getSql(sqlId, parameters);
        if (cl.getCanonicalName().startsWith("java")) {
            return namedParameterJdbcTemplate.queryForList(sql, parameters, cl);
        } else {
            List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sql, parameters);
            return BeanUtils.toList(cl, list);
        }
    }

    public static <T> T queryForObject(String sqlId, Map<String, Object> parameters, Class<T> cl) {
        logger.debug("parameters={}", parameters);
        String sql = getSql(sqlId, parameters);
        if (cl.getCanonicalName().startsWith("java")) {
            return namedParameterJdbcTemplate.queryForObject(sql, parameters, cl);
        } else {
            List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sql, parameters);
            return list.isEmpty() ? null : BeanUtils.toBean(cl, list.get(0));
        }
    }

    public static int update(String sqlId, Map<String, Object> parameters) {
        String sql = getSql(sqlId, parameters);
        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    private static String getSql(String name, Map<String, Object> parameters) {
        try {
            if (templateLoader.findTemplateSource(name) == null) {
                loadSql(name);
            }

            Template template = cfg.getTemplate(name);
            StringWriter out = new StringWriter();
            template.process(parameters, out);
            String sql = out.toString().trim();
            logger.debug("name = {}, sql == {}", name, sql);
            out.close();
            return sql;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static int deleteById(Class<?> cl, Object id) {
        String tableName = getTableName(cl);
        String idName = getIdName(cl);
        Map<String, Object> p = new HashMap<String, Object>();
        p.put(idName, id);
        return namedParameterJdbcTemplate.update("delete from " + tableName + " where " + idName + " = :" + idName, p);
    }

    public static <T> T findById(Class<T> cl, Object id) {
        String tableName = getTableName(cl);
        String idName = getIdName(cl);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(idName, id);
        return BeanUtils.toBean(cl, namedParameterJdbcTemplate.queryForMap("select * from " + tableName + " where " + idName + " = :" + idName, paramMap));
    }

    public static <T> T findByObj(Class<T> cl, String objName, Object obj) {
        String tableName = getTableName(cl);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(objName, obj);
        return BeanUtils.toBean(cl, namedParameterJdbcTemplate.queryForMap("select * from " + tableName + " where " + objName + " = :" + objName, paramMap));
    }

    private static String getIdName(Class<?> cl) {
        Field[] fields = cl.getDeclaredFields();
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                Column col = field.getAnnotation(Column.class);
                String colName = col.name();
                if (StringUtils.isBlank(colName)) {
                    colName = field.getName();
                }
                return colName;
            }
        }
        throw new RuntimeException("can not find id");
    }

    private static String getTableName(Class<?> cl) {
        Table tb = (Table) cl.getAnnotation(Table.class);
        String tableName = cl.getSimpleName();
        if (tb != null) {
            tableName = tb.name();
        }
        return tableName;
    }

    private static boolean contain(String s, String[] arr) {
        for (String a : arr) {
            if (s.equals(a)) {
                return true;
            }
        }
        return false;
    }

    public static int update(Object obj, String... noUpdateColumn) {
        Class<?> cl = obj.getClass();
        String tableName = getTableName(cl);
        String idName = getIdName(cl);
        StringBuilder sql = new StringBuilder("update ");
        sql.append(tableName);

        Field[] fields = cl.getDeclaredFields();
        Map<String, Object> map = new HashMap<String, Object>();
        boolean flag = true;
        for (Field field : fields) {
            field.setAccessible(true);
            Column c = (Column) field.getAnnotation(Column.class);
            if (c != null) {
                try {
                    String colName = c.name();
                    if (StringUtils.isBlank(colName)) {
                        colName = field.getName();
                    }
                    if (!contain(colName, noUpdateColumn)) {
                        Method getMethod = ReflectionUtils.findMethod(cl, "get" + StringUtils.capitalize(field.getName()));
                        if (null == getMethod) { // 首字母大写取不到数据则进行另外的处理
                            getMethod = ReflectionUtils.findMethod(cl, "get" + (field.getName()));
                        }
                        map.put(colName, ReflectionUtils.invokeMethod(getMethod, obj));
                        if (flag) {
                            sql.append(" set ");
                            flag = false;
                        } else {
                            sql.append(" ,");
                        }
                        sql.append(colName);
                        sql.append(" = :");
                        sql.append(colName);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        if (flag) {
            logger.debug("update({}) is not Annotation Column !", cl.getName());
            return 0;
        }
        sql.append(" where ");
        sql.append(idName);
        sql.append(" = :");
        sql.append(idName);

        logger.debug("update sql = {}", sql);

        return namedParameterJdbcTemplate.update(sql.toString(), map);
    }

    public static int update(Object obj) {
        Class<?> cl = obj.getClass();
        String tableName = getTableName(cl);
        String idName = getIdName(cl);
        StringBuilder sql = new StringBuilder("update ");
        sql.append(tableName);

        Field[] fields = cl.getDeclaredFields();
        Map<String, Object> map = new HashMap<String, Object>();
        boolean flag = true;
        for (Field field : fields) {
            field.setAccessible(true);
            Column c = (Column) field.getAnnotation(Column.class);
            if (c != null) {
                try {
                    String colName = c.name();
                    if (StringUtils.isBlank(colName)) {
                        colName = field.getName();
                    }
                    Method getMethod = ReflectionUtils.findMethod(cl, "get" + StringUtils.capitalize(field.getName()));
                    if (null == getMethod) { // 首字母大写取不到数据则进行另外的处理
                        getMethod = ReflectionUtils.findMethod(cl, "get" + (field.getName()));
                    }
                    map.put(colName, ReflectionUtils.invokeMethod(getMethod, obj));
                    if (flag) {
                        sql.append(" set ");
                        flag = false;
                    } else {
                        sql.append(" ,");
                    }
                    sql.append(colName);
                    sql.append(" = :");
                    sql.append(colName);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        if (flag) {
            logger.debug("update({}) is not Annotation Column !", cl.getName());
            return 0;
        }
        sql.append(" where ");
        sql.append(idName);
        sql.append(" = :");
        sql.append(idName);

        logger.debug("update sql = {}", sql);

        return namedParameterJdbcTemplate.update(sql.toString(), map);
    }

    public static void save(Object obj) {
        Class<?> cl = obj.getClass();
        String tableName = getTableName(cl);
        Field[] fields = cl.getDeclaredFields();
        Map<String, Object> map = new HashMap<String, Object>();
        for (Field field : fields) {
            field.setAccessible(true);
            Column c = (Column) field.getAnnotation(Column.class);
            if (c != null) {
                try {
                    String colName = c.name();
                    if (StringUtils.isBlank(colName)) {
                        colName = field.getName();
                    }
                    Method getMethod = ReflectionUtils.findMethod(cl, "get" + StringUtils.capitalize(field.getName()));
                    if (null == getMethod) { // 首字母大写取不到数据则进行另外的处理
                        getMethod = ReflectionUtils.findMethod(cl, "get" + (field.getName()));
                    }
                    map.put(colName, ReflectionUtils.invokeMethod(getMethod, obj));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        save(tableName, map);
    }

    public static void updateForAnd(String tableName, Map<String, Object> updateMap, Map<String, Object> whereAndMap) {
        StringBuilder sql = new StringBuilder("update ");
        sql.append(tableName);
        sql.append(" set ");
        Set<String> keys = updateMap.keySet();
        int index = 0;
        for (String key : keys) {
            if (index != 0) {
                sql.append(",");
            }
            sql.append(key + " = :" + key);
            index++;
        }
        sql.append(" where ");

        index = 0;
        keys = whereAndMap.keySet();
        for (String key : keys) {
            if (index != 0) {
                sql.append(" and ");
            }
            sql.append(key + " = :" + key);
            index++;
        }
        updateMap.putAll(whereAndMap);
        logger.debug("update sql = {}", sql);
        namedParameterJdbcTemplate.update(sql.toString(), updateMap);
    }

    public static void save(String tableName, Map<String, Object> map) {
        StringBuilder sql = new StringBuilder("insert into `");
        sql.append(tableName);
        sql.append("` (");
        Set<String> keys = map.keySet();
        int index = 0;
        StringBuilder values = new StringBuilder();
        for (String key : keys) {
            if (index != 0) {
                sql.append(",");
                values.append(",");
            }
            sql.append("`");
            sql.append(key);
            sql.append("`");
            values.append(":" + key);
            index++;
        }
        sql.append(") values(");
        sql.append(values);
        sql.append(")");
        logger.debug("save sql = {}", sql);
        namedParameterJdbcTemplate.update(sql.toString(), map);
    }

    /**
     * 根据传入参数 查询数据 返回分页之后数据 注意 其中 UTIL_A ， UTIL_RN 为 关键字 不要在SQL 语句中出现
     *
     * @param jdbcTemplate JDBC 链接
     * @param page         页面信息
     * @param sql          查询语句
     * @param parameters   查询参数
     * @param orderSql     排序参数
     * @param cl           返回数据类型
     * @return
     */
    public static <T> Page selectMysql(JdbcTemplate jdbcTemplate, Page page,
                                       String sql, List<Object> parameters, String orderSql, Class<T> cl) {
        String countSql = "SELECT COUNT(1) FROM (" + sql + ") mysql";
        logger.debug("JdbcUtils.select countSql is :" + countSql);
        logger.debug("JdbcUtils.select countParameters is :" + parameters);
        Integer total = jdbcTemplate.queryForObject(countSql, Integer.class, parameters.toArray());
        page.setTotal(total);
        if (total == 0) {
            return page;
        }
        logger.debug("total = {}", total);
        orderSql = StringUtils.isNotBlank(orderSql) ? " ORDER BY " + orderSql : " ";
        StringBuilder sf = new StringBuilder();
        sf.append(sql + orderSql + " limit ?,?");
        parameters.add(page.getStart());
        parameters.add(page.getPageSize());
        logger.debug("start = {},pageSize = {}", page.getStart(), page.getPageSize());
        logger.debug("JdbcUtils.select sql is :" + sf.toString());
        logger.debug("JdbcUtils.select parameters is :" + parameters);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sf.toString(), parameters.toArray());
        if (cl != null) {
            page.setData(BeanUtils.toList(cl, list));
        } else {
            page.setData(list);
        }
        return page;
    }
}
