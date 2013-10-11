package com.vaadin.external.atmosphere.build.xmlfilefilter;
import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Updates distribution management with vaadin-snapshot and staging repositories
 * 
 */
public class DistributionManagementFilter extends PomXmlFilter {

	public DistributionManagementFilter(File root) {
		super(root);
	}

	private static final String VAADIN_SNAPSHOTS_ID = "vaadin-snapshots";
	private static final String VAADIN_SNAPSHOTS_NAME = "Vaadin snapshots";
	private static final String VAADIN_SNAPSHOTS_URL = "http://oss.sonatype.org/content/repositories/vaadin-snapshots/";

	private static final String VAADIN_STAGING_ID = "vaadin-staging";
	private static final String VAADIN_STAGING_NAME = "Vaadin staging";
	private static final String VAADIN_STAGING_URL = "https://oss.sonatype.org/service/local/staging/deploy/maven2/";

	@Override
	public boolean needsProcessing(File f) {
		return super.needsProcessing(f) && f.getParentFile().equals(getRoot());
	}

	@Override
	public void process(File f, Document d) throws Exception {
		Element distributionManagement = (Element) findNode(d,
				"/project/distributionManagement");
		if (distributionManagement == null) {
			// Create distributionManagement if not found
			Element project = (Element) findNode(d, "/project");
			distributionManagement = d.createElement("distributionManagement");
			project.appendChild(distributionManagement);
		} else {
			// Remove all children
			removeChildren(distributionManagement);
		}

		// Add Vaadin repositories

		Element snapshotRepository = createRespository(d, "snapshotRepository",
				VAADIN_SNAPSHOTS_ID, VAADIN_SNAPSHOTS_NAME,
				VAADIN_SNAPSHOTS_URL);
		Element stagingRepository = createRespository(d, "repository",
				VAADIN_STAGING_ID, VAADIN_STAGING_NAME, VAADIN_STAGING_URL);

		distributionManagement.appendChild(snapshotRepository);
		distributionManagement.appendChild(stagingRepository);

		updateFile(f, d);
	}

	private Element createRespository(Document d, String tagName,
			String idText, String nameText, String urlText) {
		Element repository = d.createElement(tagName);
		Element id = d.createElement("id");
		Element name = d.createElement("name");
		Element url = d.createElement("url");

		id.setTextContent(idText);
		name.setTextContent(nameText);
		url.setTextContent(urlText);

		repository.appendChild(id);
		repository.appendChild(name);
		repository.appendChild(url);
		return repository;

	}

	protected void removeChildren(Element parent) {
		NodeList children = parent.getChildNodes();
		while (children.getLength() > 0) {
			parent.removeChild(children.item(0));
		}
	}
}
