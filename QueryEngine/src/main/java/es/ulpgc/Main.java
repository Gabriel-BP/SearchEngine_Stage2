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
        for (String token : tokens) {
            Set<String> results = invertedIndex.search(token);
            if (firstToken) {
                finalResults.addAll(results);
                firstToken = false;
            } else {
                finalResults.retainAll(results); // Intersección de resultados
            }
        }

        if (finalResults.isEmpty()) {
            System.out.println("No results found for the query.");
        } else {
            System.out.println("Documentos encontrados:");
            for (String ebookNumber : finalResults) {
                System.out.println("EbookNumber: " + ebookNumber);
            }
        }

        scanner.close();
    }
}