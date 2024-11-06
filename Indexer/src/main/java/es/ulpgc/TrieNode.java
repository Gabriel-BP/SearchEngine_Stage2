package es.ulpgc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TrieNode {
    Map<Character, TrieNode> children = new Hashtable<>();
    List<String> booksID = new ArrayList<>();
}