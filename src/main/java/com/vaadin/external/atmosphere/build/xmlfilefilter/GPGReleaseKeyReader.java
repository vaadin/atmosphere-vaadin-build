package com.vaadin.external.atmosphere.build.xmlfilefilter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

public class GPGReleaseKeyReader extends RootPomXmlFilter {

    public GPGReleaseKeyReader(File root) {
        super(root);
    }

    @Override
    public void process(File f, Document doc) throws Exception {
        Element profiles = (Element) findNode(doc, "/project/profiles");
        if (profiles == null) {
            profiles = doc.createElement("profiles");
            ((Element) findNode(doc, "/project")).appendChild(profiles);
        }
        Element profileNode = (Element) findNode(profiles,
            "profile/id[text()='release-sign-artifacts']/..");
        boolean newProfile = false;
        if (profileNode == null) {
            newProfile = true;
            profileNode = createReleaseProfile(doc, profiles);
        }

        Element pluginsNode = (Element) findNode(profileNode, "build/plugins");
        Element plugin = createPlugin(doc, pluginsNode, "org.codehaus.mojo",
            "properties-maven-plugin", "1.0-alpha-2", "initialize",
            "read-project-properties");

        Element execution = (Element) findNode(plugin, "executions/execution");
        Element configuration = doc.createElement("configuration");
        Element files = doc.createElement("files");
        Element file = doc.createElement("file");
        file.setTextContent("${gpg.passphrase.file}");
        configuration.appendChild(files);
        files.appendChild(file);
        execution.appendChild(configuration);

        pluginsNode.appendChild(plugin);
        if (newProfile) {
            Element gpgPlugin = createPlugin(doc, pluginsNode,
                "org.apache.maven.plugins", "maven-gpg-plugin", "1.1",
                "verify", "sign");
            pluginsNode.appendChild(gpgPlugin);
        }
        updateFile(f, doc);
    }

    private Element createPlugin(Document doc, Element pluginsNode,
        String groupIdText, String artifactIdText, String versionText,
        String phaseText, String goalText) {
        Element plugin = doc.createElement("plugin");
        Element groupId = doc.createElement("groupId");
        Element version = doc.createElement("version");
        Element artifactId = doc.createElement("artifactId");

        Element executions = doc.createElement("executions");
        Element execution = doc.createElement("execution");
        Element phase = doc.createElement("phase");
        Element goals = doc.createElement("goals");
        Element goal = doc.createElement("goal");

        groupId.setTextContent(groupIdText);
        artifactId.setTextContent(artifactIdText);
        version.setTextContent(versionText);

        phase.setTextContent(phaseText);
        goal.setTextContent(goalText);

        goals.appendChild(goal);

        executions.appendChild(execution);
        execution.appendChild(phase);
        execution.appendChild(goals);

        plugin.appendChild(groupId);
        plugin.appendChild(artifactId);
        plugin.appendChild(version);
        plugin.appendChild(executions);

        return plugin;
    }

    private Element createReleaseProfile(Document doc, Element profiles) {
        Element profileNode = doc.createElement("profile");
        Element profileId = doc.createElement("id");
        profileId.setTextContent("release-sign-artifacts");
        profileNode.appendChild(profileId);
        profiles.appendChild(profileNode);
        Element buildElement = doc.createElement("build");
        profileNode.appendChild(buildElement);
        buildElement.appendChild(doc.createElement("plugins"));
        return profileNode;
    }
}
