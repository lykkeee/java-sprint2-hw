import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ReportEngine reportEngine = new ReportEngine();
        Scanner scanner = new Scanner(System.in);


        while (true) {
            printMenu();
            int command = scanner.nextInt();
            if(command == 1) {
                reportEngine.loadMonthlyReport();
            } else if (command == 2) {
                reportEngine.loadYearlyReport();
            } else if (command == 3) {
                boolean answer = reportEngine.check();
                System.out.println("Результат проверки: " + answer);
            } else if (command == 4) {
                reportEngine.getMonthInfo();
            } else if (command == 5) {
               reportEngine.getYearReport();
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

