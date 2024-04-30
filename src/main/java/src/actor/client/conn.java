package src.actor.client;

import com.game.net.framework.command.NetCommand;
import com.game.net.framework.command.UnknownCommand;
import com.game.net.framework.server.WsClientConnection;
import com.game.net.framework.server.WsClientConnectionListener;

/**
 * @author wh
 * @date 2024/4/30 17:37
 */
public class conn implements WsClientConnectionListener {

    public conn(WsClientConnection conn) {
        this.conn = conn;
    }

    private final WsClientConnection conn;

    @Override
    public void onCommand(NetCommand netCommand) {

    }

    @Override
    public void onUnknownCommand(UnknownCommand unknownCommand) {

    }

    @Override
    public void onDisconnected(boolean b) {

    }
}
