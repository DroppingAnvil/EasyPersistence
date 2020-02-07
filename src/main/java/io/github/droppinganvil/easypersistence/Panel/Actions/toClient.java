package io.github.droppinganvil.easypersistence.Panel.Actions;

public class toClient {
    public static String stringify(String operation, String project, String combinedObjectID, String data1, String data2) {
        return operation + "@" +
                project + "@" +
                combinedObjectID + "@" +
                data1 + "@" +
                data2;
    }
}
