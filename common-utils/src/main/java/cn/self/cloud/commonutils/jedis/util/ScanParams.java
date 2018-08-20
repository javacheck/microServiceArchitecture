package cn.self.cloud.commonutils.jedis.util;

import cn.self.cloud.commonutils.simple.util.SafeEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static cn.self.cloud.commonutils.jedis.util.Protocol.Keyword.COUNT;
import static cn.self.cloud.commonutils.jedis.util.Protocol.Keyword.MATCH;

public class ScanParams {
    private List<byte[]> params = new ArrayList<byte[]>();
    public final static String SCAN_POINTER_START = String.valueOf(0);

    public void match(final String pattern) {
	params.add(MATCH.raw);
	params.add(SafeEncoder.encode(pattern));
    }

    public void count(final int count) {
	params.add(COUNT.raw);
	params.add(Protocol.toByteArray(count));
    }

    public Collection<byte[]> getParams() {
	return Collections.unmodifiableCollection(params);
    }
}
