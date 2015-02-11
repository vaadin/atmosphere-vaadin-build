package com.vaadin.external.atmosphere.build;

import com.vaadin.external.atmosphere.build.filefilter.FileFilter;
import com.vaadin.external.atmosphere.build.filefilter.JavascriptVersionUpdater;
import com.vaadin.external.atmosphere.build.filefilter.SLF4JPackageReferenceUpdater;
import com.vaadin.external.atmosphere.build.xmlfilefilter.AtmosphereDependencyUpdater;
import com.vaadin.external.atmosphere.build.xmlfilefilter.DistributionManagementFilter;
import com.vaadin.external.atmosphere.build.xmlfilefilter.GPGReleaseKeyReader;
import com.vaadin.external.atmosphere.build.xmlfilefilter.OrgAtmosphereLogger;
import com.vaadin.external.atmosphere.build.xmlfilefilter.ProjectGroupIdFilter;
import com.vaadin.external.atmosphere.build.xmlfilefilter.SLF4JDependencyUpdater;
import com.vaadin.external.atmosphere.build.xmlfilefilter.SLF4JRemovalValidator;
import com.vaadin.external.atmosphere.build.xmlfilefilter.XMLFileFilter;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VaadinAtmospherePreprocessor {
    public static void main(String[] args) throws Exception {
        for (String dir : args) {
            new VaadinAtmospherePreprocessor().preprocess(new File(dir));
        }
    }

    private void preprocess(File projectDir) throws Exception {
        List<FileFilter> fileFilters = new ArrayList<FileFilter>();
        List<XMLFileFilter> xmlFilters = new ArrayList<XMLFileFilter>();
        List<XMLFileFilter> validationFilters = new ArrayList<XMLFileFilter>();

        xmlFilters.add(new ProjectGroupIdFilter(projectDir));
        xmlFilters.add(new AtmosphereDependencyUpdater(projectDir));
        xmlFilters.add(new DistributionManagementFilter(projectDir));
        xmlFilters.add(new SLF4JDependencyUpdater(projectDir));
        xmlFilters.add(new GPGReleaseKeyReader(projectDir));

        validationFilters.add(new SLF4JRemovalValidator(projectDir));
        validationFilters.add(new OrgAtmosphereLogger(projectDir));

        fileFilters.add(new SLF4JPackageReferenceUpdater(projectDir));
        fileFilters.add(new JavascriptVersionUpdater(projectDir));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        Collection<File> files = listRecursively(projectDir);
        for (File f : files) {
            for (FileFilter filter : fileFilters) {
                if (filter.needsProcessing(f)) {
                    filter.process(f);
                }
            }
            for (XMLFileFilter filter : xmlFilters) {
                Document doc = null;
                if (filter.needsProcessing(f)) {
                    if (doc == null) {
                        DocumentBuilder dBuilder = dbFactory
                            .newDocumentBuilder();
                        doc = dBuilder.parse(f);

                    }
                    filter.process(f, doc);
                }
            }
            for (XMLFileFilter filter : validationFilters) {
                Document doc = null;
                if (filter.needsProcessing(f)) {
                    if (doc == null) {
                        DocumentBuilder dBuilder = dbFactory
                            .newDocumentBuilder();
                        doc = dBuilder.parse(f);

                    }
                    filter.process(f, doc);
                }
            }

        }
    }

    private Collection<File> listRecursively(File dir)
        throws FileNotFoundException {
        if (!dir.exists()) {
            throw new FileNotFoundException("Directory " + dir.getPath()
                + " does not exist");
        }
        Collection<File> found = new ArrayList<File>();
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                found.addAll(listRecursively(f));
            } else {
                found.add(f);
            }
        }
        return found;
    }

}
