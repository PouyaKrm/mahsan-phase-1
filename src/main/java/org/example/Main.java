package org.example;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.example.cli.LibraryCLI;
import org.example.exception.ItemNotFoundException;
import org.example.library.model.BaseModel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class AddPerson {
    // This function fills in a Person message based on user input.
    static org.example.protos.Person PromptForAddress(BufferedReader stdin,
                                                      PrintStream stdout) throws IOException {
        org.example.protos.Person.Builder person = org.example.protos.Person.newBuilder();

        stdout.print("Enter person ID: ");
        person.setId(Integer.valueOf(stdin.readLine()));

        stdout.print("Enter name: ");
        person.setName(stdin.readLine());

        stdout.print("Enter email address (blank for none): ");
        String email = stdin.readLine();
        if (email.length() > 0) {
            person.setEmail(email);
        }

        while (true) {
            stdout.print("Enter a phone number (or leave blank to finish): ");
            String number = stdin.readLine();
            if (number.length() == 0) {
                break;
            }

            org.example.protos.Person.PhoneNumber.Builder phoneNumber =
                    org.example.protos.Person.PhoneNumber.newBuilder().setNumber(number);

            stdout.print("Is this a mobile, home, or work phone? ");
            String type = stdin.readLine();
            if (type.equals("mobile")) {
                phoneNumber.setType(org.example.protos.Person.PhoneType.PHONE_TYPE_MOBILE);
            } else if (type.equals("home")) {
                phoneNumber.setType(org.example.protos.Person.PhoneType.PHONE_TYPE_HOME);
            } else if (type.equals("work")) {
                phoneNumber.setType(org.example.protos.Person.PhoneType.PHONE_TYPE_WORK);
            } else {
                stdout.println("Unknown phone type.  Using default.");
            }

            person.addPhones(phoneNumber);
        }

        return person.build();
    }

    // Main function:  Reads the entire address book from a file,
    //   adds one person based on user input, then writes it back out to the same
    //   file.
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage:  AddPerson ADDRESS_BOOK_FILE");
            System.exit(-1);
        }

        org.example.protos.AddressBook.Builder addressBook = org.example.protos.AddressBook.newBuilder();

        // Read the existing address book.
        try {
            addressBook.mergeFrom(new FileInputStream("out.txt"));
        } catch (FileNotFoundException e) {
            System.out.println(args[0] + ": File not found.  Creating a new file.");
        }

        // Add an address.
        addressBook.addPeople(
                PromptForAddress(new BufferedReader(new InputStreamReader(System.in)),
                        System.out));

        // Write the new address book back to disk.
        FileOutputStream output = new FileOutputStream("out.txt");
        addressBook.build().writeTo(output);
        output.close();
    }
}