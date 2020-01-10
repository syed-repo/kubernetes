package redis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import java.util.Map;

public class RedisClient {
    public static void main(String args[]) {
        System.out.println("Hello World, Maven");
        String redisHost = "my-release-redis-master";
        String redisPort = "6379";
        String redisPassword = "TZsFi2gEAU";

        JedisShardInfo shardInfo = new JedisShardInfo(redisHost, redisPort);
        shardInfo.setPassword(redisPassword);
        //Connecting to Redis server on localhost
        Jedis jedis = new Jedis(shardInfo);
        jedis.connect();
        System.out.println("Connection to server sucessfully");

        //check whether server is running or not
        System.out.println("Server is running: "+jedis.ping());

        jedis.hset("user#1", "name", "Peter");
        jedis.hset("user#1", "job", "politician");
        String name = jedis.hget("user#1", "name");
        Map<String, String> fields = jedis.hgetAll("user#1");
        String job = fields.get("job");
        System.out.println(job);
        System.out.println(name);
    }
}
