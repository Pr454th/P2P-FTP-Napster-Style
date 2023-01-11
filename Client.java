
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
	@SuppressWarnings({ "unchecked", "rawtypes", "resource", "unused" })
	public Client() {
		Socket socket;
		ArrayList al;
		ArrayList<FileInfo> arrList = new ArrayList<FileInfo>();
		Scanner scanner = new Scanner(System.in);
		ObjectInputStream ois;
		ObjectOutputStream oos;
		String string;
		Object o, b;
		String directoryPath = null;
		int peerServerPort = 0;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("*** Welcome to the Client ***");
			System.out.println(" ");

			System.out.print("Enter the peerid for this directory : ");
			int readpid = Integer.parseInt(br.readLine());

			System.out.print("Enter the port number on which the peer should act as server : ");
			peerServerPort = Integer.parseInt(br.readLine());

			System.out.print("Enter the directory that contain the files : ");
			directoryPath = br.readLine();

			ServerDownload objServerDownload = new ServerDownload(peerServerPort, directoryPath);
			objServerDownload.start();

			Socket clientThread = new Socket("localhost", 7799);

			ObjectOutputStream objOutStream = new ObjectOutputStream(clientThread.getOutputStream());
			ObjectInputStream objInStream = new ObjectInputStream(clientThread.getInputStream());

			al = new ArrayList();

			socket = new Socket("localhost", 7799);
			System.out.println("Connection has been established with the server");

			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());

			File folder = new File(directoryPath);
			File[] listofFiles = folder.listFiles();
			FileInfo currentFile;
			File file;

			for (int i = 0; i < listofFiles.length; i++) {
				currentFile = new FileInfo();
				file = listofFiles[i];
				currentFile.fileName = file.getName();
				currentFile.peerid = readpid;
				currentFile.portNumber = peerServerPort;
				arrList.add(currentFile);
			}

			oos.writeObject(arrList);
			while(true){
				System.out.print("Enter the desired file name that you want to downloaded from the list of the files available in the Server : ");
				String fileNameToDownload = br.readLine();
				oos.writeObject(fileNameToDownload);

				System.out.println("Waiting for the reply from Server...!!");

				ArrayList<FileInfo> peers = new ArrayList<FileInfo>();
				String ack=(String) ois.readObject();
				if(ack.equals("Found")){
					peers = (ArrayList<FileInfo>) ois.readObject();
					if (peers.size() == 0) {
						System.out.println("Requested file not found in any of the peers !!!");
					} 
					else {
						for (int i = 0; i < peers.size(); i++) {
							int result = peers.get(i).peerid;
							int port = peers.get(i).portNumber;
							System.out.println("The file is stored at peer id " + result + " on port " + port);
						}
						int clientAsServerPortNumber = 0, clientAsServerPeerid = 0;
						int chk = 1;
						while (chk != 0) {
							System.out.println("Enter the desired peer id from which you want to download the file from :");
							clientAsServerPeerid = Integer.parseInt(br.readLine());

							for (int i = 0; i < peers.size(); i++) {
								int result = peers.get(i).peerid;
								int port = peers.get(i).portNumber;
								if (result == clientAsServerPeerid) {
									clientAsServerPortNumber = port;
									chk = 0;
									break;
								}
							}
							if (chk == 1) {
								System.out.println("Entered PeerID Not Found.\nPlease Try Again...");
							}
						}
						clientAsServer(clientAsServerPeerid, clientAsServerPortNumber, fileNameToDownload, directoryPath);
					}
				}
				else{
					System.out.println("File Not Found!!");
				}
				System.out.print("Enter 1 to continue or 0 to Exit : ");
				String flg = br.readLine();
				oos.writeObject(flg);
				if(flg.equals("0")){
					break;
				}
			}
		} 
		catch (Exception e) {
			System.out.println("Error in establishing the Connection between the Client and the Server!! ");
			System.out.println("Please cross-check the host address and the port number..");
		}
	}

	public static void clientAsServer(int clientAsServerPeerid, int clientAsServerPortNumber, String fileNamedwld,
			String directoryPath) throws ClassNotFoundException {
		try {
			@SuppressWarnings("resource")
			Socket clientAsServersocket = new Socket("localhost", clientAsServerPortNumber);

			ObjectOutputStream clientAsServerOOS = new ObjectOutputStream(clientAsServersocket.getOutputStream());
			ObjectInputStream clientAsServerOIS = new ObjectInputStream(clientAsServersocket.getInputStream());

			clientAsServerOOS.writeObject(fileNamedwld);
			int readBytes = (int) clientAsServerOIS.readObject();

			byte[] b = new byte[readBytes];
			clientAsServerOIS.readFully(b);
			OutputStream fileOPstream = new FileOutputStream(directoryPath + "//" + fileNamedwld);

			@SuppressWarnings("resource")

			BufferedOutputStream BOS = new BufferedOutputStream(fileOPstream);
			BOS.write(b, 0, (int) readBytes);

			System.out.println("Requested file - " + fileNamedwld + ", has been downloaded to your desired directory "
					+ directoryPath);
			System.out.println(" ");
			System.out.println("Display file " + fileNamedwld);

			BOS.flush();
		} catch (IOException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
