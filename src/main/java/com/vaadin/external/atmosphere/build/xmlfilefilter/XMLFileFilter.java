package com.vaadin.external.atmosphere.build.xmlfilefilter;
import java.io.File;

import org.w3c.dom.Document;

public interface XMLFileFilter {

	boolean needsProcessing(File f);

	void process(File f, Document d) throws Exception;
}
