package eu.tsp.sal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.genetics.GeneticAlgorithm;

/**
 * Individual for the algorithm
 * 
 * @author Son Han
 *
 */
public class SensorIndividual extends SensorChromosome {
   private int Emax = 1000;     // current energy level of individual
   private final int Er = 1;    // energy used for communication from individual to OA
   private final int Eo = 2;    // energy used for communication from center to OA
   
   private int n;  // length of chronosome
   private int M;  // number of 1s
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
           int oaIndex = Emax = GeneticAlgorithm.getRandomGenerator().nextInt(M);
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
    * Returns energy left after doing all the communications:
    *  from AAs to OAs (Er)
    *  from center to OA (Eo)
    *  
    *  Emax = 
    */
   public double fitness() {
       //Emax -= Er;
       Emax = GeneticAlgorithm.getRandomGenerator().nextInt(1000);
       return M * (Emax - (n-M) * Er - Eo);       
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
