package per.hxl.stewardtokgo.Chat;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Net.TCP.ClientBaseClass;
import per.hxl.stewardtokgo.Net.NetReceive;
import per.hxl.stewardtokgo.utils.ConstantValue;

public class ChatClient extends ClientBaseClass {

    private final NetReceive netReceive;
    private static Pattern  pattern = Pattern.compile("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?");

    public ChatClient(NetReceive netReceive) throws IOException {
        Matcher matcher = pattern.matcher(ConstantValue.SERVERADRR);
        matcher.find();
        String addr = matcher.group();
        int port =ConstantValue.CHATPRDPORT;
        if ("qa".equals(addr.split("-")[0])){
            port = ConstantValue.CHATQAPORT;
        }
        this.Connect(addr,port,this);
        this.netReceive =netReceive;
    }

    @Override
    protected void ServersBreakoff() {
        netReceive.ServersBreakoff();
    }

    @Override
    public boolean Send(String Data) {

        byte[] sendbytes = Data.getBytes();
        return super.Send(sendbytes,sendbytes.length);
    }

    @Override
    public void Receive(Socket client, byte[] data, int lenght) {
        netReceive.Receive(client,new String(data,0,lenght));
    }
}
