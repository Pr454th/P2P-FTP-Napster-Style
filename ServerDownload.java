import java.io.*;
import java.net.*;
   
public class ServerDownload extends Thread{
    int peerServerPort;
    String  directoryPath;

    ServerDownload(int peerServerPort,String directoryPath) {        
    	this.peerServerPort=peerServerPort;
    	this.directoryPath=directoryPath;    
    }
    public void run(){
    	try {
            ServerSocket dwldServerSocket = new ServerSocket(peerServerPort);
            while(true){
    			Socket dwldSocket = dwldServerSocket.accept();
    			new ServerDownloadThread(dwldSocket,directoryPath).start();
            }   
        } 
    	catch (Exception ex) {
    			System.out.println(ex.getMessage());
    	}
    }    
} 
class ServerDownloadThread extends Thread{
    Socket dwldThreadSocket;
    String directoryPath;

    public ServerDownloadThread(Socket dwldThreadSocket,String directoryPath){
        this.dwldThreadSocket=dwldThreadSocket;       
        this.directoryPath=directoryPath;
    }

	public void run(){
        try{
            ObjectOutputStream objOS = new ObjectOutputStream(dwldThreadSocket.getOutputStream());
            ObjectInputStream objIS = new ObjectInputStream(dwldThreadSocket.getInputStream());
            
            String fileName = (String)objIS.readObject();
            String fileLocation;
            File myFile = new File(directoryPath+"//"+fileName);
            long length = myFile.length();
            
            byte [] byte_arr = new byte[(int)length];
            
            objOS.writeObject((int)myFile.length());
            objOS.flush();
            
            FileInputStream FIS=new FileInputStream(myFile);
            BufferedInputStream objBIS = new BufferedInputStream(FIS);
            objBIS.read(byte_arr,0,(int)myFile.length());
                            
            objOS.write(byte_arr,0,byte_arr.length);
            
            objOS.flush();                
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
         }
    }
}