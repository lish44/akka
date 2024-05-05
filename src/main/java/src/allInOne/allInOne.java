package src.allInOne;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import src.actor.worldService;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/4/25 23:55
 */
public class allInOne {
    public static void main(String[] args) throws InterruptedException {

        ActorSystem app = ActorSystem.create("App");
        ActorRef world = app.actorOf(worldService.props(), "worldService");

        world.tell("creat", ActorRef.noSender());

        Thread.sleep(2000);

        world.tell("login", ActorRef.noSender());

        // gameActor.tell(new src.actor.gameActor.ShutdownMessage(), ActorRef.noSender());

    }
}
