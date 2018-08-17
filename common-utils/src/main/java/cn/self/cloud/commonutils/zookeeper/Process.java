package cn.self.cloud.commonutils.zookeeper;

public class Process {

    String id;
    String module;  //服务名称
    String pid; //进程号
    String host; //服务地址
    int port; //监听端口
    long time; // 心跳上报的当前时间，用于判断是否接收到心跳
    String dir;  // 部署目录

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    int weight; //服务权重,用于支持基于权重的服务路由算法
    private int count = 0; //旧的机制，用于记录心跳重试失败的次数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                ", module='" + module + '\'' +
                ", pid='" + pid + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", time=" + time +
                ", dir='" + dir + '\'' +
                ", weight=" + weight +
                ", count=" + count +
                '}';
    }
}

