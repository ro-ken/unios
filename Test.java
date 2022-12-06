import structure.Address;
import structure.type.TransmitPackage;
import structure.type.TransmitType;
import util.MyConfig;

public class Test {
    public static PlatformContext context;
    public static void main(String[] args) {
        context = PlatformContext.getInstance();
        String manager = MyConfig.get("manager");
        System.out.println(manager);
        context.run();
        udpTest();
    }

    private static void udpTest() {
        context.sender.send(new Address("localhost"),new TransmitPackage(TransmitType.NetsMaintain,"Control"));
        context.sender.send(new Address("localhost"),new TransmitPackage(TransmitType.NetsMaintain,"Control"));
    }
}
