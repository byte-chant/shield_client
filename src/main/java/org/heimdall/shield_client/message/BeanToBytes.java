package org.heimdall.shield_client.message;

import java.util.UUID;

public class BeanToBytes {

    private Integer length;

    private Short magic;

    private Short type;

    private String uuid;

    private String body;

    public BeanToBytes(Short type, String body){
        this((short)(9527), type, UUID.randomUUID().toString().replace("-", ""), body);
    }

    public BeanToBytes(Short magic, Short type, String uuid, String body){
        this.magic = magic;
        this.type = type;
        this.uuid = uuid;
        this.body = body;
    }

    public byte[] toByteArray() throws Exception{
        byte[] uuidBytes = uuid.getBytes("UTF-8");
        byte[] bodyBytes = body.getBytes("UTF-8");
        Integer contentLength = 2 + 2 + uuidBytes.length + bodyBytes.length;
        byte[] bytes = new byte[contentLength + 4];
        bytes[0] = (byte)((contentLength >> 24) & 0xFF);
        bytes[1] = (byte)((contentLength >> 16) & 0xFF);
        bytes[2] = (byte)((contentLength >> 8) & 0xFF);
        bytes[3] = (byte)(contentLength & 0xFF);
        bytes[4] = (byte)((magic >> 8) & 0xFF);
        bytes[5] = (byte)(magic & 0xFF);
        bytes[6] = (byte)((type >> 8) & 0xFF);
        bytes[7] = (byte)(type & 0xFF);
        for(int idx = 8; idx < 8 + uuidBytes.length; idx++){
            bytes[idx] = uuidBytes[idx - 8];
        }
        for(int idx = 8 + uuidBytes.length; idx < 8 + uuidBytes.length + bodyBytes.length; idx++){
            bytes[idx] = bodyBytes[idx - 8 - uuidBytes.length];
        }
        return bytes;
    }
}
