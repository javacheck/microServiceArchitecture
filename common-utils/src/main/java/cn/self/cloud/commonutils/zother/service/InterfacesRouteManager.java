package cn.self.cloud.commonutils.zother.service;

import cn.self.cloud.commonutils.zookeeper.Process;
import cn.self.cloud.commonutils.zother.service.impl.InterfacesRouteServiceImpl;
import org.redkale.service.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 进行服务模块管理的接口类
 * Created by cp on 2017/7/10.
 */
public class InterfacesRouteManager implements Service {

    @Resource
    InterfacesRouteServiceImpl impl;

    /***
     * 刷新当前微服务体系中的模块信息列表
     *
     * @param processList
     */
    public void refreshProcesses(List<Process> processList) {
        impl.refreshProcesses(processList);
    }
}
