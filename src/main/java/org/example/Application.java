package org.example;

import com.mysql.cj.exceptions.NumberOutOfRange;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Application {
    private InputDevice id;
    private OutputDevice od;

    public Application(InputDevice id, OutputDevice od) {
        this.id = id;
        this.od = od;
    }

    public void run(Connection conn) {
        od.write("Welcome back to your favorite book management app!! How would you like to continue?");
        od.write("1. As an owner");
        od.write("2. As a friend");
        od.write("Your option(1/2):");
        String option = id.nextLine();
        while(option.compareTo("1")!=0 && option.compareTo("2")!=0)
        {
            od.write("Please select a valid option(1/2): ");
            option = id.nextLine();
        }
        try {
            if(option.compareTo("1")==0)
                ownerMode(conn);
            else
                friendMode(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBookshelves(Connection conn, ArrayList<Bookshelf> bookshelves) throws SQLException {
        /* =============================================== */
        /* LOAD BOOKSHELVES FROM DATABASE  */
        od.write("Loading bookshelves...");
        String query = " select count(*) from bookshelves; ";
        Statement stmt = conn.createStatement();
       ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int noOfBookshelves = rs.getInt(1);
        od.write("Number of bookshelves found: " + noOfBookshelves);

        /* LOAD BOOKSHELVES FROM DATABASE  */
        query = " select * from bookshelves; ";
        rs = stmt.executeQuery(query);
//         bookshelves= new ArrayList<>();
        Bookshelf bs;
        for(int i=0;i<noOfBookshelves;i++) {
            rs.next();
            int id = rs.getInt(1);
            String name = rs.getString(2);
            bs = new Bookshelf(id, name);
            bookshelves.add(bs);
        }

        /* ============================================== */
        /* GET ISBN FOR AVAILABLE & WISHLIST BOOKS */
        String pqueryav = "Select isbn, pagesRead from available_books;";
        String pquerywl = "Select isbn from wishlist;";
        Statement booktype = conn.createStatement();

        //create array of isbn for available (bought) books
        ArrayList<String> bbooks = new ArrayList<>();
        ArrayList<Integer> pgread = new ArrayList<>();
        ResultSet rsbtype = booktype.executeQuery(pqueryav);

        while(rsbtype.next())
        {
            bbooks.add(rsbtype.getString(1));
            pgread.add(rsbtype.getInt(2));

        }

        //create array of isbn for wishlist books
        ArrayList<String> wbooks = new ArrayList<>();
        rsbtype = booktype.executeQuery(pquerywl);
        while(rsbtype.next())
        {
            wbooks.add(rsbtype.getString(1));
        }

        /* =============================================== */
        /* LOAD BOOKS FOR EACH BOOKSHELF  */
        String pquery = "Select * from books where isbn in (select isbn from books_bookshelves where bookshelf_id = ?);";
        PreparedStatement pstmt =conn.prepareStatement(pquery);
        String pqAuth = "Select * from authors where id = ?;";
        PreparedStatement psauth =conn.prepareStatement(pqAuth);
        for(int i=0;i<noOfBookshelves;i++)  //FOR EACH BOOKSHELF
        {
            int id = bookshelves.get(i).getId();  //GET BOOKSHELF ID
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();  // SELECT ALL THE BOOKS IN THIS BOOKSHELF
            while(rs.next())
            {
                //GET DATA FOR EACH BOOK
                String isbn = rs.getString(1);
                int authorid = rs.getInt(2);
                // GET AUTHOR WITH THIS ID
                psauth.setInt(1, authorid);
                String fname, lname;
                int authyear;
                Author author;
                ResultSet rsAuth = psauth.executeQuery();
                //CREATE AUTHOR OBJECT
                if(rsAuth.next())
                {
                    fname = rsAuth.getString(2);
                    lname = rsAuth.getString(3);
                    authyear = rsAuth.getInt(4);
                    author = new Author(authorid,fname, lname, authyear);
                }
                else
                {
                    od.write("Could not find author with id = "+authorid);
                    author = new Author();
                }
                String title = rs.getString(3);
                int year = rs.getInt(4);
                int pages= rs.getInt(5);
                //CREATE BOUGHTBOOK
                if(bbooks.contains(isbn))
                {
                    BoughtBook bb = new BoughtBook(author, isbn ,title, year, pages);

                    bb.setPagesRead(pgread.get(bbooks.indexOf(isbn)));

                    if(bb.getPagesRead() == pages)
                        bb.setIsRead(true);
                    bookshelves.get(i).addBook(bb);
                }
                //CREATE WISHLIST BOOK
                else if (wbooks.contains(isbn))
                {
                    WishlistBook wb = new WishlistBook(author, isbn ,title, year, pages);
                    bookshelves.get(i).addBook(wb);
                }
            }
        }
    }
    public void friendMode(Connection conn) throws SQLException {
    String query = "select password from users where username=?;";
    PreparedStatement stmt = conn.prepareStatement(query);
    od.write("Please enter your username: ");
    String user = id.nextLine();
    user = user.trim();
    String[] words = user.split(" ");
    user = 0 < words.length ? words[0]: "" ;
    stmt.setString(1, user);
    ResultSet rs = stmt.executeQuery();
    while(!rs.next())
    {
        od.write("This user does not exist. Remember, the username can only contain letters, digits and underscore!!");
        od.write("Please enter a valid username:");
        user = id.nextLine();
        user = user.trim();
       words = user.split(" ");
        user = 0 < words.length ? words[0]: "" ;
        stmt.setString(1, user);
      rs = stmt.executeQuery();
    }
    String actual = rs.getString(1);
    od.write("Please enter your password: ");
    String pass = id.nextLine();
    while(pass.compareTo(actual)!=0)
    {
        od.write("Incorrect password. Please try again: ");
        pass = id.nextLine();
    }
    od.write("Connection successful!!");
    ArrayList<Bookshelf> bookshelves = new ArrayList<>();
    loadBookshelves(conn, bookshelves);

        int option = -1;
        while(option !=0)
        {
            printBookshelvesMenu();
            od.write("4.Send a suggestion for your friend");
            od.write("Your option: ");
            String optionString = id.nextLine();
            try
            {
                option = Integer.parseInt(optionString);
                if(option > 4)
                    throw new NumberOutOfRange("Option out of range!");
                switch (option) {
                    case 0:
                        od.write("Exiting application....");
                        option = 0;
                        break;
                    case 1:
                        printAvailableBookshelves(bookshelves);
                        break;
                    case 2:
                        printBooksinBookshelves(bookshelves);
                        break;
                    case 3:
                        printAvailableBookshelves(bookshelves);
                        od.write("Choose a bookshelf: ");
                        String bks = id.nextLine();
                        int ok=-1;
                        while(ok==-1) {
                            try {
                                ok = Integer.parseInt(bks);
                                if(ok > bookshelves.size()+1) {
                                    ok=-1;
                                    throw new NumberOutOfRange("Number out of range!!");
                                }
                                operationsBookshelfFriend(conn, bookshelves.get(ok-1));

                            } catch (NumberFormatException | NumberOutOfRange e) {
                                od.write("! INVALID INPUT ! PLEASE CHOOSE A NUMBER !");
                            }
                        }
                        break;
                    case 4:
                        od.write("What book would you like to recommend to your friend?");
                        od.write("ISBN:");
                        String isbn = id.nextLine();
                        while (isbn.length() != 13 || !isbn.matches("\\d{13}")) {
                            od.write("Please enter a 13-digit ISBN!");
                            isbn = id.nextLine();
                        }
                        String title, fname="", lname="", answer="";
                        while(true) {
                            od.write("Title:");
                           title = id.nextLine();
                            title = title.strip();
                           if(title.length()>0)
                               break;
                           else
                               od.write("Please input a valid title!");
                        }

                        while(true) {
                            od.write("Author's First Name:");
                         answer = id.nextLine();
                         answer=answer.strip();
                         if(!answer.isEmpty()) {
                             fname = answer.split(" ")[0];
                             if(fname.length()>0)
                                 break;
                             od.write("Please input a valid first name!");
                         }
                      break;
                        }
                        while(true) {
                            od.write("Author's Last Name:");
                            answer = id.nextLine();
                            answer=answer.strip();
                            if(!answer.isEmpty()) {
                                lname = answer.split(" ")[0];
                                if (!lname.isEmpty())
                                    break;
                                od.write("Please input a valid first name!");
                            }
                        }

                        boolean inBooks = checkBookinTable(conn, isbn, "suggestions");
                        if(inBooks)
                        {
                            od.write("This book has already been recommended :)");
                        }
                        else{
                            String query2 = "insert into suggestions values (? , ?, ?, ?, ?);";
                            PreparedStatement pstmt = conn.prepareStatement(query2);
                            pstmt.setString(1, user);
                            pstmt.setString(2, isbn);
                            pstmt.setString(3, title);
                            pstmt.setString(4, fname);
                            pstmt.setString(5, lname);
                            pstmt.execute();
                            od.write("We succesfully sent a suggestion to your friend!");

                        }
                        break;
                }
            }catch (NumberFormatException | NumberOutOfRange e)
            {
                od.write("! INVALID INPUT ! PLEASE CHOOSE A NUMBER (0-3) !");
            }

        }

}
public void ownerMode(Connection conn) throws SQLException {

        /* CHECK PASSWORD FOR OWNER */
    od.write("Please enter your password: ");
    String pass = id.nextLine();
    String query = "select password from users where username='owner';";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    rs.next();
    String actual = rs.getString(1);
    while(actual.compareTo(pass)!=0)
    {
        od.write("Wrong password, please enter the correct one: ");
        pass = id.nextLine();
    }
    od.write("Connection successful!!");
    
    /*  LOAD BOOKSHELVES FROM DB */
    ArrayList<Bookshelf> bookshelves = new ArrayList<>();
    loadBookshelves(conn, bookshelves);

    /*  USER OPTIONS  */
    int option = -1;
    while(option !=0)
    {
        printBookshelvesMenu();
        od.write("4.Check out suggestions");
        od.write("Your option: ");
        String optionString = id.nextLine();
        try
        {
            option = Integer.parseInt(optionString);
            if(option > 4)
                throw new NumberOutOfRange("Option out of range!");
            switch (option) {
                case 0:
                    od.write("Exiting application....");
                    option = 0;
                    break;
                case 1:
                    printAvailableBookshelves(bookshelves);
                    break;
                case 2:
                    printBooksinBookshelves(bookshelves);
                    break;
                case 3:
                    printAvailableBookshelves(bookshelves);
                    od.write("Choose a bookshelf: ");
                    String bks = id.nextLine();
                    int ok=-1;
                    while(ok==-1) {
                        try {
                            ok = Integer.parseInt(bks);
                            if(ok > bookshelves.size()+1) {
                                ok=-1;
                                throw new NumberOutOfRange("Number out of range!!");
                            }
                    operationsBookshelfOwner(conn, bookshelves.get(ok-1));

                        } catch (NumberFormatException | NumberOutOfRange e) {
                            od.write("! INVALID INPUT ! PLEASE CHOOSE A NUMBER !");
                        }
                    }
                    break;
                case 4:
                    String sg = "select * from suggestions;";
                    stmt = conn.createStatement();
                     rs = stmt.executeQuery(sg);
                     if(!rs.next())
                         od.write("There are no suggestions :(");
                     else{
                         String user = rs.getString(1);
                         String isbn = rs.getString(2);
                         String title = rs.getString(3);
                         String fname = rs.getString(4);
                         String lname=rs.getString(5);
                         od.write(String.format("User %s has suggested %s written by %s %s (ISBN: %S).", user, title, fname, lname, isbn));
                         od.write("If you would like to keep this suggestion saved, press Y.");
                         od.write("If you would like to discard this suggestion, press N.");
                         while(true)
                         {
                             od.write("Your option:");
                             String answer = id.nextLine();
                             if(answer.equals("Y"))
                             {
                                 od.write("Recommendation will be kept");
                                 break;
                             }
                             if(answer.equals("N"))
                             {
                                 sg = "delete from suggestions where isbn = "+isbn+";";
                                 stmt.execute(sg);
                                 od.write("Recommendation has been discarded");
                                 break;
                             }
                             od.write("Please select a valid option(Y/N)");

                         }
                     }
                     od.write("\n=== These were all the recommendations for today ===\n");
                    break;
            }
        }catch (NumberFormatException | NumberOutOfRange e)
        {
            od.write("! INVALID INPUT ! PLEASE CHOOSE A NUMBER (0-3) !");
        }
    }
}

    public void printAvailableBookshelves(ArrayList<Bookshelf> bookshelves)
    {
        for(int i=0;i<bookshelves.size();i++) {
            od.write((i + 1) + "." + bookshelves.get(i).getDomain());
        }
    }
    public void printBooksinBookshelves(ArrayList<Bookshelf> bookshelves)
    {
        for(int i=0;i<bookshelves.size();i++) {
            //od.write((i + 1) + "." + bookshelves.get(i).getDomain()+" contains the following books:");
            bookshelves.get(i).displayBookshelf();
        }
    }
    public void printBookshelvesMenu()
    {
        od.write("---------------------------------------");
        od.write("Please choose one of the options below:");
        od.write("0.Exit application");
        od.write("1.Display available bookshelves");
        od.write("2.Display all books from available bookshelves");
        od.write("3.Choose a bookshelf to see more details");
    }
    public void printOwnerMenu() {
        od.write("Please choose one of the options below:");
        od.write("0.Go back to available bookshelves");
        od.write("1.Display current bookshelf");
        od.write("2.Display reading progress");
        od.write("3.Search a book by title");
        od.write("4.Search for an author by name and display their books");
        od.write("5.Modify bookshelf");
        od.write("6.Sort books alphabetically");
    }
    public void printFriendMenu() {
        od.write("Please choose one of the options below:");
        od.write("0.Go back to available bookshelves");
        od.write("1.Display current bookshelf");
        od.write("2.Display reading progress");
        od.write("3.Search a book by title");
        od.write("4.Search for an author by name and display their books");
    }
    public void printBookshelfModifyMenu() {
        od.write("0.None. Go back.");
        od.write("1.Add a book");
        od.write("2.Delete a book");
        od.write("3.Read a book");
//        od.write("4.Buy a book from Wishlist");

    }

    public boolean checkBookinTable(Connection conn, String isbn, String tableName) throws SQLException {
        boolean result = false;
        String query = "select count(*) from " + tableName +" where isbn = ?;";
        PreparedStatement stmt =conn.prepareStatement(query);
        stmt.setString(1, isbn);
        ResultSet  rs = stmt.executeQuery();
        if(rs.next() && rs.getInt(1)>0)
            result = true;
        return result;
    }
    public void ModifyBookshelf(Connection conn, Bookshelf bs) {
        od.write("What operation do you want to perform?");
        this.printBookshelfModifyMenu();
        boolean validInput2 = false;
        while (!validInput2) {
            try {
                int opt = Integer.parseInt(id.nextLine());
                if (opt == 0) {
                    validInput2 = true;
                    break;
                } else if (opt == 1) {
                    int aday = 0, ayear = 0, amonth = 0;
                    od.write("Lets start with the author!");

                    od.write("First name:");
                    String fname = id.nextLine();
                    od.write("Last name");
                    String lname = id.nextLine();
                    od.write("Date of birth (YYYY-MM-DD):");
                    boolean validDob = false;
                    while (!validDob) {
                        try {
                            String dob = id.nextLine();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = dateFormat.parse(dob);

                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                            int year = Integer.parseInt(yearFormat.format(date));
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                            int month = Integer.parseInt(monthFormat.format(date));
                            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                            int day = Integer.parseInt(dayFormat.format(date));
                            boolean validDate = this.checkDate(day, month, year);
                            if (validDate) {
                                aday = day;
                                ayear = year;
                                amonth = month;
                                validDob = true;
                            }


                        } catch (ParseException e) {
                            od.write("Error: Invalid date format. Please enter the date in the format YYYY-MM-DD.");

                        } catch (InvalidDateException e) {
                            od.write("Invalid date: " + e.getMessage());
                            od.write("Please enter a valid date!");
                        }
                    }
                    Author a = new Author(0, fname, lname, ayear);
                    /* CHECK IF AUTHOR IS IN DATABASE ALREADY */
                    String checkAuth = "select id from authors where fname=? and lname=? and year=?;";
                    PreparedStatement psAuth = conn.prepareStatement(checkAuth);
                    psAuth.setString(1, fname);
                    psAuth.setString(2, lname);
                    psAuth.setInt(3, ayear);
                    ResultSet rs = psAuth.executeQuery();
                    /* CHECK IF AUTHOR IS IN DATABASE ALREADY */
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        a.setId(id);
                    } else {
                        String insAuth = "insert into authors(id, fname, lname, year) values (null, ?, ?, ?);";
                        PreparedStatement insertAuth = conn.prepareStatement(insAuth);
                        insertAuth.setString(1, fname);
                        insertAuth.setString(2, lname);
                        insertAuth.setInt(3, ayear);
                        insertAuth.execute();
                        rs = psAuth.executeQuery();
                        rs.next();
                        int id = rs.getInt(1);
                        a.setId(id);
                    }

                    od.write("Lets continue with the book!");
                    od.write("ISBN:");
                    String isbn = id.nextLine();
                    while (isbn.length() != 13 || !isbn.matches("\\d{13}")) {
                        od.write("Please enter a 13-digit ISBN!");
                        isbn = id.nextLine();
                    }
                    boolean inBooks = checkBookinTable(conn, isbn, "books");
                    int byear = 0, bpages = 0;
                    String btitle = "";
                    if (inBooks) {
                        od.write("Hey, this book is already in our database! No need to tell us more, we got it :)");
                        String query = "select title, year, pages from books where isbn = "+isbn+";";
                        Statement stmt = conn.createStatement();
                        ResultSet rsgetbook = stmt.executeQuery(query);
                        rsgetbook.next();
                        btitle=rsgetbook.getString(1);
                        byear = rsgetbook.getInt(2);
                        bpages = rsgetbook.getInt(3);

                        boolean inbb = checkBookinTable(conn, isbn, "available_books");
                        if(inbb)
                        {
                            od.write("Great news: this book has already been purchased!");
                            query = "select pagesRead from available_books where isbn = "+isbn+";";
                            stmt = conn.createStatement();
                            rsgetbook = stmt.executeQuery(query);
                            rsgetbook.next();
                            od.write("Reading progress: "+rsgetbook.getInt(1)+"/"+bpages);
                        }

                    } else{
                        od.write("Title:");
                        btitle = id.nextLine();
                        boolean validInput3 = false;
                        while (validInput3) {
                            try {
                                od.write("Year it was published:");
                                byear = Integer.parseInt(id.nextLine());
                                od.write("Number of pages:");
                                bpages = Integer.parseInt(id.nextLine());
                                validInput3 = true;
                            } catch (NumberFormatException e) {
                                od.write("! INVALID INPUT ! PLEASE ENTER NUMBERS !");
                            }
                        }

                        String qinsertBook = "insert into books values (?, ?, ?, ?, ?);";
                        PreparedStatement insertBook = conn.prepareStatement(qinsertBook);
                        insertBook.setString(1, isbn);
                        insertBook.setInt(2, a.getId());
                        insertBook.setString(3, btitle);
                        insertBook.setInt(4, byear);
                        insertBook.setInt(5, bpages);
                        insertBook.execute();

                        od.write("Is it on your Wishlist or have you already Bought it?(W/B)");
                        String btype = id.nextLine();
                        if (btype.equals("W")) {
                            WishlistBook wb = new WishlistBook(a, isbn, btitle, byear, bpages);
                            bs.addBook(wb);
                            String insertwb = "insert into wishlist values (" + isbn + ", 0);";
                            Statement stmt = conn.createStatement();
                            stmt.execute(insertwb);
                        } else if (btype.equals("B")) {
                            BoughtBook bb = new BoughtBook(a, isbn, btitle, byear, bpages);
                            od.write("How many pages have you read so far?");
                            int rpages = Integer.parseInt(id.nextLine());
                            bb.setPagesRead(rpages);
                            int isrd = bb.getIsRead() ? 1 : 0;
                            bs.addBook(bb);
                            String insertwb = "insert into available_books values (" + isbn + "," + rpages + "," + isrd + ");";
                            Statement stmt = conn.createStatement();
                            stmt.execute(insertwb);
                        }
                    }
                    String check = "select bookshelf_id, isbn from books_bookshelves where bookshelf_id=? and isbn = ?;";
                    PreparedStatement insertBook = conn.prepareStatement(check);
                    insertBook.setInt(1, bs.getId());
                    insertBook.setString(2, isbn);
                    rs = insertBook.executeQuery();
                    if (rs.next()) {
                        od.write("This book is already in current bookshelf!");
                    } else {
                        String insb2 = "insert into books_bookshelves values(" + bs.getId() + ", " + isbn + ");";
                        Statement stmt = conn.createStatement();
                        stmt.execute(insb2);
                        od.write("Book has been added successfully!");
                    }
                    validInput2 = true;


                } else if (opt == 2) {
                    od.write("Please enter the title of the book");
                    String deltitle = id.nextLine();
                    try {
                        Book b = bs.findBookByTitle(deltitle);
                        od.write("Book found in bookshelf!\n" + b);
                        String isbn = b.getIsbn();
                        od.write("Deleting the book...");
                        bs.deleteBook(b);
                        String qbooks = "delete from books_bookshelves where isbn = " + b.getIsbn() + " and bookshelf_id=" +bs.getId() + ";";
                        Statement delbooks = conn.createStatement();
                        delbooks.execute(qbooks);
                        od.write("Book has been deleted successfully from "+bs.getDomain());
                        od.write("Would you like to delete this book from the database as well?(Y/N)");
                        String input = id.nextLine();
                        while(!Objects.equals(input, "Y") && !Objects.equals(input, "N") )
                        {
                            od.write("! INVALID INPUT please choose one of the options above (Y/N) !");
                            input = id.nextLine();
                        }
                        if(Objects.equals(input, "Y"))
                        {
                            String query = "delete from available_books where isbn = " + isbn + ";";
                            Statement st = conn.createStatement();
                            st.execute(query);
                            query = "delete from wishlist where isbn = " + isbn + ";";
                            st.execute(query);
                            query = "delete from books where isbn = " + isbn + ";";
                            st.execute(query);
                            od.write("Book deleted from database!");
                        }
                        else
                        {
                            od.write("Book will remain in the database :)");
                        }

                    } catch (BookNotFoundException e) {
                        od.write(e.getMessage());
                    }
                    validInput2 = true;
                } else if (opt == 3) {
                    od.write("Please enter the title of the book");
                    String deltitle = id.nextLine();
                    try {
                        Book b = bs.findBookByTitle(deltitle);
                        od.write("Book found in bookshelf!\n" + b);
                        if (b instanceof BoughtBook) {
                            od.write("How many pages would you like to read?");
                            while(true)
                            {
                                try {
                                 String ans = id.nextLine();
                                    int pg = Integer.parseInt(ans);
                                    if(pg<0)
                                        throw new NumberOutOfRange("I'm pretty sure you can only read a positive amount of pages. Try again:)");
                                    ((BoughtBook) b).readPages(pg);
                                    String q = "update available_books set pagesRead="+((BoughtBook) b).getPagesRead()+" , isRead = "+((BoughtBook) b).getIsRead() +" where isbn = " + b.getIsbn()+";";
                                    Statement st =conn.createStatement();
                                    st.execute(q);
                                    od.write(String.format("Congrats! Now you have read %d out of %d pages", ((BoughtBook) b).getPagesRead(), b.noOfPages));
                                   break;
                                }catch(NumberFormatException e)
                                {
                                    od.write("Incorrect number format. Please input a number");

                                }catch (NumberOutOfRange e)
                                {
                                    od.write(e.getMessage());

                                }
                            }
                            od.write("end of while");

                        } else {
                            od.write("Sorry, you cannot read this book since it's only on your wishlist :(");
                        }
                    } catch (BookNotFoundException e) {
                        od.write(e.getMessage());

                    }
                    validInput2 = true;
                }


            } catch (NumberFormatException e) {
                od.write("Invalid option format " + e.getMessage());
                od.write("Please enter a valid option from the list above.");
            }catch (SQLException e)
            {
                od.write("Sorry, could not perform operations in DB: " + e.getMessage());
            }

        }
    }

    public void operationsBookshelfOwner(Connection conn, Bookshelf bs)
    {
        od.write("You have chosen " + bs.getDomain());
        int option = -1;
        while(true)
        {
            printOwnerMenu();
            od.write("Your option:");
            String optionString = id.nextLine();
            try
            {
                option = Integer.parseInt(optionString);
                if(option >= 7)
                    throw new NumberOutOfRange("Option out of range!");
                switch (option) {
                    case 0:
                        return;
                    case 1:
                        bs.displayBookshelf();
                        break;
                    case 2:
                        bs.displayProgress();
                        break;
                    case 3:
                        od.write("Please enter the title of the book");
                        String title = id.nextLine();
                        try {
                            Book b = bs.findBookByTitle(title);
                            od.write("Book found in bookshelf!\n" + b);
                        } catch (BookNotFoundException e) {
                            od.write(e.getMessage());
                        }
                        break;
                    case 4:
                        od.write("What is the author's name?");
                        String name = id.nextLine();
                        if (bs.hasAuthor(name)) {
                            Author auth = bs.findAuthorByName(name);
                            od.write("Author found!\n" + auth + "\nWould you like to display their books?(Y/N)");
                            boolean validInput = false;
                            while (!validInput) {
                                String response = id.nextLine();
                                if (response.equals("Y")) {
                                    bs.displayBookList(bs.findBooksByAuthor(auth));
                                    validInput = true;
                                } else if (response.equals("N")) {
                                    validInput = true;
                                } else if (response.equals("-1")) {
                                    validInput = true;
                                } else {
                                    od.write("Please enter a valid response!");
                                    od.write("If you want to exit, press -1");
                                }
                            }
                        } else od.write("Unfortunately, author " + name + " is not in this Bookshelf :(");
                        break;
                    case 5:
                        ModifyBookshelf(conn, bs);
                        break;
                    case 6:
                        try {
                            bs.sortAlpha();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        bs.displayBookshelf();
                        break;

                }

            }catch (NumberFormatException | NumberOutOfRange e)
            {
                od.write("! INVALID INPUT ! PLEASE CHOOSE A NUMBER (0-3) !");
            }
        }
    }

    public void operationsBookshelfFriend(Connection conn, Bookshelf bs)
    {
        od.write("You have chosen " + bs.getDomain());
        int option = -1;
        while(true)
        {
            printFriendMenu();
            od.write("Your option:");
            String optionString = id.nextLine();
            try
            {
                option = Integer.parseInt(optionString);
                if(option >= 7)
                    throw new NumberOutOfRange("Option out of range!");
                switch (option) {
                    case 0:
                        return;
                    case 1:
                        bs.displayBookshelf();
                        break;
                    case 2:
                        bs.displayProgress();
                        break;
                    case 3:
                        od.write("Please enter the title of the book");
                        String title = id.nextLine();
                        try {
                            Book b = bs.findBookByTitle(title);
                            od.write("Book found in bookshelf!\n" + b);
                        } catch (BookNotFoundException e) {
                            od.write(e.getMessage());
                        }
                        break;
                    case 4:
                        od.write("What is the author's name?");
                        String name = id.nextLine();
                        if (bs.hasAuthor(name)) {
                            Author auth = bs.findAuthorByName(name);
                            od.write("Author found!\n" + auth + "\nWould you like to display their books?(Y/N)");
                            boolean validInput = false;
                            while (!validInput) {
                                String response = id.nextLine();
                                if (response.equals("Y")) {
                                    bs.displayBookList(bs.findBooksByAuthor(auth));
                                    validInput = true;
                                } else if (response.equals("N")) {
                                    validInput = true;
                                } else if (response.equals("-1")) {
                                    validInput = true;
                                } else {
                                    od.write("Please enter a valid response!");
                                    od.write("If you want to exit, press -1");
                                }
                            }
                        } else od.write("Unfortunately, author " + name + " is not in this Bookshelf :(");
                        break;
                    case 5:
                        //ModifyBookshelf(conn, bs);
                        break;
                }

            }catch (NumberFormatException | NumberOutOfRange e)
            {
                od.write("! INVALID INPUT ! PLEASE CHOOSE A NUMBER (0-3) !");
            }
        }
    }

    
    public boolean checkDate(int day, int month, int year) throws InvalidDateException {
        boolean valid = true;
        if (year < 0 || year > LocalDate.now().getYear()) {
            throw new InvalidDateException("Invalid year - negative or more than current year");

        }
        if (month < 1 || month > 12) {
            throw new InvalidDateException("Invalid month - not bewteen 1 and 12");
        }
        if (day < 1 || day > 31) {
            throw new InvalidDateException("Invalid day - not between 1 and 31");
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                break;
            case 2:
                if (day > 29) {
                    throw new InvalidDateException("Invalid day - cannot be more than 29 in february");
                } else if (day == 29 && year % 4 != 0)
                    throw new InvalidDateException("Invalid day - 29th february in non bisect year");
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (day > 30)
                    throw new InvalidDateException("Invalid day - cannot be more than 30 in chosen month");
                break;


        }

        return valid;
    }

}