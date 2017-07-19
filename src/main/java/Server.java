

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) {

        Server serv = new Server();
    }
    class AceptedThread extends Thread
    {
        Server serv;
        ServerSocket serverSocket = null;
        AceptedThread(Server main,ServerSocket main2)
        {
            this.serv=main;
            this.serverSocket=main2;
        }
        public void run()
        {
            while (true) {
                try {
                    Socket socket = null;
                    socket = serverSocket.accept();
                    ConnectionThread a = new ConnectionThread(socket,serv);
                    serv.conns.add(a);
                    a.start();
                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }
                // new thread for a client

            }
        }
    }
    ArrayList<ConnectionThread> conns = new ArrayList<ConnectionThread>();
    public Server()
    {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4040);
            System.out.print("Done"+"\n");
            System.out.print(serverSocket.getInetAddress()+"\n");
            System.out.print(serverSocket.getLocalPort()+"\n");
            System.out.print(serverSocket.getLocalSocketAddress()+"\n");
            System.out.print(serverSocket.getReuseAddress()+"\n");
            System.out.print(InetAddress.getLoopbackAddress()"\n");
            new AceptedThread(this,serverSocket).start();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
class ConnectionThread extends Thread{
    private Socket socket;
    private Server server;
    private PrintWriter out;
    ConnectionThread(Socket sock, Server serv)
    {
        this.server=serv;
        this.socket=sock;
    }
    public void send(String message)
    {
        if(out!=null)
        {
            out.println(message);
        }
    }
    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;
        out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("FINISH")) {
                    socket.close();
                    return;
                } else {
                    for(ConnectionThread e : server.conns)
                    {
                        e.send(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}


