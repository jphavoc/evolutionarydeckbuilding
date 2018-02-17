package main;

import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardType;
import net.demilich.metastone.game.cards.Rarity;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import org.jenetics.Genotype;
import org.jenetics.IntegerGene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author created by Jens Plümer on 03.12.17
 */
public class GAValidator {

    private GAParameter pars;

    public GAValidator() {
    }

    private boolean isLegendary(int value) {
        Card card = GASetup.getIntegerCardMap().get(value);
        return card.getRarity().isRarity(Rarity.LEGENDARY);
    }

    private boolean isCardTypeValid(int value) {

        HeroClass classOfCard = GASetup.getIntegerCardMap().get(value).getHeroClass();

        HeroClass[] forbiddenClasses = {
                HeroClass.DRUID,
//                HeroClass.HUNTER,
                HeroClass.MAGE,
                HeroClass.PALADIN,
                HeroClass.PRIEST,
                HeroClass.ROGUE,
                HeroClass.SHAMAN,
                HeroClass.WARLOCK,
                HeroClass.WARRIOR,
        };

        // if the hero class equals the player's hero class, return true
        if (classOfCard.equals(pars.getHeroClass())) {
            return true;
        }

        // otherwise check if hero class equals one of the other heroes, return false
        for (HeroClass hc : forbiddenClasses) {
            if (classOfCard.equals(hc)) {
                return false;
            }
        }

        return true;
    }

    public int getValidCard(Collection<Integer> col) {

        // select a random card
        int value = new Random().nextInt(GASetup.getCardCount());
        col.add(value);

        if (!isCardValid(col, value)) {
            return getValidCard(col);
        }

        return value;
    }

    private boolean isCardValid(Collection<Integer> col, int value) {

        if (isHeroCard(value)) {
            return false;
        }

        // if the card is a specific hero card, check if class is valid
        if (!isCardTypeValid(value)) {
            return false;
        }

        // check if card is already contained by the deck
        Card card = GASetup.getIntegerCardMap().get(value);
        Collection<Integer> duplicates = new ArrayList<>();

        col.stream().forEach(e -> {
            if (GASetup.getIntegerCardMap().get(e).getName().equals(card.getName())) {
                duplicates.add(e);
            }
        });

        int frequency = duplicates.size();
        if (frequency < 3) {
            // only one copy of a legendary is allowed
            if (isLegendary(value) && frequency == 2) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean isHeroCard(int value) {

        if (GASetup.getIntegerCardMap().containsKey(value)) {
            Card card = GASetup.getIntegerCardMap().get(value);
            CardType type = card.getCardType();
            return type.isCardType(CardType.HERO);
        }

        throw new IllegalArgumentException("index=" + value);
    }

    public boolean isDeckValid(Collection<Integer> deck) {

        for (Integer card : deck) {
            boolean valid = isCardValid(deck, card);
            if (!valid) {
                return false;
            }
        }

        return true;
    }

    public boolean isDeckValid(Genotype<IntegerGene> gt) {
        return isDeckValid(gt.getChromosome()
                .stream()
                .map(e -> e.getAllele())
                .collect(Collectors.toList()));
    }

    public boolean isDeckValid(Card[] deck) {

        Collection<Integer> col = new ArrayList<>();

        for (Card c : deck) {
            col.add(GASetup.getIntegerCardMap().entrySet()
                    .stream()
                    .filter(p -> p.getValue().getName().equals(c.getName()))
                    .findFirst()
                    .get()
                    .getKey());
        }

        return isDeckValid(col);
    }

    public GAParameter getPars() {
        return pars;
    }

    public void setPars(GAParameter pars) {
        this.pars = pars;
    }
}
