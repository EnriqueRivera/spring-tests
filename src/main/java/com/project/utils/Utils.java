package com.project.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

public class Utils {
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	public static String normalizeId(final String orderId) {
		return orderId == null ? "" : orderId.replaceAll("\\D+","");
	}	
	
	// convert InputStream to String
	public static String getStringFromInputStream(final InputStream is) throws IOException {

		BufferedReader br = null;
		final StringBuilder sb = new StringBuilder();
		String line;
		
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (final IOException e) {
					logger.error("Error closing resources: " + e.getMessage());
				}
			}
		}

		return sb.toString();
	}
	
	public static String getTextFromTemplateFile(final String templateFilePath) throws IOException {
    	String templateText = null;
    	InputStream resourceStream = null;
    	BufferedReader br = null;
    	String line;
    	final StringBuilder sb = new StringBuilder();
    	
    	try {
    		resourceStream = Thread.currentThread().getContextClassLoader().
    				getResourceAsStream(templateFilePath);
    		
    		br = new BufferedReader(new InputStreamReader(resourceStream));
    		
    		while ( null != (line = br.readLine())) {
    			sb.append(line);
    		}
    		
    		if (!sb.toString().isEmpty()) {
    			templateText = sb.toString();
    		}
    		
    	} catch (final IOException e) {
			throw e;
		} catch (final Exception e) {
			logger.error("Error getting template: " + e.getMessage());
		}
    	finally {
			try {
				if (resourceStream != null) {
					resourceStream.close();
				}
				
				if (br != null) {
					br.close();
				}
				
			} catch (final IOException e) {
				logger.error("Error closing resources: " + e.getMessage());
			}
		}
    	
    	return templateText;
    }
	
	public static String formatXml(final String xml) {
        try {
            final InputSource src = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));

            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.

            return writer.writeToString(document);
        } catch (final Exception e) {
        	logger.error("Error formatting XML: " + e.getMessage());
        	return xml;
        }
    }
}
