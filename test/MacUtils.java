package test;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
 
/**
 * @author: wtl
 * @License: (C) Copyright 2022, wtl Corporation Limited.
 * @Contact: 1050100468@qq.com
 * @Date: 2022/3/29 10:25
 * @Version: 1.0
 * @Description:
 */

public class MacUtils {

    public static void main(String[] args) {
        getHostIp();
        try {
            String localHostMacAddress = getLocalHostMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取该主机上所有网卡的ip
     */
    public static void getHostIp(){
        try{
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && !ip.getHostAddress().contains(":")){
                        System.out.println("本机的IP = " + ip.getHostAddress());
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
 
 
    /**
     * 获取本地主机的ip对应的MAC地址
     * @return String
     * @throws Exception 异常
     */
    public static String getLocalHostMacAddress() throws Exception {
        byte[] hardwareAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < hardwareAddress.length; i++) {
            if (i != 0) {
                stringBuilder.append("-");
            }
            //字节转换为整数
            int temp = hardwareAddress[i] & 0xff;
            String toHexString = Integer.toHexString(temp);
            System.out.println("每8位:" + toHexString);
            if (toHexString.length() == 1) {
                stringBuilder.append("0").append(toHexString);
            } else {
                stringBuilder.append(toHexString);
            }
        }
 
        return stringBuilder.toString();
    }
}