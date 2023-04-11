package fr.real.supervision.appliinfo.connector.itm.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtils.class);

	public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		return documentBuilderFactory.newDocumentBuilder();

	}

	public static Document createDocument(String s) {
		Document doc = null;
		try {
			DocumentBuilder db = getDocumentBuilder();
			InputSource is = new InputSource(new StringReader(s));
			doc = db.parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return doc;
	}

	public static List<Element> getChildElements(Element element, String tagName) {
		Node node = null;
		List<Element> elementList = new ArrayList();
		NodeList nodeList = element.getChildNodes();
		Element childElm = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && tagName.equals(node.getNodeName())) {
				childElm = (Element) node;
				elementList.add(childElm);
			}
		}

		return elementList;
	}

	public static String getFirstLevelTextContent(Node node) {
		NodeList list = node.getChildNodes();
		StringBuilder textContent = new StringBuilder();
		textContent.append("");
		for (int i = 0; i < list.getLength(); ++i) {
			Node child = list.item(i);
			if (child.getNodeType() == Node.TEXT_NODE)
				textContent.append(child.getTextContent());
		}
		return textContent.toString().trim();
	}

}
