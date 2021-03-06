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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.intellica.evam.report.data.source.ConstantDataSource;
import com.intellica.evam.report.data.source.DataSource;
import com.intellica.evam.report.data.source.DatabaseDataSource;
import com.intellica.evam.report.model.DashboardAreaChartPortlet;
import com.intellica.evam.report.model.DashboardBarChartPortlet;
import com.intellica.evam.report.model.DashboardDataTablePortlet;
import com.intellica.evam.report.model.DashboardDonutChartPortlet;
import com.intellica.evam.report.model.DashboardGroupBarChartPortlet;
import com.intellica.evam.report.model.DashboardInputPortlet;
import com.intellica.evam.report.model.DashboardInteraction;
import com.intellica.evam.report.model.DashboardLineChartPortlet;
import com.intellica.evam.report.model.DashboardMultiseriesLineChartPortlet;
import com.intellica.evam.report.model.DashboardPieChartPortlet;
import com.intellica.evam.report.model.DashboardPortlet;
import com.intellica.evam.report.model.DashboardSelectBoxPortlet;
import com.intellica.evam.report.model.DashboardTab;
import com.intellica.evam.report.model.DashboardTextboxPortlet;
import com.intellica.evam.report.model.DashboardXYChartPortlet;
import com.intellica.evam.report.service.PortletCacheService;
import com.intellica.evam.report.util.DataSourceType;
import com.intellica.evam.report.util.ParserUtils;
import com.intellica.evam.report.util.PortletType;

@Controller
public class PanelController {
	
	private static final Logger logger = LoggerFactory.getLogger(PanelController.class);
	private static final String DASHBOARD_FILE_PATTERN = "user%d/dashboard.xml";
	
	@Autowired
	PortletCacheService portletCacheService;
	
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
					dashboardTabs.add(this.parseTab(userId, tabElement));
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
	
	private DashboardTab parseTab(int userId, Element tabElement) {
		String tabKey = tabElement.getElementsByTagName("key").item(0).getTextContent();
		String tabTitle = tabElement.getElementsByTagName("title").item(0).getTextContent();
		DashboardTab newTab = new DashboardTab(tabKey, tabTitle);

		// parse portlets of tab
		NodeList portletList = tabElement.getElementsByTagName("portlet");
		for(int i = 0; i < portletList.getLength(); i++) {
			Node portletNode = portletList.item(i);
			if(portletNode.getNodeType() == Node.ELEMENT_NODE) {
				Element portletElement = (Element) portletNode;
				DashboardPortlet portlet = this.parsePortlet(portletElement);
				if(portlet != null) { 
					newTab.addPortlet(portlet.getPortletKey(), portlet);
					portletCacheService.setPortlet(userId, tabKey, portlet.getPortletKey(), portlet);
				}
			}
			else {
				logger.error("Unexpected portlet node in xml, cannot convert to element node");
			}
		}
		
		return newTab;
	}
	
	private DashboardPortlet parsePortlet(Element portletElement) {
		// must fields
		String portletKey = ParserUtils.readSingleTagValueString(portletElement, "key");
		String portletTitle = ParserUtils.readSingleTagValueString(portletElement, "title");
		PortletType portletType = PortletType.fromName(ParserUtils.readSingleTagValueString(portletElement, "type"));
		// optional fields
		Integer portletWidth = ParserUtils.readSingleTagValueInteger(portletElement, "width", 50);		
		Integer refreshInterval = ParserUtils.readSingleTagValueInteger(portletElement, "refreshInterval", -1);
		Boolean autoStart = ParserUtils.readSingleTagValueBoolean(portletElement, "autoStart", true);
		
		// data source
		Element dataSourceElement = (Element) portletElement.getElementsByTagName("data-source").item(0);
		DataSource dataSource = this.parseDataSource(dataSourceElement);
		
		// type specific fields and portlet creation
		DashboardPortlet portlet = this.portletFactory(portletType, dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		switch(portletType) {
		case LINE_CHART:
		case MULTISERIES_LINE_CHART:
		case AREA_CHART:
		case BAR_CHART:
		case GROUP_BAR_CHART:
			DashboardXYChartPortlet xyChart = (DashboardXYChartPortlet) portlet;
			// optional fields			
			xyChart.setAxisXName(ParserUtils.readSingleTagValueString(portletElement, "x-axis"));
			xyChart.setAxisYName(ParserUtils.readSingleTagValueString(portletElement, "y-axis"));
			xyChart.setAxisXType(ParserUtils.readSingleTagValueString(portletElement, "x-value-type", "string"));
			xyChart.setAxisYType(ParserUtils.readSingleTagValueString(portletElement, "y-value-type", "number"));
			xyChart.setAxisXFormat(ParserUtils.readSingleTagValueString(portletElement, "x-value-format"));
			xyChart.setAxisYFormat(ParserUtils.readSingleTagValueString(portletElement, "y-value-format"));			
			xyChart.setBrush(ParserUtils.readSingleTagValueBoolean(portletElement, "brush"));
			if(portletType == PortletType.LINE_CHART || 
			   portletType == PortletType.MULTISERIES_LINE_CHART || 
			   portletType == PortletType.AREA_CHART) {
				((DashboardLineChartPortlet) xyChart).setInterpolationMethod(ParserUtils.readSingleTagValueString(portletElement, "interpolationMethod"));
			}
			break;	
		case DATA_TABLE:
			DashboardDataTablePortlet tablePortlet = (DashboardDataTablePortlet) portlet;
			// optional fields	
			NodeList columnNamesTags = portletElement.getElementsByTagName("column-names");
			if(columnNamesTags.getLength() > 0) {
				tablePortlet.setColumnNames(ParserUtils.readMultipleTagValueString((Element) columnNamesTags.item(0), "column").toArray(new String[0]));
			}
			break;
		case TEXT_BOX:
		case SELECT_BOX:
			DashboardInputPortlet inputPortlet = (DashboardInputPortlet) portlet;
			// optional fields			
			NodeList interactionsTags = portletElement.getElementsByTagName("interactions");
			if(interactionsTags.getLength() > 0) {
				List<DashboardInteraction> interactionList = this.parseInteractions((Element) interactionsTags.item(0));
				inputPortlet.setInteractions(interactionList);
			}
			break;
		default:
			break;
		}
		
		return portlet;
	}
	
	private DashboardPortlet portletFactory(PortletType portletType, 
											DataSource dataSource,
											String portletKey, 
											String portletTitle, 
											Integer portletWidth,
											Integer refreshInterval,
											Boolean autoStart)
	{
		switch (portletType) {
		case TEXT_BOX: return new DashboardTextboxPortlet(dataSource, portletKey,portletTitle, portletWidth, refreshInterval, autoStart);
		case SELECT_BOX: return new DashboardSelectBoxPortlet(dataSource, portletKey,portletTitle, portletWidth, refreshInterval, autoStart);
		case DATA_TABLE: return new DashboardDataTablePortlet(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		case LINE_CHART: return new DashboardLineChartPortlet(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		case MULTISERIES_LINE_CHART: return new DashboardMultiseriesLineChartPortlet(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		case AREA_CHART: return new DashboardAreaChartPortlet(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		case BAR_CHART: return new DashboardBarChartPortlet(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		case GROUP_BAR_CHART: return new DashboardGroupBarChartPortlet(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		case PIE_CHART: return new DashboardPieChartPortlet(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		case DONUT_CHART: return new DashboardDonutChartPortlet(dataSource, portletKey, portletTitle, portletWidth, refreshInterval, autoStart);
		default: return null;
		}
	}

	private DataSource parseDataSource(Element dataSourceElement) {
		// must fields
		DataSourceType dataSourceType = DataSourceType.fromName(ParserUtils.readSingleTagValueString(dataSourceElement, "type"));
		switch (dataSourceType) {
		case DATABASE:
			// specific fields
			String queryTemplate = ParserUtils.readSingleTagValueString(dataSourceElement, "query");
			return new DatabaseDataSource(queryTemplate);
		case CONSTANT:
			// specific fields
			Element valuesElement = (Element) dataSourceElement.getElementsByTagName("values").item(0);
			String delimiter = ParserUtils.readSingleTagValueString(valuesElement, "delimiter", ",");
			List<String> values = ParserUtils.readMultipleTagValueString(valuesElement, "value");
			return new ConstantDataSource(delimiter, values);
		default:
			return null;
		}
	}
	
	private List<DashboardInteraction> parseInteractions(Element interactionsElement) {
		List<DashboardInteraction> interactions = new ArrayList<DashboardInteraction>();
		NodeList interactionTags = interactionsElement.getElementsByTagName("interaction");
		for(int i = 0; i < interactionTags.getLength(); i++) {
			// interaction fields
			Element interactionElement = (Element) interactionTags.item(i);
			String tabKey = ParserUtils.readSingleTagValueString(interactionElement, "tab-key");
			String portletKey = ParserUtils.readSingleTagValueString(interactionElement, "portlet-key");
			String variable = ParserUtils.readSingleTagValueString(interactionElement, "variable");
			Boolean trigger = ParserUtils.readSingleTagValueBoolean(interactionElement, "trigger", true);
			interactions.add(new DashboardInteraction(tabKey, portletKey, variable, trigger));
		}
		
		return interactions;
	}
}