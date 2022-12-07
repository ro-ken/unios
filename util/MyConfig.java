package util;

import structure.Address;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:41
 * @Description: TODO
 */
public class MyConfig {
    private static Properties config = new Properties();
    public static Address myAddress = new Address();
    static {
        try {
            String filename = "config.properties";
            config.load(new FileInputStream(MyUtils.getResPath(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set localhost IP
        if ("auto".equals(config.getProperty("getAddressMode"))){
            try {
                myAddress.setIp(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }else {
            myAddress.setIp(config.getProperty("address"));
        }
    }

    /**
     * get properties values
     * @param key
     * @return
     */
    public static String get(String key){
        return config.getProperty(key);
    }
}
