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
evam.selectParseFormat = function(type, extraFormat) {
	switch(type) {
	case 'string':
	default:
		return function(x) {return x;};
	case 'number':
		return function(x) {return +x;};
	case 'time':
		return d3.time.format(extraFormat).parse;
	}
};
evam.selectScaleFormat = function(type, range) {
	switch(type) {
	case 'string':
	default:
		var ordinal_scale = d3.scale.ordinal().rangePoints(range);
		ordinal_scale.invert = function(e) {
			var d = this.domain();
	        var r = this.range(); 
	        return d[d3.bisect(r, e)];
		};
		return ordinal_scale;
	case 'number':
		return d3.scale.linear().range(range);
	case 'time':
		return d3.time.scale().range(range);
	}
};
evam.selectDomainExtentFormat = function(type, marginRatio) {
	switch(type) {
	case 'string':
	default:
		return function(data) {return data;};
	case 'time':	
		return function(data) { 
			var extent = d3.extent(data);
			var margin = ((extent[1].getTime() - extent[0].getTime()) * (marginRatio || 0));
			return [new Date(extent[0].getTime() - margin), new Date(extent[1].getTime() + margin)]; 
		};		
	case 'number':
		return function(data) { 
			var extent = d3.extent(data);
			var margin = ((extent[1] - extent[0]) * (marginRatio || 0));
			return [extent[0] - margin, extent[1] + margin]; 
		};
	}
};
evam.selectAxisTickFormat = function(type) {
	switch(type) {
	case 'string':
	default:
		return function(data, i) {return data;};
	case 'time':	
		return evam.customTimeFormat;		
	case 'number':
		return d3.format("s");
	}
};
evam.spinnerOptions = {
	lines: 13, // The number of lines to draw
	length: 30, // The length of each line
	width: 10, // The line thickness
	radius: 25, // The radius of the inner circle
	corners: 1, // Corner roundness (0..1)
	rotate: 0, // The rotation offset
	direction: 1, // 1: clockwise, -1: counterclockwise
	color: '#000', // #rgb or #rrggbb or array of colors
	speed: 0.8, // Rounds per second
	trail: 40, // Afterglow percentage
	shadow: false, // Whether to render a shadow
	hwaccel: false, // Whether to use hardware acceleration
	className: 'spinner', // The CSS class to assign to the spinner
	zIndex: 2e9, // The z-index (defaults to 2000000000)
	top: 'auto', // Top position relative to parent in px
	left: 'auto' // Left position relative to parent in px
};

// Graph 
evam.Graph = function(options){ 
	this.key				= options.key;
	this.title				= options.title;
	this.refreshInterval	= options.refreshInterval || -1;
	this.parentObject		= $("#evam_drawing_" + options.key);	
	this.outerWidth			= this.parentObject.width();
	this.outerHeight		= this.parentObject.height();
	this.requestUrl			= options.requestUrl;
	this.requestData		= options.requestData || {};	
	this.spinner			= new Spinner(evam.spinnerOptions);	
	this.updateTimerId		= null;
};
evam.Graph.prototype.initialize = function() {};
evam.Graph.prototype.prepareData = function(data) {};
evam.Graph.prototype.draw = function(data) {};
evam.Graph.prototype.update = function() {
	var chart = this;
	// remove timer
	window.clearTimeout(chart.updateTimerId);
	chart.updateTimerId = null;
	// start request
	chart.startSpinner();
	d3.json(chart.requestUrl, function(error, data) {
		if (error) console.warn(error);
		else {
			try {
				chart.prepareData(data);
				chart.draw(data);
			}
			catch(err) {
				console.error(err);
			}
		}		
		chart.stopSpinner();
		if(chart.refreshInterval > 0) {		
			chart.updateTimerId = setTimeout(function(){ chart.update(); }, chart.refreshInterval * 1000);
		}
	})
	.header("Content-Type","application/x-www-form-urlencoded")
    .send("POST");
};
evam.Graph.prototype.startSpinner = function() {
	this.spinner.spin(this.parentObject[0]);
};
evam.Graph.prototype.stopSpinner = function() {
	this.spinner.stop();
};
evam.Graph.prototype.resize = function() {
	var targetWidth = this.parentObject.width();    
    this.parentObject.children('svg').attr("width", targetWidth);	
};

// X-Y Chart
evam.XYchart = function(options){ 
	evam.Graph.call(this, options);	
	this.xAxisTitle			= options.xAxis || 'X';
	this.yAxisTitle			= options.yAxis || 'Y';
	this.xAxisType			= options.xAxisType;
	this.yAxisType			= options.yAxisType;
	this.parseX				= evam.selectParseFormat(options.xAxisType, options.xAxisFormat);
	this.parseY				= evam.selectParseFormat(options.yAxisType, options.yAxisFormat);
	this.domainX			= evam.selectDomainExtentFormat(options.xAxisType);
	this.domainY			= evam.selectDomainExtentFormat(options.yAxisType, 0.1);
	this.tickFormatX		= evam.selectAxisTickFormat(options.xAxisType);
	this.tickFormatY		= evam.selectAxisTickFormat(options.yAxisType);
	this.outerHeight		= options.height || 500;
	this.margin 			= {top: 10, right: 20, bottom: 20, left: 45};
	this.width 				= this.outerWidth - this.margin.left - this.margin.right;
    this.height 			= this.outerHeight - this.margin.top - this.margin.bottom;    
    this.bisectY 			= d3.bisector(function(d) { return d.x; }).left;   
};
evam.XYchart.prototype = Object.create(evam.Graph.prototype);
evam.XYchart.prototype.initialize = function() { 
	this.x 					= evam.selectScaleFormat(this.xAxisType, [0, this.width]);
	this.y 					= evam.selectScaleFormat(this.yAxisType, [this.height, 0]);
	this.xAxis 				= d3.svg.axis().scale(this.x).orient("bottom").tickSize(-this.height).tickPadding(10).tickFormat(this.tickFormatX).tickSubdivide(true);
	this.yAxis 				= d3.svg.axis().scale(this.y).orient("left").tickSize(-this.width).tickFormat(this.tickFormatY).tickSubdivide(true);
};

// Line Chart
evam.Linechart = function(options){ 
	evam.XYchart.call(this, options);
	this.interpolate		= options.interpolate || 'linear';
	this.hasBrush			= options.brush;
	if(this.hasBrush) {
		this.margin 		= {top: 5, right: 20, bottom: 100, left: 45};
		this.margin2 		= {top: 430, right: 20, bottom: 20, left: 45};
		this.width 			= this.outerWidth - this.margin.left - this.margin.right;
		this.height 		= this.outerHeight - this.margin.top - this.margin.bottom;
		this.height2 		= this.outerHeight - this.margin2.top - this.margin2.bottom;
	}
}; 
evam.Linechart.prototype = Object.create(evam.XYchart.prototype);
evam.Linechart.prototype.initialize = function() { 
	evam.XYchart.prototype.initialize.call(this);
	var lineChart 			= this;
	this.line 				= d3.svg.line().interpolate(lineChart.interpolate)
										   .x(function(d) { return lineChart.x(d.x); })
	   									   .y(function(d) { return lineChart.y(d.y); });
	this.svg 				= d3.select(this.parentObject[0]).append("svg").attr("width", this.outerWidth).attr("height", this.outerHeight)
								.attr("viewBox", "0 0 " + this.outerWidth + " " + this.outerHeight).attr("preserveAspectRatio", "xMidYMid");
	this.focus 				= this.svg.append("g").attr("class", "focus").attr("transform", "translate(" + lineChart.margin.left + "," + lineChart.margin.top + ")");
	if(this.hasBrush) {
		this.x2 			= evam.selectScaleFormat(this.xAxisType, [0, this.width]);
		this.y2 			= evam.selectScaleFormat(this.yAxisType, [this.height2, 0]);
		this.xAxis2 		= d3.svg.axis().scale(this.x2).orient("bottom").tickFormat(this.tickFormatX);	
		this.line2 			= d3.svg.line().interpolate(lineChart.interpolate)
										   .x(function(d) { return lineChart.x2(d.x); })
		   								   .y(function(d) { return lineChart.y2(d.y); });
		this.brushed 		= function() {
			if(lineChart.xAxisType == "string") {
				var startPos = lineChart.x2.domain().indexOf(lineChart.brush.extent()[0]) - 1;
				var endPos = lineChart.brush.extent()[1] ? lineChart.x2.domain().indexOf(lineChart.brush.extent()[1]) : lineChart.x2.domain().length;
				if(lineChart.brush.empty()) {
					lineChart.x.domain(lineChart.x2.domain());
					startPos = 0;
					endPos =  lineChart.x2.domain().length;
				}
				else {
					lineChart.x.domain(lineChart.x2.domain().slice(startPos, endPos));
				}						
				lineChart.focus.select(".line").datum(lineChart.context.select(".line").datum().slice(startPos, endPos));
				lineChart.focus.select(".area").datum(lineChart.context.select(".area").datum().slice(startPos, endPos));
			}
			else {
				lineChart.x.domain(lineChart.brush.empty() ? lineChart.x2.domain() : lineChart.brush.extent());
			}
			lineChart.focus.select(".area").attr("d", lineChart.area);
			lineChart.focus.select(".line").attr("d", lineChart.line);
			lineChart.focus.select(".x.axis").call(lineChart.xAxis);
		};
		this.brush 			= d3.svg.brush().x(this.x2).on("brush", this.brushed);	
		this.context 		= this.svg.append("g").attr("class", "context").attr("transform", "translate(" + lineChart.margin2.left + "," + lineChart.margin2.top + ")");
		this.svg.append("clipPath").attr("id", "clip_" + this.key).append("rect").attr("width", this.width).attr("height", this.height);
	}
};
evam.Linechart.prototype.prepareData = function(data) {
	var lineChart = this;
	// parse & sort data
	data.forEach(function(d) {
		d.x = lineChart.parseX(d.x);
	    d.y = lineChart.parseY(d.y);
	});	
	data.sort(function(a, b){ return d3.ascending(a.x, b.x); });
};
evam.Linechart.prototype.draw = function(data) { 	
	var lineChart = this;	
	// domains
	lineChart.x.domain(lineChart.domainX(data.map(function(d) { return d.x; })));	
	lineChart.y.domain(lineChart.domainY(data.map(function(d) { return d.y; })));
	if(this.hasBrush) {
		lineChart.x2.domain(lineChart.x.domain());
		lineChart.y2.domain(lineChart.y.domain());
	}
	// remove old data
	lineChart.svg.select("g.focus").selectAll("*").remove();
	lineChart.svg.select("g.context").selectAll("*").remove();	
	// upper graph
	lineChart.focus.append("path").datum(data).attr("class", "line").attr("clip-path", "url(#clip_" + lineChart.key + ")").attr("d", lineChart.line);
	lineChart.focus.append("g").attr("class", "x axis").attr("transform", "translate(0," + lineChart.height + ")").call(lineChart.xAxis);
	lineChart.focus.append("text").attr("x", lineChart.width - 6).attr("y",  lineChart.height - 6).style("text-anchor", "end").text(lineChart.xAxisTitle);
	lineChart.focus.append("g").attr("class", "y axis").call(lineChart.yAxis);
	lineChart.focus.append("text").attr("transform", "rotate(-90)").attr("x", -8).attr("dy", 12).style("text-anchor", "end").text(lineChart.yAxisTitle);
	if(lineChart.hasBrush) {
		// lower graph		
		lineChart.context.append("path").datum(data).attr("class", "line").attr("clip-path", "url(#clip_" + lineChart.key + ")").attr("d", lineChart.line2);
		lineChart.context.append("g").attr("class", "x axis").attr("transform", "translate(0," + lineChart.height2 + ")").call(lineChart.xAxis2);
		lineChart.context.append("g").attr("class", "x brush").call(lineChart.brush)
	    		 	  	 .selectAll("rect").attr("y", -6).attr("height", lineChart.height2 + 7);
	}
	// mousemove function		
	var mousemove = function() {
	    var mouseX = d3.mouse(this)[0];
		var x0 = lineChart.x.invert(mouseX);
        var i = lineChart.bisectY(data, x0, 1);
        var d0 = data[i - 1];
        var d1 = data[i];
        var d = x0 - d0.x > d1.x - x0 ? d1 : d0;
        highlight.attr("transform", "translate(" + lineChart.x(d.x) + "," + lineChart.y(d.y) + ")");
	    var text = highlight.select("text");
	    text.text(lineChart.xAxisTitle + ": " + d.x + " " + lineChart.yAxisTitle + ": " + d.y);		    
	    if(mouseX < (lineChart.width / 2)) {text.style("text-anchor", "start").attr("x", 12).attr("y", 3);}
	    else {text.style("text-anchor", "end").attr("x", -12).attr("y", 3);} 	    
	};
	// highlight object for point information
	var highlight = lineChart.focus.append("g").attr("class", "highlight").style("display", "none");
	highlight.append("circle").attr("r", 10).attr("stroke-width", 2);
	highlight.append("text").style("fill", "red").style("font-size", "12px").style("font-weight", "bold");
	lineChart.focus.append("rect").attr("class", "overlay").attr("width", lineChart.width).attr("height", lineChart.height)
       		       .on("mouseover", function() { highlight.style("display", null); })
   			       .on("mouseout", function() { highlight.style("display", "none"); })
   			       .on("mousemove", mousemove);	
};

// Multi Series Line Chart
evam.MultiSeriesLinechart = function(options){ 	
	evam.Linechart.call(this, options);	
}; 
evam.MultiSeriesLinechart.prototype = Object.create(evam.Linechart.prototype);
evam.MultiSeriesLinechart.prototype.initialize = function() { 
	evam.Linechart.prototype.initialize.call(this);
	var multiSeriesChart	= this;
	this.color				= d3.scale.category10();
	if(this.hasBrush) {
		this.brushed 		= function() {
			if(multiSeriesChart.xAxisType == "string") {
				var startPos = multiSeriesChart.x2.domain().indexOf(multiSeriesChart.brush.extent()[0]) - 1;
				var endPos = multiSeriesChart.brush.extent()[1] ? multiSeriesChart.x2.domain().indexOf(multiSeriesChart.brush.extent()[1]) : multiSeriesChart.x2.domain().length;
				if(multiSeriesChart.brush.empty()) {
					multiSeriesChart.x.domain(multiSeriesChart.x2.domain());
					startPos = 0;
					endPos =  multiSeriesChart.x2.domain().length;
				}
				else {
					multiSeriesChart.x.domain(multiSeriesChart.x2.domain().slice(startPos, endPos));
				}								
				var categoriedData = [];
				multiSeriesChart.context.selectAll(".lineSerie").each(function(data, i) {
					var newData = {
						key: data.key,
						values: data.values.slice(startPos, endPos)
					};
					categoriedData.push(newData);
				});
				multiSeriesChart.focus.selectAll(".lineSerie").data(categoriedData);
			}
			else {
				multiSeriesChart.x.domain(multiSeriesChart.brush.empty() ? multiSeriesChart.x2.domain() : multiSeriesChart.brush.extent());
			}				
			multiSeriesChart.focus.selectAll(".lineSerie").select(".line").attr("d", function(d) {return multiSeriesChart.line(d.values); });
			multiSeriesChart.focus.select(".x.axis").call(multiSeriesChart.xAxis);	
		};		
		multiSeriesChart.brush.on("brush", multiSeriesChart.brushed);
	}
};
evam.MultiSeriesLinechart.prototype.prepareData = function(data) {
	var multiSeriesChart	 = this;
	// parse data
	data.forEach(function(d) {
		d.x = multiSeriesChart.parseX(d.x);
	    d.y = multiSeriesChart.parseY(d.y);
	});		
};
evam.MultiSeriesLinechart.prototype.draw = function(data) { 	
	evam.Linechart.prototype.draw.call(this, data);
	var multiSeriesChart	 = this;
	// find keys and categorize data	
	var categoriedData = d3.nest().key(function(d) { return d.z; })
						   .sortValues(function(a,b) { return d3.ascending(a.x, b.x); }).entries(data);
	multiSeriesChart.color.domain(categoriedData.map(function(d) { return d.key; }));
	// remove lines drawn by linechart
	multiSeriesChart.focus.select("path").remove();
	multiSeriesChart.focus.select(".highlight").remove();
	multiSeriesChart.mousemove = null;
	if(multiSeriesChart.hasBrush) {
		multiSeriesChart.context.select("path").remove();
	}	
	// add line series
	var lineSerie = multiSeriesChart.focus.selectAll(".lineSerie").data(categoriedData).enter()
									.append("g").attr("class", "lineSerie");
	
	lineSerie.append("path").attr("class", "line").attr("clip-path", "url(#clip_" + multiSeriesChart.key + ")")
			 .attr("d", function(d) { return multiSeriesChart.line(d.values); })
			 .attr("data-legend", function(d) { return d.key; })
			 .style("stroke", function(d) { return multiSeriesChart.color(d.key); });	
	if(multiSeriesChart.hasBrush) {
		var lineSerieContext = multiSeriesChart.context.selectAll(".lineSerie").data(categoriedData).enter()
									    	   .append("g").attr("class", "lineSerie");

		lineSerieContext.append("path").attr("class", "line").attr("clip-path", "url(#clip_" + multiSeriesChart.key + ")")
					    .attr("d", function(d) { return multiSeriesChart.line2(d.values);})
					    .style("stroke", function(d) { return multiSeriesChart.color(d.key); });
	}
	// create legend
	var legend = multiSeriesChart.svg.append("g").attr("class","legend").style("font-size", "12px").call(d3.legend);
	legend.attr("transform","translate(" + (multiSeriesChart.width + multiSeriesChart.margin.left - legend.select("rect.legend-box").attr('width')) + "," + (multiSeriesChart.margin.top + 20) + ")");
};

// Area Chart
evam.Areachart = function(options){ 
	evam.Linechart.call(this, options);	
}; 
evam.Areachart.prototype = Object.create(evam.Linechart.prototype);
evam.Areachart.prototype.initialize = function() { 
	evam.Linechart.prototype.initialize.call(this);
	var areaChart 			= this;
	this.area 				= d3.svg.area().x(function(d) { return areaChart.x(d.x); })
    									   .y0(areaChart.height)
    									   .y1(function(d) { return areaChart.y(d.y); });
	if(areaChart.hasBrush) {
		this.area2 			= d3.svg.area().x(function(d) { return areaChart.x2(d.x); })
										   .y0(areaChart.height2)
										   .y1(function(d) { return areaChart.y2(d.y); });
	}
};
evam.Areachart.prototype.draw = function(data) { 	
	evam.Linechart.prototype.draw.call(this, data);
	var areaChart 			= this;
	// upper graph - area
	areaChart.focus.insert("path", "path.line").datum(data).attr("class", "area").attr("clip-path", "url(#clip_" + areaChart.key + ")").attr("d", areaChart.area);
	if(areaChart.hasBrush) {
		// lower graph - area	
		areaChart.context.insert("path", "path.line").datum(data).attr("class", "area").attr("clip-path", "url(#clip_" + areaChart.key + ")").attr("d", areaChart.area2);
	}
};
