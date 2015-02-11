package com.vaadin.external.atmosphere.build.xmlfilefilter;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public abstract class PomXmlFilter implements XMLFileFilter {
    protected XPathFactory xPathfactory = XPathFactory.newInstance();
    private File root;

    public PomXmlFilter(File root) {
        this.root = root;
    }

    protected File getRoot() {
        return root;
    }

    @Override
    public boolean needsProcessing(File f) {
        return f.getName().equals("pom.xml");
    }

    protected NodeList findNodes(Object root, String xpathExpression)
        throws XPathException {
        return findType(root, xpathExpression, XPathConstants.NODESET);
    }

    protected NodeList findNode(Object root, String xpathExpression)
        throws XPathException {
        return findType(root, xpathExpression, XPathConstants.NODE);
    }

    private NodeList findType(Object root, String xpathExpression, QName node)
        throws XPathException {
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile(xpathExpression);
        return (NodeList) expr.evaluate(root, node);
    }

    protected void updateFile(File f, Document d) throws Exception {
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory
            .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(d);
        StreamResult result = new StreamResult(f);
        transformer.transform(source, result);
    }

}
