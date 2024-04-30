package src.actor.client;

import akka.actor.AbstractActor;
import akka.actor.Cancellable;
import akka.actor.Props;
import scala.concurrent.duration.Duration;
import src.actor.client.handle.onLoginHandle;

import java.util.concurrent.TimeUnit;

/**
 * @author wh
 * @date 2024/4/30 15:44
 */
public class clientActor extends AbstractActor implements Runnable {

    public static Props props() {
        return Props.create(clientActor.class);
    }

    private Cancellable cancellable;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::handle)
                .build();
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();

        cancellable = getContext().system().scheduler().scheduleWithFixedDelay(
                Duration.create(1, TimeUnit.SECONDS),
                Duration.create(1, TimeUnit.SECONDS),
                this,
                getContext().system().dispatcher() // 使用默认的调度器
        );

    }

    @Override
    public void run() {
        onBreath();
    }

    public void onBreath() {
        System.out.println("I'm breathing");
    }

    public void handle(String args) {
        onLoginHandle.handle(getSelf().path().name());
    }

}
