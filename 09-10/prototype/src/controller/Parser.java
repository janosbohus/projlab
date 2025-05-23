package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import model.core.Identifiable;
import model.insect.Insect;
import model.tecton.Tecton;
import model.mushroom.MushroomStem;
import model.mushroom.MushroomThread;
import model.mushroom.spore.Spore;

public class Parser {

    static final Random r = new Random();
    static final String[] helpGuide = {"-----------------------------------------SYSTEM COMMANDS-----------------------------------------",
            "/start: Starts the game, after this point, no more players can be added",
            "/load PATH: Loads a saved game file, from the given path",
            "/save PATH: Saves the current state of the game to a file, at the given path",
            "/exec PATH: Executes the commands in the given file",
            "/rand: Enables or disables the randomness of the random elements (-disable/-enable)",
            "/manualtrigger: Forces the execution of the given game command",
            "/list: Lists all objects of the given type (-player, -mushroomstem, -mushroomthread, -insect, -spore)",
            "/map: Prints the Map (All tectons with their neighbours)",
            "/help: Prints this guide",
            "/exit: exits the game (DOES NOT SAVE)",
            "------------------------------------------GAME COMMANDS------------------------------------------",
            "!move [INSECT_ID] [TECTON_ID]: Moves the insect to another tecton, if it's legal",
            "!eat [INSECT_ID]: The given insect eats a random (or not) spore on its tecton, if it can",
            "!cut [INSECT_ID]: The given insect cuts a random (or not) mushroom thread on its tecton, if it can",
            "!grow [TECTON_ID]: Grows a mushroom thread on the given tecton, if it's legal",
            "!plant [TECTON_ID]: Plants a mushroom stem on the given tecton, if it's legal",
            "!throw [MUSHROOMSTEM_ID] [TECTON_ID]: The given mushroom stem throws a spore to the given tecton, if it can",
            "!levelup [MUSHROOMSTEM_ID]: increases the level of the given mushroomstem, if possible",
            "!endturn: Ends the current players turn, if all of its obligatory actions have been taken"
    };

    Game game;

    public Parser(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void CMD_start(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        if (!game.startGame(Integer.parseInt(args[1])))
            System.out.println("The game couldn't have been started (maybe it already has...)");
    }

    public void CMD_test(String[] args) {
        if (args.length != 1) {
            System.out.println("invalid argument count!");
            return;
        }
        if (!game.startTestGame())
            System.out.println("The game couldn't have been started (maybe it already has...)");
    }

    public void CMD_load(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        try {
            FileInputStream file = new FileInputStream(args[1]);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of an object
            game = (Game) in.readObject();

            in.close();
            file.close();
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }

    public void CMD_save(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        try {
            FileOutputStream file = new FileOutputStream(args[1]);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(game);

            out.close();
            file.close();
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }

    public void CMD_script(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank() || line.charAt(0) == '#') {
                    if (!line.isBlank()) {
                        System.out.println(line);
                    }
                    continue;
                }
                if (getGame().getCurrentPlayer() != null) {
                    System.out.println("current turn: " + getGame().getTurn() + " (" + getGame().getCurrentPlayer().getId() + "): " + getGame().getCurrentPlayer());
                }
                System.out.println(line);
                parseCommand(line);
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void CMD_exec(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        try {
            File myObj = new File(args[1]);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                parseCommand(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void CMD_rand(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        if (Objects.equals(args[1], "-disable")) Game.random.setSeed(1);
        else if (Objects.equals(args[1], "-enable")) Game.random.setSeed(r.nextInt());
        else {
            System.out.println("invalid flag: use -disable or -enable");
        }
    }

    public void CMD_splitting(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        if (Objects.equals(args[1], "-disable")) game.getMap().setSplittingEnabled(false);
        else if (Objects.equals(args[1], "-enable")) game.getMap().setSplittingEnabled(true);
        else {
            System.out.println("invalid flag: use -disable or -enable");
        }
    }

    public void CMD_manualtrigger(String[] args) {

    }

    public void CMD_list(String[] args) {
        if (args.length != 1) {
            System.out.println("invalid argument count!");
            return;
        }

        for (int i = 0; i <= Identifiable.getMaxId(); i++) {
            Identifiable obj = game.findObject(i);
            if (obj != null) {
                System.out.println("(" + i + ") : " + obj);
            }
        }
    }

    public void CMD_map(String[] args) {
        if (args.length != 1) {
            System.out.println("invalid argument count!");
            return;
        }
        game.printMap();
    }

    public void CMD_help(String[] args) {
        if (args.length != 1) {
            System.out.println("invalid argument count!");
            return;
        }
        for (String item : helpGuide) System.out.println(item);
    }

    public void CMD_move(String[] args) {
        if (args.length != 3) {
            System.out.println("invalid argument count!");
            return;
        }
        try {
            if (!game.move((Insect) game.findObject(Integer.parseInt(args[1])), (Tecton) game.findObject(Integer.parseInt(args[2]))))
                System.out.println("Couldn't move");
        } catch (Exception e) {
            System.out.println("Invalid ID(s)");
        }
    }

    public void CMD_eat(String[] args) {
        if (args.length != 3) {
            System.out.println("invalid argument count!");
            return;
        }
        boolean oneEx = false;
        try {
            if (game.eat((Insect) game.findObject(Integer.parseInt(args[1])), (Spore) game.findObject(Integer.parseInt(args[2]))))
                return;
        } catch (Exception e) {
            oneEx = true;
        }

        try {
            if (game.eat((MushroomThread) game.findObject(Integer.parseInt(args[1])), (Insect) game.findObject(Integer.parseInt(args[2]))))
                return;
        } catch (Exception e) {
            if (oneEx) System.out.println("Invalid ID(s)");
        }
        System.out.println("Couldn't eat");
    }

    public void CMD_cut(String[] args) {
        if (args.length != 3) {
            System.out.println("invalid argument count!");
            return;
        }
        try {
            if (!game.cut((Insect) game.findObject(Integer.parseInt(args[1])), (MushroomThread) game.findObject(Integer.parseInt(args[2]))))
                System.out.println("Couldn't cut");
        } catch (Exception e) {
            System.out.println("Invalid ID(s)");
        }
    }

    public void CMD_grow(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        try {
            if (!game.growThread((Tecton) game.findObject(Integer.parseInt(args[1]))))
                System.out.println("Couldn't grow");
        } catch (Exception e) {
            System.out.println("Invalid ID(s)");
        }
    }

    public void CMD_plant(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        try {
            if (!game.plantMushroomStem((Tecton) game.findObject(Integer.parseInt(args[1]))))
                System.out.println("Couldn't grow");
        } catch (Exception e) {
            System.out.println("Invalid ID(s)");
        }
    }

    public void CMD_throw(String[] args) {
        if (args.length != 3) {
            System.out.println("invalid argument count!");
            return;
        }
        try {
            if (!game.throwSpore((MushroomStem) game.findObject(Integer.parseInt(args[1])), (Tecton) game.findObject(Integer.parseInt(args[2]))))
                System.out.println("Couldn't throw");
        } catch (Exception e) {
            System.out.println("Invalid ID(s)");
        }
    }

    public void CMD_levelup(String[] args) {
        if (args.length != 2) {
            System.out.println("invalid argument count!");
            return;
        }
        try {
            if (!game.levelUp((MushroomStem) game.findObject(Integer.parseInt(args[1]))))
                System.out.println("Couldn't level up");
        } catch (Exception e) {
            System.out.println("Invalid ID(s)");
        }
    }


    public void CMD_endturn(String[] args) {
        if (args.length != 1) {
            System.out.println("invalid argument count!");
            return;
        }
        game.endTurn();
    }


    public void parseCommand(String cmd) {
        String[] tokens = cmd.split(" ");

        switch (tokens[0]) {
            case "/start":
                CMD_start(tokens);
                break;
            case "/test":
                CMD_test(tokens);
                break;
            case "/script":
                CMD_script(tokens);
                break;
            case "/load":
                CMD_load(tokens);
                break;
            case "/save":
                CMD_save(tokens);
                break;
            case "/exec":
                CMD_exec(tokens);
                break;
            case "/rand":
                CMD_rand(tokens);
                break;
            case "/splitting":
                CMD_splitting(tokens);
                break;
            case "/manualtrigger":
                CMD_manualtrigger(tokens);
                break;
            case "/list":
                CMD_list(tokens);
                break;
            case "/map":
                CMD_map(tokens);
                break;
            case "/help":
                CMD_help(tokens);
                break;

            case "!move":
                CMD_move(tokens);
                break;
            case "!eat":
                CMD_eat(tokens);
                break;
            case "!cut":
                CMD_cut(tokens);
                break;
            case "!grow":
                CMD_grow(tokens);
                break;
            case "!plant":
                CMD_plant(tokens);
                break;
            case "!throw":
                CMD_throw(tokens);
                break;
            case "!levelup":
                CMD_levelup(tokens);
                break;
            case "!endturn":
                CMD_endturn(tokens);
                break;
            case "/exit":
                System.exit(0);
                break;
            default:
                System.out.println("invalid command!");
                break;
        }
    }
}
