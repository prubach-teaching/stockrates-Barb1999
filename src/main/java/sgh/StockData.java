package sgh;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.math.BigDecimal;
import java.io.FileWriter;

public class StockData {

    public static void getAndProcessChange(String stock) throws IOException {
        String filePath = "data_in/" + stock + ".csv";
         File stock_file = new File(filePath);
        boolean czyistnieje = stock_file.exists();
        if (czyistnieje == false) {
            download("https://query1.finance.yahoo.com/v7/finance/download/" + stock +
                            "?period1=1554504399&period2=1586126799&interval=1d&events=history",
                    filePath);
        }
        Scanner strt = new Scanner(stock_file);
        String linijka = strt.nextLine();

        FileWriter new_stock_file = new FileWriter("data_out/" + stock + ".csv");
        new_stock_file.write(linijka + ",Change" + "\n");

        while (strt.hasNextLine()) {
            linijka = strt.nextLine();
            String[] pozycja = linijka.split(",");
            float otwarcie = Float.valueOf(pozycja[1]);
            float zamkniecie = Float.valueOf(pozycja[4]);
            float zmiana = (zamkniecie - otwarcie) / otwarcie;
            new_stock_file.write(linijka + "," + zmiana * 100 + "\n");
        }
        new_stock_file.close();
    }
    public static void download(String url, String fileName) throws IOException {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }

    public static void main(String[] args) throws IOException {
        String[] stocks = new String[] { "IBM", "MSFT", "GOOG" };
        for (String s : stocks) {
            getAndProcessChange(s);
        }
    }
}
