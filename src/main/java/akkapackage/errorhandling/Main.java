package akkapackage.errorhandling;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;

public class Main {
    public static void main(String[] args) {
        ActorRef<String> testSystem = ActorSystem.create(MainActor.create(), "lifeCycle");
        testSystem.tell("start");
    }
}
