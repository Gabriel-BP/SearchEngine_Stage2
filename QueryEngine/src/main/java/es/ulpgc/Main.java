package es.ulpgc;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione la fuente de datos:");
        System.out.println("1. CSV");
        System.out.println("2. Datamart");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        DataSource dataSource;
        if (choice == 1) {
            String filePath = "index_content.csv";
            dataSource = new CSVDataSource(filePath);
        } else if (choice == 2) {
            String datamartPath = "datamart_content";
            System.out.println("Loading data from the datamart...");
            dataSource = new DatamartDataSource(datamartPath);
        } else {
            System.out.println("Invalid choice. Please restart the program and enter 1 or 2.");
            return;
        }

        InvertedIndex invertedIndex = new InvertedIndex(dataSource);

        System.out.println("Welcome to the Query Engine!");
        System.out.println("Please, write down your query:");

        String userInput = scanner.nextLine();
        Query query = new Query(userInput);

        QueryTokenizer tokenizer = new QueryTokenizer();
        List<String> tokens = tokenizer.tokenize(query);

        Set<String> finalResults = new HashSet<>();
        boolean firstToken = true;
        boolean foundAny = false;

        for (String token : tokens) {
            Set<String> results = invertedIndex.search(token);
            if (!results.isEmpty()) {
                foundAny = true;
                System.out.println("Results found for the term: " + token);
                for (String ebookNumber : results) {
                    System.out.println("EbookNumber: " + ebookNumber);
                }
                if (firstToken) {
                    finalResults.addAll(results);
                    firstToken = false;
                } else {
                    finalResults.retainAll(results);
                }
            } else {
                System.out.println("No results were found for the term: " + token);
            }
        }

        scanner.close();
    }
}