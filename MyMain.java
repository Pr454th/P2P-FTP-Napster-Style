import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyMain {
	public static void main(String args[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			System.out.println("*** P2P FTP using Centralized Directory Architecture ***");
			System.out.println("1. To run the Server");
			System.out.println("2. To run the Client");
			System.out.print("Please enter your Choice : ");
			int choice = Integer.parseInt(br.readLine());
			if (choice == 1) {
				new Server();
			} else if (choice == 2) {
				new Client();
			} else {
				System.out.println("Your choice is incorrect");
			}
		}
	}
}
