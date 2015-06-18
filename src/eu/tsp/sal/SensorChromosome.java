package eu.tsp.sal;


import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.InvalidRepresentationException;


/**
 * Chromosome represented by a vector of 0s and 1s.
 *
 * @since 2.0
 */
public abstract class SensorChromosome extends AbstractListChromosome<Integer> {

    /**
     * Constructor.
     * @param representation list of {0,1} values representing the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public SensorChromosome(List<Integer> representation) throws InvalidRepresentationException {
        super(representation);
    }

    /**
     * Constructor.
     * @param representation array of {0,1} values representing the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public SensorChromosome(Integer[] representation) throws InvalidRepresentationException {
        super(representation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkValidity(List<Integer> chromosomeRepresentation) throws InvalidRepresentationException {
        
    }

    /**
     * Returns a representation of a random binary array of length <code>length</code>.
     * @param length length of the array
     * @return a random binary array of length <code>length</code>
     */
    public static List<Integer> randomBinaryRepresentation(int length) {
        // random binary list
        List<Integer> rList= new ArrayList<Integer> (length);
        for (int j=0; j<length; j++) {
            rList.add(GeneticAlgorithm.getRandomGenerator().nextInt(2));
        }
        return rList;
    }

    @Override
    protected boolean isSame(Chromosome another) {
        // type check
        if (! (another instanceof SensorChromosome)) {
            return false;
        }
        SensorChromosome anotherBc = (SensorChromosome) another;
        // size check
        if (getLength() != anotherBc.getLength()) {
            return false;
        }

        for (int i=0; i< getRepresentation().size(); i++) {
            if (!(getRepresentation().get(i).equals(anotherBc.getRepresentation().get(i)))) {
                return false;
            }
        }
        // all is ok
        return true;
    }
}
