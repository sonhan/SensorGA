package eu.tsp.sal;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.FixedGenerationCount;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;
import org.apache.commons.math3.genetics.TournamentSelection;

/**
 * Wireless Sensor Network.
 * @author Son Han
 */
public class WSN {

    // parameters for the GA
    
    private static final int LENGTH = 256; // number of sensors
    private static final int OA_NUMBER = 30; // fixed number of OAs
    private static final int POPULATION_SIZE = 10;
    private static final int NUM_GENERATIONS = 50;
    private static final double ELITISM_RATE = 0.2;
    private static final double CROSSOVER_RATE = 0.8;
    private static final double MUTATION_RATE = 0.005;
    private static final int TOURNAMENT_ARITY = 2; // should be less than POPULATION_SIZE

    /**
     * Algorithm run on a number of sensors = LENGTH
     * 
     * Population has POPULATION_SIZE individuals (SensorIndividual class)
     * each individual has LENTH genes (~ sensor)
     * 
     *  
     * 
     */
    
    public static void main(String[] args) {
        /**
         *  initialize a new genetic algorithm with
         *      Crossover policy
         *      CROSSOVER_RATE
         *      Mutation policy
         *      MUTATION_RATE
         *      Selection Policy 
         *      
         */
           
        SensorGeneticAlgorithm ga = new SensorGeneticAlgorithm(
                new NPointCrossover(2),
                CROSSOVER_RATE, // all selected chromosomes will be recombined (=crosssover)
                new SensorMutation(),
                MUTATION_RATE,
                new SensorTournamentSelection(TOURNAMENT_ARITY)
        );

        //assertEquals(0, ga.getGenerationsEvolved());
        //System.out.println(ga.getGenerationsEvolved());

        // initial population of POPULATION_SIZE SensorIndividual
        //Population initial = randomPopulation(LENGTH, POPULATION_SIZE);
        Population initial = randomPopulationWithFixedOA(OA_NUMBER, LENGTH, POPULATION_SIZE);
        
        
        
        List<Chromosome> list = ((ElitisticListPopulation) initial).getChromosomes();
        
        //System.out.println("Generation (iteration): " + initial);
        int i = 0;
        for (Chromosome individual : list) {
            System.out.println("Individual " + i++ + " = " + individual);
            System.out.println("Solution = " + ((SensorIndividual) individual).solution());
            
        }
        
        
        
        
        // stopping conditions
        StoppingCondition stopCond = new FixedGenerationCount(NUM_GENERATIONS);

        // best initial chromosome
        Chromosome bestInitial = initial.getFittestChromosome();
        System.out.println("Best Individual in initial population (highest fitness) =" + bestInitial);
        System.out.println("Solution  of the best individual = " + ((SensorIndividual) bestInitial).solution());
        
        // run the algorithm
        Population finalPopulation = ga.evolve(initial, stopCond);

        // best SensorIndividual from the final population
        Chromosome bestFinal = finalPopulation.getFittestChromosome();
        System.out.println("Best Individual in final population (highest fitness) =" + bestFinal);
        //System.out.println("Solution of the best individual = " + ((SensorIndividual) bestFinal).solution());
        printSolution((SensorIndividual) bestFinal);

        // Assertion
        
        // assertTrue(bestFinal.compareTo(bestInitial) > 0);
        // assertEquals(NUM_GENERATIONS, ga.getGenerationsEvolved());
        System.out.println((bestFinal.compareTo(bestInitial) > 0)? 
                "Final generation is better than the ancestors":
                    "Final generation is worse than the ancestors!!!!");
        //System.out.println(ga.getGenerationsEvolved());
    }
    
    private static void printSolution(SensorIndividual in) {
        List<List<Integer>> solutions = in.solution();
        int count = 0;
        for (int i = 0; i < solutions.size(); i++) {
            
            List<Integer> solution = solutions.get(i);
            System.out.print(solution.get(0));
            System.out.print("{");
            
            for (int j = 1; j < solution.size(); j++) {
                System.out.print(solution.get(j));
                if (j != solution.size() - 1) System.out.print(" ");
            }
            
            count += solution.size() - 1;
            System.out.println("}");
        }
        
        System.out.println("Total OAs: " + solutions.size());
        System.out.println("Total AAs: " + count);
    }
    
    /**
     * Initializes a random population.
     * @param   len     lenth of chromosome
     * @param   popSize population size
     */
    private static ElitisticListPopulation randomPopulation(int len, int popSize) {
        List<Chromosome> popList = new ArrayList<>();
        
        for (int i = 0; i < popSize; i++) {
            List<Integer> rList= new ArrayList<Integer> (len);
            
            // set each element randomly to 0 or 1
            for (int j = 0; j < len; j++)
                rList.add(GeneticAlgorithm.getRandomGenerator().nextInt(2));
            
            // count the number of 1 and set to m
            int M = 0;
            for (int e : rList)
                if (e == 1) M++;
            
            // update 0 with a random number according to the algorithm
            // random of (cj, cj + M)
            int c = 2;
            for (int j = 0; j < rList.size(); j++) {
                int e = rList.get(j);
                if (e == 1) continue;
                else {
                    int val = c + GeneticAlgorithm.getRandomGenerator().nextInt(M);
                    rList.set(j, val);
                    c += M;
                }
            }
                        
            Chromosome randChrom = new SensorIndividual(rList);
            popList.add(randChrom);
            
        }
        return new ElitisticListPopulation(popList, popList.size(), ELITISM_RATE);
    }
    
    
    /**
     * Initializes a random population with a fixed number of 1s (m)
     * @param   M       fixed number of OA
     * @param   len     lenth of chromosome
     * @param   popSize population size
     */
    private static ElitisticListPopulation randomPopulationWithFixedOA(int M, int len, int popSize) {
        List<Chromosome> popList = new ArrayList();
        
        for (int i = 0; i < popSize; i++) {
            List<Integer> rList= new ArrayList<Integer> (len);
            
            // Level 1 encoding with a fixed number of 1s
            for (int j = 0; j < len; j++)   rList.add(0);
            int count = 0;
            while (count < M) {
                int index = GeneticAlgorithm.getRandomGenerator().nextInt(len);
                if (rList.get(index) == 0) {
                    rList.set(index, 1);
                    count++;
                }
            }
            
            // Level 2 encoding
            // update 0 with a random number according to the algorithm
            // random of (cj, cj + M)
            int c = 2;
            for (int j = 0; j < rList.size(); j++) {
                int e = rList.get(j);
                if (e == 1) continue;
                else {
                    int val = c + GeneticAlgorithm.getRandomGenerator().nextInt(M);
                    rList.set(j, val);
                    c += M;
                }
            }
                        
            Chromosome randChrom = new SensorIndividual(rList);
            popList.add(randChrom);
            
        }
        return new ElitisticListPopulation(popList, popList.size(), ELITISM_RATE);
    }
    
    
    
    public static void print2DArray(int[][] a) {
        for (int i = 0; i < a.length; i++)
            System.out.println(Arrays.toString(a[i]));
    }

}