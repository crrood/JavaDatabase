import java.io.BufferedReader;
import java.io.InputStreamReader;

public class main {
	
	public static void main(String[] args){
		// Initialize DataBase instance
		DataBase db = new DataBase();
		
		// Initialize input
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String rawInput = "";
		String[] splitInput;
		
		// Input loop
		while(!rawInput.matches("end")){
			try{
				// Get commands from stdin
				rawInput = stdin.readLine().toLowerCase();
				splitInput = rawInput.split(" ");
				
				if(splitInput.length > 0){
					// Input handling
					switch(splitInput[0]){
					// Variable setting
					case "set":
						if(splitInput.length < 2){
							System.out.println("Specify a variable to set");
						}
						else if(splitInput.length < 3){
							System.out.println("Specify a value to set variable to");
						}
						else{
							// Set variable
							db.set(splitInput[1], splitInput[2]);
						}
						break;
					case "get":
						if(splitInput.length < 2){
							System.out.println("Specify a variable to get");
						}
						else{
							// Get variable
							System.out.println(db.get(splitInput[1]));
						}
						break;
					case "unset":
						if(splitInput.length < 2){
							System.out.println("Specify a variable to unset");
						}
						else{
							// Unset variable
							db.unset(splitInput[1]);
						}
						break;
					case "numequalto":
						if(splitInput.length < 2){
							System.out.println("Specify a value to check for");
						}
						else{
							// Run numequalto
							System.out.println(db.numEqualTo(splitInput[1]));
						}
						break;
						
					// Session commands
					case "begin":
						db.begin();
						break;
					case "rollback":
						db.rollback();
						break;
					case "commit":
						db.commit();
						break;
					case "end":
						break;
					default:
						System.out.println(splitInput[0] + " is not a recognized command");
					}
				}
			}
			catch (Exception e){
				System.out.println(e);
			}
		}
	}
	
}