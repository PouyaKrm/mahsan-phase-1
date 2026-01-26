package org.example.importer;

import org.example.constansts.ResourceType;
import org.example.library.model.BaseModel;

import java.util.Scanner;

public interface BookImporter {
    <T extends BaseModel> T[] getModels(Scanner scanner, ResourceType resourceType, Class<T> clazz);

    <T extends BaseModel> T[] getModels(Scanner scanner, ResourceType resourceType, Class<T> clazz, String terminationLine);
}
