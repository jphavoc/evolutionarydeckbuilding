package main.simulation;

import net.demilich.metastone.SimulationConfig;
import net.demilich.metastone.game.Player;
import net.demilich.metastone.game.behaviour.GreedyOptimizeMove;
import net.demilich.metastone.game.behaviour.NoAggressionBehaviour;
import net.demilich.metastone.game.behaviour.PlayRandomBehaviour;
import net.demilich.metastone.game.behaviour.heuristic.WeightedHeuristic;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardCatalogue;
import net.demilich.metastone.game.cards.CardSet;
import net.demilich.metastone.game.cards.HeroCard;
import net.demilich.metastone.game.decks.DeckFactory;
import net.demilich.metastone.game.decks.DeckFormat;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.game.gameconfig.GameConfig;
import net.demilich.metastone.game.gameconfig.PlayerConfig;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author created by Jens Plümer on 09.11.17
 */
public class SimulationTestConfig implements SimulationConfig, Serializable {

    @Override
    public GameConfig getGameConfig() {

        DeckFormat deckFormat = new DeckFormat();
        for (CardSet set : CardSet.values()) {
            deckFormat.addSet(set);
        }
        HeroClass heroClass1 = getRandomClass();
        PlayerConfig player1Config = new PlayerConfig(DeckFactory.getRandomDeck(heroClass1, deckFormat), new PlayRandomBehaviour());
        player1Config.setName("Player 1");
//        player1Config.setBehaviour(new GreedyOptimizeMove(new WeightedHeuristic()));
        player1Config.setBehaviour(new NoAggressionBehaviour());
        player1Config.setHeroCard(getHeroCardForClass(heroClass1));
        Player player1 = new Player(player1Config);

        HeroClass heroClass2 = getRandomClass();
        PlayerConfig player2Config = new PlayerConfig(DeckFactory.getRandomDeck(heroClass2, deckFormat), new PlayRandomBehaviour());
        player2Config.setName("Player 2");
        player2Config.setBehaviour(new NoAggressionBehaviour());
        player2Config.setHeroCard(getHeroCardForClass(heroClass2));
        Player player2 = new Player(player2Config);

        GameConfig gameConfig = new GameConfig();
        gameConfig.setNumberOfGames(5);
        gameConfig.setDeckFormat(deckFormat);
        gameConfig.setPlayerConfig1(player1Config);
        gameConfig.setPlayerConfig2(player2Config);

        return gameConfig;
    }

    protected HeroClass getRandomClass() {
        HeroClass randomClass = HeroClass.ANY;
        HeroClass[] values = HeroClass.values();
        while (!randomClass.isBaseClass()) {
            randomClass = values[ThreadLocalRandom.current().nextInt(values.length)];
        }

        return randomClass;
    }

    protected HeroCard getHeroCardForClass(HeroClass heroClass) {
        for (Card card : CardCatalogue.getHeroes()) {
            HeroCard heroCard = (HeroCard) card;
            if (heroCard.getHeroClass() == heroClass) {
                return heroCard;
            }
        }
        return null;
    }

}
