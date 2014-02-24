package com.intellica.evam.report.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Author: eeroglu
 * Date: 20 Şub 2014 14:20:09
 * Package: com.intellica.evam.report.util
 *
 */
public class ParserUtils {
	// read tag value functions
	static public String readSingleTagValueString(Element element, String tagName) {
		return ParserUtils.readSingleTagValueString(element, tagName, null);
	}
	
	static public String readSingleTagValueString(Element element, String tagName, String defaultValue) {
		NodeList tagNodes = element.getElementsByTagName(tagName);
		return tagNodes.getLength() > 0 ? tagNodes.item(0).getTextContent() : defaultValue;
	}
	
	static public Integer readSingleTagValueInteger(Element element, String tagName) {
		return ParserUtils.readSingleTagValueInteger(element, tagName, null);
	}
	
	static public Integer readSingleTagValueInteger(Element element, String tagName, Integer defaultValue) {
		NodeList tagNodes = element.getElementsByTagName(tagName);
		return tagNodes.getLength() > 0 ? Integer.valueOf(tagNodes.item(0).getTextContent()) : defaultValue;
	}
	
	static public Double readSingleTagValueDouble(Element element, String tagName) {
		return ParserUtils.readSingleTagValueDouble(element, tagName, null);
	}
	
	static public Double readSingleTagValueDouble(Element element, String tagName, Double defaultValue) {
		NodeList tagNodes = element.getElementsByTagName(tagName);
		return tagNodes.getLength() > 0 ? Double.valueOf(tagNodes.item(0).getTextContent()) : defaultValue;
	}
	
	static public Boolean readSingleTagValueBoolean(Element element, String tagName) {
		return ParserUtils.readSingleTagValueBoolean(element, tagName, null);
	}
	
	static public Boolean readSingleTagValueBoolean(Element element, String tagName, Boolean defaultValue) {
		NodeList tagNodes = element.getElementsByTagName(tagName);
		return tagNodes.getLength() > 0 ? Boolean.valueOf(tagNodes.item(0).getTextContent()) : defaultValue;
	}
	
	// list readers
	static public List<String> readMultipleTagValueString(Element element, String tagName) {
		NodeList tagNodes = element.getElementsByTagName(tagName);
		List<String> resultList = new ArrayList<String>();
		for(int i = 0; i < tagNodes.getLength(); i++) {
			resultList.add(tagNodes.item(i).getTextContent());
		}
		return resultList;
	}
}
