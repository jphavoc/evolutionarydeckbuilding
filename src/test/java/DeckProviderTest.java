import javafx.util.Pair;
import main.GA;
import main.simulation.DeckProvider;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.gui.cards.CardProxy;
import net.demilich.metastone.gui.deckbuilder.DeckProxy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author created by Jens Plümer on 23.11.17
 */
public class DeckProviderTest {

    @Before
    public void setup() {
        // setup all resources
        new CardProxy();
        new DeckProxy();
    }

    @Test
    public void test() {

        Pair<String, Pair<HeroClass, Deck>> p = DeckProvider.getMidRangeHunter();

        // check for null values
        Assert.assertNotNull(p);
        Assert.assertNotNull(p.getKey());
        Assert.assertNotNull(p.getValue());
        Assert.assertNotNull(p.getValue().getKey());
        Assert.assertNotNull(p.getValue().getValue());

        // check deck name and deck hero class
        Assert.assertEquals("Purple Rank #1 Legend Midrange Hunter", p.getKey());
        Assert.assertEquals(HeroClass.HUNTER, p.getValue().getKey());

    }

}
