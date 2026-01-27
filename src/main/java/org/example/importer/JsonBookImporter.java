package org.example.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.constansts.ResourceType;
import org.example.library.model.BaseModel;
import org.example.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JsonBookImporter implements BookImporter {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonBookImporter() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz) throws IOException {
        List<T> l = mapper.readValue(inputStream,  mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        return Utils.listToArray(l, clazz);
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz, String terminationLine) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends BaseModel> void writeToFile(T[] data, Path filePath) throws IOException {

    }

    @Override
    public Object[] getModels(InputStream inputStream) throws IOException {
        return new Object[0];
    }

    @Override
    public boolean supportsStdIn() {
        return false;
    }
}
