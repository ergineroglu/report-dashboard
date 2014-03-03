<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/portlet.tld" prefix="portlet"%>

<!DOCTYPE html>
<!--[if lt IE 9 ]> <html class="ie-old"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!--><html><!--<![endif]-->

<head>

	<!-- Metadata -->
	<title>Evam Report Manager</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="description" content="Intellica - Evam Report Manager">
	<meta name="author" content="Ergin EROĞLU">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<!-- css -->
	<link href="<c:url value="/static/images/favicon.ico" />" rel="shortcut icon">
	<link href="<c:url value="/static/css/bootstrap.css" />" rel="stylesheet" media="screen">
	<link href="<c:url value="/static/css/bootstrap-multiselect.css" />" rel="stylesheet" media="screen">
	<link href="<c:url value="/static/css/datepicker.css" />" rel="stylesheet" media="screen">
	<link href="<c:url value="/static/css/slider.css" />" rel="stylesheet" media="screen">
	<link href="<c:url value="/static/css/style.css" />" rel="stylesheet" media="screen">
	<link href="<c:url value="/static/css/evam.drawing.css" />" rel="stylesheet" media="screen">
</head>

<body>

	<div id="wrapper">
		
		<!-- Sidebar -->
      	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        	<!-- Brand and toggle get grouped for better mobile display -->
        	<div class="navbar-header">          		
          		<a class="navbar-brand" href="<c:url value="/dashboard/${userId}" />">EVAM Report Manager</a>
        	</div>
        	
        	<!-- Collect the nav links, forms, and other content for toggling
        	<div class="collapse navbar-collapse navbar-ex1-collapse">
         		<ul class="nav navbar-nav navbar-right navbar-user">            		
           			<li class="dropdown user-dropdown">
           				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
           					<span class="glyphicon glyphicon-user"></span> John Smith <b class="caret"></b>
       					</a>
			            <ul class="dropdown-menu">
			                <li><a href="#"><i class="fa fa-user"></i> Profile</a></li>
			                <li><a href="#"><i class="fa fa-envelope"></i> Inbox <span class="badge">7</span></a></li>
			                <li><a href="#"><i class="fa fa-gear"></i> Settings</a></li>
			                <li class="divider"></li>
			                <li><a href="#"><i class="fa fa-power-off"></i> Log Out</a></li>
		              	</ul>
           			</li>
          		</ul>
        	</div> -->
      	</nav>
      	
      	<div id="page-wrapper">
      		<ul class="nav nav-justified nav-tabs" id="dashboardTabs">
				<c:forEach items="${dashboardTabs}" var="dashboardTab">
					<li><a data-toggle="tab" href="#${dashboardTab.tabKey}"><span class="glyphicon glyphicon-th"></span> ${dashboardTab.tabTitle}</a></li>
				</c:forEach>
			</ul>

			<div class="tab-content" style="padding: 25px 5px; border: 1px solid #ddd;">
				<c:forEach items="${dashboardTabs}" var="dashboardTab">
					<div class="tab-pane" id="${dashboardTab.tabKey}">
						<div class="row">
							<c:forEach items="${dashboardTab.tabPortlets}" var="dashboardPortlet">
          						<c:set var="numberOfCols" value="${portlet:widthClass(dashboardPortlet.value.portletWidth)}" />
          						<div class="col-md-${numberOfCols}" style="margin-bottom: 15px;">
            						<div class="panel panel-primary">
              							<div class="panel-heading">
                							<h3 class="panel-title">
                								<span class="glyphicon glyphicon-signal"></span> ${dashboardPortlet.value.portletTitle}
                								<span title="Redraw Graph" class="refresh-graph pull-right btn btn-xs glyphicon glyphicon-refresh"></span>
											</h3>
           								</div>
           								<div class="panel-body">
											<c:if test="${dashboardPortlet.value.portletType.name == 'textbox'}">
												<div class="input-group">
													<input type="text" class="form-control" value="${dashboardPortlet.value.input}">
													<span class="input-group-btn">
	        											<button class="btn btn-primary" type="button">Update</button>
	      											</span>
												</div>
											</c:if>
											
											<c:if test="${dashboardPortlet.value.portletType.name == 'areachart'}">
												<c:url var="dashboardPortletUrl" value="/data/${userId}/${dashboardTab.tabKey}/${dashboardPortlet.key}" />
												<div id="evam_drawing_${dashboardPortlet.key}" 
													 class="evam_graph areachart"
													 data-title="${dashboardPortlet.value.portletTitle}"
													 data-key="${dashboardPortlet.key}"
													 data-type="${dashboardPortlet.value.portletType.name}"
													 data-refresh-interval="${dashboardPortlet.value.refreshInterval}"
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
											</c:if>
											
											<c:if test="${dashboardPortlet.value.portletType.name == 'linechart'}">
												<c:url var="dashboardPortletUrl" value="/data/${userId}/${dashboardTab.tabKey}/${dashboardPortlet.key}" />
												<div id="evam_drawing_${dashboardPortlet.key}" 
													 class="evam_graph linechart"
													 data-title="${dashboardPortlet.value.portletTitle}"
													 data-key="${dashboardPortlet.key}"
													 data-type="${dashboardPortlet.value.portletType.name}"
													 data-refresh-interval="${dashboardPortlet.value.refreshInterval}"
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
											</c:if>
											
											<c:if test="${dashboardPortlet.value.portletType.name == 'piechart'}">
												<c:url var="dashboardPortletUrl" value="/data/${userId}/${dashboardTab.tabKey}/${dashboardPortlet.key}" />
												<div id="evam_drawing_${dashboardPortlet.key}" 
													 class="evam_graph piechart"
													 data-title="${dashboardPortlet.value.portletTitle}"
													 data-key="${dashboardPortlet.key}"
													 data-type="${dashboardPortlet.value.portletType.name}"
													 data-request-url="${dashboardPortletUrl}"
													 data-refresh-interval="${dashboardPortlet.value.refreshInterval}">
												</div>
											</c:if>
											
											<c:if test="${dashboardPortlet.value.portletType.name == 'donutchart'}">
												<c:url var="dashboardPortletUrl" value="/data/${userId}/${dashboardTab.tabKey}/${dashboardPortlet.key}" />
												<div id="evam_drawing_${dashboardPortlet.key}" 
													 class="evam_graph donutchart"
													 data-title="${dashboardPortlet.value.portletTitle}"
													 data-key="${dashboardPortlet.key}"
													 data-type="${dashboardPortlet.value.portletType.name}"
													 data-request-url="${dashboardPortletUrl}"
													 data-refresh-interval="${dashboardPortlet.value.refreshInterval}">
												</div>
											</c:if>
											
											<c:if test="${dashboardPortlet.value.portletType.name == 'multiserieslinechart'}">
												<c:url var="dashboardPortletUrl" value="/data/${userId}/${dashboardTab.tabKey}/${dashboardPortlet.key}" />
												<div id="evam_drawing_${dashboardPortlet.key}" 
													 class="evam_graph multiserieslinechart"
													 data-title="${dashboardPortlet.value.portletTitle}"
													 data-key="${dashboardPortlet.key}"
													 data-type="${dashboardPortlet.value.portletType.name}"
													 data-refresh-interval="${dashboardPortlet.value.refreshInterval}"
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
											</c:if>
										</div>
            						</div>
          						</div>
							</c:forEach>
						</div>
					</div>
				</c:forEach>
			</div>
      	</div>
   	</div>

	<!-- JS -->
	<script type="text/javascript" src="<c:url value="/static/js/spin.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery.js" />"></script>	
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap-slider.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap-multiselect.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap-datepicker.js" />"></script>		
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap-typeahead.js" />"></script>	
	<script type="text/javascript" src="<c:url value="/static/js/bootbox.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/d3.v3.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/d3.legend.js" />"></script>	
	<script type="text/javascript" src="<c:url value="/static/js/evam.drawing.js" />"></script>

	<script type="text/javascript">
		$(function() {
			// select first tab
			if($("#dashboardTabs li.active > a[data-toggle='tab']").length <= 0) {
				$("#dashboardTabs li:first > a[data-toggle='tab']").trigger('click');
				$("#dashboardTabs li:first").addClass('active');
				$($("#dashboardTabs li:first > a[data-toggle='tab']").attr('href')).addClass('active');
			}	
			
			$(".evam_graph.linechart").each(function() {
				var options = {
					key: $(this).data('key'), 
					title: $(this).data('title'), 
					refreshInterval: $(this).data('refresh-interval'),
					requestUrl: $(this).data('request-url'),
   			   	   	xAxis: $(this).data('x-axis'), 
   			   	   	yAxis: $(this).data('y-axis'),
   			   		xAxisType: $(this).data('x-axis-type'), 
			   	   	yAxisType: $(this).data('y-axis-type'),
			   	 	xAxisFormat: $(this).data('x-axis-format'), 
			   	   	yAxisFormat: $(this).data('y-axis-format'),
   			   	   	brush: $(this).data('brush'),
   			   		interpolate: $(this).data('interpolate')
				};
				var chart = new evam.Linechart(options);
				chart.initialize();
				chart.update();	
				$(this).bind('evam.resize', function() { chart.resize(); });
				$(this).parents(".panel").find('.btn.refresh-graph').click(function() { chart.update(); });
			});
			$(".evam_graph.areachart").each(function() {
				var options = {
					key: $(this).data('key'), 
					title: $(this).data('title'), 
					refreshInterval: $(this).data('refresh-interval'),
					requestUrl: $(this).data('request-url'),
   			   	   	xAxis: $(this).data('x-axis'), 
   			   	   	yAxis: $(this).data('y-axis'), 
   			   		xAxisType: $(this).data('x-axis-type'), 
		   	   		yAxisType: $(this).data('y-axis-type'),
		   	 		xAxisFormat: $(this).data('x-axis-format'), 
		   	   		yAxisFormat: $(this).data('y-axis-format'),
   			   	   	brush: $(this).data('brush'),
   			   		interpolate: $(this).data('interpolate')
				};
				var chart = new evam.Areachart(options);
				chart.initialize();
				chart.update();	
				$(this).bind('evam.resize', function() { chart.resize(); });
				$(this).parents(".panel").find('.btn.refresh-graph').click(function() { chart.update(); });
			});
			$(".evam_graph.multiserieslinechart").each(function() {
				var options = {
					key: $(this).data('key'), 
					title: $(this).data('title'), 
					refreshInterval: $(this).data('refresh-interval'),
					requestUrl: $(this).data('request-url'),
   			   	   	xAxis: $(this).data('x-axis'), 
   			   	   	yAxis: $(this).data('y-axis'), 
   			   		xAxisType: $(this).data('x-axis-type'), 
		   	   		yAxisType: $(this).data('y-axis-type'),
		   	 		xAxisFormat: $(this).data('x-axis-format'), 
		   	   		yAxisFormat: $(this).data('y-axis-format'), 
		   	   		brush: $(this).data('brush'),
   			   		interpolate: $(this).data('interpolate')
				};
				var chart = new evam.MultiSeriesLinechart(options);
				chart.initialize();
				chart.update();	
				$(this).bind('evam.resize', function() { chart.resize(); });
				$(this).parents(".panel").find('.btn.refresh-graph').click(function() { chart.update(); });
			});
			$(".evam_graph.piechart").each(function() {
				var options = {
					key: $(this).data('key'), 
					title: $(this).data('title'), 
					refreshInterval: $(this).data('refresh-interval'),
					requestUrl: $(this).data('request-url')
				};
				var chart = new evam.Piechart(options);
				chart.initialize();
				chart.update();	
				$(this).bind('evam.resize', function() { chart.resize(); });
				$(this).parents(".panel").find('.btn.refresh-graph').click(function() { chart.update(); });
			});
			$(".evam_graph.donutchart").each(function() {
				var options = {
					key: $(this).data('key'), 
					title: $(this).data('title'), 
					refreshInterval: $(this).data('refresh-interval'),
					requestUrl: $(this).data('request-url')
				};
				var chart = new evam.Donutchart(options);
				chart.initialize();
				chart.update();	
				$(this).bind('evam.resize', function() { chart.resize(); });
				$(this).parents(".panel").find('.btn.refresh-graph').click(function() { chart.update(); });
			});
			
			$(window).resize(function(){
				$(".evam_graph").trigger('evam.resize');
			});			
		});
	</script>
</body>

</html>