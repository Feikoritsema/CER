import java.io.*;
import java.net.*;
import java.util.Enumeration;

public class Server {

    protected ServerSocket serverSocket;
    protected Socket clientSocket;

    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;

    JsonMessageFactory jsonMessageFactory;

    public Server(int port) {
        jsonMessageFactory = new JsonMessageFactory();

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Can't not open server socket on port: " + port);
            System.err.print(e);
        }
    }

    public void waitForConnection() {
        try {
            clientSocket = serverSocket.accept();
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Can't open client socket");
            System.err.print(e);
        }
    }

    public Message receive() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line, json;

            while ((line = in.readLine()) != null) {
                builder.append(line);
            }

            json = builder.toString();
            return jsonMessageFactory.jsonToMessage(json);
        } catch (IOException e) {
            System.err.println("Can't read input");
            System.err.print(e);
        }

        return new Message();
    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Can't close socket");
            System.err.print(e);
        }
    }

    public boolean isClosed() {
        return clientSocket.isClosed();
    }

    /**
     *
     * @return the local IP address this machines listens to.
     * @throws UnknownHostException if no matching ip is found.
     */
    public static InetAddress findMachinesLocalIP() throws UnknownHostException {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    String ip = address.getHostAddress();
                    if (ip.indexOf("192.") == 0 || ip.indexOf("145.") == 0 || ip.indexOf("129.") == 0) {
                        return address;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        throw new UnknownHostException();
    }
}
