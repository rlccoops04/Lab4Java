import Client.UDP_Client;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Main {
    public static void main(String args[]) {
        String address = args[0];
        int port = Integer.parseInt(args[1]);

        File f = new File("C:\\Java\\Files\\settings.txt");
        StringBuilder sb = new StringBuilder();
        if(f.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(f.getAbsoluteFile()));
                try{
                    String s;
                    while((s = br.readLine())!=null){//построчное чтение
                        sb.append(s);
                    }
                }finally{br.close();}
            }catch(IOException e){throw new RuntimeException();}
        }
        String pathToLog = sb.toString();
        UDP_Client client = new UDP_Client(address, port, pathToLog);
        client.run();
    }
}
