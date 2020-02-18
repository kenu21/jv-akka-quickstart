package akkapackage.lifecycle;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

class MainActor extends AbstractBehavior<String> {

    static Behavior<String> create() {
        return Behaviors.setup(MainActor::new);
    }

    private MainActor(ActorContext<String> context) {
        super(context);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder().onMessageEquals("start", this::start).build();
    }

    private Behavior<String> start() {
        ActorContext<String> testKit = getContext();
        ActorRef<String> first = testKit.spawn(StartStopActor1.create(), "first");
        first.tell("stop");
        return Behaviors.same();
    }
}
