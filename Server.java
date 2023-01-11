import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.util.logging.*;

public class Server {
	public static ArrayList<FileInfo> globalArray = new ArrayList<FileInfo>();

	public Server() throws Exception {

		System.out.println("**** Welcome to the Server ****");
		System.out.println(" ");
		ServerSocket serverSocket = new ServerSocket(7799);
		System.out.println("Server started!!\n");
		System.out.println("Waiting for the Client to be connected ..");
		
		while(true){
			Socket socket = serverSocket.accept();
			new ServerTestClass(socket, globalArray).start();
		}
	}
}

class ServerTestClass extends Thread {
	protected Socket socket;
	ArrayList<FileInfo> globalArray;

	public ServerTestClass(Socket clientSocket, ArrayList<FileInfo> globalArray) {
		this.socket = clientSocket;
		this.globalArray = globalArray;
	}

	ArrayList<FileInfo> filesList = new ArrayList<FileInfo>();
	ObjectOutputStream oos;
	ObjectInputStream ois;
	String str,flg;
	int index=-1;
	Boolean rep=false;
	
	@SuppressWarnings("unchecked")

	public void run() {
		try {
			InputStream is = socket.getInputStream();
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(is);
			filesList = (ArrayList<FileInfo>) ois.readObject();
			System.out.println("All the available files from the given directory have been recieved to the Server!");
			for (int i = 0; i < filesList.size(); i++) {
				globalArray.add(filesList.get(i));
			}
			System.out.println("Total number of files available in the Server that are received from all the connected clients: "+ globalArray.size());
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		while(true){
			try {
				str = (String) ois.readObject();
			}
			catch (Exception ex) {}

			ArrayList<FileInfo> sendingPeers = new ArrayList<FileInfo>();

			for (int j = 0; j < globalArray.size(); j++) {
				FileInfo fileInfo = globalArray.get(j);
				Boolean tf = fileInfo.fileName.equals(str);
				if (tf) {
					index = j;
					sendingPeers.add(fileInfo);
				}
			}
			if(index==-1){
				try {
					oos.writeObject("NotFound");
				}
				catch (Exception ex) {}
			}
			else{
				try {
					oos.writeObject("Found");
					oos.writeObject(sendingPeers);
					sendingPeers.clear();
				}catch (Exception ex) {}
			}
		}
	}
}
