package transmit.impl;

import context.PlatformContext;
import structure.type.NetsMaintainPackage;
import structure.type.TransmitPackage;

import transmit.Receiver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:03
 * @Description: TODO
 */
public class UDPReceiver implements Receiver {
    private final int port = 10000;
    private final int packageSize = 10240; // size: Byte
    private DatagramSocket socket;
    private final PlatformContext context;

    public UDPReceiver(){
        context = PlatformContext.getInstance();
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("UDPServer running!");
        byte [] bytes = new byte[packageSize];
        DatagramPacket packet = new DatagramPacket(bytes,bytes.length);
        while (true) {
            try {
                socket.receive(packet);
                ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(bytes,0, packet.getLength()));
                TransmitPackage transmitPackage = (TransmitPackage) objectStream.readObject();
                distribute(transmitPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * according the TransmitType to distribute different queue
     * @param dp
     */
    private void distribute(TransmitPackage dp) {
        switch (dp.getType()){
            case NetsMaintain: context.manager.offer((NetsMaintainPackage) dp.getBody());
            case TaskUndo:;
            case TaskDone:;
        }
    }

}
