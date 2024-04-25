package src.actor;

import akka.actor.AbstractActor;
import akka.actor.Cancellable;
import akka.actor.Props;
import java.util.concurrent.TimeUnit;
import scala.Option;
import scala.concurrent.duration.Duration;
import src.handle.login;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/4/25 23:58
 */
public class gameActor extends AbstractActor implements Runnable {

    public static Props props() {
        return Props.create(gameActor.class);
    }

    int a;
    @Override public void run() {

        System.out.println("I'm breathing");
        if (a++ > 4) {
            int b =  1/0;
        }
    }

    public static class ShutdownMessage {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String[].class, login::handle)
                .match(ShutdownMessage.class, msg -> {
                    getContext().stop(getSelf());
                })
                .build();
    }


    private Cancellable cancellable;

    @Override
    public void preStart() throws Exception {
        super.preStart();

        cancellable =  getContext().system().scheduler().scheduleWithFixedDelay(
                Duration.Zero(), // 延迟时间，立即执行
                Duration.create(1, TimeUnit.SECONDS), // 间隔时间，每秒执行一次
                this,
                getContext().system().dispatcher() // 使用默认的调度器
        );

    }

    @Override
    public void postStop() {
        getContext().getSystem().terminate();
        cancellable.cancel();
    }
}
