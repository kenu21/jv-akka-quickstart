package akkapackage.actorsmodel;

import akka.actor.typed.ActorSystem;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        final ActorSystem<GreeterMain.SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "helloakka");

        greeterMain.tell(new GreeterMain.SayHello("Charles"));

        try {
            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ignored) {
        } finally {
            greeterMain.terminate();
        }
    }
}
