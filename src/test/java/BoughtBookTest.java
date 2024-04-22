import org.example.Author;
import org.example.Book;
import org.example.BoughtBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoughtBookTest {
    @Test
    void test_EmptyConstructor()
    {
        BoughtBook bb = new BoughtBook();
        assertNull(bb.getAuthor());
        assertEquals(bb.getTitle(), "");
        assertEquals(bb.getYear(), 0);
        assertEquals(bb.getNoOfPages(), 0);
        assertFalse(bb.getIsRead());
        assertEquals(bb.getPagesRead(), 0);
    }
    @Test
    void test_ArgsConstructor()
    {
        Author a = new Author(100,"FNAME", "LNAME",  2004);
        BoughtBook bb = new BoughtBook(a, "0000","TITLE", 1994, 455);
        assertFalse(bb.getIsRead());
        assertEquals(bb.getPagesRead(), 0);
    }
}
