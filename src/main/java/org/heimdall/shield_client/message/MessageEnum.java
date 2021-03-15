package org.heimdall.shield_client.message;

public enum MessageEnum {

    PING_PONG((short)0, PingPongExecutor.class);

    private Short type;

    private Class clazz;

    MessageEnum(Short type, Class clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

}

