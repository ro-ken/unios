package transmit.impl;

import transmit.Receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:03
 * @Description: TODO
 */
public class UDPReceiver implements Receiver {
    private int port = 10000;
    private int packageSize = 1024; // size: Byte
    DatagramSocket socket;
    @Override
    public void run() {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        System.out.println("UDPServer running!");

        while (true) {
            byte [] bytes = new byte[packageSize];
            DatagramPacket dp = new DatagramPacket(bytes,bytes.length);

            try {
                socket.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = dp.getData();
            int length = dp.getLength();

            System.out.println(new String(data,0,length));
        }

    }

}
