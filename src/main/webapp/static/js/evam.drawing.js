evam = window.evam || {};

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
	this.margin 			= {top: 5, right: 10, bottom: 90, left: 45};
	this.width 				= this.outerWidth - this.margin.left - this.margin.right;
    this.height 			= this.outerHeight - this.margin.top - this.margin.bottom;
    this.parseX 			= d3.time.format("%d.%m.%Y").parse;
}; 
evam.Areachart.prototype = Object.create(evam.Graph.prototype);
evam.Areachart.prototype.initialize = function() { 
	var areaChart 			= this;
	this.x 					= d3.time.scale().range([0, this.width]);
	this.y 					= d3.scale.linear().range([this.height, 0]);
	this.xAxis 				= d3.svg.axis().scale(this.x).orient("bottom").tickFormat(d3.time.format("%Y.%m.%d"));
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
		areaChart.y.domain([0, d3.max(data, function(d) { return d.y; })]);
		// axises		
		areaChart.svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + areaChart.height + ")").call(areaChart.xAxis);
		areaChart.svg.selectAll("text").style("text-anchor", "end").attr("dx", "-.8em").attr("dy", ".15em")
	 	  						       .attr("transform", function(d) { return "rotate(-70)"; });
		areaChart.svg.append("text").attr("x", areaChart.width - 6).attr("y",  areaChart.height - 6).style("text-anchor", "end").text(areaChart.xAxisTitle);
		areaChart.svg.append("g").attr("class", "y axis").call(areaChart.yAxis);
		areaChart.svg.append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(areaChart.yAxisTitle);
		// area
		areaChart.svg.append("path").datum(data).attr("class", "area").attr("d", areaChart.area);	
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
		focus.append("circle").attr("r", 10);
		focus.append("text").attr("x", 12).attr("dy", ".8em");
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
    this.height2 			= this.outerHeight - this.margin2.top - this.margin2.bottom;    
}; 
evam.AreachartBrush.prototype = Object.create(evam.Areachart.prototype);
evam.AreachartBrush.prototype.initialize = function() { 	
	var areaChartBrush 		= this;
	this.x 					= d3.time.scale().range([0, this.width]);
	this.y 					= d3.scale.linear().range([this.height, 0]);
	this.x2 				= d3.time.scale().range([0, this.width]);
	this.y2 				= d3.scale.linear().range([this.height2, 0]);
	this.xAxis				= d3.svg.axis().scale(this.x).orient("bottom"),
	this.xAxis2 			= d3.svg.axis().scale(this.x2).orient("bottom");
	this.yAxis				= d3.svg.axis().scale(this.y).orient("left");
	this.area 				= d3.svg.area().interpolate("monotone")
										   .x(function(d) { return areaChartBrush.x(d.x); })
    									   .y0(areaChartBrush.height)
    									   .y1(function(d) { return areaChartBrush.y(d.y); });
	this.area2 				= d3.svg.area().interpolate("monotone")
    							 		   .x(function(d) { return areaChartBrush.x2(d.x); })
    							 		   .y0(areaChartBrush.height2)
    							 		   .y1(function(d) { return areaChartBrush.y2(d.price); });
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
		// sort data and extend domains	
		data.sort(function(a, b) { return a.x - b.x; });
		areaChartBrush.x.domain([data[0].x, data[data.length - 1].x]);
		areaChartBrush.y.domain([0, d3.max(data, function(d) { return d.y; })]);
		areaChartBrush.x2.domain(areaChartBrush.x.domain());
		areaChartBrush.y2.domain(areaChartBrush.y.domain());		
		// area
		areaChartBrush.focus.append("path").datum(data).attr("class", "area").attr("d", areaChartBrush.area);	
		// axises		
		areaChartBrush.focus.append("g").attr("class", "x axis").attr("transform", "translate(0," + areaChartBrush.height + ")").call(areaChartBrush.xAxis);
		areaChartBrush.focus.append("text").attr("x", areaChartBrush.width - 6).attr("y",  areaChartBrush.height - 6).style("text-anchor", "end").text(areaChartBrush.xAxisTitle);
		areaChartBrush.focus.append("g").attr("class", "y axis").call(areaChartBrush.yAxis);
		areaChartBrush.focus.append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(areaChartBrush.yAxisTitle);
		areaChartBrush.context.append("path").datum(data).attr("class", "area").attr("d", areaChartBrush.area2);
		areaChartBrush.context.append("g").attr("class", "x axis").attr("transform", "translate(0," + areaChartBrush.height2 + ")").call(areaChartBrush.xAxis2);
		areaChartBrush.context.append("g").attr("class", "x brush").call(areaChartBrush.brush)
	    		 	  .selectAll("rect").attr("y", -6).attr("height", areaChartBrush.height2 + 7);
	})
	.header("Content-Type","application/x-www-form-urlencoded")
    .send("POST");
};