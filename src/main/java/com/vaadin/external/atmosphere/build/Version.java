package com.vaadin.external.atmosphere.build;

public class Version {
	public static String getVersion() {
		String version = System.getProperty("newVersion");
		if (version == null) {
			throw new IllegalArgumentException("Use -DnewVersion=%version%");
		}
		return version;
	}
}
