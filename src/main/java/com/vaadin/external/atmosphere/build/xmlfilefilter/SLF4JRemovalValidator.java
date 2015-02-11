package com.vaadin.external.atmosphere.build.xmlfilefilter;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SLF4JRemovalValidator extends PomXmlFilter {

    public SLF4JRemovalValidator(File root) {
        super(root);
    }

    @Override
    public void process(File f, Document d) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        // StringBuilder contents = new StringBuilder();
        String line;
        // boolean needUpdate = false;
        while ((line = reader.readLine()) != null) {
            if (line.contains("slf4j")) {
                if (line.contains("<exclude>org.slf4j:slf4j-api</exclude>")) {
                    // this is ok
                } else if (line
                    .contains("<groupId>com.vaadin.external.slf4j</groupId>")) {
                    // this is ok
                } else if (line
                    .contains(
                        "<slf4j-impl-version>1.6.1</slf4j-impl-version>")) {
                    // this is ok
                } else if (line
                    .contains("<slf4j-version>1.6.1</slf4j-version>")) {
                    // this is ok
                } else {
                    throw new IllegalStateException("File " + f.getPath()
                        + " contains slf4j: " + line);
                }
            }
        }
        reader.close();
    }
}
