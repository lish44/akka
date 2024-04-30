package src.allInOne;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import src.actor.gameActor;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/4/25 23:55
 */
public class allInOne {
    public static void main(String[] args) throws InterruptedException {

        ActorSystem system = ActorSystem.create("GameSystem");
        ActorRef gameActor = system.actorOf(src.actor.gameActor.props(), "gameActor");

        gameActor.tell("creat", ActorRef.noSender());

        Thread.sleep(2000);

        gameActor.tell("login", ActorRef.noSender());

        // gameActor.tell(new src.actor.gameActor.ShutdownMessage(), ActorRef.noSender());

    }
}
