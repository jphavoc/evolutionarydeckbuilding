package main.simulation;

import net.demilich.metastone.game.Attribute;
import net.demilich.metastone.game.behaviour.IBehaviour;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardCollection;
import net.demilich.metastone.game.cards.HeroCard;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.decks.DeckFactory;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.game.gameconfig.PlayerConfig;
import net.demilich.metastone.game.spells.desc.BattlecryDesc;
import net.demilich.metastone.game.spells.desc.SpellDesc;
import net.demilich.metastone.game.spells.desc.condition.ConditionDesc;
import net.demilich.metastone.game.targeting.CardLocation;
import net.demilich.metastone.game.targeting.CardReference;
import net.demilich.metastone.game.targeting.TargetSelection;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

/**
 * @author created by Jens Plümer on 13.11.17
 */
public class PlayerConfigWrapper extends PlayerConfig implements Externalizable {

    public PlayerConfigWrapper() {
        super();
    }

    public PlayerConfigWrapper(PlayerConfig config) {
        this();
        setBehaviour(config.getBehaviour());
        setDeck(config.getDeck());
        setHeroCard(config.getHeroCard());
        setName(config.getName());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        // write name
        out.writeObject(getName());
        // write behaviour
//        out.writeObject(getBehaviour());//TODO not serializable; write a reference, a name to the object

        // write deck
        Deck deck = getDeck();
//        out.writeObject(deck.getCards());//TODO card collection not serializable; well cards are not serializebale
//        out.writeChars(deck.getDescription());//TODO nullpointer
//        out.writeChars(deck.getFilename());//TODO nullpointer
        out.writeObject(deck.getName());
//        out.writeObject(deck.getHeroClass());

        // write hero
        HeroCard hero = getHeroCard();
        // write battle cry
        BattlecryDesc battlecryDesc = hero.getBattlecry();
//        out.writeObject(battlecryDesc.targetSelection);//TODO nullpointer
//        out.writeObject(battlecryDesc.condition);//TODO nullpointer
//        out.writeObject(battlecryDesc.spell);//TODO nullpointer
//        out.writeChars(battlecryDesc.description);//TODO nullpointer
        // write mana cost
//        out.writeInt(hero.getBaseManaCost());
        // write attributes
//        out.writeObject(hero.getAttributes());
        // write card id
//        out.writeObject(hero.getCardId());
        // write card reference
//        CardReference cardReference = hero.getCardReference();
//        out.writeInt(cardReference.getCardId());
//        out.writeInt(cardReference.getPlayerId());
//        out.writeObject(cardReference.getCardName());
//        out.writeObject(cardReference.getLocation());
        // write owner id
//        out.writeInt(hero.getOwner());
        // write id
//        out.writeInt(hero.getId());
        // write card location
//        out.writeObject(hero.getLocation());
        // write description
//        out.writeChars(hero.getDescription());//TODO nullpointer
        // write collectible
//        out.writeBoolean(hero.isCollectible());

        //23

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        // read name
        setName((String) in.readObject());
        // read behaviour
//        setBehaviour((IBehaviour) in.readObject());

        // read deck
//        CardCollection cardCollection = (CardCollection) in.readObject();
//        String descritpion = (String) in.readObject();
//        String filename = (String) in.readObject();
        String name = (String) in.readObject();
//        HeroClass heroClass = (HeroClass) in.readObject();
        // build deck
        Deck deck = new Deck(null);
//        deck.getCards().addAll(cardCollection);
//        deck.setDescription(description);
//        deck.setFilename(filename);
        deck.setName(name);
//        setDeck(deck);

        // write hero
//        HeroCard hero = (HeroCard) in.readObject();
        // write battle cry
        BattlecryDesc battlecryDesc = new BattlecryDesc();
//        battlecryDesc.targetSelection = (TargetSelection) in.readObject();
//        battlecryDesc.condition = (ConditionDesc) in.readObject();
//        battlecryDesc.spell = (SpellDesc) in.readObject();
//        battlecryDesc.description = (String) in.readObject();
//        hero.setBattlecry(battlecryDesc);
        // write mana cost
//        hero.setAttribute(Attribute.BASE_MANA_COST, in.readInt());
        // write attributes
//        Map<Attribute, Object> attributeObjectMap = (Map<Attribute, Object>) in.readObject();
//        for(Map.Entry<Attribute, Object> entry : attributeObjectMap.entrySet()) {
//            hero.setAttribute(entry.getKey(), entry.getValue());
//        }
        // write card id
//        in.readObject();
        // write card reference
//        int cardId = in.readInt();
//        int playerId = in.readInt();
//        String cardname = (String) in.readObject();
//        CardLocation cardLocation = (CardLocation) in.readObject();
//        CardReference cardReference = new CardReference(playerId, cardLocation, cardId, cardname);
        // write owner id
//        hero.setOwner(in.readInt());
        // write id
//        hero.setId(in.readInt());
        // write card location
//        hero.setLocation((CardLocation) in.readObject());
        // write description
//        hero.setDescription((String) in.readObject());
        // write collectible
//        hero.setCollectible(in.readBoolean());
//        setHeroCard(hero);

        //24
    }
}
