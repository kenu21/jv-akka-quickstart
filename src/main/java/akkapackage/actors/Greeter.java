package akkapackage.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.slf4j.Logger;

import java.util.Objects;

public class Greeter extends AbstractBehavior<Greeter.Greet> {
    public static final class Greet {
        public final String whom;
        public final ActorRef<Greeted> replyTo;

        public Greet(String whom, ActorRef<Greeted> replyTo) {
            this.whom = whom;
            this.replyTo = replyTo;
        }
    }

    public static final class Greeted {
        public final String whom;
        public final ActorRef<Greet> from;

        public Greeted(String whom, ActorRef<Greet> from) {
            this.whom = whom;
            this.from = from;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Greeted greeted = (Greeted) o;
            return Objects.equals(whom, greeted.whom) &&
                    Objects.equals(from, greeted.from);
        }

        @Override
        public int hashCode() {
            return Objects.hash(whom, from);
        }

        @Override
        public String toString() {
            return "Greeted{" +
                    "whom='" + whom + '\'' +
                    ", from=" + from +
                    '}';
        }
    }

    private Greeter(ActorContext<Greet> context) {
        super(context);
    }

    @Override
    public Receive<Greet> createReceive() {
        return newReceiveBuilder().onMessage(Greet.class, this::onGreet).build();
    }

    private Behavior<Greet> onGreet(Greet command) {
        ActorContext<Greet> context = getContext();
        Logger log = context.getLog();
        log.info("Hello {}!", command.whom);

        ActorRef<Greet> self = context.getSelf();
        command.replyTo.tell(new Greeted(command.whom, self));
        return this;
    }

    public static Behavior<Greet> create() {
        return Behaviors.setup(Greeter::new);
    }
}
