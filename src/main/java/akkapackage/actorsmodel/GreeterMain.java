package akkapackage.actorsmodel;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class GreeterMain extends AbstractBehavior<GreeterMain.SayHello> {

    public static final class SayHello {
        public final String name;

        public SayHello(String name) {
            this.name = name;
        }
    }

    private final ActorRef<Greeter.Greet> greeter;

    private GreeterMain(ActorContext<SayHello> context) {
        super(context);
        greeter = context.spawn(Greeter.create(), "greeter");
    }

    @Override
    public Receive<SayHello> createReceive() {
        return newReceiveBuilder().onMessage(SayHello.class, this::onSayHello).build();
    }

    private Behavior<SayHello> onSayHello(SayHello command) {
        ActorContext<SayHello> context = getContext();
        ActorRef<Greeter.Greeted> replyTo = context.spawn(GreeterBot.create(3), command.name);

        greeter.tell(new Greeter.Greet(command.name, replyTo));

        return this;
    }

    public static Behavior<SayHello> create() {
        return Behaviors.setup(GreeterMain::new);
    }
}
