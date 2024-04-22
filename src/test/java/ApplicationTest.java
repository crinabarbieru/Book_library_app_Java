
import org.example.InputDevice;
import org.example.InvalidDateException;
import org.example.OutputDevice;
import org.junit.jupiter.api.Test;
import org.example.Application;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
public class ApplicationTest {
    @Test
    void test_checkDate1()
    {
        int day = 20;
        int month = 1;
        int year = 2004;
        boolean expected = true;
        InputDevice id = new InputDevice(System.in);
        OutputDevice od = new OutputDevice(System.out);
        Application app = new Application(id, od);
        boolean actual;
        try {
            actual = app.checkDate(day, month, year);
        } catch (InvalidDateException e) {
            actual = false;
        }
        assertEquals(expected, actual);
    }

   @Test
    void test_checkDate2()
    {
        int day = 33;
        int month = 1;
        int year = 2004;
        boolean expected = false;
        InputDevice id = new InputDevice(System.in);
        OutputDevice od = new OutputDevice(System.out);
        Application app = new Application(id, od);
        boolean actual;
        try {
            actual = app.checkDate(day, month, year);
        } catch (InvalidDateException e) {
            actual = false;
        }
        assertEquals(expected, actual);
    }
    @Test
    void test_checkDate3()
    {
        int day = 29;
        int month = 2;
        int year = 2005;
        boolean expected = false;
        InputDevice id = new InputDevice(System.in);
        OutputDevice od = new OutputDevice(System.out);
        Application app = new Application(id, od);
        boolean actual;
        try {
            actual = app.checkDate(day, month, year);
        } catch (InvalidDateException e) {
            actual = false;
        }
        assertEquals(expected, actual);
    }
    @Test
    void test_checkDate4()
    {
        int day = 24;
        int month = 2;
        int year = 2026;
        boolean expected = false;
        InputDevice id = new InputDevice(System.in);
        OutputDevice od = new OutputDevice(System.out);
        Application app = new Application(id, od);
        boolean actual;
        try {
            actual = app.checkDate(day, month, year);
        } catch (InvalidDateException e) {
            actual = false;
        }
        assertEquals(expected, actual);
    }
    @Test
    void test_checkDate5()
    {
        int day = 24;
        int month = 2;
        int year = -78;
        boolean expected = false;
        InputDevice id = new InputDevice(System.in);
        OutputDevice od = new OutputDevice(System.out);
        Application app = new Application(id, od);
        boolean actual;
        try {
            actual = app.checkDate(day, month, year);
        } catch (InvalidDateException e) {
            actual = false;
        }
        assertEquals(expected, actual);
    }
}
