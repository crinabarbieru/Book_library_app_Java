
import org.example.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
public class BookshelfTest {
  private static Bookshelf testbs;
  private static Author a;
  private static Author b;
  private static Author c;
  private static BoughtBook bb1;
  private static BoughtBook bb2;
  private static BoughtBook bb3;
  private static BoughtBook bb4;
  private static WishlistBook wb1;
  private static WishlistBook wb2;
  private static WishlistBook wb3;
  private static WishlistBook wb4;

  @BeforeAll
  public static void initialise_data() {
      testbs = new Bookshelf(123, "TESTDOMAIN");
      a = new Author(100,"AFIRST", "ALAST", 1875);
      b = new Author(200,"BFIRST", "BLAST",  1999);
      c = new Author(300,"CFIRST", "CLAST", 1978);

      bb1 = new BoughtBook(a, "01","A TITLE", 1895, 355);
      bb2 = new BoughtBook(a, "02","A TITLE", 1900, 834);
      bb3 = new BoughtBook(b, "03","B TITLE", 1999, 355);
      bb4 = new BoughtBook(c, "04","XYZ", 1988, 437);
      testbs.addBook(bb3);
      testbs.addBook(bb2);
      testbs.addBook(bb1);

      wb1 = new WishlistBook(a, "001","A WISHTITLE", 1987, 344);
      wb2 = new WishlistBook(b, "002","BDC WISHTITLE", 1966, 84);
      wb3 = new WishlistBook(a,"003","GH WISHTITLE", 1987, 674);
      wb4 = new WishlistBook(c,"004", "YTZX", 1899, 674);
      Book[] barr = new Book[]{wb3, wb1, wb2};
      testbs.addBooks(List.of(barr));
  }

    @Test
    void test_EmptyConstructor()
    {
        Bookshelf emptybs = new Bookshelf();
        assertEquals(-1, emptybs.getId());
        assertEquals(emptybs.getDomain(), "");
        assertEquals(0, emptybs.getAvailableBooks().size());
        assertEquals(0, emptybs.getWishlistBooks().size());
    }
  @Test
    void test_ArgsConstructor()
    {
        assertEquals(123, testbs.getId());
        assertEquals(testbs.getDomain(), "TESTDOMAIN");
        assertArrayEquals(new BoughtBook[]{bb3, bb2, bb1}, testbs.getAvailableBooks().toArray());
        assertArrayEquals(new WishlistBook[]{wb3, wb1, wb2}, testbs.getWishlistBooks().toArray());
    }


    @Test
    void test_sortAlpha1()
    {
        Bookshelf bsort = new Bookshelf();
        bsort.addBooks(List.of(new Book[]{bb3, bb2, bb1, wb3, wb1, wb2}));
        try {
            ArrayList<BoughtBook> bbarr = bsort.getAvailableBooks();
            Collections.sort(bbarr);
            ArrayList<WishlistBook> wbarr = bsort.getWishlistBooks();
            Collections.sort(wbarr);
            bsort.sortAlpha();
            assertArrayEquals(new ArrayList[]{bbarr}, new ArrayList[]{bsort.getAvailableBooks()});
            assertArrayEquals(new ArrayList[]{wbarr}, new ArrayList[]{bsort.getWishlistBooks()});

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void test_sortAlpha2()
    {
        Bookshelf bsort = new Bookshelf();
        bsort.addBooks(List.of(new Book[]{bb3, bb2, wb3, bb1, wb1, wb2, bb4, wb4}));
        try {
            ArrayList<BoughtBook> bbarr = bsort.getAvailableBooks();
            Collections.sort(bbarr);
            ArrayList<WishlistBook> wbarr = bsort.getWishlistBooks();
            Collections.sort(wbarr);
            bsort.sortAlpha();
            assertArrayEquals(new ArrayList[]{bbarr}, new ArrayList[]{bsort.getAvailableBooks()});
            assertArrayEquals(new ArrayList[]{wbarr}, new ArrayList[]{bsort.getWishlistBooks()});

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test_findBooksByAuthorA()
    {

        ArrayList<Book> expected = new ArrayList<>();
        expected.addAll(List.of(new Book[]{bb2, bb1, wb3, wb1}));
        ArrayList<Book> actual =  testbs.findBooksByAuthor(a);
        assertArrayEquals(new ArrayList[]{expected}, new ArrayList[]{actual});
    }
    @Test
    void test_findBooksByAuthorB()
    {

        ArrayList<Book> expected = new ArrayList<>();
        expected.addAll(List.of(new Book[]{bb3, wb2}));
        ArrayList<Book> actual =  testbs.findBooksByAuthor(b);
        assertArrayEquals(new ArrayList[]{expected}, new ArrayList[]{actual});
    }

    @Test
    void test_findBooksByAuthorC()
    {
        Bookshelf bsort = new Bookshelf();
        bsort.addBooks(List.of(new Book[]{bb3, bb2, bb1, wb3, wb1, wb2, bb4, wb4}));
        ArrayList<Book> expected = new ArrayList<>();
        expected.addAll(List.of(new Book[]{bb4, wb4}));
        ArrayList<Book> actual =  bsort.findBooksByAuthor(c);
        assertArrayEquals(new ArrayList[]{expected}, new ArrayList[]{actual});
    }



}
