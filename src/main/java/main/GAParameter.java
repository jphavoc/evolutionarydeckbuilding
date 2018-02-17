package main;

import net.demilich.metastone.game.entities.heroes.HeroClass;

/**
 * @author created by Jens Plümer on 01.12.17
 */
public class GAParameter {

    public final int STEADY_FITNESS;
    public final double MUTATION_RATE;
    public final float NUMBER_OF_GAMES;
    public final int POPULATION_SIZE;
    public final int MAX_GENERATIONS;
    public final int TOURNAMENT_SELECTIONS;
    public final double REFERENCE_GENERATION;
    public final HeroClass heroClass;
    private int muu;
    private int lambda;

    public GAParameter(int muu, int lambda, double mutationRate, int numberOfGames, int maxGenerations,
                   int steadyFitness, double refGen, int tournamentSelections, HeroClass clazz) {

        this.muu = muu;
        this.lambda = lambda;
        MUTATION_RATE = mutationRate;
        NUMBER_OF_GAMES = numberOfGames;
        POPULATION_SIZE = lambda;
        MAX_GENERATIONS = maxGenerations;
        STEADY_FITNESS = steadyFitness;
        REFERENCE_GENERATION = refGen;
        TOURNAMENT_SELECTIONS = tournamentSelections;
        heroClass = clazz;

    }

    public double getMUTATION_RATE() {
        return MUTATION_RATE;
    }

    public float getNUMBER_OF_GAMES() {
        return NUMBER_OF_GAMES;
    }

    public int getPOPULATION_SIZE() {
        return POPULATION_SIZE;
    }

    public int getMAX_GENERATIONS() {
        return MAX_GENERATIONS;
    }

    public int getTOURNAMENT_SELECTIONS() {
        return TOURNAMENT_SELECTIONS;
    }

    public double getREFERENCE_GENERATION() {
        return REFERENCE_GENERATION;
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    public int getMuu() {
        return muu;
    }

    public int getLambda() {
        return lambda;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("\n=====\n");
        builder.append("(" + muu + ", " + lambda + ")-EA\n");
        builder.append("=====\n");
        builder.append("population size=" + POPULATION_SIZE + "\n");
        builder.append("max generations=" + MUTATION_RATE + "\n");
        builder.append("max fitness=" + 128 + "\n");//TODO add value
        builder.append("number of games=" + NUMBER_OF_GAMES+ "\n");
        builder.append("hero class=" + heroClass + "\n");

        return builder.toString();
    }
}
