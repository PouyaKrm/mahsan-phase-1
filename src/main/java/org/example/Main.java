package org.example;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.example.cli.LibraryCLI;
import org.example.library.model.BaseModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        var cli = new LibraryCLI();
        cli.start();
    }

//    public static void main(String[] args) throws IOException {
//         final Map<String, String> items = new HashMap();
//       items.put("test", "result");
//       System.out.println(items.get("test"));
//    }
}