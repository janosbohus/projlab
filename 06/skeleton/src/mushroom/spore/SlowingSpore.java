package mushroom.spore;

import mushroom.Mushroomer;
import core.Debug;
import effect.*;
import tecton.Tecton;

public class SlowingSpore extends Spore {

    /**
     * Konstruktor, beállítja a spóra owner-ét (a játékos, akihez tartozik), a helyét (a tekton, amin van), a tapértékét, illetve a rovarokra gyakorolt hatását
     * @param owner A spórát tulajdonló játékos
     * @param location A tekton, amin a spóra van
     */
    public SlowingSpore(Mushroomer owner, Tecton location) {
        this.owner = owner;
        this.location = location;
        nutrition = 10;
        effect = new SlowEffect();
        Debug.DBGFUNC("Lassító spóra lerakva");
    }
}
