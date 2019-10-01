import java.util.HashMap;
import java.util.LinkedList;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.Map;

public class ChatManager{

    HashMap<String,Boolean> usernameMap;
    HashMap<String,Boolean> onlineMap;
    HashMap<String,InetAddress > addressMap;
    HashMap<String,Integer> portMap;

    private Byte buff[];

    public ChatManager(){

        System.out.println("ChatManager is Alive now");
    }

    public Boolean userExists(String username){

        return usernameMap.containsKey(username);
    }

    public Boolean isOnline(String username){

        if(usernameMap.containsKey(username) == true &&  onlineMap.get(username) == true)return true;

        return false;
    }

    public Boolean registerUser(String username,InetAddress inet,int port){

    	if(userExists(username))return false;

    	usernameMap.put(username,true);
    	onlineMap.put(username,true);
    	addressMap.put(username,inet);
    	portMap.put(username,port);

    	return true;
    }

    Boolean removeUser(String username){

    	if(userExists(username) == false)return false;

    	onlineMap.replace(username,false);

    	return true;

    }



    LinkedList<DatagramPacket> process(DatagramPacket pkt){

    	String response = "";
    	String username;
    	String message;
    	InetAddress inet;
    	int port;
    	int commFlag;
    	Boolean sendFlag = false;
    	LinkedList<DatagramPacket> sendDatagramList = new LinkedList<DatagramPacket>();

        Scanner scn = new Scanner(new String(pkt.getData()));
        commFlag = scn.nextInt();
        username = scn.next();

        if(commFlag == CommunicationFlags.FirstNotification){

        	try{        	

	        	inet = InetAddress.getByName(scn.next());
	        	port = scn.nextInt();


	        	if(registerUser(username,inet,port) == true){

	        		response = username + " Registered Sucessfully";
	        		sendFlag = true;
	        	}

	        	else response = username + " User Already Registered ";

	        }

	        catch(Exception ex){

	        	response = username + " Caused an Exception@CM.process";
	        }


        }

        else if(commFlag == CommunicationFlags.LastNotification){

   	    	if(removeUser(username)){

   	    		response = username + " Left Chatroom ";

   	    	}
        	
        }

        else {

        	message = scn.next();
        	response = username + " : " + message;
        }

        for(Map.Entry mapElement: addressMap.entrySet()){

    		String key = (String)mapElement.getKey();

    		if(key != username){

    			DatagramPacket dpkt = new DatagramPacket(response.getBytes(),response.length(),addressMap.get(key),portMap.get(key));

    			sendDatagramList.push(dpkt);
    		}
    	}

        return sendDatagramList;
    }
}
