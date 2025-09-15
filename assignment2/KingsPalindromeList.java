import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Reads a list of numbers, and can reconstruct the corresponding list of
 * Palindromes,
 * produce the size of the largest magic set, and the content of that magic set.
 * 
 * Usage:
 * The input consists of three lines. The first contains the task number:
 * 1. correct the numbers into palindromes
 * 2. produce the length of the longest magic set
 * 3. produce the longest magic set
 * The second line of input contains the count of numbers on the list of numbers.
 * The third line contains the list of numbers that will be reconstructed.

 * 
 * @author Jerzy Puchalski
 * @ID 2253461
 * @author Kerem Can Ayhan
 * @ID 2010399
 * 
 */
class KingsPalindromeList {

    /**
     * Takes number as string it divides the number into two parts, takes the front part,
     * reverses it and replaces the back part with it, if the new palindrome is smaller than the
     * original number then the middle of the number will be increased by 1.
     * */
    static String correctNumber(String number) {
        // the value as it was, only parsed to a long for future comparison
        long startValue = Long.parseLong(number);
        // length of the frragment we are gonna
        int len = (number.length() + 1) / 2 - 1;
        // the lindrome we are trying to make
        String newNumber = number.substring(0, len + 1);
        // used in case the new number is to small
        int mul = 1;
        if (number.length() % 2 == 0) {
            newNumber += number.charAt(len);
            mul = 11;
        }
        // temp value for reversing the tail to make it a lindrome
        StringBuffer tailEnd = new StringBuffer(number.substring(0, len));
        tailEnd.reverse();
        newNumber += tailEnd;

        if (startValue > Long.parseLong(newNumber)) {
            newNumber = Long.toString(Long.parseLong(newNumber) + (long) (Math.pow(10, len) * mul));
        }
        return newNumber;
    }

    /**
     * Produces the magic set with n as the biggest number in it, from numbers within "allNumbers".
     */
    static ArrayList<String> checkMagicSetAtN(String n, HashSet<String> allNumbers) {
        ArrayList<String> strings = new ArrayList<>();
        // System.err.println(n);
        for (int i = 0; i <= n.length() / 2; ++i) {
            String subString = n.substring(i, n.length() - i);
            // System.err.println(subString);
            if (allNumbers.contains(subString)) {
                // System.err.println("found substring");
                strings.add(subString);
            }
        }
        return strings;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int task = scanner.nextInt();
        int numberCount = scanner.nextInt();
        ArrayList<String> numbers = new ArrayList<>(numberCount);

        for (int i = 0; i < numberCount; i++) {
            numbers.add(Long.toString(scanner.nextLong()));
        }

        ArrayList<String> palindromes = new ArrayList<>(numberCount);

        for (String string : numbers) {
            palindromes.add(correctNumber(string));
        }
        scanner.close();
        if (task == 1) {
            System.out.println(String.join(" ", palindromes));
            return;
        }
        HashSet<String> palindromeSet = new HashSet<>(palindromes);
        ArrayList<String> longest = new ArrayList<>();

        for (String string : palindromes) {

            ArrayList<String> candidate = checkMagicSetAtN(string, palindromeSet);
            //System.err.println(candidate);
            String candidateFirst = candidate.get(0);

            if (candidate.size() > longest.size() || (candidate.size() == longest.size()
                    && Long.parseLong(candidateFirst) > Long.parseLong(longest.get(0)))) {
                longest = candidate;
            }

        }
        if (task == 2) {
            System.out.println(longest.size());
        } else if (task == 3) {
            Collections.reverse(longest);
            System.out.println(String.join(" ", longest));
        }
    }
}
