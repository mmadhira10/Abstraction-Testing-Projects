import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DensePolynomialTest
{
    @Test
    void degree()
    {
        //
        Polynomial d1 = new DensePolynomial("-x^2 - 12");
        assertEquals(2, d1.degree());

        Polynomial zero = new DensePolynomial("0");
        assertEquals(0, zero.degree());

        Polynomial oneTerm = new DensePolynomial("5x^7");
        assertEquals(7, oneTerm.degree());
    }

    @Test
    void getCoefficient()
    {
        Polynomial d1 = new DensePolynomial("2x^3 + 3x + 1");
        assertEquals(2, d1.getCoefficient(3));
        assertEquals(3, d1.getCoefficient(1));
        assertEquals(1,d1.getCoefficient(0));
        assertThrows(IllegalArgumentException.class,
                () -> d1.getCoefficient(4));
        assertThrows(IllegalArgumentException.class,
                () -> d1.getCoefficient(-1));
    }

    @Test
    void isZero()
    {
        Polynomial d1 = new DensePolynomial("2x^3 + 3x + 1");
        assertFalse(d1.isZero());

        Polynomial zero = new DensePolynomial("0");
        assertTrue(zero.isZero());

        Polynomial one = new DensePolynomial("1");
        assertFalse(one.isZero());
    }

    @Test
    void add()
    {
        Polynomial c1 = new DensePolynomial("5");
        Polynomial c2 = new DensePolynomial("-10");
        assertEquals(new DensePolynomial("-5"), c1.add(c2));

        Polynomial c3 = new SparsePolynomial("-10");
        assertEquals(new DensePolynomial("-5"), c1.add(c3));

        Polynomial d1 = new DensePolynomial("-x^2 - 12");
        Polynomial d2 = new DensePolynomial("x^2 - 9");
        assertEquals(new DensePolynomial("-21"), d1.add(d2) );

        Polynomial s1 = new SparsePolynomial("3x^3 + 3x + 1");
        assertEquals(new DensePolynomial("3x^3 - x^2 + 3x - 11"), d1.add(s1));

        Polynomial d3 = new DensePolynomial("-x^2 + 5x - 12");
        Polynomial d4 = new DensePolynomial("x^2 - 3x + 12");
        assertEquals(new DensePolynomial("2x"), d4.add(d3));

        Polynomial s2 = new SparsePolynomial("3x^3 + 3x + 1 - 3x^-3");
        assertThrows(IllegalArgumentException.class,
                () -> d1.add(s2));
        assertThrows(NullPointerException.class,
                () -> d1.add(null));
    }

    @Test
    void multiply()
    {
        Polynomial c1 = new DensePolynomial("5");
        Polynomial c2 = new DensePolynomial("-10");
        assertEquals(new DensePolynomial("-50"), c1.multiply(c2));

        Polynomial d1 = new DensePolynomial("x - 1");
        Polynomial d2 = new DensePolynomial("x + 1");
        assertEquals(new DensePolynomial("x^2 - 1"), d1.multiply(d2));

        Polynomial one = new DensePolynomial("1");
        Polynomial zero = new DensePolynomial("0");

        assertEquals(new DensePolynomial("x - 1"), d1.multiply(one));
        assertEquals(new DensePolynomial("0"), d1.multiply(zero));

        Polynomial s1 = new SparsePolynomial("3x^3 + 3x + 1");
        assertEquals(new DensePolynomial("3x^4 - 3x^3 + 3x^2 - 2x - 1"), d1.multiply(s1));

        Polynomial s2 = new SparsePolynomial("3x^3 + 3x + 1 + 10x^-1");
        assertThrows(IllegalArgumentException.class,
                () -> d1.multiply(s2));

        assertThrows(NullPointerException.class,
                () -> d1.multiply(null));
    }

    @Test
    void subtract()
    {
        Polynomial c1 = new DensePolynomial("5");
        Polynomial c2 = new DensePolynomial("-10");
        assertEquals(new DensePolynomial("15"), c1.subtract(c2));

        Polynomial c3 = new SparsePolynomial("-10");
        assertEquals(new DensePolynomial("15"), c1.subtract(c3));

        Polynomial d1 = new DensePolynomial("x^2 - 12");
        Polynomial d2 = new DensePolynomial("x^2 - 9");
        assertEquals(new DensePolynomial("-3"), d1.subtract(d2) );

        Polynomial s1 = new SparsePolynomial("3x^3 + 3x + 1");
        assertEquals(new DensePolynomial("-3x^3 + x^2 - 3x - 13"), d1.subtract(s1));

        Polynomial d3 = new DensePolynomial("x^2 + 5x - 12");
        Polynomial d4 = new DensePolynomial("x^2 - 3x - 12");
        assertEquals(new DensePolynomial("-8x"), d4.subtract(d3));

        Polynomial s2 = new SparsePolynomial("3x^3 + 3x + 1 - 3x^-3");
        assertThrows(IllegalArgumentException.class,
                () -> d1.subtract(s2));

        assertThrows(NullPointerException.class,
                () -> d1.subtract(null));
    }

    @Test
    void minus()
    {
        Polynomial c3 = new DensePolynomial("-10");
        assertEquals(new DensePolynomial("10"), c3.minus());

        Polynomial d3 = new DensePolynomial("x^2 + 5x - 12");
        assertEquals(new DensePolynomial("-x^2 - 5x + 12"), d3.minus());

        Polynomial zero = new DensePolynomial("0");
        assertEquals(new DensePolynomial("0"), zero.minus());
    }

    @Test
    void wellFormed()
    {
        Polynomial c3 = new DensePolynomial("-10");
        assertTrue(c3.wellFormed());

        Polynomial d3 = new DensePolynomial("x^2 + 5x - 12");
        assertTrue(d3.wellFormed());

        assertThrows(IllegalArgumentException.class,
                () -> new DensePolynomial("x^2 + 5x - 12 + 3x^-3"));

        assertThrows(IllegalArgumentException.class,
                () -> new DensePolynomial("x^2 + 5x - 12.0"));
    }
}