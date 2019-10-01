public class Message{


    String sender;
    String content;

    public String ToString(){

        return ( sender + " ( "+content+" ) -> " + content );
    }
}
