package pattern_matching;

/******************************************************************************
 *  Compilation:  javac KMP.java
 *  Execution:    java KMP pattern text
 *  Dependencies: StdOut.java
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  KMP algorithm.
 *
 *  % java KMP abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:               abracadabra
 *
 *  % java KMP rab abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:         rab
 *
 *  % java KMP bcara abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                                   bcara
 *
 *  % java KMP rabrabracad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java KMP abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ******************************************************************************/

/**
 *  The {@code KMP} class finds the first occurrence of a pattern string
 *  in a text string.
 *  <p>
 *  This implementation uses a version of the Knuth-Morris-Pratt substring search
 *  algorithm. The version takes time proportional to <em>n</em> + <em>m R</em>
 *  in the worst case, where <em>n</em> is the length of the text string,
 *  <em>m</em> is the length of the pattern, and <em>R</em> is the alphabet size.
 *  It uses extra space proportional to <em>m R</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/53substring">Section 5.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class KMP {   
    private final int M;       // length of pattern
    private int[] ot;       	// the overlap table
    private String P;			// pattern string
    /**
     * Preprocesses the pattern string.
     *
     * @param P the pattern string
     */
    public KMP (String pat) {
    	this.P = pat;
    	M = P.length();
    	ot = new int[M];
    	ot[0] = 0;	

		for (int pos=1; pos < M; pos ++){ // first is always zero, we start from 1
			int prev_overlap = ot[pos - 1];

			if (P.charAt(pos) == P.charAt(prev_overlap))
				ot[pos] = prev_overlap + 1;
			else{
				while ((P.charAt(pos)!=P.charAt(prev_overlap)) && prev_overlap >= 1){
					// try extend a smaller prefix - based on P [of_table[pos-1]]
					prev_overlap = ot[prev_overlap - 1];
				}

				if (P.charAt(pos) == P.charAt(prev_overlap))
					ot[pos] = prev_overlap + 1;
			}
		}    		
    }  

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     *
     * @param  T the text string
     * @return the index of the first occurrence of the pattern string this.P
     *         in the text string; -1 if no such match
     */
    public int search(String T) {
    	int N = T.length();

		int i = 0; // current position at which to compare character in T
		int j = 0; // current position at which to compare character in P

		while (i < N) { //while current position in T is within bounds
			// loop while characters match
			while (j < M && i < N && T.charAt(i) == P.charAt(j)){
				i ++;
				j ++;
			}
			if (j == M)
				return (i - M);

			if (j == 0)
				i++;
			else {
				int of_prev = ot[j - 1];
				if (ot[j]>ot[j-1]){
					i++;
					j=0;
				}
				else if(of_prev == 0) // we did not match any character - restart pattern					
					j = 0;
				else
					j = of_prev;  // skip characters in P according to the OF
			}
		}
		return -1;                  // not found
    }

    /**
     * Takes a pattern string and an input string as command-line arguments;
     * searches for the pattern string in the text string; and prints
     * the first occurrence of the pattern string in the text string.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
    	String P = "THEM";
        String T = "THETHEYTHYTHEMEM";
    	
        if (args.length >= 2) {
        	P = args[0];
            T = args[1];
        }  	  

        KMP kmp = new KMP(P);
        int firstIndex = kmp.search(T);       

        // print results
        System.out.println("text:       " + T);

        // from brute force search method 
        System.out.print("pattern:    ");
        for (int i = 0; i < firstIndex; i++)
        	System.out.print(" ");
        System.out.println(P);
    }
}

