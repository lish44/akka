package src.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import scala.Option;
import scala.concurrent.duration.Duration;
import src.actor.client.clientActor;
import src.handle.login;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/4/25 23:58
 */
public class gameActor extends AbstractActor {

    private final List<ActorRef> clientActors = new ArrayList<>();


    public static Props props() {
        return Props.create(gameActor.class);
    }

    public static class ShutdownMessage {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::handle)
                .match(ShutdownMessage.class, msg -> {
                    getContext().stop(getSelf());
                })
                .build();
    }


    @Override
    public void preStart() throws Exception {
        super.preStart();
        getContext().actorOf(worldService.props(), "worldService");
    }

    @Override
    public void postStop() {
        getContext().getSystem().terminate();
    }

    public void handle(String msg) {

        switch (msg) {
            case "creat":
                ActorRef c = getContext().actorOf(clientActor.props(), "client" + UUID.randomUUID());
                clientActors.add(c);
                break;
            case "login":
                for (ActorRef clientActor : clientActors) {
                    clientActor.tell("login", getSelf());
                }
            default:
                System.out.println("I'm handling " + msg);
        }
    }
}
