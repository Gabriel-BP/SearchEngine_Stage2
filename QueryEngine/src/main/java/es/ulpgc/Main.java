package es.ulpgc;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Query Engine!");
        System.out.println("Please, write down your query:");

        String userInput = scanner.nextLine();
        Query query = new Query(userInput);

        QueryTokenizer tokenizer = new QueryTokenizer();
        List<String> tokens = tokenizer.tokenize(query);

        System.out.println("Tokens generated:");
        for (String token : tokens) {
            System.out.println(token);
        }

        scanner.close();
    }
}