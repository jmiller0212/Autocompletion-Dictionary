// Jarod Miller

import java.util.ArrayList;

public class DLB {

    private DLBnode root;
    public int searchHits;

    public static class DLBnode {
        private char value;
        private DLBnode sibling;
        private DLBnode child;

        DLBnode(char value, DLBnode sibling, DLBnode child) {
            this.value = value;
            this.sibling = sibling;
            this.child = child;
        }
    }

    // Initializes an empty string symbol table
    public DLB() { }

    public void addWord(String key) {
        // create the root node
        if(root == null) {
            root = new DLBnode(key.charAt(0), null, null);
        }
        DLBnode currentNode = root;
        for(int i = 0; i < key.length(); i++) {
            // if this is the first letter, find the key for that value
            if(i == 0) {
                currentNode = findSibling(currentNode, key.charAt(i));
            }
            if(i > 0) {
                if(currentNode.child == null) {
                    DLBnode newNode = new DLBnode(key.charAt(i), null, null);
                    currentNode.child = newNode;
                    currentNode = currentNode.child;
                }
                else if(currentNode.child != null) {
                    currentNode = findSibling(currentNode.child, key.charAt(i));
                }
            }
        }
        // handles terminator node
        if(currentNode.child == null) {
            DLBnode terminatorNode = new DLBnode('^', null, null);
            currentNode.child = terminatorNode;
        }
        else if(currentNode.child != null) {
            currentNode = findSibling(currentNode.child, '^');
        }
    }
    public DLBnode findSibling(DLBnode currentSibling, char key) {
        if(currentSibling.value == key) {
            return currentSibling;
        }
        while(currentSibling.value != key) {
            if(currentSibling.sibling == null) {
                DLBnode newNode = new DLBnode(key, null, null);
                currentSibling.sibling = newNode;
                return newNode;
            }
            else if(currentSibling.sibling.value == key) {
                return currentSibling.sibling;
            }
            currentSibling = currentSibling.sibling;
        }
        return null;
    }

// start of preorder traversal attempt
    public ArrayList<String> suggestWords(String key) {
        DLBnode n = findPrefix(key);
        ArrayList<String> suggestions = new ArrayList<String>(5);
        suggestions = preorder(n, key, suggestions);
        // reset the search hits for next time
        searchHits = 0;
        return suggestions;
    }
// start the recursion
    private ArrayList<String> preorder(DLBnode currentNode, String key, ArrayList<String> al) {
        if(searchHits < 5) {
            if(currentNode == null)
                return null;
            // this means we have just found a word (no child node).
            if(currentNode.value == '^') {
                // with no child or sibling
                if(currentNode.sibling == null) {
                    // we add the word
                    al.add(key);
                    searchHits++;
                    // and backtrack
                    return al;
                }
                // with no child
                else {
                    // we add the word
                    al.add(key);
                    searchHits++;
                    // and enter the sibling node in search for another word.
                    preorder(currentNode.sibling, key, al);
                    return al;
                }
            }
            // continue traversing down the tree
            if(currentNode.child != null) {
                String c = Character.toString(currentNode.value);
                key += c;
                preorder(currentNode.child, key, al);
            }
            // then finally we check the sibling
            if(currentNode.sibling != null) {
                key = key.substring(0, key.length() - 1);
                preorder(currentNode.sibling, key, al);
            }
        }
        return al;
    }
    // findPrefix will accept a string (or the inputted character) and search through the DLB for the prefix
    // and it will return the currentNode to begin the preorder traversal (to autocomplete the word)
    // this node will act as the "root" node in our depth first preorder traversal
    private DLBnode findPrefix(String key) {
        DLBnode currentNode = root;
        for(int i = 0; i < key.length(); i++) {
            while(currentNode.value != key.charAt(i)) {
                currentNode = currentNode.sibling;
                if(currentNode == null) {
                    return null;
                }
                if(currentNode.value == key.charAt(i)) {
                    break;
                }
            }
            currentNode = currentNode.child;
        }
        return currentNode;
    }
}