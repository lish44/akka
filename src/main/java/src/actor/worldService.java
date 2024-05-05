package src.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.game.net.framework.server.WsClientConnection;
import com.game.net.framework.server.WsClientConnectionListener;
import com.game.net.framework.server.WsClientConnectionListenerFactory;
import com.game.net.framework.server.WsNetServer;
import com.sunweb.game.util.DateUtil;
import src.actor.client.conn;
import src.packet.loginMsg;

/**
 * @author wh
 * @date 2024/4/30 17:00
 */
public class worldService extends AbstractActor implements WsClientConnectionListenerFactory {

    public static Props props() {
        return Props.create(worldService.class);
    }

    private WsNetServer server;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .build();
    }

    @Override
    public WsClientConnectionListener createListener(WsClientConnection wsClientConnection) {

        conn c = new conn(wsClientConnection);


        // todo 向 agent 请求注册一个 clientActor 用于处理客户端消息


        return c;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        server = new WsNetServer(7700);
        server.setManagerTimeOutInMS((int) (DateUtil.MS_PER_MINUTE));
        server.setClientConnectionListenerFactory(this);
        server.getCommandSet().addCommandClasses(new Class[] {loginMsg.class});
        server.start();
    }

    @Override
    public void postStop() throws Exception {
        server.stop();
    }
}
