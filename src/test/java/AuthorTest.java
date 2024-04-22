import org.example.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class AuthorTest {

    @Test
    void test_EmptyConstructor()
    {
        Author a = new Author();
        assertEquals(a.getFname(), "");
        assertEquals(a.getLname(), "");
        assertEquals(a.getYear(), 0);
//       assertEquals(a.getInterests().size(), 0);
    }
    @Test
    void test_ArgsConstructor()
    {
        Author a = new Author(77,"FNAME", "LNAME", 2004);
//        a.addInterest("interest1");
//        a.addInterest("interest2");
//        a.addInterest("interest3");
        assertEquals(a.getId(), 77);
        assertEquals(a.getFname(), "FNAME");
        assertEquals(a.getLname(), "LNAME");
        assertEquals(a.getYear(), 2004);
//        ArrayList<String> expint= new ArrayList<>();
//        expint.add("interest1");
//        expint.add("interest2");
//        expint.add("interest3");
//        assertArrayEquals(a.getInterests().toArray(), expint.toArray());

    }

}
