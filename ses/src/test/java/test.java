import org.junit.Before;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;

public class test {
    @Before
    public void setUp() throws Exception{
        Socket socket = new Socket("localhost", 8080);
        BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
}
