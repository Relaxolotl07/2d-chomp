import java.util.*;
import java.io.File; // Add this import statement
import java.io.PrintWriter; // Add this import statement

public class Positions {
    private Hashtable<Integer, PositionGrid> positions;

    // private int doubleHash(ArrayList<Integer> position) {
    //     int hash = 0;
    //     int i = 0;
    //     while(positions.containsKey(hash)) {
    //         hash = (hash1(position) + i * hash2(position)) % positions.size();
    //         i++;
    //     }
        
    //     return hash;
    // }

    // private int hash1(ArrayList<Integer> position) {
    //     int hash = 0;
    //     int number1, number2, number3, number4, totalnumbers = 0;
    //     // take first two and last 2 numbers
    //     if (position.size() > 4) {
    //         number1 = position.get(0);
    //         number2 = position.get(1);
    //         number3 = position.get(position.size() - 2);
    //         number4 = position.get(position.size() - 1);
    //         totalnumbers = number1 * 1000 + number2 * 100 + number3 * 10 + number4;
    //     }
    //     else {
    //         // add position checking
    //         for (int i = 0; i < position.size(); i++) {
    //             totalnumbers += position.get(i) * Math.pow(10, i);
    //         }
    //     }
    //     // find the middle one number in totalnumbers squared
    //     hash = (int) Math.pow(totalnumbers, 2) / 100 % 10;
    //     return hash;
    // }

    // private int hash2(ArrayList<Integer> position) {
    //     int hash = position.size() % 7;  
    //     return hash;
    // }

    public Positions () {
        positions = new Hashtable<Integer, PositionGrid>();

        try (Scanner sc = new Scanner(new File("nightmare.tsv"))) {
            readPositions(sc);
            sc.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readPositions(Scanner sc) {
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\t");
            ArrayList<Integer> position = new ArrayList<Integer>();
            for (int i = 0; i < parts.length - 1; i++) {
                // numbers separated by spaces in the line
                String[] numbers = parts[i].split(" ");
                for (int j = 0; j < numbers.length; j++) {
                    position.add(Integer.parseInt(numbers[j]));
                }
            }
            addPosition(new PositionGrid(position, parts[parts.length - 1].charAt(0)));
        }
    }

    public void addPosition(PositionGrid position) {
        int hash = position.getPosition().hashCode();
        positions.put(hash, position);

        // save to file
        savePositions();
    }

    public void savePositions() {
        try (PrintWriter pw = new PrintWriter(new File("nightmare.tsv"))) {
            for (PositionGrid position : positions.values()) {
                ArrayList<Integer> positionList = position.getPosition();
                for (int i = 0; i < positionList.size(); i++) {
                    pw.print(positionList.get(i));
                    if (i < positionList.size() - 1) {
                        pw.print(" ");
                    }
                }
                pw.print("\t" + position.getPositionType() + "\n");
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PositionGrid findPosition(ArrayList<Integer> position) {
        int hash = position.hashCode();
        return positions.get(hash);
    }
}


class PositionGrid {
    private ArrayList<Integer> position;
    private char positionType;

    public PositionGrid(ArrayList<Integer> position, char positionType) {
        this.position = position;
        this.positionType = positionType;
    }

    public ArrayList<Integer> getPosition() {
        return position;
    }

    public char getPositionType() {
        return positionType;
    }
}