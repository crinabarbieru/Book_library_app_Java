package org.example;

import java.util.ArrayList;
import java.util.Collections;

public class SortThread extends Thread {
    protected ArrayList<? extends Book> booklist;
    public SortThread(ArrayList<? extends Book> arr)
    {
        this.booklist = arr;
    }

    @Override
    public void run()
    {
        int len = booklist.size();
        for(int i=0;i<len-1;i++)
            for(int j=i+1;j<len;j++)
            {
                if(booklist.get(i).getTitle().compareTo(booklist.get(j).getTitle())>0 || (booklist.get(i).getTitle().compareTo(booklist.get(j).getTitle())>0 && booklist.get(i).getYear()>booklist.get(j).getYear()))
                    Collections.swap(booklist, i, j);
            }
    }
}
