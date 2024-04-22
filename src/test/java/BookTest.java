import org.example.Author;
import org.example.Book;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
public class BookTest {

    @Test
    void test_EmptyConstructor()
    {
        Book b = new Book();
        assertNull(b.getAuthor());
        assertEquals(b.getIsbn(), "0000000000000");
        assertEquals(b.getTitle(), "");
        assertEquals(b.getYear(), 0);
        assertEquals(b.getNoOfPages(), 0);
    }

    @Test
    void test_ArgsConstructor()
    {
        Author a = new Author(100,"FNAME", "LNAME",  2004);
        Book b = new Book(a, "0000","TESTTITLE", 1984, 500);
        assertEquals(b.getIsbn(), "0000");
        assertEquals(b.getTitle(), "TESTTITLE");
        assertEquals(b.getYear(), 1984);
        assertEquals(b.getNoOfPages(), 500);
    }

}
