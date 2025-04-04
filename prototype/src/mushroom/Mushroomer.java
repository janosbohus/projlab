package mushroom;

import java.util.*;

import mushroom.spore.*;
import core.Debug;
import core.Player;
import insect.Insect;
import tecton.*;

/**
 * A Mushroomer osztály egy játékost reprezentál, aki gombákat kezel.
 * A gombász képes spórákat, gombatesteket és gombafonalakat kezelni.
 */
public class Mushroomer extends Player implements ISpore, IStem, IThread {
    private List<Spore> spores = new ArrayList<Spore>();
    private List<MushroomStem> stems = new ArrayList<MushroomStem>();
    private List<MushroomThread> threads = new ArrayList<MushroomThread>();

    /**
     * Konstruktor, amely inicializálja a gombász spóráit, gombatestjeit és
     * gombafonalait.
     *
     * @param spores  A gombász spóráinak listája.
     * @param stems   A gombász gombatestjeinek listája.
     * @param threads A gombász gombafonalainak listája.
     */
    public Mushroomer(List<Spore> spores, List<MushroomStem> stems, List<MushroomThread> threads) {
        Debug.DBGFUNC("Mushroomer konstruktor");
        this.spores = spores;
        this.stems = stems;
        this.threads = threads;
    }

    /**
     * Alapértelmezett konstruktor
     * Üres listákat hoz létre a spóráknak, gombatesteknek és gombafonalaknak.
     */
    public Mushroomer(Tecton location) {
        Debug.DBGFUNC("Mushroomer alapértelmezett konstruktor");
        stems.add(new MushroomStem(location, this));
        threads.add(new MushroomThread(location, this));
    }

    /**
     * Gombatestet helyez el egy adott tektonon.
     *
     * @param tecton A tekton, ahová a gombatestet elhelyezzük.
     * @return Igaz, ha a gombatest elhelyezése sikeres, egyébként hamis.
     */
    public Boolean plantMushroomstem(Tecton tecton) {
        MushroomStem ms = new MushroomStem(tecton, this);

        if (!hasThread(tecton))
            return false;

        MushroomThread thread = threads.stream().filter(th -> th.hasEaten()).findFirst().get();
        if (thread != null && tecton.add(ms)) {
            thread.setEaten(false);
            add(ms);
            addScore(1);
            return true;
        }

        List<Spore> sp = getSpores(tecton);
        if (sp.size() >= ms.getCost() && tecton.add(ms)) {
            for (int i = 0; i < ms.getCost(); i++)
                sp.get(i).remove();

            add(ms);
            addScore(1);
            return true;
        }

        return false;
    }

    /**
     * Gombafonalat növeszt egy adott tektonon.
     *
     * @param tecton A tekton, ahová a gombafonalat növesztjük.
     * @return Igaz, ha a gombafonal növesztése sikeres, egyébként hamis.
     */
    public Boolean growMushroomthread(Tecton tecton) {
        // TODO: limit per turn
        Debug.DBGFUNC("Gombafonal növesztése" + tecton.hasThread(this) + " neighbourHasThread="
                + tecton.neighbourHasThread(this));

        MushroomThread mt = new MushroomThread(tecton, this);
        if (!tecton.hasThread(this) && tecton.neighbourHasThread(this)) {
            if (tecton.add(mt)) {
                return add(mt);
            }
        }
        return false;
    }

    /**
     * Spórát dob egy gombatestből egy adott tektonra.
     *
     * @param ms     A gombatest, amelyből a spórát dobjuk.
     * @param tecton A tekton, ahová a spórát dobjuk.
     * @return Igaz, ha a spóra dobása sikeres, egyébként hamis.
     */
    public Boolean throwSpore(MushroomStem ms, Tecton tecton) {
        return ms.throwSpore(tecton);
    }

    public boolean eatWith(MushroomThread thread, Insect insect) {
        return thread.eat(insect);
    }

    /**
     * A gombatest szintet lép.
     *
     * @param ms A szintet lépő gombatest.
     * @return Igaz, ha a fejlesztés sikeres, egyébként hamis.
     */
    public Boolean levelUp(MushroomStem ms) {
        Debug.DBGFUNC("Gombatest szintlépés");
        return ms.levelUp();
    }

    public List<Spore> getSpores(Tecton tecton) {
        return spores.stream().filter(spore -> spore.getLocation() == tecton).toList();
    }

    public boolean hasThread(Tecton tecton) {
        return threads.stream().filter(th -> th.getLocation() == tecton).toArray().length == 1;
    }
    
    /**
     * A kör végén végrehajtandó műveletek.
     * Meghívja a gombászhoz tartozó gombatestek, gombafonalak és spórák endTurn
     * metódusait.
     */
    @Override
    public void endTurn() {
        Debug.DBGFUNC("Kör vége");
        stems.forEach(ms -> ms.endTurn());
        threads.forEach(th -> th.endTurn());
        spores.forEach(sp -> sp.endTurn());
    }

    /**
     * Hozzáad egy spórát a gombászhoz.
     *
     * @param sp A hozzáadandó spóra.
     * @return Igaz, ha a hozzáadás sikeres, egyébként hamis.
     */
    @Override
    public boolean add(Spore sp) {
        Debug.DBGFUNC("Spóra hozzáadása");
        return spores.add(sp);
    }

    /**
     * Elvesz egy spórát a gombásztól.
     *
     * @param sp Az eltávolítandó spóra.
     * @return Igaz, ha az eltávolítás sikeres, egyébként hamis.
     */
    @Override
    public boolean remove(Spore sp) {
        Debug.DBGFUNC("Spóra eltávolítása");
        return spores.remove(sp);
    }

    @Override
    public List<Spore> getSpores() {
        return spores;
    }

    /**
     * Hozzáad egy gombatestet a gombászhoz.
     *
     * @param ms A hozzáadandó gombatest.
     * @return Igaz, ha a hozzáadás sikeres, egyébként hamis.
     */
    @Override
    public boolean add(MushroomStem ms) {
        Debug.DBGFUNC("Gombatest hozzáadása");
        stems.add(ms);
        System.out.println(stems);
        return true;
    }

    /**
     * Elvesz egy gombatestet a gombásztól.
     *
     * @param ms Az eltávolítandó gombatest.
     * @return Igaz, ha az eltávolítás sikeres, egyébként hamis.
     */
    @Override
    public boolean remove(MushroomStem ms) {
        Debug.DBGFUNC("Gombatest eltávolítása");
        return stems.remove(ms);
    }

    /**
     * Visszaadja a gombászhoz tartozó gombatestek listáját.
     *
     * @return A gombászhoz tartozó gombatestek listája.
     */
    @Override
    public List<MushroomStem> getStems() {
        Debug.DBGFUNC("Gombászhoz tartozó gombatestek lekérése");
        return stems;
    }

    /**
     * Hozzáad egy gombafonalat a gombászhoz.
     *
     * @param th A hozzáadandó gombafonal.
     * @return Igaz, ha a hozzáadás sikeres, egyébként hamis.
     */
    @Override
    public boolean add(MushroomThread th) {
        Debug.DBGFUNC("Gombafonal hozzáadása");
        return threads.add(th);
    }

    /**
     * Elvesz egy gombafonalat a gombásztól.
     *
     * @param th Az eltávolítandó gombafonal.
     * @return Igaz, ha az eltávolítás sikeres, egyébként hamis.
     */
    @Override
    public boolean remove(MushroomThread th) {
        Debug.DBGFUNC("Gombafonal eltávolítása");
        return threads.remove(th);
    }

    /**
     * Visszaadja a gombászhoz tartozó gombafonalak listáját.
     *
     * @return A gombászhoz tartozó gombafonalak listája.
     */
    @Override
    public List<MushroomThread> getThreads() {
        Debug.DBGFUNC("Gombászhoz tartozó gombafonalak lekérése");
        return threads;
    }
}