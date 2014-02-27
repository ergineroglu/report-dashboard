evam = window.evam || {};

// General Functions
evam.customTimeFormat = d3.time.format.multi([
	[".%L", function(d) { return d.getMilliseconds(); }],
	[":%S", function(d) { return d.getSeconds(); }],
	["%I:%M", function(d) { return d.getMinutes(); }],
	["%I %p", function(d) { return d.getHours(); }],
	["%a %d", function(d) { return d.getDay() && d.getDate() != 1; }],
	["%b %d", function(d) { return d.getDate() != 1; }],
	["%b", function(d) { return d.getMonth(); }],
	["%Y", function() { return true; }]
]);
evam.radionToAngle = function(angle, offset, threshold) {
    var a = angle * 180 / Math.PI + offset;
    return a > threshold ? a - 180 : a;
};
evam.pieChartTextAngle = function(angle, offset) {
	var a = angle * 180 / Math.PI + offset;
	return 	0 < a && a < 180 ? a - 90 : 180 < a && a < 270 ? a + 90 : -90 < a && a < 0 ? a + 90 : a;
};

// Graph 
evam.Graph = function(key, title, xAxis, yAxis, requestUrl, requestData){ 
	this.key				= key;
	this.title				= title;
	this.xAxisTitle			= xAxis;
	this.yAxisTitle			= yAxis;
	this.jqueryObject		= $("#evam_drawing_" + key);	
	this.outerWidth			= this.jqueryObject.width();
	this.outerHeight		= this.jqueryObject.height();
	this.requestUrl			= requestUrl;
	this.requestData		= requestData;	
};
evam.Graph.prototype.initialize = function() { 
	
};
evam.Graph.prototype.draw = function() { 
	
};

// Area Chart
evam.Areachart = function(key, title, xAxis, yAxis, requestUrl, requestData){ 
	evam.Graph.call(this, key, title, xAxis, yAxis, requestUrl, requestData);	
	this.outerHeight		= 500;
	this.margin 			= {top: 5, right: 10, bottom: 20, left: 45};
	this.width 				= this.outerWidth - this.margin.left - this.margin.right;
    this.height 			= this.outerHeight - this.margin.top - this.margin.bottom;
    this.parseX 			= d3.time.format("%d.%m.%Y").parse;
}; 
evam.Areachart.prototype = Object.create(evam.Graph.prototype);
evam.Areachart.prototype.initialize = function() { 
	var areaChart 			= this;
	this.x 					= d3.time.scale().range([0, this.width]);
	this.y 					= d3.scale.linear().range([this.height, 0]);
	this.xAxis 				= d3.svg.axis().scale(this.x).orient("bottom").tickFormat(evam.customTimeFormat);
	this.yAxis 				= d3.svg.axis().scale(this.y).orient("left");
	this.area 				= d3.svg.area().x(function(d) { return areaChart.x(d.x); })
    									   .y0(areaChart.height)
    									   .y1(function(d) { return areaChart.y(d.y); });
	this.svg 				= d3.select(this.jqueryObject[0]).append("svg").attr("width", this.outerWidth).attr("height", this.outerHeight)
											   			  	 .append("g").attr("transform", "translate(" + this.margin.left + "," + this.margin.top + ")");
};
evam.Areachart.prototype.draw = function() { 	
	var areaChart = this;
	d3.json(this.requestUrl, function(error, data) {		
		if (error) return console.warn(error);		
		// parse data
		data.forEach(function(d) {
			d.x = areaChart.parseX(d.x);
		    d.y = +d.y;
		});
		// sort data and extend domains	
		data.sort(function(a, b) { return a.x - b.x; });
		areaChart.x.domain([data[0].x, data[data.length - 1].x]);
		areaChart.y.domain([0, 1.1 * d3.max(data.map(function(d) { return d.y; }))]);
		// area
		areaChart.svg.append("path").datum(data).attr("class", "area").attr("d", areaChart.area);
		// axises		
		areaChart.svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + areaChart.height + ")").call(areaChart.xAxis);
		areaChart.svg.append("text").attr("x", areaChart.width - 6).attr("y",  areaChart.height - 6).style("text-anchor", "end").text(areaChart.xAxisTitle);
		areaChart.svg.append("g").attr("class", "y axis").call(areaChart.yAxis);
		areaChart.svg.append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(areaChart.yAxisTitle);
		// mousemove function
		var bisectY = d3.bisector(function(d) { return d.x; }).left;
		var mousemove = function() {
		    var x0 = areaChart.x.invert(d3.mouse(this)[0]);
	        var i = bisectY(data, x0, 1);
	        var d0 = data[i - 1];
	        var d1 = data[i];
	        var d = x0 - d0.x > d1.x - x0 ? d1 : d0;
		    focus.attr("transform", "translate(" + areaChart.x(d.x) + "," + areaChart.y(d.y) + ")");
		    focus.select("text").text(d.y);
		};
		// focus object for point information
		var focus = areaChart.svg.append("g").attr("class", "focus").style("display", "none");
		focus.append("circle").attr("r", 10).attr("stroke-width", 2);
		focus.append("text").attr("x", 0).attr("y", -12).style("fill", "red").style("text-anchor", "middle").style("font-size", "12px").style("font-weight", "bold");
		areaChart.svg.append("rect").attr("class", "overlay").attr("width", areaChart.width).attr("height", areaChart.height)
	       		     .on("mouseover", function() { focus.style("display", null); })
       			     .on("mouseout", function() { focus.style("display", "none"); })
       			     .on("mousemove", mousemove);		  
	})
	.header("Content-Type","application/x-www-form-urlencoded")
    .send("POST");
};

// Area Chart with Brush
evam.AreachartBrush = function(key, title, xAxis, yAxis, requestUrl, requestData){ 
	evam.Areachart.call(this, key, title, xAxis, yAxis, requestUrl, requestData);		
	this.margin 			= {top: 5, right: 10, bottom: 100, left: 45};
	this.margin2 			= {top: 430, right: 10, bottom: 20, left: 45};
	this.width 				= this.outerWidth - this.margin.left - this.margin.right;
    this.height 			= this.outerHeight - this.margin.top - this.margin.bottom;
    this.height2 			= this.outerHeight - this.margin2.top - this.margin2.bottom;    
}; 
evam.AreachartBrush.prototype = Object.create(evam.Areachart.prototype);
evam.AreachartBrush.prototype.initialize = function() { 	
	var areaChartBrush 		= this;
	this.x 					= d3.time.scale().range([0, this.width]);
	this.x2 				= d3.time.scale().range([0, this.width]);
	this.y 					= d3.scale.linear().range([this.height, 0]);	
	this.y2 				= d3.scale.linear().range([this.height2, 0]);
	this.xAxis				= d3.svg.axis().scale(this.x).orient("bottom").tickFormat(evam.customTimeFormat);
	this.xAxis2 			= d3.svg.axis().scale(this.x2).orient("bottom").tickFormat(evam.customTimeFormat);
	this.yAxis				= d3.svg.axis().scale(this.y).orient("left");	
	this.area 				= d3.svg.area().x(function(d) { return areaChartBrush.x(d.x); })
    									   .y0(areaChartBrush.height)
    									   .y1(function(d) { return areaChartBrush.y(d.y); });
	this.area2 				= d3.svg.area().x(function(d) { return areaChartBrush.x2(d.x); })
    							 		   .y0(areaChartBrush.height2)
    							 		   .y1(function(d) { return areaChartBrush.y2(d.y); });
	// brushed function
	this.brushed = function() {
		areaChartBrush.x.domain(areaChartBrush.brush.empty() ? areaChartBrush.x2.domain() : areaChartBrush.brush.extent());
		areaChartBrush.focus.select(".area").attr("d", areaChartBrush.area);
		areaChartBrush.focus.select(".x.axis").call(areaChartBrush.xAxis);
	};
	this.brush 				= d3.svg.brush().x(this.x2).on("brush", this.brushed);
	this.svg 				= d3.select(this.jqueryObject[0]).append("svg").attr("width", this.outerWidth).attr("height", this.outerHeight);	
	this.focus 				= this.svg.append("g").attr("class", "focus").attr("transform", "translate(" + areaChartBrush.margin.left + "," + areaChartBrush.margin.top + ")");
	this.context 			= this.svg.append("g").attr("class", "context").attr("transform", "translate(" + areaChartBrush.margin2.left + "," + areaChartBrush.margin2.top + ")");
	this.svg.append("defs").append("clipPath").attr("id", "clip").append("rect").attr("width", this.width).attr("height", this.height);
};
evam.AreachartBrush.prototype.draw = function() { 	
	var areaChartBrush = this;
	d3.json(this.requestUrl, function(error, data) {		
		if (error) return console.warn(error);		
		// parse data
		data.forEach(function(d) {
			d.x = areaChartBrush.parseX(d.x);
		    d.y = +d.y;
		});
		// extend domains
		areaChartBrush.x.domain(d3.extent(data.map(function(d) { return d.x; })));
		areaChartBrush.y.domain([0, 1.1 * d3.max(data.map(function(d) { return d.y; }))]);
		areaChartBrush.x2.domain(areaChartBrush.x.domain());
		areaChartBrush.y2.domain(areaChartBrush.y.domain());				
		// upper graph
		areaChartBrush.focus.append("path").datum(data).attr("class", "area").attr("d", areaChartBrush.area);
		areaChartBrush.focus.append("g").attr("class", "x axis").attr("transform", "translate(0," + areaChartBrush.height + ")").call(areaChartBrush.xAxis);
		areaChartBrush.focus.append("text").attr("x", areaChartBrush.width - 6).attr("y",  areaChartBrush.height - 6).style("text-anchor", "end").text(areaChartBrush.xAxisTitle);
		areaChartBrush.focus.append("g").attr("class", "y axis").call(areaChartBrush.yAxis);
		areaChartBrush.focus.append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(areaChartBrush.yAxisTitle);
		// lower graph		
		areaChartBrush.context.append("path").datum(data).attr("class", "area").attr("d", areaChartBrush.area2);
		areaChartBrush.context.append("g").attr("class", "x axis").attr("transform", "translate(0," + areaChartBrush.height2 + ")").call(areaChartBrush.xAxis2);
		areaChartBrush.context.append("g").attr("class", "x brush").call(areaChartBrush.brush)
	    		 	  .selectAll("rect").attr("y", -6).attr("height", areaChartBrush.height2 + 7);
	})
	.header("Content-Type","application/x-www-form-urlencoded")
    .send("POST");
};

//Pie Chart
evam.Piechart = function(key, title, requestUrl, requestData) {
	evam.Graph.call(this, key, title, 'x', 'y', requestUrl, requestData);
	this.outerHeight = 500;
	this.margin = {
		top : 5,
		right : 10,
		bottom : 20,
		left : 45
	};
	this.width = this.outerWidth - this.margin.left - this.margin.right;
	this.height = this.outerHeight - this.margin.top - this.margin.bottom;
	this.radius = Math.min(this.width, this.height) / 2;
};
evam.Piechart.prototype = Object.create(evam.Graph.prototype);
evam.Piechart.prototype.initialize = function(value) {
	var innerRadiusValue = 0;
	if (value == "donut"){
		innerRadiusValue = this.radius - 130;
	}
	this.color = d3.scale.category20c();
	this.arc = d3.svg.arc().outerRadius(this.radius - 10).innerRadius(innerRadiusValue);
	this.pie = d3.layout.pie().sort(null).value(function(d) {
		return d.y;
	});
	this.svg = d3.select(this.jqueryObject[0]).append("svg").attr("width",
			this.width).attr("height", this.height)
			.attr("style", "padding:50px; cursor:pointer;").append("g").attr(
			"transform",
			"translate(" + this.width / 2 + "," + this.height / 2 + ")");

};
evam.Piechart.prototype.draw = function(value) {
	var pieChart = this;
	var text = "";
	var c1, c2, c3 = 1;
	if (value == "donut"){
		c1 = 0.1;
		c2 = 1.6;
		c3 = 1;
	} else {
		c1 = 0.1;
		c2 = 2.3;
		c3 = 1.2;
	}
	d3.json(
			this.requestUrl,
			function(error, data) {
				if (error)
					return console.warn(error);
				// parse data
				data.forEach(function(d) {
					d.y = +d.y;
				});
				var r2d = Math.PI / 180;
				var g = pieChart.svg.selectAll(".arc").data(pieChart.pie(data))
						.enter()
						.append("g")
						.attr("class", "arc")
						.on("mouseover",
							function(d) {
								var c = pieChart.arc.centroid(d);
								pieChart.svg.selectAll(".arc").attr("transform", "translate(0,0)")
																.style("font-size", "14px");
								pieChart.svg.selectAll(".arc").filter(function(x) { return x.data.x == d.data.x; })
																.attr("transform", "translate(" + c[0] * c1 + "," + c[1] * c1 + ")")
																.style("font-size", "20px");
								focus.style("display", null);
								focus.attr("transform", "translate(" + c[0] * c2 + "," + c[1] * c2 + ") rotate(" + evam.pieChartTextAngle((d.startAngle + d.endAngle) / 2, -90) + ")");
							    focus.select("text").text(d.data.y).style("fill", fill()).style("text-anchor", "middle");
								function fill() {
									return pieChart.color(d.data.x);
								};
							})
						.on("mouseout", 
							function(d) {
								focus.style("display", "none");
								pieChart.svg.selectAll(".arc").attr("transform", "translate(0,0)")
																.style("font-size", "14px");
							});
				g.append("path").attr("d", pieChart.arc).style("fill",
						function(d) {
							return pieChart.color(d.data.x);
						});

				g.append("text")
					.attr("transform", 
						function(d) {
							var c = pieChart.arc.centroid(d);
							return "translate(" + c[0] * c3 + "," + c[1] * c3 + ") rotate(" + evam.radionToAngle((d.startAngle + d.endAngle) / 2, -90, 90) + ")";
						})					
					.attr("dy", ".35em")
					.attr("class", "pieChartArc")
					.style("text-anchor", "middle")
					.text(
						function(d) {
							return d.data.x;
						});
				var focus = pieChart.svg.append("g").attr("class", "focus").style("display", "none");				
				focus.append("text").style("fill", "red").style("text-anchor", "middle").style("font-size", "20px").style("font-weight", "bold");
			}).header("Content-Type", "application/x-www-form-urlencoded")
			.send("POST");
};