package es.ulpgc;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.loadIndex("index_content.csv");

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
                    finalResults.retainAll(results); // Intersección de resultados
                }
            } else {
                System.out.println("No results were found for the term: " + token);
            }
        }

//        if (foundAny && !finalResults.isEmpty()) {
//            System.out.println("\nMatching documents for all terms:");
//            for (String ebookNumber : finalResults) {
//                System.out.println("EbookNumber: " + ebookNumber);
//            }
//        } else if (!foundAny) {
//            System.out.println("No results were found for your query.");
//        }

        scanner.close();
    }
}