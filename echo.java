import java.util.Scanner;

class Echo {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (true) {

            int i = s.nextInt();
            if (i == -1) {

                break;
            }
            System.out.println(i);
        }
        s.close();
    }
}
