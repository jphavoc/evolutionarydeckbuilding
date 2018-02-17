package main.simulation;

import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardSet;
import net.demilich.metastone.game.cards.HeroCard;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.decks.DeckFormat;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.game.gameconfig.GameConfig;
import net.demilich.metastone.game.gameconfig.PlayerConfig;
import net.demilich.metastone.game.targeting.CardReference;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * @author created by Jens Plümer on 13.11.17
 */
public class GameConfigWrapper extends GameConfig implements Externalizable {

    public GameConfigWrapper() {
        super();
    }

    public GameConfigWrapper(GameConfig config) {
        this();
        setNumberOfGames(config.getNumberOfGames());
        setDeckFormat(config.getDeckFormat());
        setPlayerConfig1(config.getPlayerConfig1());
        setPlayerConfig2(config.getPlayerConfig2());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        // write number of games
        out.writeInt(getNumberOfGames());

        // write deckformat
        DeckFormat deckFormat = getDeckFormat();
//        out.writeObject(deckFormat);
        out.writeObject(deckFormat.getName());
//        out.writeChars(deckFormat.getFilename());
        int numberOfCardSets = deckFormat.getCardSets().size();
        out.writeInt(numberOfCardSets);
        for (CardSet set :deckFormat.getCardSets()) {
            out.writeObject(set);
        }

        // write player config 1 & 2s
        out.writeObject(new PlayerConfigWrapper(getPlayerConfig1()));
        out.writeObject(new PlayerConfigWrapper(getPlayerConfig2()));

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        // read number of games
        setNumberOfGames(in.readInt());

        // read deckformat
        DeckFormat deckFormat = new DeckFormat();
//        deckFormat.setFilename((String) in.readObject());
        deckFormat.setName((String) in.readObject());
        List<CardSet> cardSets = new ArrayList<>();
        int numberOfCardSets = in.readInt();
        for(int i = 0; i < numberOfCardSets; i++) {
            cardSets.add((CardSet) in.readObject());
        }
        deckFormat.getCardSets().addAll(cardSets);
        setDeckFormat(deckFormat);

        // read player config 1 & 2
        setPlayerConfig1((PlayerConfig) in.readObject());
        setPlayerConfig2((PlayerConfig) in.readObject());

    }
}
