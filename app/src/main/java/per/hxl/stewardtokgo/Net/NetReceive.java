package per.hxl.stewardtokgo.Net;

import java.net.Socket;

public interface NetReceive {

     void ServersBreakoff();
    void Receive(Socket client, String msg );

}
