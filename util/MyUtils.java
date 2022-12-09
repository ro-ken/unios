package util;

import structure.Address;
import structure.Node;
import structure.SubNet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:50
 * @Description: TODO
 */
public class MyUtils {
    public static String root;
    static {
        root = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
    }
    public static String getResPath(String res){
        return root + res;
    }


    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printLOGO() {
        String logoAddr = root+ "util/banner.txt";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logoAddr)))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void printAllNodes(SubNet subNet){
        StringBuilder sb = new StringBuilder();
        sb.append("netNo:").append(subNet.getNo()).append(":");
        List<Node> nodeList = subNet.getNodeList();
        for (Node node:nodeList){
            sb.append("[").append(node.getNo()).append(":").append(node.getAddress().getLastAddr()).append("]<-");
        }
        System.out.println(sb);
    }


    public static Address getHostIp(){
        Address address = null;
        try{
            InetAddress ip = InetAddress.getLocalHost();
            if (!ip.isLoopbackAddress()){
                address = new Address(ip.getHostAddress());
            }else{
                Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                while (allNetInterfaces.hasMoreElements()){
                    NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()){
                        ip = (InetAddress) addresses.nextElement();
                        //ip doesn't belong to 127.0.0.0 ~ 127.255.255.255
                        if (ip instanceof Inet4Address && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")){
                            address = new Address(ip.getHostAddress());
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if (address == null){
            System.out.println("could not find local address!");
        }else{
            System.out.println("find address : "+ address.getIp());
        }
        return address;
    }
}
