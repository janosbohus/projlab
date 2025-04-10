package effect;

import core.Debug;
import insect.*;

public class FastEffect extends Effect {
    /**
     * Érvényesíti a hatást egy rovarra
     *
     * @param i A rovar, amire kifejti hatását
     */
    public void apply(Insect i) {
        duration--;
        i.setSpeedModifier(2);
        Debug.DBGFUNC("Gyorsító hatás van érvényben a rovaron");
    }
}
