import java.util.*;

//import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckPolynomial
{
    public static void main(String []args)
    {
//        Polynomial d1 = new DensePolynomial("-x^2 - 12");
//        Polynomial d2 = new DensePolynomial("x^2 - 9");
//
//        Polynomial s1 = new SparsePolynomial("3x^3 + 3x + 1");
//        Polynomial s2 = new SparsePolynomial( "3x^3 + 3x^2 - 2x - 3x^-3");
//        System.out.println( d1.minus().toString() );
//        System.out.println( (s1.add(s2)).toString() );
        Map<Integer, Integer> red = new TreeMap<>(Collections.reverseOrder());

        //Polynomial pq = s1.minus();
        //System.out.println(d1.add(s1)); //TODO: doesnt work if the x is + or - 1 in the coefficient for add and sub

//        Polynomial d3 = new DensePolynomial("-x^2 + 5x - 12");
//        Polynomial d4 = new SparsePolynomial("x^2 - 3x + 12");
//        System.out.println(d4.add(d3));
//
//        Polynomial d6 = new SparsePolynomial("-x^2 - 12");
//        Polynomial d7 = new SparsePolynomial("x^2 - 9");
//        System.out.println(d6.add(d7));


//        Polynomial d8 = new SparsePolynomial("-x^2 + 5x - 12");
//        Polynomial d9 = new SparsePolynomial("x^2 - 3x + 12");
//
//        System.out.println(d8.add(d9));

        Polynomial d0 = new SparsePolynomial("x - 1");
        Polynomial d00 = new SparsePolynomial("0");
        System.out.println(d0.multiply(d00));

//        System.out.println(new SparsePolynomial("-4x^-2 + 3x^-7"));

//        SparsePolynomial finale = (SparsePolynomial) s1.add(s2);
//        for( Integer i : finale.getPolynomialSparse().keySet())
//            System.out.println(i + " " + finale.getPolynomialSparse().get(i));

//        SparsePolynomial bum = new SparsePolynomial("2x^4 + 3x^3 + 3x + 1 - 3x^-3");
//        System.out.println(bum.toString());

    }
}
