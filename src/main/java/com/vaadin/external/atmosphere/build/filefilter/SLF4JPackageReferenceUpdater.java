package com.vaadin.external.atmosphere.build.filefilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SLF4JPackageReferenceUpdater extends JavaFileUpdater {

    public SLF4JPackageReferenceUpdater(File root) {
        super(root);
    }

    @Override
    public void process(File f) throws Exception {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(f),
                StandardCharsets.UTF_8));
        StringBuilder contents = new StringBuilder();
        String line;
        boolean needUpdate = false;
        while ((line = reader.readLine()) != null) {
            if (line.contains("import org.slf4j")) {
                needUpdate = true;
                line = line.replace("import org.slf4j",
                    "import com.vaadin.external.org.slf4j");
            }
            if (line.contains("private org.slf4j")) {
                // Variable declarations
                needUpdate = true;
                line = line.replace("private org.slf4j",
                    "private com.vaadin.external.org.slf4j");
            }
            contents.append(line + "\n");
        }

        reader.close();
        if (needUpdate) {
            writeFile(f, contents.toString());
        }

    }

}
