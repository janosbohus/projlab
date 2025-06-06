import java.util.Scanner;
import controller.*;
public class Main {
    
    static Parser parser = new Parser(new Game());
    public static void main(String[] args) {
        System.out.println("running prototype");
        Scanner s = new Scanner(System.in);
        while(true) {
            if (parser.getGame().getCurrentPlayer() != null) {
                System.out.println("current turn: " + parser.getGame().getTurn() + " (" + parser.getGame().getCurrentPlayer().getId() + "): " + parser.getGame().getCurrentPlayer());
            }
            String line = s.nextLine();

            //skip comments
            while (line.isBlank() || line.charAt(0) == '#') {
                line = s.nextLine();
            }
            parser.parseCommand(line);
        }
    }
}