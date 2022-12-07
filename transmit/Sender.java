package transmit;

import structure.Address;
import structure.type.TransmitPackage;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:01
 * @Description: TODO
 */
public interface Sender{
    void send(Address address, TransmitPackage transmitPackage);
    void boardCast(TransmitPackage transmitPackage);
}
