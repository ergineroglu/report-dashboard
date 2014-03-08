<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/json.tld" prefix="json"%>
<%@ tag import="com.intellica.evam.report.util.PortletType" %>

<%@ attribute name="userId" %>
<%@ attribute name="dashboardTab" type="com.intellica.evam.report.model.DashboardTab" %>
<%@ attribute name="dashboardPortlet" type="java.util.Map.Entry" %>
<%@ attribute name="portletIcon" %>
<%@ attribute name="numberOfCols" %>

<c:url var="dashboardPortletUrl" value="/data/${userId}/${dashboardTab.tabKey}/${dashboardPortlet.key}" />
<div class="col-md-${numberOfCols}" style="margin-bottom: 15px;">
	<div class="panel panel-primary">	
		<div class="panel-heading">
			<h3 class="panel-title">
				<span class="glyphicon ${portletIcon}"></span> ${dashboardPortlet.value.portletTitle}
				<span title="Redraw Graph" class="refresh-graph pull-right btn btn-xs glyphicon glyphicon-refresh"></span>
			</h3>
		</div>
		<div class="panel-body">
			<c:choose>
				<c:when test="${dashboardPortlet.value.portletType == 'TEXT_BOX' ||
								dashboardPortlet.value.portletType == 'SELECT_BOX'}" >
					<div id="evam_drawing_${dashboardPortlet.key}" 
	 			 		 class="evam_graph ${dashboardPortlet.value.portletType.name}"
	 			 		 data-title="${dashboardPortlet.value.portletTitle}"
						 data-key="${dashboardPortlet.key}"
						 data-type="${dashboardPortlet.value.portletType.name}"
						 data-refresh-interval="${dashboardPortlet.value.refreshInterval}"
						 data-auto-start="${dashboardPortlet.value.autoStart}"
					 	 data-request-url="${dashboardPortletUrl}"
					 	 data-interactions='${json:toJson(dashboardPortlet.value.interactions)}' >
				 		
				 		<c:if test="${dashboardPortlet.value.portletType == 'TEXT_BOX' }">
					 		<div class="input-group">
								<input type="text" class="form-control">
								<span class="input-group-btn">
									<button class="btn btn-primary update" type="button">Update</button>
								</span>
							</div>	 
					 	</c:if>
					 	
					 	<c:if test="${dashboardPortlet.value.portletType == 'SELECT_BOX' }">
					 		<div class="input-group">
								<select class="form-control">
									<option value="">Please choose...</option>
								</select>
								<span class="input-group-btn">
									<button class="btn btn-primary update" type="button">Update</button>
								</span>
							</div>	 
					 	</c:if>
 			 		</div>
				</c:when>
				
				<c:when test="${dashboardPortlet.value.portletType == 'LINE_CHART' ||
								dashboardPortlet.value.portletType == 'MULTISERIES_LINE_CHART' ||
								dashboardPortlet.value.portletType == 'AREA_CHART' }" >
					
					<div id="evam_drawing_${dashboardPortlet.key}" 
						 class="evam_graph ${dashboardPortlet.value.portletType.name}"
						 data-title="${dashboardPortlet.value.portletTitle}"
						 data-key="${dashboardPortlet.key}"
						 data-type="${dashboardPortlet.value.portletType.name}"
						 data-refresh-interval="${dashboardPortlet.value.refreshInterval}"
						 data-auto-start="${dashboardPortlet.value.autoStart}"
						 data-request-url="${dashboardPortletUrl}"
						 data-x-axis="${dashboardPortlet.value.axisXName}"
						 data-y-axis="${dashboardPortlet.value.axisYName}"
						 data-x-axis-type="${dashboardPortlet.value.axisXType}"
						 data-y-axis-type="${dashboardPortlet.value.axisYType}"
						 data-x-axis-format="${dashboardPortlet.value.axisXFormat}"
						 data-y-axis-format="${dashboardPortlet.value.axisYFormat}"
						 data-brush="${dashboardPortlet.value.brush}"
						 data-interpolate="${dashboardPortlet.value.interpolationMethod}">
					</div>
				</c:when>
				
				<c:when test="${dashboardPortlet.value.portletType == 'PIE_CHART' ||
								dashboardPortlet.value.portletType == 'DONUT_CHART' }" >
					
					<div id="evam_drawing_${dashboardPortlet.key}" 
	 			 		 class="evam_graph ${dashboardPortlet.value.portletType.name}"
	 			 		 data-title="${dashboardPortlet.value.portletTitle}"
						 data-key="${dashboardPortlet.key}"
						 data-type="${dashboardPortlet.value.portletType.name}"
						 data-refresh-interval="${dashboardPortlet.value.refreshInterval}"
						 data-auto-start="${dashboardPortlet.value.autoStart}"
					 	 data-request-url="${dashboardPortletUrl}">
				 	</div>
			 	</c:when>
			 	
			 	<c:when test="${dashboardPortlet.value.portletType == 'DATA_TABLE' }" >
			 		
			 		<div id="evam_drawing_${dashboardPortlet.key}" 
	 			 		 class="evam_graph ${dashboardPortlet.value.portletType.name}"
	 			 		 data-title="${dashboardPortlet.value.portletTitle}"
						 data-key="${dashboardPortlet.key}"
						 data-type="${dashboardPortlet.value.portletType.name}"
						 data-refresh-interval="${dashboardPortlet.value.refreshInterval}"
						 data-auto-start="${dashboardPortlet.value.autoStart}"
					 	 data-request-url="${dashboardPortletUrl}">
				 		<div class="table-responsive">
				 			<table class="table table-striped">
								<thead>
									<tr>
								  	<c:forEach items="${dashboardPortlet.value.columnNames}" var="column">
								  		<th><c:out value="${column}"/></th>
								  	</c:forEach>
								  	</tr>
							  	</thead>
							  	<tbody></tbody>								  	
							</table>
				 		</div>
			 		</div>
			 	</c:when>
			 	
			</c:choose>
		</div>
	</div>
</div>

