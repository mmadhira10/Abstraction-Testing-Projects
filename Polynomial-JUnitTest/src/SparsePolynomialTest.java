import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SparsePolynomialTest {

    @Test
    void degree()
    {
        Polynomial d1 = new SparsePolynomial("-x^2 - 12");
        assertEquals(2, d1.degree());

        Polynomial zero = new SparsePolynomial("0");
        assertEquals(0, zero.degree());

        Polynomial oneTerm = new SparsePolynomial("5x^7");
        assertEquals(7, oneTerm.degree());

        Polynomial neg1 = new SparsePolynomial("5x^6 - 6x^-3");
        assertEquals(6, neg1.degree());

        Polynomial neg2 = new SparsePolynomial("6x^-3");
        assertEquals(-3, neg2.degree());
    }

    @Test
    void getCoefficient()
    {
        Polynomial d1 = new SparsePolynomial("2x^3 + 3x + 1 - 3x^-4");
        assertEquals(2, d1.getCoefficient(3));
        assertEquals(3, d1.getCoefficient(1));
        assertEquals(1,d1.getCoefficient(0));
        assertEquals(-3, d1.getCoefficient(-4));
        assertThrows(IllegalArgumentException.class,
                () -> d1.getCoefficient(4));
        assertThrows(IllegalArgumentException.class,
                () -> d1.getCoefficient(-1));
    }

    @Test
    void isZero()
    {
        Polynomial d1 = new SparsePolynomial("2x^3 + 3x + 1");
        assertFalse(d1.isZero());

        Polynomial zero = new SparsePolynomial("0");
        assertTrue(zero.isZero());

        Polynomial one = new SparsePolynomial("1");
        assertFalse(one.isZero());
    }

    @Test
    void add()
    {
        Polynomial c1 = new SparsePolynomial("5");
        Polynomial c2 = new SparsePolynomial("-10");
        assertEquals(new SparsePolynomial("-5"), c1.add(c2));

        Polynomial c3 = new DensePolynomial("-10");
        assertEquals(new SparsePolynomial("-5"), c1.add(c3));

        Polynomial d1 = new SparsePolynomial("-x^2 - 12");
        Polynomial d2 = new SparsePolynomial("x^2 - 9");
        assertEquals(new SparsePolynomial("-21"), d1.add(d2) );

        Polynomial s1 = new DensePolynomial("3x^3 + 3x + 1");
        assertEquals(new SparsePolynomial("3x^3 - x^2 + 3x - 11"), d1.add(s1));

        Polynomial d3 = new SparsePolynomial("-x^2 + 5x - 12");
        Polynomial d4 = new DensePolynomial("x^2 - 3x + 12");
        assertEquals(new SparsePolynomial("2x"), d3.add(d4));

        Polynomial s2 = new SparsePolynomial("3x^3 + 3x + 1 - 3x^-3 + 4x^-6");
        Polynomial s3 = new SparsePolynomial("-x^3 + 7x + 2 + 3x^-3 + 7x^-4");
        assertEquals(new SparsePolynomial("2x^3 + 10x + 3 + 7x^-4 + 4x^-6"), s2.add(s3));

        assertThrows(NullPointerException.class,
                () -> d1.add(null));
    }

    @Test
    void multiply()
    {
        Polynomial c1 = new SparsePolynomial("5");
        Polynomial c2 = new SparsePolynomial("-10");
        assertEquals(new SparsePolynomial("-50"), c1.multiply(c2));

        Polynomial d1 = new SparsePolynomial("x - 1");
        Polynomial d2 = new SparsePolynomial("x + 1");
        assertEquals(new SparsePolynomial("x^2 - 1"), d1.multiply(d2));

        Polynomial one = new SparsePolynomial("1");
        Polynomial zero = new SparsePolynomial("0");

        assertEquals(new SparsePolynomial("x - 1"), d1.multiply(one));

        Polynomial d0 = new SparsePolynomial("x - 1 + 3x^-1");
        Polynomial s1 = new DensePolynomial("3x^3 + 3x + 1");
        assertEquals(new SparsePolynomial("3x^4 - 3x^3 + 12x^2 - 2x + 8 + 3x^-1"), d0.multiply(s1));

        Polynomial d00 = new SparsePolynomial("0");
        assertEquals(new SparsePolynomial("0"), d0.multiply(d00));

        assertThrows(NullPointerException.class,
                () -> d1.multiply(null));
    }

    @Test
    void subtract()
    {
        Polynomial c1 = new SparsePolynomial("5");
        Polynomial c2 = new SparsePolynomial("-10");
        assertEquals(new SparsePolynomial("15"), c1.subtract(c2));

        Polynomial c3 = new DensePolynomial("-10");
        assertEquals(new SparsePolynomial("15"), c1.subtract(c3));

        Polynomial d1 = new SparsePolynomial("x^2 - 12");
        Polynomial d2 = new SparsePolynomial("x^2 - 9");
        assertEquals(new SparsePolynomial("-3"), d1.subtract(d2) );

        Polynomial s1 = new DensePolynomial("3x^3 + 3x + 1");
        assertEquals(new SparsePolynomial("-3x^3 + x^2 - 3x - 13"), d1.subtract(s1));

        Polynomial d3 = new SparsePolynomial("x^2 + 5x - 12 - 8x^-2");
        Polynomial d4 = new SparsePolynomial("x^2 - 3x - 12");
        assertEquals(new SparsePolynomial("-8x + 8x^-2"), d4.subtract(d3));

        assertThrows(NullPointerException.class,
                () -> d1.subtract(null));
    }

    @Test
    void minus()
    {
        Polynomial c3 = new SparsePolynomial("-10");
        assertEquals(new SparsePolynomial("10"), c3.minus());

        Polynomial d3 = new SparsePolynomial("x^2 + 5x - 12 - 9x^-10");
        assertEquals(new SparsePolynomial("-x^2 - 5x + 12 + 9x^-10"), d3.minus());

        Polynomial zero = new SparsePolynomial("0");
        assertEquals(new SparsePolynomial("0"), zero.minus());
    }

    @Test
    void wellFormed()
    {
        Polynomial c3 = new SparsePolynomial("-10");
        assertTrue(c3.wellFormed());

        Polynomial d3 = new SparsePolynomial("x^2 + 5x - 12 - 10x^-190");
        assertTrue(d3.wellFormed());

        assertThrows(IllegalArgumentException.class,
                () -> new SparsePolynomial("x^2 + 5x - 12.0"));
    }
}