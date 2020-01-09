package helloworld;
import redis.clients.jedis.Jedis; 
public class HelloWorld {
    public static void main(String args[]) {
	System.out.println("Hello World, Maven");

	//Connecting to Redis server on localhost
        Jedis jedis = new Jedis("localhost");

        System.out.println("Connection to server sucessfully");

        //check whether server is running or not
        System.out.println("Server is running: "+jedis.ping());
    }
}
