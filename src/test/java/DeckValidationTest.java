import main.GA;
import main.GAParameter;
import main.GASetup;
import main.GAValidator;
import net.demilich.metastone.game.cards.*;
import net.demilich.metastone.game.entities.heroes.Hero;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;

/**
 * @author created by Jens Plümer on 21.11.17
 */
public class DeckValidationTest {

    private static final Logger log = LoggerFactory.getLogger(DeckValidationTest.class);

    @Test
    public void validateDeck_returns_false() {

        new GASetup();
        GASetup.loadCardMap();
        GAValidator val = new GAValidator();
        val.setPars(new GAParameter(
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                HeroClass.HUNTER
        ));

        // common cards
        Card c1 = GASetup.getCard("Damaged Golem");
        Card c2 = GASetup.getCard("Friendly Bartender");

        Assert.assertFalse(c1 == null);
        Assert.assertFalse(c2 == null);

        Card[] cards = {
                c1,
                c1,

                c2,
                c2,
                c2
        };

        boolean valid = val.isDeckValid(cards);
        Assert.assertFalse(valid);

        // check for correct frequent of legendary card (HUNTER)
        // only one copy of a legendary is allowed
        Card c3 = GASetup.getCard("Hogger, Doom of Elwynn");
        Assert.assertFalse(c3 == null);

        Card[] cards2 = {
                c1,
                c1,

                c3,
                c3,
        };

        valid = val.isDeckValid(cards2);
        Assert.assertFalse(valid);

        // check for cards that does not correspond to the hero class
        Card c4 = GASetup.getCard("Blizzard");
        Assert.assertFalse(c3 == null);

        Card[] cards4 = {
                c4,
        };

        valid = val.isDeckValid(cards2);
        Assert.assertFalse(valid);


        // at least check a deck that is valid
        Card[] cards3 = {
                c1,
                c1,
                c2,
                c2,
                c3,
        };

        valid = val.isDeckValid(cards3);
        Assert.assertTrue(valid);

    }

}
