package example.utils;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HashData {
    private static final Map<String,String> hash_0x01 = new ConcurrentHashMap<>();
    private static final Map<String,String> hash_0x02 = new ConcurrentHashMap<>();
    private static final Queue<String> hash_0x03 = new ConcurrentLinkedQueue<>();
    private HashData(){}
    public static void addHash(String key,String field,String value){
        switch(key){
            case "0x01":{
                hash_0x01.remove(field);
                hash_0x01.put(field,value);
            }break;
            case "0x02":{
                hash_0x02.remove(field);
                hash_0x02.put(field,value);
            }break;
            case "0x03":{
                hash_0x03.add(field);
                if (hash_0x03.size() >= 10){
                    hash_0x03.poll();
                }
            }
            default:break;
        }
        System.out.println(key + "  " + field + "  "  + value);
    }
    public static String getHashValue(String key,String field){
        switch(key)
        {
            case "0x01":{
                if(hash_0x01.containsKey(field)){
                    String res = hash_0x01.get(field);
                    hash_0x01.remove(field);
                    return res;
                }
            }break;
            case "0x02":{
                if(hash_0x02.containsKey(field)){
                    String res = hash_0x02.get(field);
                    hash_0x02.remove(field);
                    return res;
                }
            }break;
            case "0x03":{
                if (hash_0x03.isEmpty()) return "1000-1000";
                else return hash_0x03.poll();
            }
            default:break;
        }
        return null;
    }
    public static boolean exists(String key,String field){
        switch(key){
            case "0x01":return hash_0x01.containsKey(field);
            case "0x02":return hash_0x02.containsKey(field);
            case "0x03":return !hash_0x03.isEmpty();
            default:return false;
        }
    }
}
