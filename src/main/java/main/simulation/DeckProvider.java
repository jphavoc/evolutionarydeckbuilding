package main.simulation;

import javafx.util.Pair;
import main.GA;
import main.GASetup;
import main.GAStatistic;
import net.demilich.metastone.game.cards.Card;
import net.demilich.metastone.game.cards.CardCatalogue;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.decks.DeckFactory;
import net.demilich.metastone.game.entities.heroes.HeroClass;
import net.demilich.metastone.gui.deckbuilder.CardEntryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @author created by Jens Plümer on 23.11.17
 */
public class DeckProvider {

    private static final Logger log = LoggerFactory.getLogger(DeckFactory.class);

    public static Pair<String, Pair<HeroClass, Deck>> getMidRangeDruid() throws IllegalArgumentException {

        String s = "### Mid-Range Druid Meta Snapshot #16\n" +
                "# Class: Druid\n" +
                "# Format: Wild\n" +
                "#\n" +
                "# 2x (0) Innervate\n" +
                "# 2x (2) Wild Growth\n" +
                "# 2x (2) Wrath\n" +
                "# 2x (3) Savage Roar\n" +
                "# 2x (3) Shade of Naxxramas\n" +
                "# 2x (4) Keeper of the Grove\n" +
                "# 2x (4) Piloted Shredder\n" +
                "# 1x (4) Sen'jin Shieldmasta\n" +
                "# 2x (4) Swipe\n" +
                "# 1x (5) Big Game Hunter\n" +
                "# 2x (5) Druid of the Claw\n" +
                "# 2x (5) Force of Nature\n" +
                "# 1x (5) Harrison Jones\n" +
                "# 1x (5) Loatheb\n" +
                "# 1x (5) Sludge Belcher\n" +
                "# 1x (6) Emperor Thaurissan\n" +
                "# 2x (7) Ancient of Lore\n" +
                "# 1x (7) Dr. Boom\n" +
                "# 1x (9) Cenarius\n" +
                "#\n" +
                "AAEBAZICCPsE+QyQB/oOgQ7WEZ4QJAv+AeQIxAbmBfgN2QSQEEC0Be0DmAcA\n" +
                "#\n" +
                "# To use this deck, copy it to your clipboard and create a new deck in Hearthstone";

        return generateDeckFromString(s);
    }

    public static Pair<String, Pair<HeroClass, Deck>> getMidRangeHunter() {

        String s = "### Purple Rank #1 Legend Midrange Hunter\n" +
                "# Class: Hunter\n" +
                "# Format: Wild\n" +
                "#\n" +
                "# 2x (1) Abusive Sergeant\n" +
                "# 1x (1) Hunter's Mark\n" +
                "# 2x (1) Leper Gnome\n" +
                "# 1x (2) Explosive Trap\n" +
                "# 2x (2) Freezing Trap\n" +
                "# 1x (2) Glaivezooka\n" +
                "# 2x (2) Haunted Creeper\n" +
                "# 2x (2) Knife Juggler\n" +
                "# 2x (2) Mad Scientist\n" +
                "# 2x (3) Animal Companion\n" +
                "# 2x (3) Eaglehorn Bow\n" +
                "# 2x (3) Kill Command\n" +
                "# 1x (3) Unleash the Hounds\n" +
                "# 1x (4) Houndmaster\n" +
                "# 2x (4) Piloted Shredder\n" +
                "# 1x (5) Loatheb\n" +
                "# 2x (5) Sludge Belcher\n" +
                "# 2x (6) Savannah Highmane\n" +
                "#\n" +
                "AAEBAR8GjQHJBNsP2wnrB/oODPIBkgWHBPUNsQj3DbUD/gyoApAQgQ7tCQA=\n" +
                "#\n" +
                "# To use this deck, copy it to your clipboard and create a new deck in Hearthstone";

        return generateDeckFromString(s);
    }

    public static Pair<String, Pair<HeroClass, Deck>> getTempoMage() {

        String s = "### Hyunher Tempo Mage\n" +
                "# Class: Mage\n" +
                "# Format: Wild\n" +
                "#\n" +
                "# 1x (1) Clockwork Gnome\n" +
                "# 2x (1) Mana Wyrm\n" +
                "# 2x (1) Mirror Image\n" +
                "# 2x (2) Flamecannon\n" +
                "# 2x (2) Frostbolt\n" +
                "# 2x (2) Mad Scientist\n" +
                "# 2x (2) Sorcerer's Apprentice\n" +
                "# 2x (2) Unstable Portal\n" +
                "# 2x (3) Arcane Intellect\n" +
                "# 2x (3) Flamewaker\n" +
                "# 2x (3) Mirror Entity\n" +
                "# 2x (4) Fireball\n" +
                "# 2x (4) Mechanical Yeti\n" +
                "# 1x (5) Azure Drake\n" +
                "# 1x (5) Loatheb\n" +
                "# 1x (6) Emperor Thaurissan\n" +
                "# 1x (7) Archmage Antonidas\n" +
                "# 1x (7) Dr. Boom\n" +
                "#\n" +
                "AAEBAf0EBoIQuQb6DtYRuAieEAyVA7wIhw+WBfcN5gSJD6sE4xHDAbsC/g8A\n" +
                "#\n" +
                "# To use this deck, copy it to your clipboard and create a new deck in Hearthstone";

        return generateDeckFromString(s);
    }

    public static Pair<String, Pair<HeroClass, Deck>> getAggroPaladin() {

        String s = "### Aggro Paladin Meta Snapshot #17\n" +
                "# Class: Paladin\n" +
                "# Format: Wild\n" +
                "#\n" +
                "# 2x (1) Abusive Sergeant\n" +
                "# 1x (1) Blessing of Might\n" +
                "# 2x (1) Leper Gnome\n" +
                "# 2x (2) Equality\n" +
                "# 2x (2) Knife Juggler\n" +
                "# 2x (2) Shielded Minibot\n" +
                "# 2x (3) Aldor Peacekeeper\n" +
                "# 1x (3) Coghammer\n" +
                "# 1x (3) Divine Favor\n" +
                "# 1x (3) Ironbeak Owl\n" +
                "# 2x (3) Muster for Battle\n" +
                "# 2x (4) Consecration\n" +
                "# 2x (4) Piloted Shredder\n" +
                "# 2x (4) Truesilver Champion\n" +
                "# 1x (5) Loatheb\n" +
                "# 1x (5) Quartermaster\n" +
                "# 1x (5) Solemn Vigil\n" +
                "# 2x (6) Argent Commander\n" +
                "# 1x (8) Tirion Fordring\n" +
                "#\n" +
                "AAEBAZ8FCEbrD6cFogL6DuwP4hH6BgvyAZIF9AWxCOoPjwntD9wDkBDPBpkCAA==\n" +
                "#\n" +
                "# To use this deck, copy it to your clipboard and create a new deck in Hearthstone";

        return generateDeckFromString(s);
    }

    public static Pair<String, Pair<HeroClass, Deck>> getShadowMadnessPriest() {

        String s = "### MatGagne Shadow Madness Priest\n" +
                "# Class: Priest\n" +
                "# Format: Wild\n" +
                "#\n" +
                "# 2x (0) Circle of Healing\n" +
                "# 2x (1) Light of the Naaru\n" +
                "# 2x (1) Northshire Cleric\n" +
                "# 2x (1) Power Word: Shield\n" +
                "# 2x (1) Zombie Chow\n" +
                "# 2x (2) Shrinkmeister\n" +
                "# 2x (2) Wild Pyromancer\n" +
                "# 2x (3) Acolyte of Pain\n" +
                "# 2x (3) Injured Blademaster\n" +
                "# 1x (3) Shadow Word: Death\n" +
                "# 1x (3) Velen's Chosen\n" +
                "# 2x (4) Auchenai Soulpriest\n" +
                "# 2x (4) Shadow Madness\n" +
                "# 1x (5) Harrison Jones\n" +
                "# 1x (5) Holy Nova\n" +
                "# 1x (5) Vol'jin\n" +
                "# 2x (6) Cabal Shadow Priest\n" +
                "# 1x (6) Sylvanas Windrunner\n" +
                "#\n" +
                "AAEBAa0GBtMKjw+QB8kGiw+5DQzSCo0P8gzlBNkNkA/2B/sM1QjtAdwBkAIA\n" +
                "#\n" +
                "# To use this deck, copy it to your clipboard and create a new deck in Hearthstone";

        return generateDeckFromString(s);
    }

    public static Pair<String, Pair<HeroClass, Deck>> getOilRogue() {

        String s = "### Toefoo Rank 1 Oil Rogue\n" +
                "# Class: Rogue\n" +
                "# Format: Wild\n" +
                "#\n" +
                "# 2x (0) Backstab\n" +
                "# 2x (0) Preparation\n" +
                "# 2x (1) Deadly Poison\n" +
                "# 1x (1) Southsea Deckhand\n" +
                "# 1x (2) Bloodmage Thalnos\n" +
                "# 2x (2) Eviscerate\n" +
                "# 2x (2) Sap\n" +
                "# 1x (3) Earthen Ring Farseer\n" +
                "# 1x (3) Edwin VanCleef\n" +
                "# 1x (3) Fan of Knives\n" +
                "# 2x (3) SI:7 Agent\n" +
                "# 2x (4) Blade Flurry\n" +
                "# 2x (4) Piloted Shredder\n" +
                "# 2x (4) Tinker's Sharpsword Oil\n" +
                "# 1x (5) Antique Healbot\n" +
                "# 2x (5) Azure Drake\n" +
                "# 1x (5) Harrison Jones\n" +
                "# 1x (5) Loatheb\n" +
                "# 2x (7) Sprint\n" +
                "#\n" +
                "AAEBAaIHCNQF7QXzDLICmwX1D5AH+g4LtAGGCcsDiAfNA90IqAiQEK8QuQb2BAA=\n" +
                "#\n" +
                "# To use this deck, copy it to your clipboard and create a new deck in Hearthstone";

        return generateDeckFromString(s);
    }

    public static Pair<String, Pair<HeroClass, Deck>> getMechShaman() {

        String s = "### Phonetap's Mech Shaman\n" +
                "# Class: Shaman\n" +
                "# Format: Wild\n" +
                "#\n" +
                "# 2x (1) Cogmaster\n" +
                "# 2x (1) Earth Shock\n" +
                "# 2x (2) Annoy-o-Tron\n" +
                "# 2x (2) Crackle\n" +
                "# 2x (2) Mechwarper\n" +
                "# 2x (2) Rockbiter Weapon\n" +
                "# 2x (2) Whirling Zap-o-matic\n" +
                "# 1x (3) Lava Burst\n" +
                "# 2x (3) Powermace\n" +
                "# 2x (3) Spider Tank\n" +
                "# 1x (4) Mechanical Yeti\n" +
                "# 2x (4) Piloted Shredder\n" +
                "# 1x (5) Doomhammer\n" +
                "# 2x (5) Fel Reaver\n" +
                "# 1x (5) Loatheb\n" +
                "# 2x (6) Fire Elemental\n" +
                "# 1x (7) Dr. Boom\n" +
                "# 1x (8) Ragnaros the Firelord\n" +
                "#\n" +
                "AAEBAaoIBuAG/g/gAvoOnhD2AgyMD/8FhRDWD5QP7wHVD9QP3A+QEL4PvQEA\n" +
                "#\n" +
                "# To use this deck, copy it to your clipboard and create a new deck in Hearthstone";

        return generateDeckFromString(s);
    }

    public static Pair<String, Pair<HeroClass, Deck>> getMalyLock() {

        String s = "### Neviilz MalyGod Meta Snapshot #18\n" +
                "# Class: Warlock\n" +
                "# Format: Wild\n" +
                "#\n" +
                "# 1x (1) Abusive Sergeant\n" +
                "# 2x (1) Mortal Coil\n" +
                "# 1x (1) Soulfire\n" +
                "# 1x (1) Zombie Chow\n" +
                "# 2x (2) Darkbomb\n" +
                "# 2x (3) Blackwing Technician\n" +
                "# 1x (3) Earthen Ring Farseer\n" +
                "# 2x (3) Ironbeak Owl\n" +
                "# 1x (4) Defender of Argus\n" +
                "# 1x (4) Hellfire\n" +
                "# 2x (4) Imp-losion\n" +
                "# 1x (4) Shadowflame\n" +
                "# 2x (4) Twilight Drake\n" +
                "# 2x (5) Antique Healbot\n" +
                "# 2x (5) Azure Drake\n" +
                "# 2x (5) Big Game Hunter\n" +
                "# 2x (5) Blackwing Corruptor\n" +
                "# 1x (5) Sludge Belcher\n" +
                "# 1x (6) Emperor Thaurissan\n" +
                "# 1x (9) Malygos\n" +
                "#\n" +
                "AAEBAf0GCvIBzgfZDfMM+wW2B5MBgQ7WEbQDCsQIrRDoEqIC3Q+NCPUPuQb5DOkSAA==\n" +
                "#\n" +
                "# To use this deck, copy it to your clipboard and create a new deck in Hearthstone";

        return generateDeckFromString(s);
    }

    public static Pair<String, Pair<HeroClass, Deck>> getControlWarrior() {

        String s = "### Toefoo Rank 1 Cwarrior\n" +
                "# Class: Warrior\n" +
                "# Format: Wild\n" +
                "#\n" +
                "# 2x (1) Shield Slam\n" +
                "# 2x (2) Armorsmith\n" +
                "# 2x (2) Cruel Taskmaster\n" +
                "# 2x (2) Execute\n" +
                "# 2x (3) Acolyte of Pain\n" +
                "# 2x (3) Fiery War Axe\n" +
                "# 2x (3) Shield Block\n" +
                "# 2x (4) Death's Bite\n" +
                "# 1x (5) Big Game Hunter\n" +
                "# 1x (5) Brawl\n" +
                "# 1x (5) Harrison Jones\n" +
                "# 2x (5) Sludge Belcher\n" +
                "# 1x (6) Emperor Thaurissan\n" +
                "# 2x (6) Shieldmaiden\n" +
                "# 1x (6) Sylvanas Windrunner\n" +
                "# 1x (7) Baron Geddon\n" +
                "# 1x (7) Dr. Boom\n" +
                "# 1x (8) Grommash Hellscream\n" +
                "# 1x (9) Alexstrasza\n" +
                "# 1x (9) Ysera\n" +
                "#\n" +
                "AAEBAQcK+QxLkAfWEbkN0AKeENICxQSiCQqiBNQEnQKRBvsMkQP/B40OgQ7lDwA=\n" +
                "#\n" +
                "# To use this deck, copy it to your clipboard and create a new deck in Hearthstone";

        return generateDeckFromString(s);
    }

    public static Collection<Pair<String, Pair<HeroClass, Deck>>> getAll() {

        Collection<Pair<String, Pair<HeroClass, Deck>>> col = new ArrayList<>();

        col.add(getAggroPaladin());
        col.add(getControlWarrior());
        col.add(getMalyLock());
        col.add(getMechShaman());
        col.add(getMidRangeDruid());
        col.add(getMidRangeHunter());
        col.add(getOilRogue());
        col.add(getShadowMadnessPriest());
        col.add(getTempoMage());

        return col;
    }

    public static void foo(String path) {

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for(File f : listOfFiles) {

            try {

                String content = new String(Files.readAllBytes(f.toPath()));
                DeckProvider.generateDeckFromString(content);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }

    public static void main(String[] args) {


//        DeckProvider.foo("/Users/jp/Git/evolutionarydeckbuilding/src/main/resources/meta_snapshot_16");
        log.info(GASetup.loadCardMap().toString());
//        log.info(CardCatalogue.getCardByName("Spider").toString());

    }

    public static Pair<String, Pair<HeroClass, Deck>> generateDeckFromString(String s) throws IllegalArgumentException {

        String deckName = null;
        String clazz = null;
        String format = null;
        HeroClass hero = null;
        Card[] cards = new Card[30];
        int count = 0;

        final String reg1 = "(?<=..x\\s)(.)*";
        final String reg2 = "\\d+x";
        final String reg3 = "\\d+x\\s.*";
        //TODO should only be compiled once
        final Pattern pattern = Pattern.compile(reg3);

        String[] array = s.split(System.getProperty("line.separator"));
        for (int i = 0; i < array.length; i++) {


            // get the name of the deck
            if (i == 0) {

                deckName = array[i]
                        .replace("###\\s*", "")
                        .replaceAll("###", "")
                        .trim();

                log.debug("deckName=" + deckName);

                continue;
            }

            String l = array[i]
                    .replaceAll("#", "")
                    .replaceAll("\\(\\d+\\)", "");

            if (pattern.matcher(l).find()) {

                String name = l.replaceAll(reg2, "")
                        .trim();
                int number = Integer.valueOf(l.replaceAll(reg1, "")
                        .replaceAll("x", "")
                        .trim());

                log.debug("name=" + name);
                log.debug("number=" + number);

                for (int c = 0; c < number; c++) {
                    cards[count] = CardCatalogue.getCardByName(name);
                    count++;
                }

                continue;
            }

            if (l.contains("Class: ")) {

                clazz = l.replaceFirst("Class: ", "")
                        .trim();

                log.debug("Class=" + clazz);

                continue;
            }

            if (l.contains("Format: ")) {

                format = l.replaceFirst("Format: ", "")
                        .trim();

                log.debug("Format=" + format);

                continue;
            }

            log.debug("deckName=" + deckName);

        }

        log.debug("cards=" + cards.toString());

        Deck deck = DeckFactory.getDeckConsistingof(30, cards);
        if (!deck.isFull()
                && !deck.isTooBig()
                && deckName != null
                && clazz != null) {

            // select hero class from string
            switch (clazz) {
                case "Warrior":
                    hero = HeroClass.WARRIOR;
                    break;
                case "Hunter":
                    hero = HeroClass.HUNTER;
                    break;
                case "Rogue":
                    hero = HeroClass.ROGUE;
                    break;
                case "Druid":
                    hero = HeroClass.DRUID;
                    break;
                case "Mage":
                    hero = HeroClass.MAGE;
                    break;
                case "Warlock":
                    hero = HeroClass.WARLOCK;
                    break;
                case "Priest":
                    hero = HeroClass.PRIEST;
                    break;
                case "Shaman":
                    hero = HeroClass.SHAMAN;
                    break;
                case "Paladin":
                    hero = HeroClass.PALADIN;
                    break;
                default:
                    hero = null;
            }

            return new Pair(deckName, new Pair(hero, deck));
        }

        throw new IllegalArgumentException("deckName=" + deckName + "clazz=" + clazz + ", hero=" + hero + "\n content:\n" + s);

    }


}
