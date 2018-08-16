package cn.self.cloud.commonutils;

import cn.self.cloud.commonutils.eximport.CSVUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestCsv {
	/**
     * CSV导出
     * 
     * @throws Exception
     */
    @Test
    public void exportCsv() {
        List<String> dataList=new ArrayList<String>();
        dataList.add("1,张三,男");
        dataList.add("2,李四,男");
        dataList.add("3,小红,女");
        System.out.println(dataList.size());
        boolean isSuccess=CSVUtils.exportCsv(new File("D:/test/model.csv"), dataList);
        System.out.println(isSuccess);
    }
    
    /**
     * CSV导出
     * 
     * @throws Exception
     */
    /*@Test
    public void importCsv()  {
        List<String> dataList=CSVUtils.importCsv(new File("D:/test/ljq.csv"));
        if(dataList!=null && !dataList.isEmpty()){
            for(String data : dataList){
                System.out.println(data);
            }
        }
    }*/

}
