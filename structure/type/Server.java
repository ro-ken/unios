package structure.type;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 * @Author: ro_kin
 * @Data:2022/12/6 12:07
 * @Description: TODO
 */
public class Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(1688);//监听端口号，1688
        byte[] buff = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buff, buff.length);
        socket.receive(packet);
        System.out.println(packet.getLength());
        ByteArrayInputStream byteArrayStram = new ByteArrayInputStream(buff,0, packet.getLength());
        ObjectInputStream objectStream = new ObjectInputStream(byteArrayStram);
        TransmitPackage data = (TransmitPackage) objectStream.readObject();
        System.out.println(data);
        objectStream.close();
        byteArrayStram.close();
    }
}
