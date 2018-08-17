package cn.self.cloud.commonutils.zother.entity;

import org.redkale.convert.json.JsonConvert;

/**
 * Created by cp on 2017/8/1.
 */
public class UserInfo {
    private String name;
    private int tjid;
    private String sessionid;
    private int type;
    private String product;
    private String secretkey;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTjid() {
        return tjid;
    }

    public void setTjid(int tjid) {
        this.tjid = tjid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }
}
