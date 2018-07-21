package cn.self.cloud.commonutils.id;

import cn.self.cloud.commonutils.validate.ValidateUtils;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Random;

/**
 * Snowflake Node节点ID生成器
 */
public class NodeUtil {
    public static int getNode() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();//获取本地主机
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new Random().nextInt(1024) + 1;
        }

        String ip = address.getHostAddress();//获取ip地址

        if (ValidateUtils.isEmpty(ip) || ip.equals("127.0.0.1") || ip.equals("0.0.0.0")) {
            try {
                ip = getIP();
            } catch (SocketException e) {
                e.printStackTrace();
                try {
                    return getNodeByMacAddress();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return new Random().nextInt(1024) + 1;//随机数得到节点
            }
        }

        String[] bs = ip.split("\\.");//得到ip地址的每一位

        int b3 = Integer.parseInt(bs[2]);//ip地址第3位
        int b4 = Integer.parseInt(bs[3]);//ip地址第4位

        int node = b3 % 4 * 256 + b4 + 1;//计算节点号

        return node;
    }

    //Unix下获取本地IP
    private static String getIP() throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    if (!ip.isLoopbackAddress() && ip.isSiteLocalAddress())
                        return ip.getHostAddress();
                }
            }
        }
        return null;
    }

    //通过Mac地址计算节点
    private static int getNodeByMacAddress() throws UnknownHostException, SocketException {
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte[] mac = network.getHardwareAddress();

        if (ValidateUtils.isEmpty(mac)) {
            mac = NetworkInterface.getByName("eth0").getHardwareAddress();
        }

        int sum = 0;

        for (int i = 0; i < mac.length; i++) {
            sum += mac[i] & 0xff;
        }

        int node = sum % 1024 + 1;

        return node;
    }
}
