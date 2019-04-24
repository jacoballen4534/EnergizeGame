package Multiplayer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BinaryWriter {
    private List<Byte> buffer;

    public BinaryWriter() {
        this.buffer = new ArrayList<>();
    }

    public BinaryWriter(int size) {
        this.buffer = new ArrayList<>(size);
    }

    public void write(byte data) { //Add a byte to end
        buffer.add(data);
    }

    public void write(byte[] data) { //Append another byte array
        for (int i = 0; i < data.length; i++) {
            buffer.add(data[i]);
        }
    }

    public void write(int data) { //Convert int to byte array
        byte[] b = ByteBuffer.allocate(4).putInt(data).array(); //Create a buffer of length 4 to put the integer in. (Java int = 4 bytes)
        write(b);
    }

    public byte[] getBuffer() { //Convert the byte list into a byte array
        Byte[] array = new Byte[buffer.size()]; //As Byte List can't be converted to byte array. Need to iterate through the full list, placing moving each byte
        buffer.toArray(array);
        byte[] result = new byte[buffer.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
}
