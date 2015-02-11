package com.vaadin.external.atmosphere.build.xmlfilefilter;

import org.w3c.dom.Document;

import java.io.File;

public interface XMLFileFilter {

    boolean needsProcessing(File f);

    void process(File f, Document d) throws Exception;
}
