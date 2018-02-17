package main.simulation;

import net.demilich.metastone.SimulationConfig;
import net.demilich.metastone.game.behaviour.Behaviour;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardCatalogue;
import net.demilich.metastone.game.cards.HeroCard;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.game.gameconfig.GameConfig;

import java.util.stream.Collectors;

/**
 * @author created by Jens Plümer on 10.11.17
 */
public abstract class AbstractSimulationConfig implements SimulationConfig {

    protected final HeroClass p1HeroClass;
    protected final HeroClass p2HeroClass;
    protected final float numberOfGames;
    protected final Behaviour p1Behaviour;
    protected final Behaviour p2Behaviour;
    protected final Deck p1Deck;
    protected final Deck p2Deck;

    public AbstractSimulationConfig(float games, HeroClass opponent, Behaviour opponentBehaviour, Deck opponentDeck, HeroClass player, Behaviour playerBehaviour, Deck playerDeck) {
        this.numberOfGames = games;
        this.p2HeroClass = opponent;
        this.p1HeroClass = player;
        this.p2Behaviour = opponentBehaviour;
        this.p1Behaviour = playerBehaviour;
        this.p2Deck = opponentDeck;
        this.p1Deck = playerDeck;
    }

    protected HeroCard getHeroCardForClass(HeroClass heroClass) {
        for (Card card : CardCatalogue.getHeroes()) {
            HeroCard heroCard = (HeroCard) card;
            if (heroCard.getHeroClass() == heroClass) {
                return heroCard;
            }
        }

        throw new IllegalArgumentException("hero class not found:" + heroClass);
    }

    @Override
    abstract public GameConfig getGameConfig();

    @Override
    public String toString() {

        return "[numerOfGames=" + numberOfGames +
                ", p1Deck=" + p1Deck.getCards().toList().stream().map(e -> e.getCardId()).collect(Collectors.toList()) +
                "]";

    }
}
