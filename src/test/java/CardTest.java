import main.GA;
import main.GASetup;
import net.demilich.metastone.game.cards.Card;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;

/**
 * @author created by Jens Plümer on 30.11.17
 */
public class CardTest {

    private Map<Integer, Card> map;

    @Before
    public void setup() {
        map = GASetup.loadCardMap();
    }

    @Test
    public void loadCardMap_returns_true() {

        // check if for duplicated cards
        Assert.assertFalse(new HashSet<>(map.values()).size() != map.values().size());
        // check for null values
        Assert.assertFalse(map.values().stream().anyMatch(p -> p == null));

    }
}
