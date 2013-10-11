package com.vaadin.external.atmosphere.build.xmlfilefilter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.xpath.XPathException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SLF4JDependencyUpdater extends PomXmlFilter {

	private static final String VAADIN_SLF4J_GROUPID = "com.vaadin.external.slf4j";
	private static Set<String> slf4jArtifactsToRemove = new HashSet<String>();
	static {
		slf4jArtifactsToRemove.add("jul-to-slf4j");
		slf4jArtifactsToRemove.add("jcl-over-slf4j");
		slf4jArtifactsToRemove.add("log4j-over-slf4j");
	}

	public SLF4JDependencyUpdater(File root) {
		super(root);
	}

	@Override
	public void process(File f, Document doc) throws Exception {
		System.out.println(f);
		NodeList dependencyNodes = findNodes(doc,
				"/project/dependencies/dependency");
		for (int i = 0; i < dependencyNodes.getLength(); i++) {
			Element dependency = (Element) dependencyNodes.item(i);
			Element groupId = (Element) findNode(dependency, "groupId");
			String groupIdText = groupId.getTextContent();

			if (groupIdText.equals("org.slf4j")) {
				addDependencyIfNotPresent(doc, VAADIN_SLF4J_GROUPID,
						"vaadin-slf4j-jdk14", "${slf4j-impl-version}");

				dependency.getParentNode().removeChild(dependency);
			}
		}
		updateFile(f, doc);

	}

	private void addDependencyIfNotPresent(Document doc, String groupid,
			String artifactId, String version) throws XPathException {
		Element e = (Element) findNode(doc,
				"/project/dependencies/dependency/artifactId[text()='"
						+ artifactId + "']/../groupId[text()='" + groupid
						+ "']");
		if (e != null) {
			return;
		}

		addDependency(doc, groupid, artifactId, version);
	}

	private void addDependency(Document doc, String groupid, String artifactId,
			String version) throws XPathException {
		Element dep = doc.createElement("dependency");
		Element group = doc.createElement("groupId");
		Element art = doc.createElement("artifactId");
		Element vers = doc.createElement("version");

		group.setTextContent(groupid);
		art.setTextContent(artifactId);
		vers.setTextContent(version);

		dep.appendChild(group);
		dep.appendChild(art);
		dep.appendChild(vers);
		Element dependenciesNode = (Element) findNode(doc,
				"/project/dependencies");
		dependenciesNode.appendChild(dep);
	}
}
