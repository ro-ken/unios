package transmit.impl;

import transmit.Receiver;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:03
 * @Description: TODO
 */
public class UDPReceiver implements Receiver {

    @Override
    public void run() {
        System.out.println("UDPReceiver running!");
    }
}
