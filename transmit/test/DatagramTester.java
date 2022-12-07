package transmit.test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class DatagramTester {

    private int port = 8000;
    private DatagramSocket sendSocket;
    private DatagramSocket receiveSocket;
    private static final int MAX_LENGTH = 3584;
    
    
    public DatagramTester() throws IOException {
        
        sendSocket = new DatagramSocket();
        receiveSocket = new DatagramSocket(port);
        receiver.start();
        sender.start();
    }
    
    
    public static byte[] longToByte(long[] data) throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bao);
        for(int i=0; i<data.length; i++) {
            dos.writeLong(data[i]);
        }
        dos.close();
        return bao.toByteArray();
    }
    
    
    public static long[] byteToLong(byte[] data) throws IOException {
        long[] result = new long[data.length/8];
        ByteArrayInputStream bai = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bai);
        for(int i=0; i<data.length/8; i++) {
            result[i] = dis.readLong();
        }
        return result;
    }
    
    
    public void send(byte[] bigData) throws IOException {
        
        DatagramPacket packet = new DatagramPacket(bigData, 0, 512, InetAddress.getByName("localhost"), port);
        int bytesSent = 0;
        int count = 0;
//        packet.setData("1233412354345245546345632".getBytes());

//        while(bytesSent < bigData.length) {
//            sendSocket.send(packet);
//
//            System.out.println("SendSocket > 第" + (++count) + "次发送了" + packet.getLength() + "个字节");
//            bytesSent += packet.getLength();
//            int remain = bigData.length - bytesSent;
//            int length = (remain > 512) ? 512 : remain;
//            packet.setData(bigData, bytesSent, length);
////            packet.setData("dfaserw".getBytes());
//
//        }
        {
            packet.setData("1233412354345245546345632".getBytes());
            sendSocket.send(packet);
            packet.setData("dfaserw".getBytes());
            sendSocket.send(packet);
            packet.setData("poiupiopuiopuopoupiopuiopopu".getBytes());
            sendSocket.send(packet);
            packet.setData("##".getBytes());
            sendSocket.send(packet);
        }
    }
    
    
    public byte[] receive() throws IOException {
        
        byte[] bigData = new byte[MAX_LENGTH];
        DatagramPacket packet = new DatagramPacket(bigData, 0, MAX_LENGTH);
        int bytesReceived = 0;
        int count = 0;
        long beginTime = System.currentTimeMillis();
        
//        while((bytesReceived < bigData.length) && (System.currentTimeMillis() - beginTime < 60000*5)) {
//            receiveSocket.receive(packet);
//            System.out.println("ReceiveSocket > 第" + (++count) + "次接收到" + packet.getLength() + "个字节");
//            bytesReceived += packet.getLength();
//            System.out.println(Arrays.toString(packet.getData()));
////            packet.setData(bigData, bytesReceived, MAX_LENGTH - bytesReceived);
//        }
        receiveSocket.receive(packet);
        System.out.println("len="+packet.getLength());
        System.out.println(Arrays.toString(packet.getData()));
        System.out.println(new String(packet.getData(),0,packet.getLength()));      // package 和 byte[] 可以复用，不过要指明长度
        receiveSocket.receive(packet);
        System.out.println("len="+packet.getLength());
        System.out.println(Arrays.toString(packet.getData()));
        System.out.println(new String(packet.getData(),0,packet.getLength()));

        receiveSocket.receive(packet);
        System.out.println("len="+packet.getLength());
        System.out.println(Arrays.toString(packet.getData()));
        System.out.println(new String(packet.getData(),0,packet.getLength()));
        receiveSocket.receive(packet);
        System.out.println("len="+packet.getLength());
        System.out.println(Arrays.toString(packet.getData()));
        System.out.println(new String(packet.getData(),0,packet.getLength()));
        return packet.getData();
    }
    
    
    
    public Thread sender = new Thread() {
        
        public void run() {
            long[] longArray = new long[MAX_LENGTH/8];
            for(int i=0; i<longArray.length; i++) {
                longArray[i] = i+1;
            }
            try {
                send(longToByte(longArray));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    
    
    public Thread receiver = new Thread() {
        
        public void run() {
            try {
                long[] longArray = byteToLong(receive());
                
                for(int i=0; i<longArray.length; i++) {
                    if(i%100 == 0)
                        System.out.println();
                    System.out.print(longArray[i] + ",");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    
    
    public static void main(String[] args) throws IOException {
        
        new DatagramTester();
    }
}
