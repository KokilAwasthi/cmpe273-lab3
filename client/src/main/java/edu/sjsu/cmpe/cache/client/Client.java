package edu.sjsu.cmpe.cache.client;
import com.google.common.hash.*;
import java.util.*;

public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        System.out.println("updating Cache Client...");
                
       /* CacheServiceInterface cache = new DistributedCacheService(
                "http://localhost:3000");

        cache.put(1, "kokil");
        System.out.println("put(1 => kokil)");

        String value = cache.get(1);
        System.out.println("get(1) => " + value);
         System.out.println("Existing Cache Client...");*/
        ArrayList<CacheServiceInterface> servers = new ArrayList();
        servers.add(new DistributedCacheService("http://localhost:3000"));
        servers.add(new DistributedCacheService("http://localhost:3001"));
        servers.add(new DistributedCacheService("http://localhost:3002"));
        
        List<String> inputs = Arrays.asList("a","b","c","d","e","f","g","h","i","j");
        ConsistentHash<CacheServiceInterface> hash = new ConsistentHash<CacheServiceInterface>(Hashing.md5(), 3, servers);
        for(int i=1; i<=10; i++) {
            CacheServiceInterface server = hash.get(i);
            server.put(i, inputs.get(i-1));
        }
        //Get all keys :
        for(int i=1; i<=10; i++) {
            CacheServiceInterface server = hash.get(i);
            System.out.println(i + " => " + server.get(i));
        }
        
    }

}
