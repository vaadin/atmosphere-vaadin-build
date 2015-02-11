package com.vaadin.external.atmosphere.build.xmlfilefilter;

import java.io.File;

public abstract class RootPomXmlFilter extends PomXmlFilter {

    public RootPomXmlFilter(File root) {
        super(root);
    }

    @Override
    public boolean needsProcessing(File f) {
        return super.needsProcessing(f) && f.getParentFile().equals(getRoot());
    }

}
