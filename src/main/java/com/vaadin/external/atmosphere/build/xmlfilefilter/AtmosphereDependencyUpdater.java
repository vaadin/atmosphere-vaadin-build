package com.vaadin.external.atmosphere.build.xmlfilefilter;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.vaadin.external.atmosphere.build.Version;

/**
 * Updates org.atmosphere / xyz dependencies to com.vaadin.external.atmosphere
 * 
 */
public class AtmosphereDependencyUpdater extends PomXmlFilter {
	public static final String GROUP_ID_PREFIX = ProjectGroupIdFilter.GROUP_ID_PREFIX;

	public AtmosphereDependencyUpdater(File root) {
		super(root);
	}

	@Override
	public void process(File f, Document doc) throws Exception {
		NodeList nodes = findNodes(doc,
				"/project/dependencies/dependency/groupId[text()='org.atmosphere']");
		for (int i = 0; i < nodes.getLength(); i++) {
			Element groupId = (Element) nodes.item(i);
			groupId.setTextContent(GROUP_ID_PREFIX);
		}

		// Internal dependencies
		Element compatVersion = (Element) findNode(doc,
				"/project/properties/compat-version");
		if (compatVersion != null) {
			compatVersion.setTextContent(Version.getVersion());
		}

		updateFile(f, doc);
	}

}
