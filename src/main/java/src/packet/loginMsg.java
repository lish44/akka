package src.packet;

import com.game.net.framework.codec.diy.DIYCommand;
import src.gen.proto.UserLoginProto;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/5/1 02:21
 */
public class loginMsg extends DIYCommand {

    UserLoginProto.UserLoginReq msg;

    UserLoginProto.UserLoginRsp.Builder rsp;

    public loginMsg() {
        super(0);
    }

    @Override
    public byte[] toBytes() {
        return getRsp().build().toByteArray();
    }

    @Override
    public void toObject(byte[] bytes) throws Exception {
        msg = UserLoginProto.UserLoginReq.parseFrom(bytes);
    }

    public UserLoginProto.UserLoginRsp.Builder getRsp() {
        _code = 1;
        return rsp != null ? rsp : (rsp = UserLoginProto.UserLoginRsp.newBuilder());
    }

    public UserLoginProto.UserLoginReq getMsg() {
        return msg;
    }

}
