import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyMain {
	public static void main(String args[]) throws Exception{

		Boolean flag=false;

		while(!flag){
			System.out.println("MAIN MENU");
			System.out.println("1. To run the Server");
			System.out.println("2. To run the Client");
			System.out.println("3. To exit");
			System.out.print("Please enter your Choice : ");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		
			int choice = Integer.parseInt(br.readLine());
			
			if(choice == 1){		
				Server s = new Server();
			}
			else if(choice == 2){
				Client c = new Client();
			}
			else if(choice == 3){
				flag=true;
			}
			else{
				System.out.println("Your choice is incorrect");
			}
		}
		System.out.println("TERMINATED...");
	}
}
