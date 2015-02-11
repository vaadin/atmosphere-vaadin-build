package com.vaadin.external.atmosphere.build.xmlfilefilter;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;

public class OrgAtmosphereLogger extends PomXmlFilter {

    public OrgAtmosphereLogger(File root) {
        super(root);
    }

    @Override
    public void process(File f, Document d) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        // StringBuilder contents = new StringBuilder();
        String line;
        // boolean needUpdate = false;
        while ((line = reader.readLine()) != null) {
            if (line.contains("org.atmosphere")) {
                if (line.contains("<gwtModule>")) {
                    // Ok, refers to a class name which we do not change
                } else if (f.getAbsolutePath().contains("jquery-pubsub")) {
                    // Contains some commented out examples we don't care about
                } else {
                    Logger.getLogger(getClass().getName())
                        .warning("File " + f.getPath()
                            + " contains org.atmosphere: " + line);
                }
            }
        }
        reader.close();
    }
}
