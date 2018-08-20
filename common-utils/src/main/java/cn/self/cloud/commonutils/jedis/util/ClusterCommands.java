package cn.self.cloud.commonutils.jedis.util;

import java.util.List;

public interface ClusterCommands {
    String clusterNodes();

    String clusterMeet(final String ip, final int port);

    String clusterAddSlots(final int... slots);

    String clusterDelSlots(final int... slots);

    String clusterInfo();

    List<String> clusterGetKeysInSlot(final int slot, final int count);

    String clusterSetSlotNode(final int slot, final String nodeId);

    String clusterSetSlotMigrating(final int slot, final String nodeId);

    String clusterSetSlotImporting(final int slot, final String nodeId);
}
