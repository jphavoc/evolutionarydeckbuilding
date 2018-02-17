package main.evolution;

import org.jenetics.IntegerGene;
import org.jenetics.Mutator;
import org.jenetics.util.MSeq;
import org.jenetics.util.RandomRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.jenetics.internal.math.base.clamp;
import static org.jenetics.internal.math.random.indexes;

/**
 * @author created by Jens Plümer on 10.11.17
 */
public class DeckMutator<C extends Comparable<? super C>> extends Mutator<IntegerGene, C> {

    private static final Logger log = LoggerFactory.getLogger(DeckMutator.class);

    private double mutationRate;

    public DeckMutator() {
        super();
    }

    @Override
    protected int mutate(MSeq<IntegerGene> genes, double p) {

        final Random random = RandomRegistry.getRandom();

        return (int) indexes(random, genes.length(), p)
                .peek(i -> genes.set(i, mutate(genes.get(i), random)))
                .count();
    }

    IntegerGene mutate(final IntegerGene gene, final Random random) {

        final double min = gene.getMin().doubleValue();
        final double max = gene.getMax().doubleValue();
        final double alpha = mutationRate;
        final double value = gene.doubleValue();
        final double gaussian = random.nextGaussian();

        return gene.newInstance(clamp(gaussian * alpha + value, min, max));
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getMutationRate() {
        return this.mutationRate;
    }

}
