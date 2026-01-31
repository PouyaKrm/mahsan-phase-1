package org.example;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.example.cli.LibraryCLI;
import org.example.exception.ItemNotFoundException;
import org.example.library.model.BaseModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, ItemNotFoundException, InterruptedException {
        var cli = new LibraryCLI();
        cli.start();
    }
}