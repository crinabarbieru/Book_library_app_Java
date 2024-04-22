package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class Bookshelf implements Externalizable{
    private int id;
    private String domain;

    private ArrayList<WishlistBook> wishlistBooks;
    private ArrayList<BoughtBook> availableBooks;

    public Bookshelf(int id, String domain) {
        this.id = id;
        this.domain = domain;
        wishlistBooks = new ArrayList<>();
        availableBooks = new ArrayList<>();
    }

    public Bookshelf() {
        this.id = -1;
        this.domain = "";
        wishlistBooks = new ArrayList<>();
        availableBooks = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getDomain() {
        return domain;
    }


    public ArrayList<WishlistBook> getWishlistBooks() {
        return wishlistBooks;
    }

    public void setWishlistBooks(ArrayList<WishlistBook> wishlistBooks) {
        this.wishlistBooks = wishlistBooks;
    }

    public ArrayList<BoughtBook> getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(ArrayList<BoughtBook> availableBooks) {
        this.availableBooks = availableBooks;
    }

    public void addBook(Book b)
    {
        if(b instanceof WishlistBook)
            wishlistBooks.add((WishlistBook) b);
        else if(b instanceof BoughtBook)
            availableBooks.add((BoughtBook) b);
    }
    public void addBooks(Collection<? extends Book> c)
    {
        for(Book b: c)
            this.addBook(b);
    }

    public HashMap<String, Integer> bookshelfMap()
    {
        HashMap<String, Integer> hmap = new HashMap<>();
        for(WishlistBook wb: wishlistBooks)
        {
            String key = wb.getAuthor().getFname() + " " + wb.getAuthor().getLname();
            if( hmap.containsKey(key))
            {
                int nrbooks = hmap.get(key);
                int result = hmap.replace(key, nrbooks+1);
            }
            else {
                hmap.put(key, 1);
            }
        }
        for(BoughtBook bb: availableBooks)
        {
            String key = bb.getAuthor().getFname() + " " + bb.getAuthor().getLname();
            if( hmap.containsKey(key))
            {
                int nrbooks = hmap.get(key);
                int result = hmap.replace(key, nrbooks+1);
            }
            else {
                hmap.put(key, 1);
            }
        }
        return hmap;
    }

    public void displayBookshelf() {
        System.out.println("\n\t "+domain );
        System.out.println("========================================");
        if(availableBooks.size() == 0)
        {
            System.out.println("There are no available books at the moment :(");
        }
        else {
            System.out.println("\nThe available books are:");
            System.out.println("------------------------");
            for (BoughtBook b : availableBooks) {
                String s = String.format("|\tTitle: %s \n|\tAuthor: %s \n|\tYear: %d \n|\tPages: %d\n", b.getTitle(), b.bookAuthorName(), b.getYear(), b.getNoOfPages());
                System.out.println(s);
            }
        }

        if(wishlistBooks.size()==0)
        {
            System.out.println("There are no books in the wishlist so far :(");
        }
        else {
            System.out.println("\nThe wishlist contains:");
            System.out.println("----------------------");

            for (WishlistBook b : wishlistBooks) {
                String s = String.format("|\tTitle: %s \n|\tAuthor: %s \n|\tYear: %d \n|\tPages: %d\n", b.getTitle(), b.bookAuthorName(), b.getYear(), b.getNoOfPages());
                System.out.println(s);
            }
        }
        System.out.println(" ");
    }

    public void displayProgress() {
        System.out.println("Reading status for Bookshelf of " + domain + ":");
        System.out.println("====================");
        for (BoughtBook b : availableBooks) {
                int i;
                String s = String.format("Title: %s \nAuthor: %s \nPages Read: %d/%d ", b.getTitle(), b.bookAuthorName(), ((Readable) b).getPagesRead(), b.getNoOfPages());
                System.out.println(s);

                System.out.print("[");
                for (i = 0; i < 20; i++) {
                    if (i < 20 * b.getPagesRead() / b.getNoOfPages())
                        System.out.print("|");
                    else System.out.print(" ");
                }
                System.out.print("]\n");
                System.out.println("======================");

        }
    }

    public Book findBookByTitle(String t) throws BookNotFoundException{

        for (Book bk: wishlistBooks) {
            if (bk.getTitle().equals(t)) {
                return bk;
            }
        }
        for (Book bk: availableBooks) {
            if (bk.getTitle().equals(t)) {
                return bk;
            }
        }
       throw new BookNotFoundException("Book "+t+ " is not in this bookshelf!!");
    }
    public void deleteBook(Book b)
    {
        if(b instanceof BoughtBook)
             availableBooks.remove(b);
        else if(b instanceof WishlistBook)
            wishlistBooks.remove(b);

    }
    public ArrayList<Book> findBooksByAuthor(Author a)
    {
        ArrayList<Book> blist = new ArrayList<>();
        FindAuthorThread bbt = new FindAuthorThread(this.availableBooks, a);
        FindAuthorThread wbt = new FindAuthorThread(this.wishlistBooks, a);
        bbt.start();
        wbt.start();
        try {
            bbt.join();
             wbt.join();
             blist.addAll(bbt.getResult());
             blist.addAll(wbt.getResult());
        return blist;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void displayBookList(ArrayList<Book> blist)
    {
        for (Book b : blist) {
            if(b instanceof BoughtBook)
            System.out.println("Available now:");
            else
                System.out.println("In Wishlist:");

            String s = String.format("|\tTitle: %s \n|\tAuthor: %s \n|\tYear: %d \n|\tPages: %d", b.getTitle(), b.bookAuthorName(), b.getYear(), b.getNoOfPages());
            System.out.println(s);

        }
    }
    public boolean hasBook(Book b)
    {
        for(Book bk: wishlistBooks)
            if(b.equals(bk))
                return true;
        for(Book bk: availableBooks)
            if(b.equals(bk))
                return true;
        return false;
    }
    public Author findAuthorByName(String name)
    {
        for(Book b: wishlistBooks)
            if(b.getAuthor().fullName().equals(name))
                return b.getAuthor();
        for(Book b: availableBooks)
            if(b.getAuthor().fullName().equals(name))
                return b.getAuthor();
        return null;
    }
    public boolean hasAuthor(String name)
    {
        HashMap<String, Integer> hmap = this.bookshelfMap();
        Set<String> authorSet;
        authorSet = hmap.keySet();
        for(String s: authorSet)
            if(s.equals(name))
                return true;
        return false;
    }


    public void saveBookshelf(String path)
    {
        try {
        ObjectMapper objectMapper = new ObjectMapper();
        FileOutputStream fout = new FileOutputStream(path);
        OutputDevice od = new OutputDevice(fout);
        String res = objectMapper.writeValueAsString(this);
        od.write(res);
        } catch (JsonProcessingException e) {
            System.out.println("Saving operation has not succeded because " + e.getMessage());

        } catch (FileNotFoundException e) {
            System.out.println("Error opening file. Please enter a valid file path:");
            Scanner scan = new Scanner(System.in);
            String newpath = scan.nextLine();
            loadBookshelf(newpath);
        }

    }

    public static Bookshelf loadBookshelf(String path)
    {
        Bookshelf b;
        FileInputStream fin = null;
        try {
        ObjectMapper objectMapper = new ObjectMapper();
         fin = new FileInputStream(path);
        InputDevice id = new InputDevice(fin);
        String line = id.nextLine();
             b = objectMapper.readValue(line, Bookshelf.class);

        } catch (JsonProcessingException e) {
            System.out.println("Loading operation has not succeded because " + e.getMessage());
            b = null;
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file. Please enter a valid file path:");
            Scanner scan = new Scanner(System.in);
            String newpath = scan.nextLine();
            b = loadBookshelf(newpath);

        }finally
        {
            if( fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
            return b;

    }

    public void sortAlpha() throws InterruptedException {
        SortThread bt = new SortThread(this.availableBooks);
        SortThread wt = new SortThread(this.wishlistBooks);
        bt.start();
        wt.start();
        bt.join();
        wt.join();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(domain);
        out.writeObject(availableBooks);
        out.writeObject(wishlistBooks);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.domain = (String) in.readObject();
        this.availableBooks = (ArrayList<BoughtBook>) in.readObject();
        this.wishlistBooks = (ArrayList<WishlistBook>) in.readObject();
    }


}


