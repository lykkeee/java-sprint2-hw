import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportEngine {
    public YearlyReport yearlyReport = new YearlyReport();

    public HashMap<Integer, MonthlyReport> monthlyReports = new HashMap<>();

    HashMap<Integer, Integer> monthlyProfitByMonthlyReport = new HashMap<>();
    HashMap<Integer, Integer> monthlyProfitByYearlyReport = new HashMap<>();
    HashMap<Integer, Integer> monthlyExpenseByMonthlyReport = new HashMap<>();
    HashMap<Integer, Integer> monthlyExpenseByYearlyReport = new HashMap<>();
    public void saveStatistics() {
        for (Integer month : monthlyReports.keySet()) {
            for (MonthData monthData : monthlyReports.get(month).informationFromReport) {
                if (monthData.isExpense) {
                    monthlyExpenseByMonthlyReport.put(month, monthData.quantity * monthData.sumOfOne + monthlyExpenseByMonthlyReport.getOrDefault(month, 0));
                } else {
                    monthlyProfitByMonthlyReport.put(month, monthData.quantity * monthData.sumOfOne + monthlyProfitByMonthlyReport.getOrDefault(month, 0));
                }
            }
        }
        if(yearlyReport.informationFromReport != null) {
            for (YearData yearData : yearlyReport.informationFromReport) {
                if (yearData.isExpense) {
                    monthlyExpenseByYearlyReport.put(yearData.month, yearData.amount);
                } else {
                    monthlyProfitByYearlyReport.put(yearData.month, yearData.amount);
                }
            }
        }
    }

    public boolean checkIsMonthlyReportLoaded() {
        boolean isMonthlyReportLoaded = false;
        for (int month = 1; month < 13; month++) {
            if (monthlyReports.get(month) != null) {
                isMonthlyReportLoaded = true;
            }
        }
        return isMonthlyReportLoaded;
    }

    public boolean check() {
        saveStatistics();
        boolean flag = true;
        if (yearlyReport.informationFromReport == null) {
            System.out.println("Загрузите годовой отчёт");
            flag = false;
        } else if (!checkIsMonthlyReportLoaded()) {
            System.out.println("Загрузите месячный отчет");
            flag = false;
        } else {
            for (int month = 1; month < 13; month++) {
                int profitAmountByMonth = monthlyProfitByMonthlyReport.getOrDefault(month, 0);
                int profitAmountByYear = monthlyProfitByYearlyReport.getOrDefault(month, 0);
                if (profitAmountByYear != profitAmountByMonth) {
                    System.out.println("В месяце №" + month + " в месячном отчёте сумма прибыли равна " + profitAmountByMonth + ", а в годовом отчёте сумма прибыли равна " + profitAmountByYear + ".");
                    flag = false;
                }
                int expenseAmountByMonth = monthlyExpenseByMonthlyReport.getOrDefault(month, 0);
                int expenseAmountByYear = monthlyExpenseByYearlyReport.getOrDefault(month, 0);
                if (expenseAmountByYear != expenseAmountByMonth) {
                    System.out.println("В месяце №" + month + " в месячном отчёте сумма расходов равна " + expenseAmountByMonth + ", а в годовом отчёте сумма расходов равна " + expenseAmountByYear + ".");
                    flag = false;
                }
            }
        }
        return flag;
    }

    List<String> readFileContents(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            return null;
        }
    }



    public void loadMonthlyReport () {
        for (int month = 1; month < 13; month++) {
            MonthlyReport monthlyReport = new MonthlyReport();
            monthlyReport.year = 2021;
            monthlyReport.month = month;
            List<String> content = readFileContents("resources/m.20210" + month + ".csv");
            if (content != null) {
                monthlyReport.informationFromReport = new ArrayList<>();
                for (int i = 1; i < content.size(); i++) {
                    String line = content.get(i);
                    String[] parts = line.split(",");
                    String itemName = parts[0];
                    boolean isExpense = Boolean.parseBoolean(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    int sumOfOne = Integer.parseInt(parts[3]);


                    MonthData monthData = new MonthData(itemName, isExpense, quantity, sumOfOne);
                    monthlyReport.informationFromReport.add(monthData);
                }
                monthlyReports.put(month, monthlyReport);
                System.out.println("Отчёт за месяц №" + month + " успешно считан");
            } else {
                System.out.println("Отчёт за месяц № " + month + " не считан. Возможно файл не находится в нужной директории");
            }

        }
    }

    public void getMonthInfo() {

        for (int month = 1; month < 13; month++) {
            if(monthlyReports.get(month) != null) {
                int maxProfit = 0;
                String topProductName = "";
                int maxExp = 0;
                String worthProductName = "";
                for (MonthData monthData : monthlyReports.get(month).informationFromReport) {
                    if (!monthData.isExpense) {
                        if ((monthData.quantity * monthData.sumOfOne) > maxProfit) {
                            maxProfit = monthData.quantity * monthData.sumOfOne;
                            topProductName = monthData.itemName;
                        }
                    }
                }
                for (MonthData monthData : monthlyReports.get(month).informationFromReport) {
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
            } else {
                System.out.println("Необходимо загрузить отчёт за месяц №" + month);
            }
        }
    }

    public void loadYearlyReport() {
        List<String> content = readFileContents("resources/y.2021.csv");
        yearlyReport.informationFromReport = new ArrayList<>();
        if (content != null) {
            for (int i = 1; i < content.size(); i++) {
                String line = content.get(i);
                String[] parts = line.split(",");
                int month = Integer.parseInt(parts[0]);
                int amount = Integer.parseInt(parts[1]);
                boolean isExpense = Boolean.parseBoolean(parts[2]);

                YearData yearData = new YearData(month, amount, isExpense);
                yearlyReport.informationFromReport.add(yearData);
            }
            System.out.println("Отчёт за 2021 год успешно считан");
        } else {
            System.out.println("Отчёт за 2021 год не считан. Возможно файл не находится в нужной директории");
        }

    }

    public void getYearReport() {
        if (yearlyReport.informationFromReport != null) {
            System.out.println("Год 2021");
            int month = 0;
            int profit = 0;
            int firstNumber = 0;
            int amountProfit = 0;
            int amountExpense = 0;

            for (YearData yearData : yearlyReport.informationFromReport) {
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
        } else {
            System.out.println("Необходимо загрузить отчёт за 2021 год");
        }

    }



}
