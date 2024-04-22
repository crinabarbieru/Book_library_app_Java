package org.example;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;

public class Author implements Externalizable, Comparable {
    private String fname, lname;
    private int id;
    private int year;

    public Author()
    {
        this.fname = "";
        this.lname = "";
        this.year = 0;
        this.id = 0;
    }
    public Author(int id, String fname, String lname, int year) {
        this.fname = fname;
        this.lname = lname;
        this.year = year;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //getters
    public String getFname(){
        return fname;
    }
    public String getLname()
    {
        return lname;
    }
    public String fullName()
    {
        return fname + " " + lname;
    }
    public int getYear()
    {
        return year;
    }

    // SETTERS
    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        String s1 = String.format(" %d", year);
        String s2 = "";
        return fname + " " +lname+s1+s2;
    }
    public String authorInfo()
    {
        String info = this.toString();
        String s1 = String.format(" was born in %d.", year);
        return info+s1;
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.fname);
        out.writeObject(this.lname);
        out.writeObject(this.year);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.fname= (String) in.readObject();
        this.lname= (String) in.readObject();
        this.year= (int) in.readObject();
    }


    @Override
    public int compareTo(Object o) {
        if(o instanceof Author)
         {
            if(this.fname.equals(((Author) o).getFname()) && this.lname.equals(((Author) o).getLname()) && this.year==((Author) o).getYear())
                return 0;

        }
        return -1;
    }
}

