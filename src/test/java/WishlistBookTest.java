import org.example.Author;
import org.example.WishlistBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WishlistBookTest {
    @Test
    void test_EmptyConstructor()
    {
        WishlistBook wb = new WishlistBook();
        assertNull(wb.getAuthor());
        assertEquals(wb.getTitle(), "");
        assertEquals(wb.getYear(), 0);
        assertEquals(wb.getNoOfPages(), 0);
        assertFalse(wb.getBought());

    }
    @Test
    void test_ArgsConstructor()
    {
        Author a = new Author(100,"FNAME", "LNAME", 2004);
        WishlistBook wb = new WishlistBook(a, "0000","TITLE", 1994, 455);
       assertFalse(wb.getBought());
    }
}
