import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    TreeMap<Integer, Integer> bid = new TreeMap<>();
    TreeMap<Integer, Integer> ask = new TreeMap<>();
    private final StringBuilder data = new StringBuilder();


    public static void main(String[] args) {
        Main main = new Main();
        main.readData();
        main.writeData(main.data.toString());

    }
    public void readData() {

        try (FileReader fr = new FileReader("input.txt");
             BufferedReader bufferedReader = new BufferedReader(fr)) {
            while (bufferedReader.ready()) {
                String s = bufferedReader.readLine();
                String[] array = split(s);
                String prefix = array[0];
                switch (prefix) {
                    case ("u") -> updateData(array);
                    case ("q") -> printData(array);
                    case ("o") -> doOrder(array);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateData(String[] s) {
        String end = s[3];
        int keyPrice = Integer.parseInt(s[1]);
        int valueSize = Integer.parseInt(s[2]);
        switch (end) {
            case ("bid") -> setBid(keyPrice, valueSize);
            case ("ask") -> setAsk(keyPrice, valueSize);
        }
    }
    private void setBid(int key, int value) {
        if (value > 0) {
            bid.put(key, value);
        } else {
            bid.remove(key);
        }
    }

    private void setAsk(int key, int value) {
        if (value > 0) {
            ask.put(key, value);
        } else {
            ask.remove(key);
        }
    }

    private void doOrder(String[] s) {
        int size = Integer.parseInt(s[2]);
        switch (s[1]) {
            case ("buy") -> buy(size);
            case ("sell") -> sell(size);
        }
    }

    private void buy(int size) {
        while (size > 0) {
            int[] keyValue = getBestPriceAsk();
            int keyMinPrice = keyValue[0];
            int currentSize = keyValue[1];
            int resultSize = currentSize - size;
            if (resultSize > 0) {
                ask.put(keyMinPrice, resultSize);
                return;
            } else if (resultSize < 0) {
                ask.remove(keyMinPrice);
                size = size - currentSize;
            } else {
                ask.remove(keyMinPrice);
                return;
            }
        }
    }

    private void sell(int size) {
        while (size > 0) {
            int[] keyValue = getBestPriceBid();
            int keyMaxPrice = keyValue[0];
            int currentSize = keyValue [1];
            int resultSize = currentSize - size;
            if (resultSize > 0) {
                bid.put(keyMaxPrice, resultSize);
                return;
            } else if (resultSize < 0) {
                bid.remove(keyMaxPrice);
                size = size -currentSize;
            } else {
                bid.remove(keyMaxPrice);
                return;
            }
        }
    }

    private void printData(String[] s) {
        switch (s[1]) {
            case ("best_bid") -> {
                int [] keyValue = getBestPriceBid();
                if ( keyValue [0] != -1){
                    data.append(keyValue [0]).append(",").append(keyValue[1]).append('\n');
                }
            }
            case ("best_ask") -> {
                int [] keyValue = getBestPriceAsk();
                if (keyValue[0] != -1){
                    data.append(keyValue[0]).append(',').append(keyValue[1]).append('\n');
                }
            }
            case ("size") -> data.append(getSizeSpecifiedPrice(Integer.parseInt(s[2]))).append('\n');
        }
    }

    private int[] getBestPriceAsk() {
        Map.Entry<Integer, Integer> entry = ask.firstEntry();
        if (entry != null){
            int[] array = new int[2];
            array[0] = entry.getKey();
            array[1] = entry.getValue();
            return array;
        } else return new int[]{-1, -1};

    }

    private int[] getBestPriceBid() {
        Map.Entry<Integer, Integer> entry = bid.lastEntry();
        if (entry != null){
            int[] array = new int[2];
            array[0] = entry.getKey();
            array[1] = entry.getValue();
            return array;
        }else return new int[] {-1,-1};
    }

    private void writeData(String data) {
            try (FileWriter fileWriter = new FileWriter("output.txt", true)) {
                fileWriter.write(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private int getSizeSpecifiedPrice(int price) {
        if (bid.containsKey(price)) return bid.get(price);
        else return ask.getOrDefault(price, 0);
    }

    private String [] split (String s){
        String [] splitData = new String[4];
        int count = 0;
        int beginIndex = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ','){
                splitData [count] = s.substring(beginIndex, i);
                beginIndex = i+1;
                count++;
            }
        }
        splitData [count] = s.substring(beginIndex);
        return splitData;
    }


}
