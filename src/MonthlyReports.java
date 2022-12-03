import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class MonthlyReports {

    public HashMap<Integer, ArrayList<MonthData>> infoFromReport = new HashMap<>();

    public void loadReport (Integer month, String path) {
        ArrayList<MonthData> informationFromReport = new ArrayList<>();
        String content = readFileContents(path);
        String[] lines = content.split("\r?\n");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            String[] parts = line.split(",");
            String itemName = parts[0];
            boolean isExpense = Boolean.parseBoolean(parts[1]);
            int quantity = Integer.parseInt(parts[2]);
            int sumOfOne = Integer.parseInt(parts[3]);

            MonthData monthData = new MonthData(itemName, isExpense, quantity, sumOfOne);
            informationFromReport.add(monthData);
        }
        infoFromReport.put(month, informationFromReport);
    }

    public void getMonthInfo() {
        for (Integer month : infoFromReport.keySet()) {
            int maxProfit = 0;
            String topProductName = "";
            int maxExp = 0;
            String worthProductName = "";
            for (MonthData monthData : infoFromReport.get(month)) {
                if (!monthData.isExpense) {
                    if ((monthData.quantity * monthData.sumOfOne) > maxProfit) {
                        maxProfit = monthData.quantity * monthData.sumOfOne;
                        topProductName = monthData.itemName;
                    }
                }
            }
            for (MonthData monthData : infoFromReport.get(month)) {
                if (monthData.isExpense) {
                    if ((monthData.quantity * monthData.sumOfOne) > maxExp) {
                        maxExp = monthData.quantity * monthData.sumOfOne;
                        worthProductName = monthData.itemName;
                    }
                }
            }
            System.out.println("Месяц №" + month);
            System.out.println("Самый прибыльный товар: " + topProductName + ". Прибыль " + maxProfit);
            System.out.println("Самая большая трата: " + worthProductName + ". Расход " + maxExp);
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
