package lt.viko.eif.tkalabin.client;

import java.io.*;
import java.net.Socket;

public class JavaClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public static void main(String[] args) {
        //Console cns = System.console();
        JavaClient client = new JavaClient();
        try {

            client.startConnection("127.0.0.1", 6666);

            //System.out.println("Write full path to file:");
            client.sendFile("publishers.xml");
            //while (client.dataInputStream.readUTF() != "Job ended!")
            System.out.println("Server responded: " + client.dataInputStream.readUTF());


            client.dataInputStream.close();
            client.dataOutputStream.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            client.stopConnection();
        }
    }

    /**
     * Connects to local server
     *
     * @param ip   by default - 127.0.0.1
     * @param port by default - 6666
     * @throws IOException
     */
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);

        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Sends file to local server
     *
     * @param path path to the file
     * @throws Exception
     */
    private void sendFile(String path) throws Exception {
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        dataOutputStream.writeUTF(file.getName());

        // send file size
        dataOutputStream.writeLong(file.length());
        // break file into chunks
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }

    /**
     * Stops connection with server
     */
    public void stopConnection() {
        try {
            dataOutputStream.close();
            dataInputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Exception occured: " + e.getMessage());
        }
    }


}