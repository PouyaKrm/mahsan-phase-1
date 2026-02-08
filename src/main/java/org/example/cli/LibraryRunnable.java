package org.example.cli;

import org.example.concurrent.LibraryCommand;
import org.example.constansts.LibraryOperationType;

import java.util.concurrent.BlockingQueue;

public class LibraryRunnable implements Runnable {

   private final BlockingQueue<LibraryCommand> commands;

    public LibraryRunnable(BlockingQueue<LibraryCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void run() {
        try {
            LibraryCommand command;
            while (!(command = commands.take()).getOperationType().equals(LibraryOperationType.END)) {
                command.execute();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
