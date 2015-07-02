package eu.tsp.sal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.genetics.GeneticAlgorithm;

/**
 * Individual for the algorithm
 * 
 * @author Son Han
 *  *
 */
public class SensorIndividual extends SensorChromosome {
    
   
   private final int Emax_THRESHOLD = 13320;     // current energy level of individual
   private final int Er = 50;    // energy used for communication from individual to OA
   private final int Eo = 2;    // energy used for communication from center to OA
   
   private int n;  // length of chronosome
   private int M;  // number of 1s
   
   
   private final int Mmax = 7200; 
   private final int NA = 3;    
   private final int Mo = 10;  
   
   private final double W1 = 0.5; 
   private final double W2 = 0.5;    
    
   
   private List<List<Integer>> solution = new ArrayList<List<Integer>>(M);
   
   public SensorIndividual(List<Integer> representation) {
       super(representation);
       n = representation.size();
       M = 0;
       List<Integer> oaList = new ArrayList<>();
       
       // Add each OA (index of 1) to the head of each list
       for (int i = 0; i < n; i++) {
           if (getRepresentation().get(i) != 1) continue;
           
           M++;
           oaList.add(i);
           
           List<Integer> list = new ArrayList<>();
           list.add(i);
           solution.add(list);
       }
       
       for (int i = 0; i < n; i++) {
           if (getRepresentation().get(i) == 1) continue;
           int oaIndex = GeneticAlgorithm.getRandomGenerator().nextInt(M);
           solution.get(oaIndex).add(i);
       }
   }

   /**
    * @return list of list of OAs and corresponding AAs
    * each list contains an OA following by a list of AA
    * e.g., [6,0,1] >> 6 {0,1}: node 6 is OA, node 0 and node 1 communicate via node 6
    */
   public List<List<Integer>> solution() {
       return solution;
   }
   
   
   /**
    * fitness function 
    */
   public double fitness() {
       return W1 * fitness_energy() + W2 * fitness_mem();       
   }
   /**
    * Returns energy left after doing all the communications:
    *  from AAs to OAs (Er)
    *  from center to OA (Eo)
    *  
    *  Emax = 
    */
   public double fitness_energy() {
       //Emax -= Er;
       // Current energy level
       int Emax = Emax_THRESHOLD/2 + GeneticAlgorithm.getRandomGenerator().nextInt(Emax_THRESHOLD/2);
       return M * (Emax - Eo) - (n-M) * Er ;       
   }
   
   public double fitness_mem() {
       // Current memory
       int Mj = 50 + GeneticAlgorithm.getRandomGenerator().nextInt(70);
       return M * (Mmax - NA * Mj - Mo);       
   }

   @Override
   public AbstractListChromosome<Integer> newFixedLengthChromosome(List chromosomeRepresentation) {
       return new SensorIndividual(chromosomeRepresentation);
   }
   
   public static void main (String[] args) {
       List<Integer> rep = new ArrayList<>();
       rep.add(0);rep.add(1);rep.add(1);rep.add(0);rep.add(0);rep.add(1);rep.add(0);rep.add(0);
       
       SensorIndividual in = new SensorIndividual(rep);
       WSN.printSolution(in);
   }
}
