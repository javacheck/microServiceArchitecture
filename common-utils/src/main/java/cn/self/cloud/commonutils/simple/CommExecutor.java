package cn.self.cloud.commonutils.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个全局的线程池封装
 * Created by ChenPing on 14-10-11.
 */
public class CommExecutor {
    private static CommExecutor commExecutor = null;
    Logger logger = LoggerFactory.getLogger(CommExecutor.class);
    ;
    private Map<String, ExecutorService> executorServiceMap = new HashMap<String, ExecutorService>();

    private CommExecutor() {
    }

    public static CommExecutor getInstance() {
        if (commExecutor == null) {
            commExecutor = new CommExecutor();
        }
        return commExecutor;
    }

    public ExecutorService getExecutorService(String name) {
        if (executorServiceMap.containsKey(name))
            return executorServiceMap.get(name);
        else {
            int maxCount = Integer.parseInt(ConfigHelper.getInstance().get("executorMaxThreadCount"));
            ExecutorService executorService = Executors.newFixedThreadPool(maxCount);
            logger.info("完成了最大执行线程数为" + maxCount + "的线程池初始化");
            executorServiceMap.put(name, executorService);
            return executorService;
        }

    }
}
