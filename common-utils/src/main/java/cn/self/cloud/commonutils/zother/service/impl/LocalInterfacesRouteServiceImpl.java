package cn.self.cloud.commonutils.zother.service.impl;

import cn.self.cloud.commonutils.zother.entity.ModuleInfo;
import cn.self.cloud.commonutils.zother.service.ConfigService;
import org.apache.commons.lang.StringUtils;
import org.redkale.convert.json.JsonConvert;
import org.redkale.service.AbstractService;
import org.redkale.util.AnyValue;
import org.redkale.util.TypeToken;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 基于随机的路由选择服务实现，带权重判断
 * Created by cp on 2017/7/10.
 */
public class LocalInterfacesRouteServiceImpl extends AbstractService {

    Logger logger = Logger.getLogger(getClass().getCanonicalName());
    @Resource
    ConfigService configService;
    /***
     * 存储当前有效的路由信息 存储结构：  moduleName-> [Process]
     */
    Map<String, String> currentRouteMap = new HashMap();

    public LocalInterfacesRouteServiceImpl() {
//        currentRouteMap.put("basecsb", "120.26.80.24:5100");
////        currentRouteMap.put("basecsb","localhost:5100");
//        currentRouteMap.put("baseacc", "120.26.80.24:6066");
////        currentRouteMap.put("baseacc","localhost:6066");
//        currentRouteMap.put("baseid", "120.26.80.24:6065");
////        currentRouteMap.put("baseid", "localhost:6065");
//        currentRouteMap.put("baseuser", "120.26.80.24:6064");
////        currentRouteMap.put("baseuser","localhost:6064");
//        currentRouteMap.put("bizniuyun", "120.26.80.24:6063");
//        currentRouteMap.put("bizsettle", "120.26.80.24:6067");
//        currentRouteMap.put("basekworld", "120.26.80.24:6072");
////        currentRouteMap.put("basekworld", "localhost:6072");
//        currentRouteMap.put("basecomm", "120.26.80.24:6070");
////        currentRouteMap.put("basecomm", "localhost:6070");
//        currentRouteMap.put("baseoil", "120.26.80.24:6071");
//        currentRouteMap.put("basecustomer", "120.26.80.24:6080");
////        currentRouteMap.put("basecustomer", "localhost:6080");
//        currentRouteMap.put("basetransport", "120.26.80.24:6076");
////        currentRouteMap.put("basetransport", "localhost:6076");
//        currentRouteMap.put("baseunion", "120.26.80.24:6078");
////        currentRouteMap.put("baseunion", "localhost:6078");
//        currentRouteMap.put("basepush", "120.26.80.24:6079");
////        currentRouteMap.put("basepush", "localhost:6079");
//        currentRouteMap.put("basedemo", "localhost:6065");
    }

    @Override
    public void init(AnyValue config) {
        String mapJsonString = configService.getString("property.moduleCallMapping", "[]");
        List<ModuleInfo> moduleInfoList = JsonConvert.root().convertFrom(new TypeToken<List<ModuleInfo>>() {
        }.getType(), mapJsonString);
        for (ModuleInfo moduleInfo : moduleInfoList) {
            currentRouteMap.put(moduleInfo.getModule(), moduleInfo.getUrl());
        }
    }

    public String getRoute(String moduleName) {
        String s = currentRouteMap.get(moduleName);
        if (StringUtils.isEmpty(s)) {
            logger.log(Level.WARNING, "没有找到指定的服务" + moduleName);
            return null;
        }
        String urlPreffix = "http://" + s;
        return urlPreffix;
    }

}
