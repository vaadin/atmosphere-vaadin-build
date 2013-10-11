package com.vaadin.external.atmosphere.build.filefilter;
import java.io.File;

public abstract class JavaFileUpdater extends AbstractFileFilter {

	public JavaFileUpdater(File root) {
		super(root);
	}

	@Override
	public boolean needsProcessing(File f) {
		if (f.isDirectory()) {
			return false;
		}
		return f.getName().endsWith(".java");
	}

}
