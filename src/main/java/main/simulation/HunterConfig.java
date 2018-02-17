package main.simulation;

import main.GASetup;
import net.demilich.metastone.SimulationConfig;
import net.demilich.metastone.game.Player;
import net.demilich.metastone.game.behaviour.Behaviour;
import net.demilich.metastone.game.behaviour.GreedyOptimizeMove;
import net.demilich.metastone.game.behaviour.NoAggressionBehaviour;
import net.demilich.metastone.game.behaviour.PlayRandomBehaviour;
import net.demilich.metastone.game.behaviour.heuristic.WeightedHeuristic;
import net.demilich.metastone.game.cards.CardSet;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.decks.DeckFactory;
import net.demilich.metastone.game.decks.DeckFormat;
import net.demilich.metastone.game.entities.heroes.Hero;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.game.gameconfig.GameConfig;
import net.demilich.metastone.game.gameconfig.PlayerConfig;
import net.demilich.metastone.gui.deckbuilder.DeckFormatProxy;

/**
 * @author created by Jens Plümer on 10.11.17
 */
public class HunterConfig extends AbstractSimulationConfig {

    public HunterConfig(float games, HeroClass opponent, Behaviour opponentBehaviour, Deck opponentDeck, Behaviour playerBehaviour, Deck playerDeck) {
        super(games, opponent, opponentBehaviour, opponentDeck, HeroClass.HUNTER, playerBehaviour, playerDeck);

    }

    @Override
    public GameConfig getGameConfig() {

        PlayerConfig p1Config = new PlayerConfig(p1Deck, p1Behaviour);
        p1Config.setName("p1");
        p1Config.setHeroCard(getHeroCardForClass(p1HeroClass));

        PlayerConfig p2Config = new PlayerConfig(p2Deck, p2Behaviour);
        p2Config.setName("p2");
        p2Config.setHeroCard(getHeroCardForClass(p2HeroClass));

        GameConfig gameConfig = new GameConfig();
        gameConfig.setNumberOfGames((int) numberOfGames);
        gameConfig.setPlayerConfig1(p1Config);
        gameConfig.setPlayerConfig2(p2Config);
        gameConfig.setDeckFormat(GASetup.getDeckFormat());

        return gameConfig;
    }
}
