package network;

import interfaces.SerializeConversor;

import java.io.IOException;
import java.io.Serializable;

public class Room implements Serializable {
    static final SerializeConversor<Message> chatMessageSerializeConvert = new SerializeConversor<>() {
        @Override
        public byte[] serialize(Message obj) throws IOException {
            return new byte[0];
        }

        @Override
        public Message deserialize(byte[] data) throws IOException {
            return null;
        }
    };


}