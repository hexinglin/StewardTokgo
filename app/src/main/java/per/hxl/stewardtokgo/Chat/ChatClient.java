package per.hxl.stewardtokgo.Chat;

import java.io.IOException;
import java.net.Socket;

import Net.TCP.ClientBaseClass;
import per.hxl.stewardtokgo.Net.NetReceive;
import per.hxl.stewardtokgo.utils.ConstantValue;

public class ChatClient extends ClientBaseClass {

    private final NetReceive netReceive;

    public ChatClient(NetReceive netReceive) throws IOException {
        this.Connect("118.24.6.171",1315,this);
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
