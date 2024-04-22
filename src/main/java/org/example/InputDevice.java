package org.example;

import java.io.InputStream;
import java.util.Scanner;

public class InputDevice {
    private InputStream is;
    private Scanner scan;

    public InputDevice(InputStream is) {
        this.is = is;
        scan = new Scanner(is);
    }
    public String nextLine()
    {
        String line = scan.nextLine();
        return line;
    }

}
