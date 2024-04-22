package org.example;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BoughtBook extends Book implements Readable{
    private  boolean isRead;
    private int pagesRead;
    public BoughtBook(Author author, String isbn,String title, int year, int pages) {
        super(author, isbn,title, year, pages);
        isRead = false;
        pagesRead = 0;
    }

    public BoughtBook() {
        this.author = null;
        this.title = "";
        this.year = 0;
        noOfPages = 0;
        isRead = false;
        pagesRead = 0;
    }

    public void setPagesRead(int pagesRead) {
        if(pagesRead < this.noOfPages)
        this.pagesRead = pagesRead;
        else
        {
            this.pagesRead = noOfPages;
            isRead = true;
        }

    }

    public void setIsRead(boolean read) {

        isRead = read;
        if(read)
            pagesRead = noOfPages;
    }
    public boolean getIsRead()
    {
        return isRead;
    }

    public void markAsRead()
    {
        pagesRead = this.noOfPages;
        isRead = true;
    }

    public boolean checkIsRead() {
        return (noOfPages == pagesRead);
    }

    @Override
    public void readPages(int pg) {
        int aux = pagesRead;
        pagesRead = aux + pg;
        if(pagesRead >= noOfPages) {
            pagesRead = noOfPages;
            this.markAsRead();
        }
        System.out.println("PAGES READ: "+pagesRead);
    }
    public int getPagesRead() {
        return pagesRead;
    }

    @Override
    public String toString() {
        String sp = super.toString();
        String sb = String.format(". It is available in the Bookshelf, and so far %d pages have been read.", pagesRead);
        sp= sp.concat(sb);
        return sp;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(pagesRead);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        this.pagesRead = (int) in.readObject();
       this.isRead = (boolean) in.readObject();


    }


}

