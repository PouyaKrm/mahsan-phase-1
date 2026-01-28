package org.example.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.library.model.BaseModel;
import org.example.utils.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class JsonBookImporterImpl implements BookImporter {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonBookImporterImpl() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, Class<T> tClass) throws IOException {
        List<T> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<T> result = mapper.readValue(line, mapper.getTypeFactory().constructCollectionType(List.class, tClass));
                items.addAll(result);
            }
        }
        return Utils.listToArray(items, tClass);
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, Class<T> clazz, String terminationLine) {
        throw new UnsupportedOperationException("method is not implemented yet");
    }

    @Override
    public <T extends BaseModel> void writeToFile(T[] data, Path folderPath, String fileName) throws IOException {
        var extIn = fileName.lastIndexOf(".");
        var bf = new StringBuffer();
        bf.append(fileName.substring(0, extIn));
        bf.append(".txt");
        var filePath = folderPath.resolve(bf.toString());
        Files.createDirectories(filePath.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            writer.write(mapper.writeValueAsString(data));
            writer.newLine();
        }
    }

    @Override
    public boolean supportsStdIn() {
        return false;
    }
}
