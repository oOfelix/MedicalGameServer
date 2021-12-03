package org.northwest.newserver.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class HashData {
    private static final Map<String,String> hash_0x01 = new ConcurrentHashMap<>();
    private static final Map<String,String> hash_0x02 = new ConcurrentHashMap<>();
    private static final Queue<String> hash_0x03 = new ConcurrentLinkedQueue<>();
    private static String con_0x03 = "76-50";
    private HashData(){}
    public static void addHash(String ID,String Payload,String time){
        switch(ID){
            case "0x01":{
                hash_0x01.remove(Payload);
                hash_0x01.put(Payload,time);
            }break;
            case "0x02":{
                hash_0x02.remove(Payload);
                hash_0x02.put(Payload,time);
            }break;
            case "0x03":{
//                hash_0x03.add(Payload);
//                if (hash_0x03.size() >= 2){
//                    hash_0x03.poll();
//                }
                con_0x03 = Payload;
            }
            default:break;
        }
    }
    public static String getHashValue(String ID,String Payload){
        switch(ID)
        {
            case "0x01":{
                if(hash_0x01.containsKey(Payload)){
                    String res = hash_0x01.get(Payload);
                    hash_0x01.remove(Payload);
                    return res;
                }
            }break;
            case "0x02":{
                if(hash_0x02.containsKey(Payload)){
                    String res = hash_0x02.get(Payload);
                    hash_0x02.remove(Payload);
                    return res;
                }
            }break;
            case "0x03":{
//                if (hash_0x03.isEmpty()) return "1000-1000";
//                else return hash_0x03.poll();
                return con_0x03;
            }
            default:break;
        }
        return null;
    }
    public static boolean exists(String ID,String Payload){
        switch(ID){
            case "0x01":return hash_0x01.containsKey(Payload);
            case "0x02":return hash_0x02.containsKey(Payload);
            case "0x03":return !hash_0x03.isEmpty();
            default:return false;
        }
    }
}

