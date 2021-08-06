package example.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashData {
    private static Map<String,String> hash_0x01 = new ConcurrentHashMap<>();
    private static Map<String,String> hash_0x02 = new ConcurrentHashMap<>();
    private HashData(){}
    public static void addHash(String key,String field,String value){
        switch(key)
        {
            case "0x01":{
                if(hash_0x01.containsKey(field))
                    hash_0x01.remove(field);
                hash_0x01.put(field,value);
            }break;
            case "0x02":{
                if(hash_0x02.containsKey(field))
                    hash_0x02.remove(field);
                hash_0x02.put(field,value);
            }break;
            default:break;
        }
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
            default:break;
        }
        return null;
    }
    public static boolean exists(String key,String field){
        switch(key)
        {
            case "0x01":return hash_0x01.containsKey(field);
            case "0x02":return hash_0x02.containsKey(field);
            default:return false;
        }
    }
}
