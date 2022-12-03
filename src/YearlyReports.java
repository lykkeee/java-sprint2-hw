import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;


public class YearlyReports {
    public ArrayList<YearData> informationFromReport = new ArrayList<>();
    public HashMap<Integer, ArrayList<YearData>> infoFromReportYear = new HashMap<>();

    public void loadReport(int year, String path) {
        String content = readFileContents(path);
        String[] lines = content.split("\r?\n");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            String[] parts = line.split(",");
            int month = Integer.parseInt(parts[0]);
            int amount = Integer.parseInt(parts[1]);
            boolean isExpense = Boolean.parseBoolean(parts[2]);

            YearData yearData = new YearData(month, amount, isExpense);
            informationFromReport.add(yearData);
        }
        infoFromReportYear.put(year, informationFromReport);
    }

    public void getYearReport() {
        for (Integer year : infoFromReportYear.keySet()) {
            System.out.println("Год " + year);
            int month = 0;
            int profit = 0;
            int firstNumber = 0;
            int amountProfit = 0;
            int amountExpense = 0;

            for (YearData yearData : informationFromReport) {
                if (!yearData.isExpense) {
                    amountProfit += yearData.amount;
                } else {
                    amountExpense += yearData.amount;
                }
                if (month == yearData.month) {
                    if (!yearData.isExpense) {
                        profit = yearData.amount - firstNumber;
                    } else {
                        profit = firstNumber - yearData.amount;
                    }
                    System.out.println("Месяц №" + yearData.month + " прибыль: " + profit);
                } else {
                    firstNumber = yearData.amount;
                }
                month = yearData.month;
            }
            System.out.println("Средний доход за год: " + amountProfit / 12);
            System.out.println("Средний расход за год: " + amountExpense / 12);
        }
    }

    public String readFileContents(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. Возможно файл не находится в нужной директории.");
            return null;
        }
    }

}
