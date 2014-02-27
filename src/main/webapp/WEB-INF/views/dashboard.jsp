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
        	
        	<!-- Collect the nav links, forms, and other content for toggling -->
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
        	</div><!-- /.navbar-collapse -->
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
          						<div class="col-lg-${numberOfCols}">
            						<div class="panel panel-primary">
              							<div class="panel-heading">
                							<h3 class="panel-title">
                								<span class="glyphicon glyphicon-signal"></span> ${dashboardPortlet.value.portletTitle} 
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
													 data-x-axis="${dashboardPortlet.value.axisXName}"
													 data-y-axis="${dashboardPortlet.value.axisYName}"
													 data-request-url="${dashboardPortletUrl}">
												</div>
											</c:if>
											
											<c:if test="${dashboardPortlet.value.portletType.name == 'areachartbrush'}">
												<c:url var="dashboardPortletUrl" value="/data/${userId}/${dashboardTab.tabKey}/${dashboardPortlet.key}" />
												<div id="evam_drawing_${dashboardPortlet.key}" 
													 class="evam_graph areachartbrush"
													 data-title="${dashboardPortlet.value.portletTitle}"
													 data-key="${dashboardPortlet.key}"
													 data-type="${dashboardPortlet.value.portletType.name}"
													 data-x-axis="${dashboardPortlet.value.axisXName}"
													 data-y-axis="${dashboardPortlet.value.axisYName}"
													 data-request-url="${dashboardPortletUrl}">
												</div>
											</c:if>
											
											<c:if test="${dashboardPortlet.value.portletType.name == 'piechart'}">
												<c:url var="dashboardPortletUrl" value="/data/${userId}/${dashboardTab.tabKey}/${dashboardPortlet.key}" />
												<div id="evam_drawing_${dashboardPortlet.key}" 
													 class="evam_graph piechart"
													 data-title="${dashboardPortlet.value.portletTitle}"
													 data-key="${dashboardPortlet.key}"
													 data-type="${dashboardPortlet.value.portletType.name}"
													 data-request-url="${dashboardPortletUrl}">
												</div>
											</c:if>
											
											<c:if test="${dashboardPortlet.value.portletType.name == 'donutchart'}">
												<c:url var="dashboardPortletUrl" value="/data/${userId}/${dashboardTab.tabKey}/${dashboardPortlet.key}" />
												<div id="evam_drawing_${dashboardPortlet.key}" 
													 class="evam_graph donutchart"
													 data-title="${dashboardPortlet.value.portletTitle}"
													 data-key="${dashboardPortlet.key}"
													 data-type="${dashboardPortlet.value.portletType.name}"
													 data-request-url="${dashboardPortletUrl}">
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
	<script type="text/javascript" src="<c:url value="/static/js/jquery.js" />"></script>	
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap-slider.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap-multiselect.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap-datepicker.js" />"></script>		
	<script type="text/javascript" src="<c:url value="/static/js/bootstrap-typeahead.js" />"></script>	
	<script type="text/javascript" src="<c:url value="/static/js/bootbox.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/d3.v3.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/evam.drawing.js" />"></script>

	<script type="text/javascript">
		$(function() {
			// select first tab
			if($("#dashboardTabs li.active > a[data-toggle='tab']").length <= 0) {
				$("#dashboardTabs li:first > a[data-toggle='tab']").trigger('click');
				$("#dashboardTabs li:first").addClass('active');
				$($("#dashboardTabs li:first > a[data-toggle='tab']").attr('href')).addClass('active');
			}	
			
			$(".evam_graph.areachart").each(function() {
				var chart = new evam.Areachart($(this).data('key'), $(this).data('title'), 
											   $(this).data('x-axis'), $(this).data('y-axis'),
							   			   	   $(this).data('request-url'), null);
				chart.initialize();
				chart.draw();
			});
			$(".evam_graph.areachartbrush").each(function() {
				var chart = new evam.AreachartBrush($(this).data('key'), $(this).data('title'), 
											   		$(this).data('x-axis'), $(this).data('y-axis'),
							   			   	   		$(this).data('request-url'), null);
				chart.initialize();
				chart.draw();
			});
			$(".evam_graph.piechart").each(function() {
				var chart = new evam.Piechart($(this).data('key'), $(this).data('title'), 
							   			   	   		$(this).data('request-url'), null);
				chart.initialize("");
				chart.draw("");
			});
			$(".evam_graph.donutchart").each(function() {
				var chart = new evam.Piechart($(this).data('key'), $(this).data('title'), 
							   			   	   		$(this).data('request-url'), null);
				chart.initialize("donut");
				chart.draw("donut");
			});
		});
		
// 		function createAreaChart() {
// 			var margin = {top: 5, right: 10, bottom: 90, left: 45},
// 		    width = $("#areachart_scenario_values").width() - margin.left - margin.right,
// 		    height = 500 - margin.top - margin.bottom;

// 			var parseDate = d3.time.format("%d-%b-%y").parse;
// 			var bisectDate = d3.bisector(function(d) { return d.date; }).left;
// 			var formatValue = d3.format(",.2f");
// 		    var formatCurrency = function(d) { return "$" + formatValue(d); };
	
// 			var x = d3.time.scale()
// 			    .range([0, width]);
	
// 			var y = d3.scale.linear()
// 			    .range([height, 0]);
	
// 			var xAxis = d3.svg.axis()
// 			    .scale(x)
// 			    .orient("bottom")			    
// 			    .tickFormat(d3.time.format("%Y-%m-%d"));;
	
// 			var yAxis = d3.svg.axis()
// 			    .scale(y)
// 			    .orient("left");
	
// 			var area = d3.svg.area()
// 			    .x(function(d) { return x(d.date); })
// 			    .y0(height)
// 			    .y1(function(d) { return y(d.close); });
	
// 			var svg = d3.select("#areachart_scenario_values").append("svg")
// 			    .attr("width", width + margin.left + margin.right)
// 			    .attr("height", height + margin.top + margin.bottom)
// 			  	.append("g")
// 			    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
			
// 			var data_url = '<c:url value="/data/${userId}/scenario_view/scenario_values" />';
// 			d3.tsv(data_url, function(error, data) {
// 				data.forEach(function(d) {
// 					d.date = parseDate(d.date);
// 				    d.close = +d.close;
// 			  	});
				
// 				data.sort(function(a, b) {
// 				    return a.date - b.date;
// 				  });

// 				  x.domain([data[0].date, data[data.length - 1].date]);
// 				  y.domain(d3.extent(data, function(d) { return d.close; }));

// 			  	svg.append("path")
// 				      .datum(data)
// 				      .attr("class", "area")
// 				      .attr("d", area);
			  	
// 			    svg.append("g")
// 			      .attr("class", "x axis")
// 			      .attr("transform", "translate(0," + height + ")")
// 			      .call(xAxis)
// 			      .selectAll("text")  
// 		            .style("text-anchor", "end")
// 		            .attr("dx", "-.8em")
// 		            .attr("dy", ".15em")
// 		            .attr("transform", function(d) {
// 		                return "rotate(-65)";
//                   });
//                  svg.append("text")      // text label for the x axis
// 			        .attr("x", width - 6)			        
// 			        .attr("y",  height - 6)
// 			        .style("text-anchor", "end")
// 			        .text("Date");

// 			  svg.append("g")
// 			      .attr("class", "y axis")
// 			      .call(yAxis);
			  
// 			  svg.append("text")
// 			      .attr("transform", "rotate(-90)")
// 			      .attr("y", 6)
// 			      .attr("dy", ".71em")
// 			      .style("text-anchor", "end")
// 			      .text("Prices ($)");
			  
// 			  var focus = svg.append("g")
// 		      .attr("class", "focus")
// 		      .style("display", "none");

// 		  focus.append("circle")
// 		      .attr("r", 10);

// 		  focus.append("text")
// 		      .attr("x", 12)
// 		      .attr("dy", ".8em");
		  
// 		  var areachart_mousemove = function() {
// 			    var x0 = x.invert(d3.mouse(this)[0]),
// 			        i = bisectDate(data, x0, 1),
// 			        d0 = data[i - 1],
// 			        d1 = data[i],
// 			        d = x0 - d0.date > d1.date - x0 ? d1 : d0;
// 			    focus.attr("transform", "translate(" + x(d.date) + "," + y(d.close) + ")");
// 			    focus.select("text").text(formatCurrency(d.close));
// 		  };

// 		  svg.append("rect")
// 		      .attr("class", "overlay")
// 		      .attr("width", width)
// 		      .attr("height", height)
// 		      .on("mouseover", function() { focus.style("display", null); })
// 		      .on("mouseout", function() { focus.style("display", "none"); })
// 		      .on("mousemove", areachart_mousemove);		  
// 		  });
//		}
	</script>
</body>

</html>