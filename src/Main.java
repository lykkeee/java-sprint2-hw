import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        YearlyReports yearlyReports = new YearlyReports();
        MonthlyReports monthlyReports = new MonthlyReports();

        Checker checker = new Checker(monthlyReports, yearlyReports);
        Scanner scanner = new Scanner(System.in);


        while (true) {
            printMenu();
            int command = scanner.nextInt();
            if(command == 1) {
                for (int i = 1; i < 4; i++) {
                    monthlyReports.loadReport(i, "resources/m.20210" + i + ".csv");
                }
            } else if (command == 2) {
                yearlyReports.loadReport(2021, "resources/y.2021.csv");
            } else if (command == 3) {
                boolean answer = checker.check();
                checker.check();
                System.out.println("Результат проверки: " + answer);
            } else if (command == 4) {
                monthlyReports.getMonthInfo();
            } else if (command == 5) {
                yearlyReports.getYearReport();
            } else if (command == 0) {
                break;
            } else {
                System.out.println("Такой команды не существует");
            }
        }

    }



    public static void printMenu() {
        System.out.println("Выберите команду:");
        System.out.println("1. Считать все месячные отчёты");
        System.out.println("2. Считать годовой отчёт");
        System.out.println("3. Сверить отчёты");
        System.out.println("4. Вывести информацию о всех месячных отчётах");
        System.out.println("5. Вывести информацию о годовом отчёте");
        System.out.println("0. Завершить программу");
    }
}

