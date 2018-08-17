package cn.self.cloud.commonutils.zother.entity;

/**
 * Created by cp on 2016/10/18.
 */
public class ConnectionPoolInfo {
    private int maxPoolSize; //连接池中保留的最大连接数 Default: 15
    private int minPoolSize; //连接池最小连接数
    private int initialPoolSize; //初始化连接池连接数 Default: 3
    private int idleConnectionTestPeriod;// 空置时进行连接尝试的间隔 比如写120，
    // 则每120秒检查所有连接池中的空闲连接 Default: 0
    private String preferredTestQuery;//进行连接尝试时使用的sql语句
    private int maxIdleTime; //最大空闲时间,比如配置1800，
    // 则1800秒内未使用连接就会被丢弃,若为0则永不丢弃。Default: 0
    private boolean testConnectionOnCheckout;//因性能消耗大请只在需要的时候使用它。
    // 如果设为true那么在每个connection提交的
    // 时候都将校验其有效性。建议使用idleConnectionTestPeriod或automaticTestTable
    // 等方法来提升连接测试的性能。Default: false
//    private boolean testConnectionOnCheckin;//如果设为true那么在取得连接的同时将校验连接的有效性。Default: false
//    private int acquireIncrement;//当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3
//    private boolean autoCommitOnClose;//连接关闭时默认将所有未提交的操作回滚。Default: false
//    private boolean breakAfterAcquireFailure;//获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常。但是数据源仍有效
    //    保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试
    // 获取连接失败后该数据源将申明已断开并永久关闭。Default: false
//    private int checkoutTimeout;//当连接池用完时客户端调用getConnection()后等待获取新连接的时间，
    // 超时后将抛出SQLException,如设为0则无限期等待。单位毫秒。Default: 0
//    private String connectionTesterClassName;//通过实现ConnectionTester或QueryConnectionTester的类来测试连接。类名需制定全路径。
    //    Default: com.mchange.v2.c3p0.impl.DefaultConnectionTester
//    private String factoryClassLocation;//指定c3p0 libraries的路径，如果（通常都是这样）在本地即可获得那么无需设置，默认null即可
    //    Default: null
//    private int maxStatements;//JDBC的标准参数，用以控制数据源内加载的PreparedStatements数量。但由于预缓存的statements
    //    属于单个connection而不是整个连接池。所以设置这个参数需要考虑到多方面的因素。
    //如果maxStatements与maxStatementsPerConnection均为0，则缓存被关闭。Default: 0
//    private int maxStatementsPerConnection;//maxStatementsPerConnection定义了连接池内单个连接所拥有的最大缓存statements数。Default: 0
//    private int numHelperThreads;//c3p0是异步操作的，缓慢的JDBC操作通过帮助进程完成。扩展这些操作可以有效的提升性能
    //通过多线程实现多个操作同时被执行。Default: 3
//    private int acquireRetryAttempts;//定义在从数据库获取新连接失败后重复尝试的次数。Default: 30


    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public void setInitialPoolSize(int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    public int getIdleConnectionTestPeriod() {
        return idleConnectionTestPeriod;
    }

    public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
        this.idleConnectionTestPeriod = idleConnectionTestPeriod;
    }

    public String getPreferredTestQuery() {
        return preferredTestQuery;
    }

    public void setPreferredTestQuery(String preferredTestQuery) {
        this.preferredTestQuery = preferredTestQuery;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public boolean isTestConnectionOnCheckout() {
        return testConnectionOnCheckout;
    }

    public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) {
        this.testConnectionOnCheckout = testConnectionOnCheckout;
    }
}
