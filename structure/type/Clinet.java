package structure.type;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @Author: ro_kin
 * @Data:2022/12/6 12:05
 * @Description: TODO
 */
public class Clinet {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket = new DatagramSocket();

        //DatagramPacket(byte[] buf, int length, int length, InetAddress address, int port)

        DatagramPacket packet = new DatagramPacket(new byte[0], 0, InetAddress.getByName("127.0.0.1"), 1688);
        TransmitPackage transmitPackage = new TransmitPackage(TransmitType.TaskDone,"dfasf23456");

        //对象->对象流->字节数组流->字节数组
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);
        objectStream.writeObject(transmitPackage);
        byte[] arr = byteArrayStream.toByteArray();
        System.out.println(arr.length);
        packet.setData(arr);//填充DatagramPacket
        socket.send(packet);//发送
        objectStream.close();
        byteArrayStream.close();
    }
}
