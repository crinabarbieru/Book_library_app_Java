package org.example;

import java.io.IOException;
import java.io.OutputStream;

public class OutputDevice {
    private OutputStream os;
    public OutputDevice()
    {
        this.os = System.out;
    }

    public OutputDevice(OutputStream os) {
        this.os = os;
    }
    public void write(String msg)
    {
        try {
            os.write(msg.getBytes());
            os.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
