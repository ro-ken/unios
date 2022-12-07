package transmit.impl;

import context.PlatformContext;
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
    private static final String boardCastIP = "255.255.255.255";

    public UDPSender() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Address address, TransmitPackage transmitPackage) {
        InetAddress inetAddress = address.toInetAddress();
        _send(inetAddress,transmitPackage);
    }

    private void _send(InetAddress inetAddress, TransmitPackage transmitPackage){
        byte[] bytes = transmitPackage.getBytes();
        DatagramPacket dp = new DatagramPacket(bytes,bytes.length,inetAddress,port);
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void boardCast(TransmitPackage transmitPackage) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName("255.255.255.255");
            _send(inetAddress,transmitPackage);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
