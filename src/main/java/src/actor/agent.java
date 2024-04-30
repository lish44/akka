package src.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.game.net.framework.command.NetCommand;

import java.util.Map;

/**
 * @author wh
 * @date 2024/4/30 18:14
 */
public class agent extends AbstractActor {

    Map<String, ActorRef> clientActorMap;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(NetCommand.class, this::handle)
                .build();
    }

    private void handle(NetCommand netCommand) {

    }

}
