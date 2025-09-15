import java.util.Scanner;

class Pi {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int iterCount = scanner.nextInt();
        double quarterPi = 1;
        for (int i = 1; i <= iterCount; ++i) {
            quarterPi -= (((i & 1) << 1) - 1) / (2. * i + 1);
        }
        System.out.println("Pi: %f".formatted(quarterPi * 4));

    }
}
