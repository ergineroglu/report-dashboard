<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="evam"%>
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
	<link href="<c:url value="/static/images/favicon.png" />" rel="shortcut icon">
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
          		<a class="navbar-brand" href="<c:url value="/dashboard/${userId}" />">
          			<img src="<c:url value="/static/images/logo.png" />" alt="EVAM Report Manager" />
       			</a>
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
								<c:set var="portletIcon" value="${portlet:portletIcon(dashboardPortlet.value.portletType)}" />
								<evam:portlet userId="${userId}"
											  dashboardTab="${dashboardTab}"
											  dashboardPortlet="${dashboardPortlet}"
											  portletIcon="${portletIcon}"
											  numberOfCols="${numberOfCols}">
								</evam:portlet>
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
	<script type="text/javascript" src="<c:url value="/static/js/stacktrace.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/d3.v3.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/d3.legend.js" />"></script>	
	<script type="text/javascript" src="<c:url value="/static/js/evam.drawing.js" />"></script>

	<script type="text/javascript">
		$(function() {
			// initialize portlets if first opening of tab
			$("#dashboardTabs li > a[data-toggle='tab']").on('shown.bs.tab', function(e) {
				var tabPane = $($(e.target).attr('href'));
				if(! tabPane.hasClass('initialized')) {
					var portletList = tabPane.find(".evam_graph");
					portletList.each(function() { evam.initializePortlet($(this)); });
					$(window).resize(function(){ portletList.trigger('evam.resize'); });
					tabPane.addClass('initialized');
				}				
			});
			
			// select first tab
			if($("#dashboardTabs li.active > a[data-toggle='tab']").length <= 0) {
				$("#dashboardTabs li:first > a[data-toggle='tab']").trigger('click');
				$("#dashboardTabs li:first").addClass('active');
				$($("#dashboardTabs li:first > a[data-toggle='tab']").attr('href')).addClass('active');
			}
		});
	</script>
</body>

</html>