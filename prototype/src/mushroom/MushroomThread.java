package mushroom;

import core.Debug;
import core.Entity;
import insect.Insect;
import tecton.Tecton;

public class MushroomThread extends Entity {
    private boolean eaten;
    private boolean cutOff;
    private int cutOffDuration;
    private int maxCutOffDuration;

    /**
     * Konstruktor
     *
     * @param owner    A gombafonalat tulajdonló gombász
     * @param location A tekton, amin a gombafonál van
     */
    public MushroomThread(Tecton location, Mushroomer owner) {
        super(location, owner);
        eaten = false;
        cutOff = false;
        cutOffDuration = 0;
        maxCutOffDuration = 2;
        Debug.DBGFUNC("Gombafonal létrehozva");
    }

    public boolean isConnected() {
        // TODO: van-e gombatest melyhez csatlakozunk?
        return true;
    }

    public boolean hasEaten() {
        return eaten;
    }

    public void setEaten(boolean b) {
        this.eaten = b;
    }

    public void setCutoff(boolean b) {
        this.cutOff = b;
    }

    public boolean eat(Insect insect) {
        if (insect.isParalyzed()) {
            insect.remove();
            eaten = true;
            return true;
        }
        return false;
    }

    /**
     * Thread törlése a pályáról
     */
    @Override
    public void remove() {
        getLocation().remove(this);
        ((Mushroomer) getOwner()).remove(this);
        Debug.DBGFUNC("Gombafonal törölve");
    }

    /**
     * Kör vége
     */
    @Override
    public void endTurn() {
        if (cutOff) {
            cutOffDuration++;
            if (cutOffDuration == maxCutOffDuration) {
                remove();
            }
        }
        Debug.DBGFUNC("Kör vége");
    }
}
