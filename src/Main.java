import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

public class Main {
    static HashMap<Integer, Integer> bid = new HashMap<>();
    static HashMap<Integer, Integer> ask = new HashMap<>();
    static HashMap<Integer, Integer> spread = new HashMap<>();


    public static void main(String[] args) {
        createOutputFile();
        readData();

    }

    public static void buy(int size) {
        int keyMinPrice = bestPriceAsk();
        int currentSize = ask.get(keyMinPrice);
        int resultSize = currentSize - size;
        if (resultSize > 0) {
            ask.put(keyMinPrice, resultSize);
        } else {
            ask.remove(keyMinPrice);
        }
    }

    public static void sell(int size) {
        int keyMaxPrice = bestPriceBid();
        int currentSize = bid.get(keyMaxPrice);
        int resultSize = currentSize - size;
        if (resultSize > 0) {
            bid.put(keyMaxPrice, resultSize);
        } else {
            bid.remove(keyMaxPrice);
        }
    }

    public static void readData() {

        try (FileReader fr = new FileReader("input.txt");
             BufferedReader bufferedReader = new BufferedReader(fr)) {
            while (bufferedReader.ready()) {
                String s = bufferedReader.readLine();
                String[] array = s.split(",");
                String prefix = array[0];
                switch (prefix) {
                    case ("u") -> updateData(array);
                    case ("q") -> printData(array);
                    case ("o") -> doOrder(array);
                    default -> System.out.println("Wrong data in input.txt");
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateData(String[] s) {
        String end = s[3];
        int keyPrice = Integer.parseInt(s[1]);
        int valueSize = Integer.parseInt(s[2]);
        switch (end) {
            case ("bid") -> bid.put(keyPrice, valueSize);
            case ("ask") -> ask.put(keyPrice, valueSize);
            default -> System.out.println("Error, wrong data!");
        }
    }

    private static void printData(String[] s) {
        switch (s[1]) {
            case ("best_bid") -> writeData(bestPriceBid() + "," + bid.get(bestPriceBid()) + "\n");
            case ("best_ask") -> writeData(bestPriceAsk() + "," + ask.get(bestPriceAsk()));
            case ("size") -> writeData(getSizeSpecifiedPrice(Integer.parseInt(s[2])) + "\n");
            default -> System.out.println("Error, wrong data!");
        }
    }

    private static void doOrder(String[] s) {
        int size = Integer.parseInt(s[2]);
        switch (s[1]) {
            case ("buy") -> buy(size);
            case ("sell") -> sell(size);
            default -> System.out.println("Error, wrong data!");
        }
    }

    private static int bestPriceAsk() {
        Optional<Integer> minValue = ask.keySet().stream().min(Comparator.comparingInt(n -> n));
        return minValue.orElse(-1);

    }

    private static int bestPriceBid() {
        Optional<Integer> keyMaxPrice = bid.keySet().stream().max(Comparator.comparingInt(n -> n));
        return keyMaxPrice.orElse(-1);
    }

    private static void writeData(String s) {
        try (FileWriter fileWriter = new FileWriter("output.txt", true)) {
            fileWriter.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getSizeSpecifiedPrice(int price) {
        if (bid.containsKey(price)) return bid.get(price);
        else if (ask.containsKey(price)) return ask.get(price);
        else return spread.getOrDefault(price, -1);
    }

    private static void createOutputFile (){
        File file = new File("output.txt");
        try {
            file.delete();
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
