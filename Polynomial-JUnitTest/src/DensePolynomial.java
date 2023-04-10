import java.util.Arrays;
import java.util.ArrayList;

public class DensePolynomial implements Polynomial
{
    public int[] polynomial;
    private String fixedPoly;

    /**
     * <t>Precondtions: If the string is invalid because it doesnt follow the
     * wellformed method conditions</t>
     *
     * @throws IllegalArgumentException
     *          if the string does not fullfill the proper conditions for creating this polynomial
     * @param s
     *      The String parameter that's needed to create a Polynomial
     */
    public DensePolynomial(String s)
    {
        fixedPoly = s;

        if( this.wellFormed() == false )
        {
            throw new IllegalArgumentException("Input not the right polynomial");
        }

        String[] sTerms = s.split(" ");

        Integer highTerm = 0;

        if(sTerms[0].contains("^"))
        {
            highTerm = Integer.parseInt(sTerms[0].substring(sTerms[0].indexOf('^') + 1));
        }
        else if( sTerms[0].contains("x"))
        {
            highTerm = 1;
        }

        polynomial = new int[highTerm + 1];

        ArrayList<String> term = new ArrayList<String>();
        for( int x = 0; x < sTerms.length; x++ )
        {
            if( sTerms[x].equals("-"))
            {
                sTerms[x + 1] = "-" + sTerms[x + 1];
            }
            if( !sTerms[x].equals("-") && !sTerms[x].equals("+") && !sTerms[x].contains("x") )
            {
                sTerms[x] = sTerms[x] + "x^0";
            }
            if( !sTerms[x].equals("-") && !sTerms[x].equals("+") && !sTerms[x].contains("^") )
            {
                sTerms[x] = sTerms[x] + "^1";
            }
            if (sTerms[x].charAt(0) == 'x')
            {
                sTerms[x] = "1" + sTerms[x];
            }
            if (sTerms[x].contains("-x"))
            {
                sTerms[x] = "-1" + sTerms[x].substring(sTerms[x].indexOf('x'));
            }
            if( sTerms[x].contains("^"))
            {
                term.add(sTerms[x]);
            }
        }

        for( int i = 0; i <= highTerm; i ++)
        {
            Integer exponent = i;
            Integer coefficient = 0;
            for(int x = term.size() - 1; x >= 0; x--)
            {
                Integer poex = Integer.parseInt(term.get(x).substring(term.get(x).indexOf('^') + 1));

                if(poex == exponent )
                {
                    if(term.get(x).contains("-"))
                    {
                        coefficient = Integer.parseInt(term.get(x).substring(1, term.get(x).indexOf('x')));
                        coefficient = -1 * coefficient;
                    }
                    else
                    {
                        coefficient = Integer.parseInt(term.get(x).substring(0, term.get(x).indexOf('x')));
                    }
                }
            }
            polynomial[exponent] = coefficient;
        }
    }

    /**
     * <t>Postconditions: the largest degree of the polynomial is returned</t>
     *
     * @return the largest exponent with a non-zero coefficient. If all terms have zero exponents, it returns 0.
     */
    @Override
    public int degree()
    {
        return polynomial.length - 1;
    }

    /**
     * <t>Preconditions: the index must be in the length of the array</t>
     *
     * <t>Postconditions: Returns the coefficient corresponding to the given exponent.  Returns 0 if there is no term with that exponent
     * in the polynomial.</t>
     *
     * @param d the exponent whose coefficient is returned.
     * @return the coefficient of the term of whose exponent is d.
     */
    @Override
    public int getCoefficient(int d)
    {
        if( d >= polynomial.length || d < 0)
        {
            throw new IllegalArgumentException();
        }
        return polynomial[d];
    }

    /**
     * <t>Postconditions: returns true or false for wether
     * array has a 0 at 0 index and a length of 1</t>
     *
     * @return true if the polynomial represents the zero constant
     */
    @Override
    public boolean isZero()
    {
        return polynomial.length == 1 && polynomial[0] == 0;
    }

    /**
     * <t>Preconditions: the Polynomial object is null
     * or has a negative exponent</t>
     *
     * <t>Postcondition: Returns a polynomial by adding the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.</t>
     *
     * @param q the non-null polynomial to add to <code>this</code>
     * @return <code>this + </code>q
     * @throws NullPointerException if q is null
     */
    @Override
    public Polynomial add(Polynomial q)
    {
        if(q == null)
        {
            throw new NullPointerException();
        }

        int length = polynomial.length;
        int[] poly = new int[length];

        if(q instanceof SparsePolynomial)
        {
            for(Integer i : ((SparsePolynomial)q).getPolynomialSparse().keySet())
            {
                if(i < 0)
                {
                    throw new IllegalArgumentException("SparsePolynomial has negative value!");
                }
            }

            if( ((SparsePolynomial)q).degree() + 1 > polynomial.length )
            {
                length = ((SparsePolynomial)q).degree() + 1;
                poly = new int[length];
            }

            for( int i = 0; i < poly.length; i++)
            {
                int x = 0;
                int y = 0;

                if(((SparsePolynomial)q).getPolynomialSparse().containsKey(i))
                {
                    x = ((SparsePolynomial)q).getPolynomialSparse().get(i);
                }
                if(i < polynomial.length)
                {
                    y = polynomial[i];
                }
                poly[i] = x + y;
            }
        }
        else if(q instanceof DensePolynomial)
        {
            if( ((DensePolynomial)q).getPolynomial().length > polynomial.length )
            {
                length = ((DensePolynomial)q).getPolynomial().length;
                poly = new int[length];
            }

            for( int i = 0; i < poly.length; i++ )
            {
                int x = 0;
                int y = 0;
                if (i < ((DensePolynomial)q).getPolynomial().length)
                {
                    x = ((DensePolynomial)q).getPolynomial()[i];
                }
                if(i < polynomial.length)
                {
                    y = polynomial[i];
                }
                poly[i] = x + y;
            }
        }

        return new DensePolynomial(arrayToString(poly));
    }

    /**
     * <t>Preconditions: the Polynomial object is null
     *      or has a negative exponent</t>
     *
     * <t>Postconditions: Returns a polynomial by multiplying the parameter with the current instance.  Neither the current instance nor
     * the parameter are modified.</t>
     *
     * @param q the polynomial to multiply with <code>this</code>
     * @return <code>this * </code>q
     * @throws NullPointerException if q is null
     */
    @Override
    public Polynomial multiply(Polynomial q)
    {
        if( q == null )
        {
            throw new NullPointerException();
        }

        int length = this.degree() + q.degree() + 1;
        int[] poly = new int[length];

        for( int i = 0; i < length; i++ )
        {
            poly[i] = 0;
        }

        if(q instanceof DensePolynomial)
        {
            for( int i = 0; i < degree() + 1; i++)
            {
                for( int c = 0; c < q.degree() + 1; c++ )
                {
                    int deg = i + c;
                    int coefficient = (((DensePolynomial)q).getPolynomial()[c]) * polynomial[i];
                    poly[deg] += coefficient;
                }
            }
            //System.out.println(arrayToString(poly));
        }
        else if(q instanceof SparsePolynomial)
        {
            for(Integer i : ((SparsePolynomial)q).getPolynomialSparse().keySet())
            {
                if(i < 0)
                {
                    throw new IllegalArgumentException("SparsePolynomial has negative value!");
                }
            }

            for( int i = 0; i <= degree(); i++ )
            {
                for( Integer c : ((SparsePolynomial)q).getPolynomialSparse().keySet())
                {
                    int deg = i + c;
                    int coefficient = (((SparsePolynomial)q).getPolynomialSparse().get(c)) * polynomial[i];
                    poly[deg] = poly[deg] + coefficient;
                }
            }
        }

        return new DensePolynomial(arrayToString(poly));
    }

    /**
     * <t>Preconditions: the Polynomial object is null
     *      or has a negative exponent</t>
     *
     * <t>Postconditions: Returns a  polynomial by subtracting the parameter from the current instance. Neither the current instance nor
     * the parameter are modified.</t>
     *
     * @param q the non-null polynomial to subtract from <code>this</code>
     * @return <code>this - </code>q
     * @throws NullPointerException if q is null
     */
    @Override
    public Polynomial subtract(Polynomial q)
    {
        if(q == null)
        {
            throw new NullPointerException();
        }

        int length = polynomial.length;
        int[] poly = new int[length];

        if(q instanceof SparsePolynomial)
        {
            for(Integer i : ((SparsePolynomial)q).getPolynomialSparse().keySet())
            {
                if(i < 0)
                {
                    throw new IllegalArgumentException("SparsePolynomial has negative value!");
                }
            }

            if( ((SparsePolynomial)q).degree() + 1 > polynomial.length )
            {
                length = ((SparsePolynomial)q).degree() + 1;
                poly = new int[length];
            }

            for( int i = 0; i < poly.length; i++)
            {
                int x = 0;
                int y = 0;

                if(((SparsePolynomial)q).getPolynomialSparse().containsKey(i))
                {
                    x = ((SparsePolynomial)q).getPolynomialSparse().get(i);
                }
                if(i < polynomial.length)
                {
                    y = polynomial[i];
                }
                poly[i] = y - x;
            }
        }
        else if(q instanceof DensePolynomial)
        {
            if( ((DensePolynomial)q).getPolynomial().length > polynomial.length )
            {
                length = ((DensePolynomial)q).getPolynomial().length;
                poly = new int[length];
            }

            for( int i = 0; i < poly.length; i++ )
            {
                int x = 0;
                int y = 0;
                if (i < ((DensePolynomial)q).getPolynomial().length)
                {
                    x = ((DensePolynomial)q).getPolynomial()[i];
                }
                if(i < polynomial.length)
                {
                    y = polynomial[i];
                }
                poly[i] = y - x;
            }
        }

        return new DensePolynomial(arrayToString(poly));
    }

    /**
     *
     * <t>Postconditions: Returns a polynomial by negating the current
     * instance. The current instance is not modified.</t>
     *
     * @return -this
     */
    @Override
    public Polynomial minus()
    {
        int[] polyminus = new int[polynomial.length];
        for(int i = 0; i < polynomial.length; i++)
        {
            polyminus[i] = -1 * polynomial[i];
        }

        return new DensePolynomial(arrayToString(polyminus));
    }

    /**
     * <t>Postcondition: Checks if the class invariant holds for the current instance.</t>
     *
     * @return {@literal true} if the class invariant holds, and {@literal false} otherwise.
     * @throws
     */
    @Override
    public boolean wellFormed()
    {
        ArrayList<String> poly = new ArrayList<>();

        for(String str : fixedPoly.split(" "))
        {
            if( !str.equals("+") && !str.equals("-"))
            {
                poly.add(str);
            }
        }

        for(String str : poly )
        {
            String exp = "0";
            String coefficient = str;
            if( str.contains("^"))
            {
                exp = str.substring(str.indexOf('^') + 1);
            }

            if( str.contains("x"))
            {
                coefficient = str.substring(0 , str.indexOf('x'));
            }

            if( exp.contains("-"))
            {
                return false;
            }

            if( coefficient.contains("-") && str.contains("x"))
            {
                coefficient = str.substring(1 , str.indexOf('x'));
            }
            else if( coefficient.contains("-") && !str.contains("x"))
            {
                coefficient = str.substring(1);
            }

            if(coefficient.equals("x"))
            {
                coefficient = "1";
            }

            for(Character ch : coefficient.toCharArray())
            {
                if(!Character.isDigit(ch))
                {
                    return false;
                }
            }

            for(Character ch : exp.toCharArray())
            {
                if(!Character.isDigit(ch))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * <t>Postcondition: This is the toString method that prints the array in
     * polynomial form</t>
     *
     * @return
     *      String representation of the array
     */
    @Override
    public String toString()
    {
        return arrayToString(polynomial);
    }

    /**
     * <t>Postcondition: helper method that turns an array into a polynomial</t>
     * @param polyArray
     * @return
     */
    private String arrayToString (int[] polyArray)
    {
        assert polyArray != null;

        String poly = "";
        for( int i = polyArray.length - 1; i >= 0; i--)
        {
            String coefficient = "";
            String term = "";
            String sign = "";
            String constant = "";

            //sign
            if(!poly.equals("") && polyArray[i] > 0)
            {
                sign = " + ";
            }
            else if(!poly.equals("") && polyArray[i] < 0)//TODO
            {
                sign = " - ";
            }

            //Coefficient
            if( poly.equals("") && (polyArray[i] < -1 || polyArray[i] > 1) && i != 0)
            {
                coefficient = polyArray[i] + "";
            }
            else if( poly.equals("") && polyArray[i] == -1 && i != 0)
            {
                coefficient = "-";
            }
            else if( i < polyArray.length - 1 && i > 0 && polyArray[i] > 1)
            {
                coefficient = polyArray[i] + "";
            }
            else if ( i < polyArray.length - 1 && i > 0 && polyArray[i] < -1)
            {
                coefficient = polyArray[i] * -1 + "";
            }

            //Constant
            if(i == 0 && polyArray.length - 1 > 0 && polyArray[i] > 0)
            {
                constant = polyArray[i] + "";
            }
            else if(i == 0 && polyArray.length - 1 > 0 && polyArray[i] < 0)
            {
                constant = polyArray[i] * -1 + "";
            }
            else if(i == 0 && polyArray.length - 1 == 0)
            {
                constant = polyArray[i] + "";
            }

            //Term
            if(i > 1 && polyArray[i] != 0)
            {
                term = "x^" + i;
            }
            else if(i == 1 && polyArray[i] != 0)
            {
                term = "x";
            }

            if(i == 0)
            {
                if(!poly.equals(""))
                {
                    poly = poly + sign + constant;
                }
                else if( poly.equals(""))
                {
                    poly = polyArray[i] + "";
                }
            }
            else if( i != 0 )
            {
                poly = poly + sign + coefficient + term;
            }

        }
        return poly;
    }


    /**
     * <t>Postcondtion: returns the polynomial Integer array</t>
     * @return
     *      polynomial array
     */
    public int[] getPolynomial()
    {
        return polynomial;
    }

    /**
     * <t>Postcondition: sets a new Integer polynomial array</t>
     * @param polynomial
     *      the new polynomial array
     */
    public void setPolynomial(int[] polynomial)
    {
        this.polynomial = polynomial;
    }

    /**
     * <t>Postcondition: true/false for whether the two objects are equal</t>
     *
     * @param obj
     *      the object equated to
     * @return
     *         true or false
     */
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof DensePolynomial))
        {
            return false;
        }
        else
        {
            for( int i = 0; i < polynomial.length; i++)
            {
                if( i < ((DensePolynomial)obj).getPolynomial().length)
                {
                    if(((DensePolynomial)obj).getPolynomial()[i] != polynomial[i])
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }
        return true;
    }
}
