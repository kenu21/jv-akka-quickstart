package akkapackage.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.slf4j.Logger;

public class GreeterBot extends AbstractBehavior<Greeter.Greeted> {

    private final int max;
    private int greetingCounter;

    private GreeterBot(ActorContext<Greeter.Greeted> context, int max) {
        super(context);
        this.max = max;
    }

    @Override
    public Receive<Greeter.Greeted> createReceive() {
        return newReceiveBuilder().onMessage(Greeter.Greeted.class, this::onGreeted).build();
    }

    private Behavior<Greeter.Greeted> onGreeted(Greeter.Greeted message) {
        greetingCounter++;
        ActorContext<Greeter.Greeted> context = getContext();
        Logger log = context.getLog();
        log.info("Greeting {} for {}", greetingCounter, message.whom);

        if (greetingCounter == max) {
            return Behaviors.stopped();
        } else {
            ActorRef<Greeter.Greeted> self = context.getSelf();
            message.from.tell(new Greeter.Greet(message.whom, self));
            return this;
        }
    }

    public static Behavior<Greeter.Greeted> create(int max) {
        return Behaviors.setup(context -> new GreeterBot(context, max));
    }
}
