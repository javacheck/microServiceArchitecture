package cn.self.cloud.commonutils.simple;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的重复性检查器实现
 * Created by ChenPing on 16-1-26.
 */
public class MapBasedStringRepeatChecker implements  StringRepeatChecker {

    /***
     * 字符串存储超时的时间
     */
    public static final long Value_Store_Timeout=24*60*1000;
    /***
     * 检查是否超时的间隔
     */
    public static final long Delete_Interval=10000;
    private Timer timer;
    private static class RestoreItem{
        public String key;
        public String value;
        public long restoreTime;
    }

    private static ConcurrentHashMap<String,RestoreItem> itemMap=new ConcurrentHashMap<String, RestoreItem>();

    public  MapBasedStringRepeatChecker(){
        timer= new Timer("the restore timeout check thread",true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("检查超时开始......");
                long nowTime=System.currentTimeMillis();
                List<String> deleteKeyList=new ArrayList<String>();
                for(Map.Entry<String,RestoreItem> itemEntry:itemMap.entrySet())
                {
                    RestoreItem item=itemEntry.getValue();
                    if(nowTime-item.restoreTime>=Value_Store_Timeout){
                        deleteKeyList.add(itemEntry.getKey());
                    }
                }
                if(!deleteKeyList.isEmpty())
                {
                    for(String key:deleteKeyList)
                        itemMap.remove(key);
                    System.out.println("清除key列表："+deleteKeyList);
                }
                System.out.println("检查超时完毕......");
            }
        }, Delete_Interval, Delete_Interval);
    }

    @Override
    public String check(String key,String value) {
        if(StringUtils.isEmpty(key))
            return null;
        RestoreItem item=itemMap.get(key);
        if(item==null){
            item=new RestoreItem();
            item.restoreTime=System.currentTimeMillis();
            item.value=value;
            item.key=key;
            itemMap.put(key,item);
            return null;
        }else {
            String oldValue=item.value;
            item.restoreTime=System.currentTimeMillis();
            item.value=value;
            itemMap.put(key,item);
            return oldValue;
        }
    }

    public static void main(String[] args) {
        System.out.println("测试开始");
        MapBasedStringRepeatChecker mapcheck = new MapBasedStringRepeatChecker();
        mapcheck.check("aaaa", "1234");
        mapcheck.check("bbbb","1111");
        mapcheck.check("aaaa","2222");
        mapcheck.check("aaaa","3333");
        System.out.println(itemMap.get("aaaa").value);
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("测试结束");
    }
}
