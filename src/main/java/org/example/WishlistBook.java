package org.example;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class WishlistBook extends Book implements Buyable{
    private boolean bought;
    public WishlistBook(Author author, String isbn ,String title, int year, int pages) {
        super(author, isbn ,title, year, pages);
        bought = false;
    }

    public WishlistBook() {
        this.author = null;
        this.isbn = "0000000000000";
        this.title = "";
        this.year = 0;
        noOfPages = 0;
        bought = false;
    }

  public void setBought(boolean bought)
  {
      this.bought = bought;
  }
    public boolean getBought() {
        return bought;
    }

    @Override
    public boolean isBought() {
        return bought;
    }

    @Override
    public void buy() {
        bought = true;

    }

    @Override
    public String toString() {
        String sp = super.toString();
        String sb =". It is in the Wishlist and has not been yet bought";
        sp= sp.concat(sb);
        return sp;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.author);
        out.writeObject(title);
        out.writeObject(year);
        out.writeObject(noOfPages);
        out.writeObject(bought);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.author = (Author) in.readObject();
        this.title = (String) in.readObject();
        this.year = (int) in.readObject();
        this.noOfPages = (int) in.readObject();
        this.bought = in.readBoolean();
    }
}

