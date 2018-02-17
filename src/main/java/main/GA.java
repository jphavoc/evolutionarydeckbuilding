package main;

import javafx.util.Pair;
import main.evolution.DeckMutator;
import main.simulation.*;
import main.util.GnuPlot;
import net.demilich.metastone.game.behaviour.Behaviour;
import net.demilich.metastone.game.behaviour.GreedyOptimizeMove;
import net.demilich.metastone.game.behaviour.heuristic.WeightedHeuristic;
import net.demilich.metastone.game.cards.*;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.game.statistics.GameStatistics;
import net.demilich.metastone.game.statistics.Statistic;
import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.jenetics.engine.limit.byFitnessThreshold;

/**
 * @author created by Jens Plümer on 09.11.17
 */
public class GA {

    private static final String PATH_LOG = "logs";
    private static final String FILE_LOG = "evolutionarydeckbuilding-";

    public static Logger log = LoggerFactory.getLogger(GA.class);

    //
    private static GASetup setup;
    private static GAParameter pars;
    private static GAValidator val;
    private static GAStatistic stats;
    private static DeckMutator mutator;
    //
    private static Population<IntegerGene, Integer> ancestors;
    private static int infeasible = 0;
    private static int overallBest = 0;
    private static int currentGeneration = 0;
    private static int successfulGenerations = 0;
    private static Pair<Genotype<IntegerGene>, Pair<Integer, Map<String, GameStatistics>>> best;

    // Defines the genotype of the problem
    private final Factory<Genotype<IntegerGene>> itf2;
    // Additional number of threads to use during evolution
    private final ExecutorService executor;
    // Creates the execution environment.
    private final Engine<IntegerGene, Integer> engine;
    private double ADAPT_INRCREASE = 2.0;
    private double ADAPT_DECREASE = 0.5;

    public GA(DeckMutator mutator, Behaviour ai, GASetup setup, GAParameter pars, GAStatistic stats, GAValidator val) {

        this.mutator = mutator;
        this.setup = setup;
        this.val = val;
        this.pars = pars;
        this.stats = stats;
        this.stats.appendParameters(pars);
        this.stats.appendOpponents(setup.getOpponentList());
        this.mutator.setMutationRate(pars.MUTATION_RATE);// set initial value
        this.setup.setNumberOfGames(pars.getNUMBER_OF_GAMES());
        this.setup.setAi(ai);
        this.val.setPars(pars);



        // Defines the genotype of the problem
        itf2 = () -> {

            IntegerGene[] genes = new IntegerGene[30];
            for (int i = 0; i < genes.length; i++) {
                genes[i] = IntegerGene.of(val.getValidCard(new ArrayList<>()), 0, GASetup.getCardCount());
            }

            return Genotype.of(IntegerChromosome.of(genes));
        };

        final Predicate<? super Genotype<IntegerGene>> validator = gt -> {
            return val.isDeckValid(gt);
        };

        // Additional number of threads to use during evolution
        executor = Executors.newFixedThreadPool(20);

        // Creates the execution environment.
        engine = Engine
                .builder(GA::evaluate, itf2)
                .genotypeValidator(validator)
                .maximizing()
                .populationSize(pars.POPULATION_SIZE)
                // (muu,lambda)
//                .survivorsSize(0)
//                .offspringSelector(new TruncationSelector<>(pars.getMuu()))
//                .offspringFraction(0.7)
                .survivorsSelector (new TournamentSelector <>(4))// selection 4
                .offspringSelector (new RouletteWheelSelector <>())
                .alterers(
                        this.mutator,
                        new SinglePointCrossover<>(),
                        new MultiPointCrossover<>(2)//
//                        new IntermediateCrossover<>()// not good
                )
//                . genotypeValidator (validator)
//                . individualCreationRetries (1)
                .executor(executor)
                .build();

    }


    public Genotype<IntegerGene> start() {

        // Create evolution statistics consumer.
        final EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();

        // Start evolution stream
        Genotype<IntegerGene> result = engine.stream()
//                .limit(bySteadyFitness(pars.STEADY_FITNESS))
//                .limit(limit.byFitnessConvergence(120, 128, 0.0001))
//                .limit((Predicate<? super EvolutionResult<IntegerGene, Integer>>) byFitnessThreshold(128))
                .limit(pars.MAX_GENERATIONS)
                .map(e -> nextGen(e))
                .peek(statistics)
                .collect(EvolutionResult.toBestGenotype());

        // Set evolution outcome
        stats.setEvolutionStatistics(statistics);

        return result;
    }

    private static synchronized int evaluate(Genotype<IntegerGene> gt) {

        // get new deck from genes
        final Deck playerDeck = setup.getPlayerDeck(gt);
        // validate if deck is a feasible solution
        final boolean VALID = val.isDeckValid(gt);

        // if solution is not feasible, let it die!
        if (!VALID) {
            infeasible++;
            stats.incInfeasibleSolutions(infeasible);
            return 0;
        }

        // create new config object
        AbstractSimulationConfig config = setup.getPlayerConfig(playerDeck);

        // prepare fitness evaluation
        int fitness1 = 0;
        int fitness2 = 0;
        int totalWins = 0;
        int[] winsByOpponent = new int[setup.getOpponentCount()];

        Map<String, GameStatistics> gameStatisticsMap = new HashMap<>();
        // simulate games against ai
        for (int k = 0; k < setup.getOpponentCount(); k++) {

            // create new simulation
            SimulationRunner runner = new SimulationRunner(new SimulationProvider(config));

            // start simulation; main-thread waits until simulation is is done
            runner.run();

            // after simulation finished, get the game statistics for player 1
            GameStatistics stats = runner.getProvider().getSimulationResult().getPlayer1Stats();
            gameStatisticsMap.put(setup.getOpponentList().get(k), stats);

            // get wins from stats
            Object winStats = stats.get(Statistic.GAMES_WON);
            int currentWins = 0;
            if (winStats != null) {
                currentWins = (int) ((long) winStats);
            }

            // set wins against current opponent
            winsByOpponent[k] = currentWins;
            // accumulate totalWins
            totalWins += currentWins;

            // compute next opponent
            setup.setOpponentPointer((setup.getOpponentPointer() + 1) % (setup.getOpponentCount() - 1));

            // f1: total wins
            fitness1 = totalWins;

            // f2: compute standard deviation in regards of victories against other opponentList
            double std = 0;
            double avg = (int) ((totalWins / (pars.NUMBER_OF_GAMES * setup.getOpponentCount())));
            for (int j = 0; j < winsByOpponent.length; j++) {
                double v = (winsByOpponent[j] - avg);
                std += v * v;
            }
            std = Math.sqrt(std / winsByOpponent.length);
            fitness2 = (int) std;

        }

        int fitness = fitness1 + (fitness2 * -1);
//        log.info("fitness=" + fitness);

        if (best == null) {
            best = new Pair(gt, new Pair(fitness, gameStatisticsMap));
        }
        // save best solution with it's fitness value and game statistics
        if (best.getValue().getKey() < fitness) {
            best = null;
            best = new Pair(gt, new Pair(fitness, gameStatisticsMap));
        }

        return fitness;
    }

    private EvolutionResult<IntegerGene, Integer> nextGen(EvolutionResult<IntegerGene, Integer> c) {

        // get best fitness value in currentGeneration
        final int BEST = c.getBestFitness();
//        final int WORST = c.getWorstFitness();
        stats.getBestFitnessValues().add((double) BEST);
//        stats.getWorstFitnessValues().add((double) WORST);

        // set best solution
        if (BEST > overallBest) {
            overallBest = BEST;
        }

        // current generation's fitness values
        List<Integer> curGenFitness = c.getPopulation()
                .stream()
                .map(e -> e.getRawFitness())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // compute average and worst fitness in current generation
        double avg = 0;
        double worst = Double.MAX_VALUE;
        for(Integer f : curGenFitness) {
            avg += f;
            if(f > 0 && worst > f) {
                worst = f;
            }
        }
        avg = (avg/curGenFitness.size());
        stats.getAvgFitnessValues().add(avg);
        stats.getWorstFitnessValues().add(worst);

        if ((currentGeneration % ((int) pars.REFERENCE_GENERATION)) == 0) {

            computeSuccessfulChildren(c, curGenFitness);
            adaptMutationRate();

            // create generation detail log
            StringBuilder builder = new StringBuilder();

            builder.append("\n=====\n");
            builder.append("GENERATION " + currentGeneration + "\n");
            builder.append("=====\n");
            builder.append("best=" + BEST + "\n");
            builder.append("avg=" + avg + "\n");
            builder.append("worst=" + worst + "\n");
            builder.append("infeasible rate=" + ((double) infeasible / (double) (pars.getPOPULATION_SIZE())) + "\n");
            builder.append("mutation rate=" + mutator.getMutationRate() + "\n");
            builder.append("=====\n");

            final String details = builder.toString();

            stats.appendGenerationDetails(details);
            log.info(details);

        }

        // increment currentGeneration
        currentGeneration++; //= (int) c.getCurrentGeneration();

        return c;
    }

    private void adaptMutationRate() {

        double mutationRate = mutator.getMutationRate();
        double successRate = (double) successfulGenerations / pars.getPOPULATION_SIZE();

        log.info("successfulGen=" + successfulGenerations);
        log.info("successRate=" + successRate);

        double lambda = 1;
        // apply Rechenberg's rule
        if (successRate > (0.2)) {
            lambda = ADAPT_INRCREASE;
        } else if (successRate < (0.2)) {
            lambda = ADAPT_DECREASE;
        }

        mutationRate *= lambda;
        mutator.setMutationRate(mutationRate);
        successfulGenerations = 0;

    }

    private void computeSuccessfulChildren(EvolutionResult<IntegerGene, Integer> c, List<Integer> curGenFitness) {

        // compute successful children
        double successfulChildren = 0;
        if (ancestors == null) {
            ancestors = c.getPopulation();
        } else {

            List<Integer> lastGenFitness = ancestors
                    .stream()
                    .map(e -> e.getRawFitness())
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            for (int i = 0; i < curGenFitness.size(); i++) {
                if (curGenFitness.get(i) > lastGenFitness.get(i)) {
                    successfulChildren++;
                }
            }

            successfulGenerations = (int) successfulChildren;
            ancestors = c.getPopulation();

        }

    }

    private void foo(Genotype<IntegerGene> result) {

        // check if deck is valid
        boolean resultValid = val.isDeckValid(result);
        if (!resultValid) {
            log.error("could not found a feasible solution!");
            log.info("\ncurrentGeneration:" + currentGeneration);
        } else {


            Collection<String> lines = new ArrayList<>();
//            lines.add("\nbest solution:\n" + printDeck(resultCollection));

            stats.appendBest(best);
            lines.add(stats.toString());

            long curTime = System.currentTimeMillis();

            try {
                File path = new File(PATH_LOG);
                if (!path.exists()) {
                    path.mkdir();
                }
                String fileName = FILE_LOG + curTime + ".log";
                Files.write(Paths.get(PATH_LOG + "/" + fileName), lines);
                log.info("log file written:" + PATH_LOG + "/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // gnu plot
            GnuPlot plot = new GnuPlot(stats, curTime, PATH_LOG + "/");
//            plot.show();

        }

    }

    public void clear() {

        best = null;
        ancestors = null;
        currentGeneration = 0;
        successfulGenerations = 0;
        overallBest = 0;
        infeasible = 0;

    }

    public static GAStatistic getStats() {
        return stats;
    }

    public static void main(String[] args) {

             //
        int max = 10;
        //
        List<GAStatistic> runs = new ArrayList<>();

        // run experiment several times
        for(int r = 0; r < max; r++) {

            // configure evolution
            GA ga = new GA(
                    new DeckMutator(),
                    new GreedyOptimizeMove(new WeightedHeuristic()),
//                new GreedyOptimizeTurn(new WeightedHeuristic()),
                    new GASetup(),
                    new GAParameter(
                            2,// parents that reproduce 10
                            10,// size of population 20
                            1.8,// mutation rate 0.9
                            16,// number of games
                            50,// max generations
                            Integer.MAX_VALUE,// steady fitness
                            5,// reference generation
                            4,// tournament select
                            HeroClass.HUNTER// hero class
                    ),
                    new GAStatistic(),
                    new GAValidator()
            );

            // start the evolution
            try {
                Thread t = new Thread() {

                    @Override
                    public void run() {
                        Genotype<IntegerGene> best = ga.start();
                        ga.foo(best);
                        runs.add(GA.getStats());
                        ga.clear();
                    }

                };
                t.run();
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            Thread.currentThread().join();

        }

        GAStatistic.analyze(runs);
    }

}
