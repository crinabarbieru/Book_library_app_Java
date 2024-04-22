package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) throws SQLException {
        String user = "root";
        String pass = "";
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/book_project_p3", user, pass);
        System.out.println("Connection successful");

        InputDevice cin = new InputDevice(System.in);
        OutputDevice cout = new OutputDevice(System.out);
        Application capp = new Application(cin, cout);
        capp.run(conn);
    }
}