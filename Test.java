import util.MyConfig;

public class Test {

    public static void main(String[] args) {
        PlatformContext context = PlatformContext.getInstance();
        String manager = MyConfig.get("manager");
        System.out.println(manager);
        context.run();
    }
}
