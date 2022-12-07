import context.PlatformContext;
import structure.Address;
import structure.type.TransmitPackage;
import structure.type.TransmitType;
import util.MyConfig;

import java.io.IOException;

public class Test {
    public static PlatformContext context;
    public static void main(String[] args) throws IOException {
        context = PlatformContext.getInstance();
        String manager = MyConfig.get("manager");
        System.out.println(manager);
        context.run();
//        udpTest();
    }

    private static void udpTest() throws IOException {
        TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,"Control");
        context.sender.send(new Address("localhost"),transmitPackage);
        context.sender.send(new Address("localhost"),new TransmitPackage(TransmitType.NetsMaintain,"Control"));
//      ObjectOutputStream oos = new ObjectOutputStream(transmitPackage);
//      oos.writeObject(transmitPackage);

    }
}
