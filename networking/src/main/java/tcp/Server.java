package tcp;

import message.Message;
import message.factories.JsonMessageFactory;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

public class Server {

    protected ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Can't not open server socket on port: " + port);
            e.printStackTrace();
        }
    }

    public Socket waitForConnection() throws IOException {
        return serverSocket.accept();
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Can't close socket");
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return serverSocket.isClosed();
    }

    /**
     *
     * @return the local IP address this machines listens to.
     * @throws UnknownHostException if no matching ip is found.
     */
    public static String findMachinesLocalIP() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    String ip = address.getHostAddress();
                    if (ip.indexOf("192.") == 0 || ip.indexOf("145.") == 0 || ip.indexOf("129.") == 0) {
                        return ip;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }
}
