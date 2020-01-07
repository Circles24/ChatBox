import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;


public class Server extends Thread
{

    private DatagramSocket recvSocket;
    private DatagramSocket sendSocket;

    private Queue<DatagramPacket> q;

    private ChatManager cm;

    Thread recievingThead;
    Thread sendingThread;

    public void run(){


    	System.out.println("Server control :: "+Thread.currentThread());

        if(Thread.currentThread() == recievingThead)recievePackets();

        else sendPackets();
    }

    public void sendPackets(){

    	System.out.println("Sending thread up");

        LinkedList<DatagramPacket> sendDatagramList = new LinkedList<DatagramPacket>();

        while(true){

            try{

                if(q.size() == 0)Thread.sleep(200);

                else {

                    while(q.size() != 0){

                        sendDatagramList = cm.process(q.element());

                        for(DatagramPacket dpkt : sendDatagramList){

                        	sendSocket.send(dpkt);
                        }

                    }
                }
            }

            catch(Exception ex){

                System.out.println("Exception@Server.sendPackets :: "+ex);
            }

        }

    }

    private void recievePackets(){

    	System.out.println("Receiving thread up");

    	byte buff[] = new byte[2048];

    	DatagramPacket dpkt = new DatagramPacket(buff,2048);

    	while(true){

    		try{

    			recvSocket.receive(dpkt);
	    		q.add(dpkt);
	    		sendingThread.interrupt();

    		}

    		catch(Exception ex){

    			System.out.println("Exception@Server.recievePackets:: "+ex.getMessage());
    		}

    	}

    }

    public Server(int portNo)throws Exception
    {

        recvSocket = new DatagramSocket(portNo);
        sendSocket = new DatagramSocket();

        sendingThread = new Thread(this);
        sendingThread.start();

        recievingThead = new Thread(this);
        recievingThead.start();

        q = new LinkedList<DatagramPacket>();
    }

    public static void main(String args[]){

        try{

            new Server(Integer.parseInt(args[0]));
        }

        catch(ArrayIndexOutOfBoundsException ex){

            System.out.println("Java Server <port> ");
        }

        catch(Exception ex){

            System.out.println("Exception@Server.main :: " + ex);
        }
    }

}
