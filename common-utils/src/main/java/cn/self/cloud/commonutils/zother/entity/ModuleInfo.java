package cn.self.cloud.commonutils.zother.entity;

import org.redkale.convert.json.JsonConvert;

/**
 * Created by cp on 2017/12/26.
 */
public class ModuleInfo {
    private String module;
    private String url;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return JsonConvert.root().convertTo(this);
    }
}
