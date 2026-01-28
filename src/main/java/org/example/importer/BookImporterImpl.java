package org.example.importer;

import org.example.exception.InvalidInputData;
import org.example.library.model.BaseModel;
import org.example.library.model.ModelFactory;
import org.example.utils.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;

public class BookImporterImpl implements BookImporter {

    private final ModelFactory factory = ModelFactory.getInstance();


    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, Class<T> clazz) throws IOException {
        List<T> books = new ArrayList<>();
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                var book = recreateData(line);
                books.add((T) book);
            }

        } catch (InvalidInputData e) {
            throw new RuntimeException(e);
        }

        return Utils.listToArray(books, clazz);
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, Class<T> clazz, String terminationLine) throws IOException {
        return getModels(inputStream, clazz, Optional.of(terminationLine));
    }

    @Override
    public <T extends BaseModel> void writeToFile(T[] data,  Path folderPath, String fileName) throws IOException {
        var extIn = fileName.lastIndexOf(".");
        var bf = new StringBuffer();
        bf.append(fileName.substring(0, extIn));
        bf.append(".txt");
        var filePath = folderPath.resolve(bf.toString());
        Files.createDirectories(filePath.getParent());
        try (FileWriter fileWriter = new FileWriter(filePath.toFile(), true)) {
            for (T item : data) {
                var f = factory.getFactory(item.getClass());
                var line = createStoreLine(item);
                fileWriter.write(line);
                fileWriter.write("\n");
            }
        }
    }


    @Override
    public boolean supportsStdIn() {
        return true;
    }


    private <T extends BaseModel> T[] getModels(InputStream inputStream, Class<T> clazz, Optional<String> terminationLine) throws IOException {
        List<Object> books = new ArrayList<>();
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (terminationLine.isPresent() && line.equals(terminationLine.get())) {
                    break;
                }
                var book = factory.create(line, clazz);
                books.add(book);

            }

        } catch (InvalidInputData e) {
            throw new RuntimeException(e);
        }

        Object[] result = new Object[books.size()];
        books.toArray(result);
        return Arrays.copyOf(result, result.length,
                (Class<? extends T[]>) java.lang.reflect.Array
                        .newInstance(clazz, 0).getClass());
    }

    private BaseModel recreateData(String line) throws InvalidInputData {
        try {
            var splited = line.split(",");
            Class cs = Class.forName(splited[0]);
            var f = factory.getFactory(cs);
            line = line.substring(splited[0].length() + 1);
            splited = line.split(f.getDelimeter());
            var originalLine = String.join(f.getDelimeter(), splited);
            return factory.create(originalLine, cs);
        } catch (ClassNotFoundException e) {
            throw new InvalidInputData(MessageFormat.format("invalid class name in: {0}", line), e);
        }
    }



    private <T extends BaseModel> String createStoreLine(T item) {
        var f = factory.getFactory(item.getClass());
        return MessageFormat.format("{0}" + "," + "{1}", item.getClass().getName(), factory.parseToString(item));
    }

}

