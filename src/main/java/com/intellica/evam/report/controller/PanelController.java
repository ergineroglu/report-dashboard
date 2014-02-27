package com.intellica.evam.report.controller;

/**
 * Author: eeroglu
 * Date: 11 Şub 2014 12:43:48
 * Package: com.intellica.evam.controller
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.intellica.evam.report.model.DashboardAreaChartBrushPortlet;
import com.intellica.evam.report.model.DashboardAreaChartPortlet;
import com.intellica.evam.report.model.DashboardDonutChartPortlet;
import com.intellica.evam.report.model.DashboardPieChartPortlet;
import com.intellica.evam.report.model.DashboardPortlet;
import com.intellica.evam.report.model.DashboardTab;
import com.intellica.evam.report.model.DashboardTextboxPortlet;
import com.intellica.evam.report.util.PortletType;

@Controller
public class PanelController {
	
	private static final Logger logger = LoggerFactory.getLogger(PanelController.class);
	private static final String DASHBOARD_FILE_PATTERN = "user%d/dashboard.xml";
	
	@RequestMapping(value = "/old", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		return "oldindex";
	}
	
	@RequestMapping(value = "/dashboard/{userId}", method = RequestMethod.GET)
	public String dashboard(Model model, @PathVariable int userId) {
		List<DashboardTab> dashboardTabs = new ArrayList<DashboardTab>();
		
		// read user dashboard xml		
		try {
			// open file for parsing
			ClassPathResource cpr = new ClassPathResource(String.format(DASHBOARD_FILE_PATTERN, userId));
			File xmlFile = cpr.getFile();		
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			
			// read tabs
			NodeList tabList = doc.getElementsByTagName("tab");
			for(int i = 0; i < tabList.getLength(); i++) {
				Node tabNode = tabList.item(i);
				if(tabNode.getNodeType() == Node.ELEMENT_NODE) {
					Element tabElement = (Element) tabNode;
					dashboardTabs.add(this.parseTab(tabElement));
				}
				else {
					logger.error("Unexpected tab node in xml, cannot convert to element node");
				}
			}
		} 
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// model
		model.addAttribute("userId", userId);
		model.addAttribute("dashboardTabs", dashboardTabs);
		
		return "dashboard";
	}
	
	private DashboardTab parseTab(Element tabElement) {
		DashboardTab newTab = new DashboardTab(tabElement.getElementsByTagName("key").item(0).getTextContent(),
				   							   tabElement.getElementsByTagName("title").item(0).getTextContent());

		// parse portlets of tab
		NodeList portletList = tabElement.getElementsByTagName("portlet");
		for(int i = 0; i < portletList.getLength(); i++) {
			Node portletNode = portletList.item(i);
			if(portletNode.getNodeType() == Node.ELEMENT_NODE) {
				Element portletElement = (Element) portletNode;
				DashboardPortlet portlet = this.parsePortlet(portletElement);
				if(portlet != null) newTab.addPortlet(portlet.getPortletKey(), portlet);
			}
			else {
				logger.error("Unexpected portlet node in xml, cannot convert to element node");
			}
		}
		
		return newTab;
	}
	
	private DashboardPortlet parsePortlet(Element portletElement) {
		// must fields
		String portletKey = portletElement.getElementsByTagName("key").item(0).getTextContent();
		String portletTitle = portletElement.getElementsByTagName("title").item(0).getTextContent();
		PortletType portletType = PortletType.fromName(portletElement.getElementsByTagName("type").item(0).getTextContent());
		// optional fields
		NodeList portletWidthNodes = portletElement.getElementsByTagName("width");
		Integer portletWidth = Integer.valueOf(portletWidthNodes.getLength() > 0 ? portletWidthNodes.item(0).getTextContent() : "50");
		NodeList portletRefreshNodes = portletElement.getElementsByTagName("refreshInterval");
		Integer refreshInterval = (portletRefreshNodes.getLength() > 0 ? Integer.valueOf(portletRefreshNodes.item(0).getTextContent()) : null);
		
		// type specific fields and portlet creation
		switch(portletType) {
		case TEXT_BOX: 
			return new DashboardTextboxPortlet(portletKey, portletTitle, portletWidth, refreshInterval);
		case AREA_CHART:
			DashboardAreaChartPortlet areaChart = new DashboardAreaChartPortlet(portletKey, portletTitle, portletWidth, refreshInterval);
			areaChart.setAxisXName("X Axis");
			areaChart.setAxisYName("Y Axis");
			return areaChart;
		case AREA_CHART_BRUSH:
			DashboardAreaChartBrushPortlet areaChartBrush = new DashboardAreaChartBrushPortlet(portletKey, portletTitle, portletWidth, refreshInterval);
			areaChartBrush.setAxisXName("Zaman");
			areaChartBrush.setAxisYName("Y Axis");
			return areaChartBrush;
		case PIE_CHART:
			DashboardPieChartPortlet pieChart = new DashboardPieChartPortlet(portletKey, portletTitle, portletWidth, refreshInterval);
			return pieChart;
		case DONUT_CHART:
			DashboardDonutChartPortlet donutChart = new DashboardDonutChartPortlet(portletKey, portletTitle, portletWidth, refreshInterval);
			return donutChart;
		default:
			return null;
		}
	}
}
