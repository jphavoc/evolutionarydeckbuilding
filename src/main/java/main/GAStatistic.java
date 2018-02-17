package main;

import javafx.util.Pair;
import main.simulation.DeckProvider;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.statistics.GameStatistics;
import net.demilich.metastone.game.statistics.Statistic;
import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.engine.EvolutionStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author created by Jens Plümer on 01.12.17
 */
public class GAStatistic {


    private static final Logger log = LoggerFactory.getLogger(GAStatistic.class);

    private StringBuilder builder;

    private List<Double> bestFitnessValues;
    private List<Double> avgFitnessValues;
    private List<Double> worstFitnessValues;
    private EvolutionStatistics evolutionStatistics;
    private int infeasibleSolutions = 0;
    private GAParameter pars;

    public GAStatistic() {
        builder = new StringBuilder();
        bestFitnessValues = new ArrayList<>();
        avgFitnessValues = new ArrayList<>();
        worstFitnessValues = new ArrayList<>();
    }

    public GAStatistic(GAStatistic obj) {
//        this.builder = new StringBuilder(obj.builder);
        this.bestFitnessValues = new ArrayList<>(obj.bestFitnessValues);
        this.avgFitnessValues = new ArrayList<>(obj.avgFitnessValues);
        this.worstFitnessValues = new ArrayList<>(obj.worstFitnessValues);
//        this.evolutionStatistics = obj.evolutionStatistics.
    }


    public EvolutionStatistics getEvolutionStatistics() {
        return evolutionStatistics;
    }

    public void setEvolutionStatistics(EvolutionStatistics evolutionStatistics) {
        this.evolutionStatistics = evolutionStatistics;
    }

    public List<Double> getBestFitnessValues() {
        return bestFitnessValues;
    }

    public List<Double> getAvgFitnessValues() {
        return avgFitnessValues;
    }

    public List<Double> getWorstFitnessValues() {
        return worstFitnessValues;
    }

    public void incInfeasibleSolutions(int infeasible) {
        infeasibleSolutions += infeasible;
    }

    public void appendParameters(GAParameter pars) {

        this.pars = pars;
        builder.append(pars.toString());

    }

    public void appendOpponents(Collection<String> opponents) {

        builder.append("\nopponents="
                + opponents.stream()
                .map(e -> e)
                .collect(Collectors.toList())
        );

    }

    public void appendGenerationDetails(String details) {

        builder.append(details);

    }

    public void appendBest(Pair<Genotype<IntegerGene>, Pair<Integer, Map<String, GameStatistics>>> best) {

        Collection<Integer> mana = new ArrayList<>();

        builder.append("\n=====\n");
        builder.append("BEST\n");
        builder.append("=====\n");

        // convert result from genotype to phenotype
        Collection<Card> resultCollection = new ArrayList<>();
        best.getKey().getChromosome()
                .stream()
                .sorted((o1, o2) -> Integer.compare(o1.getAllele(), o2.getAllele()))
                .forEach(x -> {

                    if (GASetup.getIntegerCardMap().containsKey(x.getAllele())) {
                        Card c = (GASetup.getIntegerCardMap().get(x.getAllele()));
                        resultCollection.add(c);
                    }
                });

        mana = appendDeck(resultCollection, mana);

        int[] occurrences = new int[8];
        for (Integer i : mana) {
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    occurrences[i - 1] += 1;
                    break;
                default:
                    occurrences[7] += 1;
                    break;
            }
        }

        builder.append("\n=====\n");
        builder.append("MANA CURVE\n");
        builder.append("=====\n");

        for (int i = 0; i < occurrences.length; i++) {
            if (i != 7) {
                builder.append((i + 1) + "=" + occurrences[i]);
            } else {
                builder.append("7 or more=" + occurrences[i]);
            }
            builder.append("\n");
        }

        builder.append("\n=====\n");
        builder.append("FITNESS=" + best.getValue().getKey() + "\n");
        builder.append("=====\n");

        builder.append("\n=====\n");
        builder.append("GAME STATISTICS\n");
        builder.append("=====\n");

        best.getValue().getValue().entrySet().stream().forEach(e -> {
            builder.append("\nopponent=" + e.getKey());
            builder.append("\n");

            Object o1 = e.getValue().get(Statistic.GAMES_WON);
            Object o2 = e.getValue().get(Statistic.GAMES_LOST);
            long won = o1 != null ? (long) o1 : 0;
            long lost = o2 != null ? (long) o2 : 0;
            long gamesPlayed = won + lost;

            builder.append("actually played games=" + gamesPlayed);

            for (Statistic statistic : Statistic.values()) {

                Object value = e.getValue().get(statistic);

                switch (statistic) {
                    case DAMAGE_DEALT:
                    case MANA_SPENT:
                    case SPELLS_CAST:
                    case TURNS_TAKEN:
                    case HEALING_DONE:
                    case MINIONS_PLAYED:
                    case CARDS_PLAYED:
                    case HERO_POWER_USED:
                    case WEAPONS_EQUIPPED:
                        if (value != null) {
                            builder.append(statistic.name() + "=" + (Long) value / gamesPlayed);
                        }
                        break;
                    case WIN_RATE:
                        builder.append(statistic.name() + "=" + value);
                        break;
                }
            }

            builder.append("\n");
        });


    }

    private Collection<Integer> appendDeck(Collection<Card> resultCollection, Collection<Integer> mana) {

        resultCollection.stream().forEach(e -> {
            builder.append("[name=" + e.getName() + ", ");
            builder.append("class=" + e.getHeroClass() + ", ");
            builder.append("rarity=" + e.getRarity() + ", ");
//            builder.append("race"+ e.getRace() + ", ");
            builder.append("mana=" + e.getBaseManaCost() + "]\n");
            mana.add(e.getBaseManaCost());
        });

        builder.append("\n");

        return mana;
    }

    @Override
    public String toString() {

        builder.append("\n=====\n");
        builder.append("JENETICS STATISTICS\n");
        builder.append("=====\n");
        builder.append(evolutionStatistics.toString());

        builder.append("\ninfeasible rate=" + (infeasibleSolutions));

        return builder.toString();
    }

    public void appendInitialDeck(Deck playerDeck) {

        builder.append("\n=====\n");
        builder.append("Initial Deck\n");
        builder.append("=====\n");

        appendDeck(playerDeck.getCards().toList(), new ArrayList<>());


    }

    public static void analyze(List<GAStatistic> statistics) {

        StringBuilder bf = new StringBuilder();

        int n = statistics.size();
        int m = statistics.get(0).getBestFitnessValues().size();

        ArrayList<Double> best = new ArrayList<>();
        ArrayList<Double> avg = new ArrayList<>();
        ArrayList<Double> worst = new ArrayList<>();

        bf.append("BEST\n");

        for (int i = 0; i < m; i++) {
            double value = 0;
            boolean b = false;
            for (int j = 0; j < n; j++) {
                if (statistics.get(j).getBestFitnessValues().get(i) == 50) {
                    value += statistics.get(j).getBestFitnessValues().get(i);
                    b = true;
                }
            }

            if (b) {
                bf.append(i);
                bf.append(' ');
                bf.append(value);
                bf.append(' ');
                bf.append("\n");
            }
//            best.add(value / n);
        }

        bf.append("AVG\n");

        for (int i = 0; i < m; i++) {
            double value = 0;
            boolean b = false;
            for (int j = 0; j < n; j++) {
                if (statistics.get(j).getAvgFitnessValues().get(i) == 50) {
                    value += statistics.get(j).getAvgFitnessValues().get(i);
                    b = true;
                }
            }

            if (b) {
                bf.append(i);
                bf.append(' ');
                bf.append(value);
                bf.append(' ');
                bf.append("\n");
            };

//            avg.add(value / n);
        }

        bf.append("WORST\n");

        for (int i = 0; i < m; i++) {
            double value = 0;
            boolean b = false;
            for (int j = 0; j < n; j++) {
                if (statistics.get(j).getWorstFitnessValues().get(i) == 50) {
                    value += statistics.get(j).getWorstFitnessValues().get(i);
                    b = true;
                }
            }

            if (b) {
                bf.append(i);
                bf.append(' ');
                bf.append(value);
                bf.append(' ');
                bf.append("\n");
            }

//            worst.add(value / n);
        }

        try (BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("logs/experiment" + "-" + System.currentTimeMillis() + ".data")))) {
            bwr.write(bf.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

        File folder = new File("/Users/jp/Git/evolutionarydeckbuilding/logs");
        File[] listOfFiles = folder.listFiles();

        List<List<Double>> avg = new ArrayList<>();

        for (File f : listOfFiles) {

            if (f.getName().contains("WORST")) {
                try {
                    List<Double> a = new ArrayList<>();
                    List<String> lines = Files.readAllLines(f.toPath());
                    for (int i = 1; i < lines.size(); i++) {
                        String[] split = lines.get(i).split(" ");
                        a.add(Double.parseDouble(split[1]));
                    }
                    avg.add(a);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        int n = avg.size();
        int m = avg.get(0).size();
        StringBuilder bf = new StringBuilder();

        log.info("n=" + n);
        log.info("m=" + m);

        for (int i = 0; i < m; i++) {

            double value = 0;
            boolean b = false;

            for (int j = 0; j < n; j++) {

                if (avg.get(j).size() == 50) {
                    value += avg.get(j).get(i);
                    b = true;
                }

            }

            if (b) {
                bf.append(i);
                bf.append(' ');
                bf.append(value / n);
                bf.append(' ');
                bf.append("\n");
            }

//            best.add(value / n);
        }

        log.info("worst=\n" + bf.toString());

//        log.info("avg=" + avg.toString());


    }

}
