import java.net.InetAddress;
import java.util.Scanner;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import CommunicationFlag;

public class Client{


	Thread sendingThread;
	Thread recevingThread;
	InetAddress serverIP;
	int serverPort;

	DatagramSocket sendSocket;
	DatagramSocket recieveSocket;

	DataOutputStream dataOut;
	ByteOutputStream byteOut;

	bytes[] buff;

	Scanner scn;

	public Client(InetAddress serverIP,int serverPort)throws Exception
	{

		sendSocket = new DatagramSocket();
		recieveSocket = new DatagramSocket();

		this.serverIP = serverIP;
		this.serverPort = serverPort;

		buff = new bytes[1024];
		byteOut = new ByteOutputStream;
		dataOut = new DataOutputStream(byteOut);

		dataOut.write(CommunicationFlag.FirstNotification);
		dataOut.flush();

		receivingThread = new Thread(this);
		receivingThread.start();

		sendingThread = new Thread(this);
		sendingThread.start();


	}

	public void run(){

		System.out.println("Client control thread :: "+Thread.currentThread());

		if(Thread.currentThread() == sendingThread)sendingFxn();

		else recevingFxn();
	}

	public void sendingFxn(){

		System.out.println("sending thread up");

		buff = 
		
		sendSocket.send(new DatagramPacket())

		String message;

		while(true){

			try{

				message = scn.next();
				buff = message.getBytes();

				sendSocket.send(new DatagramPacket(buff,message.length(),serverIP,serverPort));

			}


			catch(Exception ex){

				System.out.println("Exception@Client.sendingFxn :: "+ex.getMessage());
			}


		}
	}

	public void recevingFxn(){

		System.out.println("receving thread up");
	}

	public static void main(String args[]){

		try{

			new Client(Integer.parseInt(args[0]),Integer.parseInt(args[1]));

		}

		catch(ArrayIndexOutOfBoundException ex){

			System.out.println("java Client < Server IP >  < Server Port>");

		}

		catch(Exception ex){

			System.out.println("Exception@Client.Main :: "+ex.getMessage());
		}

	}
}