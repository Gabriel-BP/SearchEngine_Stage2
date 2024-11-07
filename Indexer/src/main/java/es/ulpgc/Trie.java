package es.ulpgc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word, String bookID) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
        }

        if (!node.booksID.contains(bookID)) {
            node.booksID.add(bookID);
        }
    }

    public Map<String, List<String>> toMap() {
        Map<String, List<String>> result = new HashMap<>();
        traverseTrie(root, "", result);
        return result;
    }

    private void traverseTrie(TrieNode node, String currentWord, Map<String, List<String>> result) {
        if (!node.booksID.isEmpty()) {
            result.put(currentWord, new ArrayList<>(node.booksID));
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            traverseTrie(entry.getValue(), currentWord + entry.getKey(), result);
        }
    }
}
