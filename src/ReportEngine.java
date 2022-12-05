import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportEngine {
    public HashMap<Integer, YearlyReport> yearlyReports = new HashMap<>();

    public HashMap<Integer, MonthlyReport> monthlyReports = new HashMap<>();


    public boolean check() {
        boolean flag = true;
        HashMap<Integer, Integer> monthlyProfitByMonthlyReport = new HashMap<>();
        HashMap<Integer, Integer> monthlyProfitByYearlyReport = new HashMap<>();
        HashMap<Integer, Integer> monthlyExpenseByMonthlyReport = new HashMap<>();
        HashMap<Integer, Integer> monthlyExpenseByYearlyReport = new HashMap<>();

        for (Integer month : monthlyReports.keySet()) {
            for (MonthData monthData : monthlyReports.get(month).informationFromReport) {
                if (monthData.isExpense) {
                    monthlyExpenseByMonthlyReport.put(month, monthData.quantity * monthData.sumOfOne + monthlyExpenseByMonthlyReport.getOrDefault(month, 0));
                } else {
                    monthlyProfitByMonthlyReport.put(month, monthData.quantity * monthData.sumOfOne + monthlyProfitByMonthlyReport.getOrDefault(month, 0));
                }
            }
        }
        for (Integer year : yearlyReports.keySet()) {
            for (YearData yearData : yearlyReports.get(year).informationFromReport) {
                if (yearData.isExpense) {
                    monthlyExpenseByYearlyReport.put(yearData.month, yearData.amount);
                } else {
                    monthlyProfitByYearlyReport.put(yearData.month, yearData.amount);
                }
            }
        }

        boolean isMonthlyReportLoaded = false;
        for (int month = 1; month < 13; month++) {
            if (monthlyReports.get(month) != null) {
                isMonthlyReportLoaded = true;
            }
        }
        if (yearlyReports.get(21) == null) {
            System.out.println("Загрузите годовой отчёт");
            flag = false;
        } else if (!isMonthlyReportLoaded) {
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

    public String readFileContents(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            return null;
        }
    }



    public void loadMonthlyReport () {
        for (int month = 1; month < 13; month++) {
            monthlyReports.put(month, new MonthlyReport());
            monthlyReports.get(month).informationFromReport = new ArrayList<>();
            String content = readFileContents("resources/m.20210" + month + ".csv");
            if (content != null) {
                String[] lines = content.split("\r?\n");
                for (int i = 1; i < lines.length; i++) {
                    String line = lines[i];
                    String[] parts = line.split(",");
                    String itemName = parts[0];
                    boolean isExpense = Boolean.parseBoolean(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    int sumOfOne = Integer.parseInt(parts[3]);


                    MonthData monthData = new MonthData(itemName, isExpense, quantity, sumOfOne);
                    monthlyReports.get(month).informationFromReport.add(monthData);
                }
                System.out.println("Отчёт за месяц №" + month + " успешно считан");
            } else {
                monthlyReports.remove(month);
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
        for (int year = 21; year < 23; year++) {
            yearlyReports.put(year, new YearlyReport());
            yearlyReports.get(year).informationFromReport = new ArrayList<>();
            String content = readFileContents("resources/y.20" + year + ".csv");
            if (content != null) {
                String[] lines = content.split("\r?\n");
                for (int i = 1; i < lines.length; i++) {
                    String line = lines[i];
                    String[] parts = line.split(",");
                    int month = Integer.parseInt(parts[0]);
                    int amount = Integer.parseInt(parts[1]);
                    boolean isExpense = Boolean.parseBoolean(parts[2]);

                    YearData yearData = new YearData(month, amount, isExpense);
                    yearlyReports.get(year).informationFromReport.add(yearData);
                }
                System.out.println("Отчёт за 20" + year + " год успешно считан");
            } else {
                yearlyReports.remove(year);
                System.out.println("Отчёт за 20" + year + " год не считан. Возможно файл не находится в нужной директории");
            }
        }
    }

    public void getYearReport() {
        for (int year = 21; year < 23; year++) {
            if (yearlyReports.get(year) != null) {
                System.out.println("Год 20" + year);
                int month = 0;
                int profit = 0;
                int firstNumber = 0;
                int amountProfit = 0;
                int amountExpense = 0;

                for (YearData yearData : yearlyReports.get(year).informationFromReport) {
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
                System.out.println("Необходимо загрузить отчёт за 20" + year + " год");
            }
        }
    }



}
