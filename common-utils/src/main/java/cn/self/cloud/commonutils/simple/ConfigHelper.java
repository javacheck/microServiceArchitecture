package cn.self.cloud.commonutils.simple;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ConfigHelper
{
    private static ResourceBundle bundle = null;
    static
    {
        try
        {
            bundle = PropertyResourceBundle.getBundle("config");
        }
        catch (Exception e)
        {
            throw new RuntimeException("读取配置文件失败!");
        }
    }
    private static ConfigHelper   helper = null;

    private ConfigHelper()
    {

    }

    public static ConfigHelper getInstance()
    {
        if ( helper == null )
            helper = new ConfigHelper();
        return helper;
    }


    public String get(String key)
    {
        return bundle.getString(key);
    }

    public static void main(String[] args)
    {
        System.out.println(getInstance().get("aa"));
    }

}
