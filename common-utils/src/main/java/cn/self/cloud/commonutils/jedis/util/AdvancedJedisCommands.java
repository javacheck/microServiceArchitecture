package cn.self.cloud.commonutils.jedis.util;

import cn.self.cloud.commonutils.simple.util.Slowlog;

import java.util.List;

public interface AdvancedJedisCommands {
    List<String> configGet(String pattern);

    String configSet(String parameter, String value);

    String slowlogReset();

    Long slowlogLen();

    List<Slowlog> slowlogGet();

    List<Slowlog> slowlogGet(long entries);

    Long objectRefcount(String string);

    String objectEncoding(String string);

    Long objectIdletime(String string);
}
