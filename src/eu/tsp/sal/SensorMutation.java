package eu.tsp.sal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.MutationPolicy;

/**
 * Mutation for {@link SensorChromosome}s. Randomly exchange two genes.
 *
 */
public class SensorMutation implements MutationPolicy {

    /**
     * Mutate the given chromosome. Randomly exchanges two genes.
     *
     * @param original the original chromosome.
     * @return the mutated chromosome.
     * @throws MathIllegalArgumentException if <code>original</code> is not an instance of {@link SensorChromosome}.
     */
    public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {
        if (!(original instanceof SensorChromosome)) {
            throw new MathIllegalArgumentException(null);
        }

        SensorChromosome origChrom = (SensorChromosome) original;
        List<Integer> newRepr = new ArrayList<Integer>(origChrom.getRepresentation());

        // randomly select 02 gene
        int geneIndex1 = GeneticAlgorithm.getRandomGenerator().nextInt(origChrom.getLength());
        int geneIndex2 = GeneticAlgorithm.getRandomGenerator().nextInt(origChrom.getLength());
        // and exchange them
        newRepr.set(geneIndex1, origChrom.getRepresentation().get(geneIndex2));
        newRepr.set(geneIndex2, origChrom.getRepresentation().get(geneIndex1));

        Chromosome newChrom = origChrom.newFixedLengthChromosome(newRepr);
        return newChrom;
    }

}
