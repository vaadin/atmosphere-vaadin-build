package com.vaadin.external.atmosphere.build.filefilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public abstract class AbstractFileFilter implements FileFilter {

    private File root;

    public AbstractFileFilter(File root) {
        this.root = root;
    }

    protected void writeFile(File f, String string) throws IOException {
        BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(f),
                "UTF-8"));
        writer.write(string);
        writer.close();
    }

    protected String readFile(File f) throws IOException {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(f),
                "UTF-8"));
        int bytesRead;
        char[] buf = new char[60000];
        StringBuilder contents = new StringBuilder();
        while ((bytesRead = reader.read(buf)) > 0) {
            contents.append(buf, 0, bytesRead);
        }
        reader.close();
        return contents.toString();
    }
}
