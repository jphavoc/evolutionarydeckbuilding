package main;

import main.simulation.DeckProvider;
import main.simulation.HunterConfig;
import net.demilich.metastone.game.behaviour.Behaviour;
import net.demilich.metastone.game.behaviour.IBehaviour;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardCatalogue;
import net.demilich.metastone.game.cards.CardSet;
import net.demilich.metastone.game.cards.CardType;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.decks.DeckFactory;
import net.demilich.metastone.game.decks.DeckFormat;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.gui.cards.CardProxy;
import net.demilich.metastone.gui.deckbuilder.DeckFormatProxy;
import net.demilich.metastone.gui.deckbuilder.DeckProxy;
import org.jenetics.Chromosome;
import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author created by Jens Plümer on 03.12.17
 */
public class GASetup {

    private static final Logger log = LoggerFactory.getLogger(GASetup.class);

    private static CardProxy cardProxy;
    private static DeckProxy deckProxy;
    private static DeckFormatProxy deckFormatProxy;
    private static DeckFormat deckFormat;
    private static Map<Integer, Card> integerCardMap;
    private static boolean cardsLoaded;
    private static int cardCount;

    private List<String> opponentList;
    private Map<String, Deck> opponentDeckMap;
    private Map<String, HeroClass> opponentClassMap;
    private int opponentPointer = 0;
    private boolean getInitialPlayerDeck;
    private float numberOfGames;
    private Behaviour ai;

    public GASetup() {

        // load resources, but only once
        if (cardProxy == null && deckProxy == null) {
            try {
                cardProxy = new CardProxy();
                deckProxy = new DeckProxy();
                deckProxy.loadDecks();
                DeckFormat deckFormat = new DeckFormat();
                for (CardSet set : CardSet.values()) {
                    deckFormat.addSet(set);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        loadOpponentDecks();
        loadCardMap();

        this.getInitialPlayerDeck = true;

    }


    public static Map<Integer, Card> loadCardMap() {

        if (!cardsLoaded) {

            integerCardMap = new HashMap<>();
            try {
                deckFormatProxy = new DeckFormatProxy();
                deckFormatProxy.loadDeckFormats();
                deckFormat = deckFormatProxy.getDeckFormatByName("Standard");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            if (deckFormat != null) {
                for (Card e : CardCatalogue.getAll().toList()) {
                    String name = e.getName();
                    if (!e.getCardType().equals(CardType.HERO)
                            && deckFormat.isInFormat(e)
                            && name != null
                            && e.isCollectible()
                            && !integerCardMap.values().stream().anyMatch(p -> p.getName().equals(name))) {

                        integerCardMap.put(cardCount, e);
                        cardCount++;
                    }
                }
            } else {
                log.error("deck format was null");
            }
            cardCount -= 1;
            cardsLoaded = true;
            log.info("cardCount=" + cardCount);
        }

        return integerCardMap;
    }

    public void loadOpponentDecks() {

        opponentDeckMap = new HashMap<>();
        opponentClassMap = new HashMap<>();
        opponentList = new ArrayList<>();

        DeckProvider.getAll().stream().forEach(d -> {
            if (d.getKey() == null || d.getValue().getKey() == null || d.getValue().getValue() == null) {
                throw new IllegalArgumentException("opponent deck not valid");
            }
            opponentDeckMap.put(d.getKey(), d.getValue().getValue());
            opponentClassMap.put(d.getKey(), d.getValue().getKey());
            opponentList.add(d.getKey());
        });
    }

    public Deck getOpponentDeck() {
        return opponentDeckMap.get(opponentList.get(opponentPointer));
    }

    public Deck getPlayerDeck(Genotype<IntegerGene> gt) {

        if (getInitialPlayerDeck) {
            getInitialPlayerDeck = false;
            Deck playerDeck = opponentDeckMap.get("Purple Rank #1 Legend Midrange Hunter");
            GA.getStats().appendInitialDeck(playerDeck);
            return playerDeck;
        }

        return buildDeck(gt.getChromosome());

    }

    private Deck buildDeck(Chromosome<IntegerGene> chromosome) {

        int i = 0;
        Card[] cards = new Card[30];
        for (IntegerGene gene : chromosome.stream().collect(Collectors.toList())) {
            cards[i] = GASetup.getIntegerCardMap().get(gene.getAllele());
            i++;
        }

        try {
            Deck deck = DeckFactory.getDeckConsistingof(30, cards);
            return deck;
        } catch (Exception e) {
//            log.info("error");
        }

        return null;
    }


    public HunterConfig getPlayerConfig(Deck playerDeck) {
        return new HunterConfig(
                numberOfGames,
                // opponent
                opponentClassMap.get(opponentList.get(opponentPointer)),
                ai,
//                new GreedyOptimizeTurn(new WeightedHeuristic()),
                getOpponentDeck(),
                // player1
                ai,
//                new GreedyOptimizeTurn(new WeightedHeuristic()),
                playerDeck);
    }

    public static CardProxy getCardProxy() {
        return cardProxy;
    }

    public static DeckProxy getDeckProxy() {
        return deckProxy;
    }

    public static Map<Integer, Card> getIntegerCardMap() {
        return integerCardMap;
    }

    public static int getCardCount() {
        return cardCount;
    }

    public List<String> getOpponentList() {
        return opponentList;
    }

    public Map<String, Deck> getOpponentDeckMap() {
        return opponentDeckMap;
    }

    public Map<String, HeroClass> getOpponentClassMap() {
        return opponentClassMap;
    }

    public int getOpponentPointer() {
        return opponentPointer;
    }

    public boolean isGetInitialPlayerDeck() {
        return getInitialPlayerDeck;
    }

    public void setOpponentPointer(int opponentPointer) {
        this.opponentPointer = opponentPointer;
    }

    public int getOpponentCount() {
        return this.opponentList.size();
    }

    public static Card getCard(String name) {
        return GASetup.getIntegerCardMap().values()
                .stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void setNumberOfGames(float numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public IBehaviour getAi() {
        return ai;
    }

    public void setAi(Behaviour ai) {
        this.ai = ai;
    }

    public static DeckFormat getDeckFormat() {
        return deckFormat;
    }
}
