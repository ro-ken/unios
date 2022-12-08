package util;

import structure.Node;
import structure.SubNet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
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

//    public static get
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
            String ip = node.getAddress().getIp();
            sb.append("[").append(node.getNo()).append(":").append(ip.substring(ip.lastIndexOf('.')+1)).append("]->");
        }
        System.out.println(sb);
    }
}
