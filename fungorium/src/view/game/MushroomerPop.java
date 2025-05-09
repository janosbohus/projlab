package view.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MushroomerPop extends JPanel{
    public MushroomerPop() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));  // Vízszintes elrendezés
        setBackground(new Color(240, 240, 240));
        JButton sporeButton = new JButton("Throw Spore");
        JButton eatButton = new JButton("Eat Insect");
        JButton threadButton = new JButton("Grow Thread");
        sporeButton.setPreferredSize(new Dimension(1000, 30));
        eatButton.setPreferredSize(new Dimension(1000, 30));
        threadButton.setPreferredSize(new Dimension(1000, 30));

        sporeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: meghívni a föggvényt, kitalálni, hogy hova fog dobni
                System.out.println("spore Gomb lett megnyomva.");
            }
        });

        eatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: meghívni a függvényt, kiválasztani a rovart
                System.out.println("eat Gomb lett megnyomva.");
            }
        });
        threadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: meghívni a föggvényt, kitalálni, hogy hova fog nőni
                System.out.println("thread  Gomb lett megnyomva.");
            }
        });
        add(sporeButton);
        add(eatButton);
        add(threadButton);
    }
}
