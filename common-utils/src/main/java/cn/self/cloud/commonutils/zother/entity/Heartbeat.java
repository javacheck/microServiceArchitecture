package cn.self.cloud.commonutils.zother.entity;

/**
 * Created by Administrator on 2016/6/22 0022.
 */


public class Heartbeat {

    String module;
    String processid;

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
