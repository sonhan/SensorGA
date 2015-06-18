package eu.tsp.sal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.genetics.GeneticAlgorithm;

public class SensorIndividual extends SensorChromosome {
   private int Emax = 1000;     // current energy level of individual
   private final int Er = 1;    // energy used for communication from individual to OA
   private final int Eo = 2;    // energy used for communication from center to OA
   
   private int n;  // length of chronosome
   private int m;  // number of 1s
   private int[][] encodes;
   public SensorIndividual(List<Integer> representation) {
       super(representation);
       n = representation.size();
       m = 0;
       for (int e : representation) 
           if (e == 1) m++;
       
       encodes = new int[m][n];
       
       int c = 2;
       for (int j = 0; j < representation.size(); j++) {
           int e = representation.get(j);
           encodes[0][j] = e;
           for (int i = 1; i < m; i++) {
               if (e == 1) encodes[i][j] = 1;
               else {
                   int val = c + GeneticAlgorithm.getRandomGenerator().nextInt(m);
                   encodes[i][j] = val;
               }
           }
           if (e != 1) c += m;
       }
       
       //System.out.println("(n, m) = (" + n + ", " + m + ")");
       //WSN.print2DArray(encodes);
       //System.out.println("Chromosome =" + this);
       //System.out.println("Solution = " + solution());
   }

   /**
    * @return list of list of OAs and corresponding AAs
    * each list contains an OA following by a list of AA
    * e.g., [6,0,1] >> 6 {0,1}: node 6 is OA, node 0 and node 1 communicate via node 6
    */
   public List<List<Integer>> solution() {
       List<List<Integer>> solution = new ArrayList<List<Integer>>(m);
       for (int i = 0; i < n; i++) {
           if (getRepresentation().get(i) != 1) continue;
           List<Integer> list = new ArrayList<>();
           list.add(i);
           solution.add(list);
       }
       
       for (int i = 0; i < n; i++) {
           if (getRepresentation().get(i) == 1) continue;
           int max = max(i);
           solution.get(max).add(i);
       }
       
       return solution;
   }
   
   private int max(int col) {
       int max = 0;
       int maxVal = encodes[0][col];
       for (int i = 1; i < m; i++)
           if (encodes[i][col] > maxVal) { 
               max = i; 
               maxVal = encodes[i][col]; 
           }
       return max;
   }
   /**
    * Returns energy left after doing all the communications:
    *  from AAs to OAs (Er)
    *  from center to OA (Eo)
    *  
    *  Emax = 
    */
   public double fitness() {
       //Emax -= Er;
       Emax = GeneticAlgorithm.getRandomGenerator().nextInt(1000);
       return m * (Emax - (n-m) * Er - Eo);       
   }

   @Override
   public AbstractListChromosome<Integer> newFixedLengthChromosome(List chromosomeRepresentation) {
       return new SensorIndividual(chromosomeRepresentation);
   }
   
}