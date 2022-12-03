import java.util.HashMap;

public class Checker {
    public MonthlyReports monthlyReports;
    public YearlyReports yearReports;

    public Checker (MonthlyReports monthlyReports, YearlyReports yearReports) {
        this.monthlyReports = monthlyReports;
        this.yearReports = yearReports;
    }

    public boolean check() {
        boolean flag = true;
        HashMap<Integer, Integer> monthlyProfitByMonthlyReport = new HashMap<>();
        HashMap<Integer, Integer> monthlyProfitByYearlyReport = new HashMap<>();
        HashMap<Integer, Integer> monthlyExpenseByMonthlyReport = new HashMap<>();
        HashMap<Integer, Integer> monthlyExpenseByYearlyReport = new HashMap<>();

        for (Integer month : monthlyReports.infoFromReport.keySet()) {
            for (MonthData monthData : monthlyReports.infoFromReport.get(month)) {
                if (monthData.isExpense) {
                    monthlyExpenseByMonthlyReport.put(month, monthData.quantity * monthData.sumOfOne + monthlyExpenseByMonthlyReport.getOrDefault(month, 0));
                } else {
                    monthlyProfitByMonthlyReport.put(month, monthData.quantity * monthData.sumOfOne + monthlyProfitByMonthlyReport.getOrDefault(month, 0));
                }
            }
        }
        for (YearData yearData : yearReports.informationFromReport) {
            if (yearData.isExpense) {
                monthlyExpenseByYearlyReport.put(yearData.month, yearData.amount);
            } else {
                monthlyProfitByYearlyReport.put(yearData.month, yearData.amount);
            }
        }
        for (int month = 1; month < 13; month++) {
            int profitAmountByMonth = monthlyProfitByMonthlyReport.getOrDefault(month, 0);
            int profitAmountByYear = monthlyProfitByYearlyReport.getOrDefault(month, 0);
            if (profitAmountByYear != profitAmountByMonth) {
                System.out.println("В месяце №" + month + " в месячном отчёте сумма прибыли равна " + profitAmountByMonth + ", а в годовом отёте сумма прибыли равна " + profitAmountByYear + ".");
                flag = false;
            }
            int expenseAmountByMonth = monthlyExpenseByMonthlyReport.getOrDefault(month, 0);
            int expenseAmountByYear = monthlyExpenseByYearlyReport.getOrDefault(month, 0);
            if (expenseAmountByYear != expenseAmountByMonth) {
                System.out.println("В месяце №" + month + " в месячном отчёте сумма расходов равна " + expenseAmountByMonth + ", а в годовом отёте сумма расходов равна " + expenseAmountByYear + ".");
                flag = false;
            }
        }
        return flag;
    }
}
