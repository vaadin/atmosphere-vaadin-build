package com.vaadin.external.atmosphere.build.filefilter;

import java.io.File;

public interface FileFilter {

    boolean needsProcessing(File f);

    void process(File f) throws Exception;
}
