package transmit.impl;

import structure.Address;
import structure.type.TransmitPackage;
import transmit.Sender;

import java.io.IOException;
import java.net.*;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:02
 * @Description: TODO
 */
public class UDPSender implements Sender {
    private DatagramSocket socket;
    private int port = 10000;

    public UDPSender() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Address address, TransmitPackage transmitPackage) {
        byte[] bytes = transmitPackage.toString().getBytes();
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(address.getIp());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DatagramPacket dp = new DatagramPacket(bytes,bytes.length,inetAddress,port);
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
