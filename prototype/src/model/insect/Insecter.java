package model.insect;

import model.core.Player;
import model.mushroom.MushroomThread;
import model.mushroom.spore.Spore;
import model.tecton.Tecton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Az Insecter osztály egy játékos, aki egy rovart irányít.
 * Ez az osztály implementálja az IInsect interfészt, hogy a játékos képes legyen spórákat enni,
 * mozogni és gombafonalakat elvágni.
 */
public class Insecter extends Player implements IInsect {
    private List<Insect> insects = new ArrayList<>();

    public Insecter() {
    }

    public Insecter(Tecton location) {
        createInsect(location);
    }

    public Insecter(Tecton location, int id) {
        super(id);
        createInsect(location);
    }

    public boolean eat(Insect insect, Spore sp) {
        return insect.eat(sp);
    }

    public boolean move(Insect insect, Tecton t) {
        return insect.move(t);
    }

    public boolean cut(Insect insect, MushroomThread th) {
        return insect.cut(th);
    }

    public boolean createInsect(Tecton location) {
        return add(new Insect(this, location));
    }

    /**
     * A kör végén végrehajtandó műveletek.
     */
    @Override
    public void endTurn() {
        insects.forEach(Insect::endTurn);
    }

    @Override
    public boolean add(Insect insect) {
        return insects.add(insect);
    }

    @Override
    public boolean remove(Insect insect) {
        return insects.remove(insect);
    }

    @Override
    public List<Insect> getInsects() {
        return insects;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Insecter insecter = (Insecter) o;
        return Objects.equals(insects, insecter.insects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), insects);
    }
}