package org.example;

import java.util.ArrayList;

public class FindAuthorThread extends Thread {
    protected ArrayList<? extends Book> booklist;
    protected Author author;
    protected ArrayList<Book> result;
    public FindAuthorThread(ArrayList<? extends Book> arr, Author auth)
    {
        this.booklist = arr;
        this.author = auth;
        result = new ArrayList<>();
    }

    public Author getAuthor() {
        return author;
    }

    public ArrayList<? extends Book> getResult(){
        return this.result;
    }
    @Override
    public void run() {
        for(Book b: booklist)
        {
            if(b.getAuthor().compareTo(this.author)==0)
                result.add(b);
        }

    }

}
