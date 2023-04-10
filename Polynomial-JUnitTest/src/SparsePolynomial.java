import java.util.*;

//TODO: The add and subtract methods don't work when the coefficients added are +/- 1, 0
// apart whether it be Sparse or Dense Polynomial

public class SparsePolynomial implements Polynomial
{
    public Map<Integer, Integer> polynomialSparse
            = new TreeMap <Integer, Integer>(Collections.reverseOrder());
    private int highestdegree;
    private int lowestdegree;
    private String strSparse;

    /**
     * <t>Precondtions: If the string is invalid because it doesnt follow the
     * wellformed method conditions</t>
     *
     * @throws IllegalArgumentException
     *          if the string does not fullfill the proper conditions for creating this polynomial
     * @param s
     *      The String parameter that's needed to create a Polynomial
     */
    public SparsePolynomial(String s)
    {
        strSparse = s;

        if( wellFormed() == false)
        {
            throw new IllegalArgumentException("this polynomial can not be sparsed");
        }

        String[] sTerms = s.split(" ");

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

        if(term.get(0).substring(term.get(0).indexOf('^') + 1).contains("-"))
        {
            highestdegree = -1 * Integer.parseInt(term.get(0).substring(term.get(0).indexOf('^') + 2));
        }
        else
        {
            highestdegree = Integer.parseInt(term.get(0).substring(term.get(0).indexOf('^') + 1));
        }

        if(term.get(term.size() - 1).substring(term.get(term.size() - 1).indexOf('^') + 1).contains("-"))
        {
            lowestdegree = -1 * Integer.parseInt(term.get(term.size() - 1).substring(term.get(term.size() - 1).indexOf('^') + 2));
        }
        else
        {
            lowestdegree = Integer.parseInt(term.get(term.size() - 1).substring(term.get(term.size() - 1).indexOf('^') + 1));
        }

        for(String st : term)
        {
            Integer degree = 0;
            if ( st.substring(st.indexOf('^') + 1).contains("-"))
            {
                degree = -1 * Integer.parseInt(st.substring(st.indexOf('^') + 2));
            }
            else
            {
                degree = Integer.parseInt(st.substring(st.indexOf('^') + 1) );
            }

            Integer coefficient = 0;
            if( st.substring(0, st.indexOf("x")).contains("-"))
            {
                coefficient = -1 * Integer.parseInt(st.substring(1, st.indexOf("x")));
            }
            else
            {
                coefficient = Integer.parseInt(st.substring(0, st.indexOf("x")));
            }

            polynomialSparse.put(degree, coefficient);
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
        return highestdegree;
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
        if( !polynomialSparse.containsKey(d))
        {
            throw new IllegalArgumentException();
        }
        return polynomialSparse.get(d);
    }

    /**
     * <t>Postconditions: returns true or false for wether
     * map has a 0 at 0 index and a size of 1</t>
     *
     * @return true if the polynomial represents the zero constant
     */
    @Override
    public boolean isZero()
    {
        return polynomialSparse.containsKey(0)
                && polynomialSparse.keySet().size() == 1
                && polynomialSparse.get(0) == 0;
    }

    /**
     * <t>Preconditions: the Polynomial object is null</t>
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
        if( q == null)
        {
            throw new NullPointerException();
        }

        Map<Integer, Integer> poly = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        int length = degree();
        int start = lowestdegree;

        if( q instanceof SparsePolynomial)
        {
            if( ((SparsePolynomial) q).degree() > length)
            {
                length = ((SparsePolynomial) q).degree();
            }

            if( ((SparsePolynomial) q).lowestdegree < start)
            {
                start = ((SparsePolynomial) q).getLowestdegree();
            }

            for ( int i = length; i >= start; i--)
            {
                int x = 0;
                int y = 0;
                if( this.polynomialSparse.containsKey(i))
                {
                    x = this.polynomialSparse.get(i);
                }
                if( ((SparsePolynomial) q).polynomialSparse.containsKey(i))
                {
                    y = ((SparsePolynomial) q).getPolynomialSparse().get(i);
                }

                if ( (x != 0 || y != 0) && x + y != 0)
                {
                    poly.put(i, x + y);
                }
            }
        }
        else if( q instanceof DensePolynomial)
        {
            if( ((DensePolynomial)q).degree() > length)
            {
                length = ((DensePolynomial)q).degree();
            }
            if ( 0 < start )
            {
                start = 0;
            }

            for( int i = length; i >= start; i--)
            {
                int x = 0;
                int y = 0;
                if( this.polynomialSparse.containsKey(i))
                {
                    x = this.polynomialSparse.get(i);
                }
                if( i >= 0 && i <= ((DensePolynomial)q).degree())
                {
                    y = ((DensePolynomial)q).getPolynomial()[i];
                }
                if ( (x != 0 || y != 0) && x + y != 0)
                {
                    poly.put(i, x + y);
                }
            }
        }

        return new SparsePolynomial(mapToString(poly));
    }

    /**
     * <t>Preconditions: the Polynomial object is null</t>
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
        if( q == null)
        {
            throw new NullPointerException();
        }

        Map<Integer, Integer> poly
                = new TreeMap<Integer, Integer>(Collections.reverseOrder());

        if(q instanceof SparsePolynomial)
        {
            for(Integer c : this.polynomialSparse.keySet())
            {
                for(Integer i : ((SparsePolynomial)q).getPolynomialSparse().keySet())
                {
                    int deg = i + c;
                    int coefficient = polynomialSparse.get(c) * ((SparsePolynomial)q).getPolynomialSparse().get(i);
                    if(poly.containsKey(deg))
                    {
                        int together = poly.get(deg) + coefficient;
                        if(together != 0)
                        {
                            poly.put(deg, together);
                        }
                        else
                        {
                            poly.remove(deg);
                        }
                    }
                    else
                    {
                        poly.put(deg, coefficient);
                    }
                }
            }
        }
        else if(q instanceof DensePolynomial)
        {
            for(Integer i : this.polynomialSparse.keySet())
            {
                for(int c = ((DensePolynomial)q).degree(); c >= 0; c--)
                {
                    int deg = i + c;
                    int coefficient = polynomialSparse.get(i) * ((DensePolynomial)q).getPolynomial()[c];
                    if( coefficient != 0)
                    {
                        if(poly.containsKey(deg))
                        {
                            int together = poly.get(deg) + coefficient;
                            if(together != 0)
                            {
                                poly.put(deg, together);
                            }
                            else
                            {
                                poly.remove(deg);
                            }
                        }
                        else
                        {
                            poly.put(deg, coefficient);
                        }
                    }
                }
            }
        }

        poly.values().removeIf(element -> Integer.compare(element, 0) == 0);

        if(poly.size() == 0)
        {
            poly.put(0, 0);
        }

        return new SparsePolynomial(mapToString(poly));
    }

    /**
     * <t>Preconditions: the Polynomial object is null</t>
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
        if( q == null)
        {
            throw new NullPointerException();
        }

        Map<Integer, Integer> poly = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        int length = degree();
        int start = lowestdegree;

        if( q instanceof SparsePolynomial)
        {
            if( ((SparsePolynomial) q).degree() > length)
            {
                length = ((SparsePolynomial) q).degree();
            }

            if( ((SparsePolynomial) q).lowestdegree < start)
            {
                start = ((SparsePolynomial) q).getLowestdegree();
            }

            for ( int i = length; i >= start; i--)
            {
                int x = 0;
                int y = 0;
                if( this.polynomialSparse.containsKey(i))
                {
                    x = this.polynomialSparse.get(i);
                }
                if( ((SparsePolynomial) q).polynomialSparse.containsKey(i))
                {
                    y = ((SparsePolynomial) q).getPolynomialSparse().get(i);
                }

                if ( (x != 0 || y != 0) && x - y != 0)
                {
                    poly.put(i, x - y);
                }
            }
        }
        else if( q instanceof DensePolynomial)
        {
            if( ((DensePolynomial)q).degree() > length)
            {
                length = ((DensePolynomial)q).degree();
            }
            if ( 0 < start )
            {
                start = 0;
            }

            for( int i = length; i >= start; i-- )
            {
                int x = 0;
                int y = 0;
                if( this.polynomialSparse.containsKey(i))
                {
                    x = this.polynomialSparse.get(i);
                }
                if( i >= 0 && i <= ((DensePolynomial)q).degree())
                {
                    y = ((DensePolynomial)q).getPolynomial()[i];
                }
                if ( (x != 0 || y != 0) && x - y != 0)
                {
                    poly.put(i, x - y);
                }
            }
        }

        return new SparsePolynomial(mapToString(poly));
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
        Map<Integer, Integer> polyminus = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        for( Integer i : polynomialSparse.keySet())
        {
            int value = -1 * polynomialSparse.get(i);
            polyminus.put(i, value);
            //System.out.println( i + " " + polyminus.get(i));
        }


        return new SparsePolynomial(mapToString(polyminus));
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

        for(String str : strSparse.split(" "))
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

            if( coefficient.contains("-") && str.contains("x"))
            {
                coefficient = str.substring(1 , str.indexOf('x'));
            }
            else if( coefficient.contains("-") && !str.contains("x"))
            {
                coefficient = str.substring(1);
            }

            if( exp.contains("-"))
            {
                exp = exp.substring(exp.indexOf("-") + 1);
            }

            if(coefficient.equals(""))
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
     * <t>Postcondition: This is the toString method that prints the Map in
     * polynomial form</t>
     *
     * @return
     *      String representation of the array
     */
    @Override
    public String toString() {
        return mapToString(polynomialSparse);
    }

    /**
     * <t>Postcondition: helper method that turns an Map into a polynomial</t>
     * @param polyMap
     * @return
     */
    private String mapToString(Map<Integer, Integer> polyMap)
    {
        assert polyMap != null;

        String poly = "";

        int largest = (int) ((TreeMap)polyMap).firstKey();

        for( Integer i : polyMap.keySet() )
        {
            String coefficient = "";
            String term = "";
            String sign = "";
            String constant = "";
            //sign
            if(i != largest && polyMap.get(i) > 0)
            {
                sign = " + ";
            }
            else if(i != largest && polyMap.get(i) < 0)
            {
                sign = " - ";
            }

            //coefficient
            if( i == largest)
            {
                if((polyMap.get(i) < -1 || polyMap.get(i) > 1) && i != 0) //highest degree
                {
                    coefficient = polyMap.get(i) + "";
                }
                else if(polyMap.get(i) == -1 && i != 0) // highest degree
                {
                    coefficient = "-";
                }
            }

            else if( i != largest)
            {
                if(i != 0 && polyMap.get(i) > 1) //middle exp w/o 0
                {
                    coefficient = polyMap.get(i) + "";
                }
                else if (i != 0 && polyMap.get(i) < -1)
                {
                    coefficient = -1 * polyMap.get(i) + "";
                }
            }

            //Constant
            if(i == 0 && polyMap.size() > 1 && polyMap.get(i) > 0)
            {
                constant = polyMap.get(i) + "";
            }
            else if(i == 0 && polyMap.size() > 1 && polyMap.get(i) < 0)
            {
                constant = polyMap.get(i) * -1 + "";
            }
            else if(i == 0 && polyMap.size() == 1)
            {
                constant = polyMap.get(i) + "";
            }

            //Term
            if(i > 1 || i <= -1)
            {
                term = "x^" + i;
            }
            else if(i == 1)
            {
                term = "x";
            }

            if(i == 0)
            {
                poly = poly + sign + constant;
            }
            else
            {
                poly = poly + sign + coefficient + term;
            }
        }

        return poly;
    }

    /**
     * Postcondition: returns the map object
     *
     * @return
     *      this map object
     */
    public Map<Integer, Integer> getPolynomialSparse()
    {
        return polynomialSparse;
    }

    /**
     * Postcondtion: sets a new value for the Map object
     * @param polynomialSparse
     *      the new map object
     */
    public void setPolynomialSparse(Map<Integer, Integer> polynomialSparse)
    {
        this.polynomialSparse = polynomialSparse;
    }

    /**
     * Postconditions: returns lowest degree of polynomial
     * @return
     */
    public int getLowestdegree()
    {
        return lowestdegree;
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
        return polynomialSparse.equals(((SparsePolynomial)obj).getPolynomialSparse());
    }

}
