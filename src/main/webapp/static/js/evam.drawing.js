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
evam.pieChartInnerTextAngle = function(angle, offset, threshold) {
    var a = angle * 180 / Math.PI + offset;
    return a > threshold ? a - 180 : a;
};
evam.pieChartOuterTextAngle = function(angle, offset) {
	var a = angle * 180 / Math.PI + offset;
	return 	0 < a && a < 180 ? a - 90 : 180 < a && a < 270 ? a + 90 : -90 < a && a < 0 ? a + 90 : a;
};
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
    this.parentObject.children('svg').attr("width", this.parentObject.width()).attr("height", this.parentObject.height());	
};

// Text Box
evam.Textbox = function(options){ 
	evam.Graph.call(this, options);	
	this.textBox 			= this.parentObject.find('input[type="text"]');
};
evam.Textbox.prototype = Object.create(evam.Graph.prototype);
evam.Textbox.prototype.prepareData = function(data) {
	this.textBox.typeahead({
		items: 5,
		minLength: 1,
		source: data.map(function(d) { return d.x; })
	});
};

// Select Box
evam.Selectbox = function(options){ 
	evam.Graph.call(this, options);	
	this.selectBox 			= this.parentObject.find('select');
};
evam.Selectbox.prototype = Object.create(evam.Graph.prototype);
evam.Selectbox.prototype.prepareData = function(data) {
	var selectObj 			= this;
	var previousValue		= selectObj.selectBox.val();
	selectObj.selectBox.find('option.generated').remove();
	$.each(data, function(i, val) {
		$("<option class='generated' value='" + val.x + "'>" + val.y + "</option>").appendTo(selectObj.selectBox);
	});
	selectObj.selectBox.find("option[value=" + previousValue + "]").prop("selected", true);
};

// Data Table
evam.Datatable = function(options){ 
	evam.Graph.call(this, options);	
};
evam.Datatable.prototype = Object.create(evam.Graph.prototype);
evam.Datatable.prototype.prepareData = function(data) {
	var tableObject 		= this.parentObject;	
	tableObject.find('table tbody > tr').remove();
	$.each(data, function(x, row) {
		var newRow = $("<tr/>");
		$.each(row.data, function(y, col) {
			$("<td>" + col + "</td>").appendTo(newRow);
		});
		newRow.appendTo(tableObject.find('table tbody'));
	});	
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

//Pie Chart
evam.Piechart = function(options) {
	evam.Graph.call(this, options);
	this.outerHeight		= options.height || Math.min(this.outerWidth, 500);
	this.margin 			= {top: 50, right: 50, bottom: 10, left: 10};
	this.width 				= this.outerWidth - this.margin.left - this.margin.right;
    this.height 			= this.outerHeight - this.margin.top - this.margin.bottom;  
	this.radius 			= Math.min(this.width, this.height) / 2;
	this.innerRadius		= 0;
	this.c1					= 0.1;
	this.c2					= 2.0;
	this.c3					= 1;
};
evam.Piechart.prototype = Object.create(evam.Graph.prototype);
evam.Piechart.prototype.initialize = function() {
	evam.Graph.prototype.initialize.call(this);
	this.color 				= d3.scale.category20c();
	this.arc 				= d3.svg.arc().outerRadius(this.radius).innerRadius(this.innerRadius);
	this.pie 				= d3.layout.pie().sort(null).value(function(d) {
		return d.y;
	});
	this.svg 				= d3.select(this.parentObject[0]).append("svg").attr("width", this.outerWidth).attr("height", this.outerHeight)
								.attr("viewBox", "0 0 " + this.outerWidth + " " + this.outerHeight).attr("preserveAspectRatio", "xMidYMid");
	this.circle				= this.svg.append("g").attr("transform", "translate(" + parseFloat(this.margin.left + (this.width / 2.0)) + "," + parseFloat(this.margin.top + (this.height / 2.0)) + ")");
};
evam.Piechart.prototype.prepareData = function(data) {
	// parse data
	data.forEach(function(d) {
		d.x = evam.selectParseFormat("string")(d.x);
	    d.y = evam.selectParseFormat("number")(d.y);
	});	
};
evam.Piechart.prototype.draw = function(data) {
	var pieChart = this;
	// create legend	
	var g = pieChart.circle.selectAll(".arc").data(pieChart.pie(data)).enter().append("g").attr("class", "arc");
	g.on("mouseover", function(d) {
		var c = pieChart.arc.centroid(d);		
		pieChart.circle.selectAll(".arc").attr("transform", "translate(0,0)");
		pieChart.circle.selectAll(".arc").filter(function(x) { return x.data.x == d.data.x; })
				.attr("transform", "translate(" + c[0] * pieChart.c1 + "," + c[1] * pieChart.c1 + ")");		
		focus.style("display", null).attr("transform", "translate(" + c[0] * pieChart.c2 + "," + c[1] * pieChart.c2 + ")");		
	    focus.select("text").text(d.data.y).style("text-anchor", c[0] > 0 ? "end" : "start");
	});
	g.on("mouseout", function(d) {
		focus.style("display", "none");
		pieChart.circle.selectAll(".arc").attr("transform", "translate(0,0)");
	});
	g.append("path").attr("d", pieChart.arc).attr("data-legend", function(d) { return d.data.x; }).style("fill", function(d) { return pieChart.color(d.data.x); });
	g.append("text").attr("class", "pieChartArc")
				    .text(function(d) { return d.data.x; }).attr("transform", function(d) {
		var c = pieChart.arc.centroid(d);
		var rotateAngle = evam.pieChartInnerTextAngle((d.startAngle + d.endAngle) / 2, -90, 90);
		var translate = parseFloat(c[0] * pieChart.c3) + "," + parseFloat(c[1] * pieChart.c3);
		return "translate(" + translate + ") rotate(" + rotateAngle + ")";
	});			
	var focus = pieChart.circle.append("g").attr("class", "focus").style("display", "none");				
	focus.append("text");	
	var legend = pieChart.svg.append("g").attr("class","legend").style("font-size", "12px").call(d3.legend);
	legend.attr("transform","translate(" + (pieChart.outerWidth - legend.select("rect.legend-box").attr('width')) + "," + 20 + ")");
};

// Donut
evam.Donutchart = function(options) {
	evam.Piechart.call(this, options);
	this.c1					= 0.06;
	this.c2					= 1.4;
	this.innerRadius		= this.radius / 2;
};
evam.Donutchart.prototype = Object.create(evam.Piechart.prototype);


// initializer functions
evam.initializePortlet = function(object) {
	try {
		// common options
		var options = {
			key: object.data('key'), 
			title: object.data('title'), 
			refreshInterval: object.data('refresh-interval'),
			requestUrl: object.data('request-url')
		};
		
		// type specific options
		if(object.hasClass('linechart') ||
		   object.hasClass('areachart') ||
		   object.hasClass('multiserieslinechart'))
		{
			options.xAxis = object.data('x-axis');
	   	   	options.yAxis = object.data('y-axis');
	   	   	options.xAxisType = object.data('x-axis-type'); 
	   	   	options.yAxisType = object.data('y-axis-type');
	   	   	options.xAxisFormat = object.data('x-axis-format'); 
	   	   	options.yAxisFormat = object.data('y-axis-format');
	   	   	options.brush = object.data('brush');
	   	   	options.interpolate = object.data('interpolate');
		};
		
		// creation type
		var creationFunction = null;
		switch(object.data('type')) {
		case 'textbox': creationFunction = evam.Textbox; break;
		case 'selectbox': creationFunction = evam.Selectbox; break;
		case 'datatable': creationFunction = evam.Datatable; break;
		case 'linechart': creationFunction = evam.Linechart; break; 
		case 'areachart': creationFunction = evam.Areachart; break;
		case 'multiserieslinechart': creationFunction = evam.MultiSeriesLinechart; break;
		case 'piechart': creationFunction = evam.Piechart; break;
		case 'donutchart': creationFunction = evam.Donutchart; break;	
		}
		
		// create
		var chart = new creationFunction(options);
		chart.initialize();
		chart.update();	
		object.bind('evam.resize', function() { chart.resize(); });
		object.parents(".panel").find('.btn.refresh-graph').click(function() { chart.update(); });
	}
	catch(err) {
		$(this).html("Cannot create portlet.\n --- Details --- \n" +
				     err.message + "\n --- Trace ---\n" +
					 printStackTrace().join("\n"));
	}
};