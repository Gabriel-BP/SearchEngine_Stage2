package es.ulpgc;

import java.util.HashMap;
import java.util.Set;

public class InvertedIndex {
    private Map<String, Set<String>> wordIndex = new HashMap<>();

    public void addBook(Book book) {
        String[] words = book.getContent().split("\\s+");
        for (String word : words) {
            wordIndex
                    .computeIfAbsent(word, k -> new HashSet<>())
                    .add(book.getTitle());
        }
    }

    public Set<String> searchByWord(String word) {
        return wordIndex.getOrDefault(word.toLowerCase(), Collections.emptySet());
    }
}