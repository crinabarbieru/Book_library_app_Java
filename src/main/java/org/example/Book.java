package org.example;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Book implements Externalizable, Comparable {
    protected String isbn;
    protected Author author;
    protected String title;
    protected int year;
    protected int noOfPages;


    public Book(Author author, String isbn ,String title, int year, int pages) {
        this.author = author;
        this.isbn = isbn;
        this.title = title;
        this.year = year;
        noOfPages = pages;
    }
    public Book()
    {
        this.author = null;
        this.isbn = "0000000000000";
        this.title = "";
        this.year = 0;
        noOfPages = 0;

    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setNoOfPages(int noOfPages) {
        this.noOfPages = noOfPages;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor(){
        return author;
    }
    public String getTitle()
    {
        return title;
    }


    public int getYear()
    {
        return year;
    }
    public int getNoOfPages(){ return noOfPages;}
    public String bookAuthorName()
    {
        String an = this.getAuthor().getFname() + " " +this.getAuthor().getLname();
        return an;
    }


    @Override
    public String toString() {
        String ta = title+ " by " + author.getFname() + " " + author.getLname();
        String y = ", written in "+year;
        String pg = ", has " + noOfPages + " pages";
        return ta + y + pg;
    }
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.author);
        out.writeObject(title);
        out.writeObject(year);
        out.writeObject(noOfPages);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.author = (Author) in.readObject();
        this.title = (String) in.readObject();
        this.year = (int) in.readObject();
        this.noOfPages = (int) in.readObject();
    }


    @Override
    public int compareTo(Object o) {
        if(o instanceof Book)
        {
           Book bo = (Book) o;
           if(this.title.compareTo(bo.getTitle()) == 0 && this.year == bo.getYear())
               return 0;
           if(this.title.compareTo(bo.getTitle()) > 0)
               return 1;
           return -1;
        }
        return -100;
    }
}

