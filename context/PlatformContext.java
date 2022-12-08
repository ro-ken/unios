package context;

import application.ApplicationServer;
import manager.ResourceManager;
import manager.impl.CircleResourceManager;
import scheduler.Scheduler;
import scheduler.impl.RRScheduler;
import structure.Node;
import structure.SubNet;

import transmit.Receiver;
import transmit.Sender;
import transmit.impl.UDPReceiver;
import transmit.impl.UDPSender;
import util.MyUtils;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 10:54
 * @Description: TODO
 */
public class PlatformContext {
    private static final PlatformContext context = new PlatformContext();

    public ResourceManager manager;
    public Scheduler scheduler;
    public Sender sender;
    public Receiver receiver;
    public ApplicationServer worker;

    public Map<Integer, SubNet> network;  // this network
    public Node selfNode;       // self node

    public static PlatformContext getInstance() {
        return context;
    }

    // single pattern should have empty method body , insure this instance be created quickly
    private PlatformContext(){
        MyUtils.printLOGO();
    }

    /**
     *
     */
    public void run(){
        this.init();
        new Thread(receiver).start();
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
        network = new HashMap<>();
    }

}