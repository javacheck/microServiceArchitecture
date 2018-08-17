package cn.self.cloud.commonutils.zother.service.impl;

import cn.self.cloud.commonutils.zookeeper.Process;
import org.redkale.service.AbstractService;
import org.redkale.service.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 基于随机的路由选择服务实现，带权重判断
 * Created by cp on 2017/7/10.
 */
public class InterfacesRouteServiceImpl extends AbstractService {

    Logger logger = Logger.getLogger(getClass().getCanonicalName());
    /***
     * 存储当前有效的路由信息 存储结构：  moduleName-> [Process]
     */
    Map<String, List<Process>> currentRouteMap = new HashMap();


    public String getRoute(String moduleName) {
        List<Process> servers = new ArrayList<>();
        synchronized (this) {
            List<Process> list = currentRouteMap.get(moduleName);
            if (list != null) {
                servers.addAll(list);
            }
        }
        if (servers.isEmpty()) {
            logger.log(Level.WARNING, "没有找到指定的服务" + moduleName);
            return null;
        }
        int totalWeight = 0;
        for (Process process : servers) {
            if (process.getWeight() <= 0) {
                totalWeight += 1;
            } else {
                totalWeight += process.getWeight();
            }
        }
        Process[] allProcesses = new Process[totalWeight];
        int index = 0;
        int listIndex = 0;
        while (index < totalWeight) {
            Process p = servers.get(listIndex);
            if (p.getWeight() <= 0) {
                allProcesses[index++] = p;
            } else {
                for (int i = 0; i < p.getWeight(); i++) {
                    allProcesses[index++] = p;
                }
            }
            listIndex++;
        }
        String urlPreffix = "http://";
        Process p = allProcesses[Id.random(totalWeight)];
        urlPreffix += p.getHost();
        urlPreffix += ":";
        urlPreffix += p.getPort();
        return urlPreffix;
    }


    public void refreshProcesses(List<Process> processList) {
        Map<String, List<Process>> map = new HashMap<>();
        if (processList == null || processList.isEmpty()) {
            synchronized (this) {
                currentRouteMap.clear();
            }
            return;
        }
        for (Process process : processList) {
            List<Process> plist = map.get(process.getModule());
            if (plist == null) {
                plist = new ArrayList<>();
                map.put(process.getModule(), plist);
            }
            plist.add(process);
        }
        synchronized (this) {
            currentRouteMap.clear();
            currentRouteMap = map;
        }
    }
}
