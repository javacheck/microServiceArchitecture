package cn.self.cloud.commonutils.jedis.util;

/**
 * Created by yumei on 16-1-6.
 */
public class RedisOpration {
    private int type; // 1 set 新增操作  2  删除key  3 push操作
    private String key;
    private String value;
    private int time;

    public RedisOpration(){}

    public RedisOpration(int type ,String key){
        this.type = type;
        this.key = key;
    }

    public RedisOpration(int type,String key,String value){
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public RedisOpration(int type,String key,String value,int time){
        this.type = type;
        this.key = key;
        this.value = value;
        this.time = time;
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
