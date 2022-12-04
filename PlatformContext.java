
import application.ApplicationServer;
import manager.ResourceManager;
import manager.impl.CircleResourceManager;
import scheduler.Scheduler;
import scheduler.impl.RRScheduler;
import transmit.Receiver;
import transmit.Sender;
import transmit.impl.UDPReceiver;
import transmit.impl.UDPSender;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 10:54
 * @Description: TODO
 */
public class PlatformContext {
    private static final PlatformContext context = new PlatformContext();

    ResourceManager manager;
    Scheduler scheduler;
    Sender sender;
    Receiver receiver;
    ApplicationServer worker;

    public static PlatformContext getInstance() {
        return context;
    }

    private PlatformContext(){}

    /**
     *
     */
    public void run(){
        this.init();
        new Thread(receiver).start();
        new Thread(sender).start();
        new Thread(worker).start();
        new Thread(scheduler).start();
        new Thread(manager).start();
    }

    /**
     * use reflect replace
     */
    private void init(){
        sender = new UDPSender();
        receiver = new UDPReceiver();
        scheduler = new RRScheduler();
        manager = new CircleResourceManager();
        worker = new ApplicationServer();
    }

}