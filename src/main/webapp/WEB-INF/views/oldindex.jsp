<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<!DOCTYPE html>
<!--[if lt IE 7]> <html class="ie lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>    <html class="ie lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>    <html class="ie lt-ie9"> <![endif]-->
<!--[if gt IE 8]> <html class="ie gt-ie8"> <![endif]-->
<!--[if !IE]><!--><html><!-- <![endif]-->
<head>
	<title>Evam Admin Panel</title>
	
	<!-- Meta -->
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE" />
	
	<!-- Bootstrap -->
	<link href="/static/js/bootstrap/css/bootstrap.css" rel="stylesheet" />
	<link href="/static/js/bootstrap/css/responsive.css" rel="stylesheet" />
	
	<!-- Glyphicons Font Icons -->
	<link href="/static/js/theme/css/glyphicons.css" rel="stylesheet" />
	
	<!-- Uniform Pretty Checkboxes -->
	<link href="/static/js/theme/scripts/plugins/forms/pixelmatrix-uniform/css/uniform.default.css" rel="stylesheet" />
	
	<!-- PrettyPhoto -->
    <link href="/static/js/theme/scripts/plugins/gallery/prettyphoto/css/prettyPhoto.css" rel="stylesheet" />
	
	<!--[if IE]><!--><script src="/static/js/theme/scripts/plugins/other/excanvas/excanvas.js"></script><!--<![endif]-->
	<!--[if lt IE 8]><script src="../../../../common/theme/scripts/plugins/other/json2.js"></script><![endif]-->
	
	<!-- Bootstrap Extended -->
	<link href="/static/js/bootstrap/extend/jasny-bootstrap/css/jasny-bootstrap.min.css" rel="stylesheet">
	<link href="/static/js/bootstrap/extend/jasny-bootstrap/css/jasny-bootstrap-responsive.min.css" rel="stylesheet">
	<link href="/static/js/bootstrap/extend/bootstrap-wysihtml5/css/bootstrap-wysihtml5-0.0.2.css" rel="stylesheet">
	<link href="/static/js/bootstrap/extend/bootstrap-select/bootstrap-select.css" rel="stylesheet" />
	<link href="/static/js/bootstrap/extend/bootstrap-toggle-buttons/static/stylesheets/bootstrap-toggle-buttons.css" rel="stylesheet" />
	
	<!-- Bootstrap -->
	<script src="/static/js/bootstrap/js/bootstrap.min.js"></script>
	
	<!-- Global -->
	<script>
	var basePath = 'common/';
	</script>

	<!-- Bootstrap Extended -->
	<script src="/static/js/bootstrap/extend/bootstrap-select/bootstrap-select.js"></script>
	<script src="/static/js/bootstrap/extend/bootstrap-toggle-buttons/static/js/jquery.toggle.buttons.js"></script>
	<script src="/static/js/bootstrap/extend/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"></script>
	<script src="/static/js/bootstrap/extend/jasny-bootstrap/js/jasny-bootstrap.min.js"></script>
	<script src="/static/js/bootstrap/extend/jasny-bootstrap/js/bootstrap-fileupload.js"></script>
	<script src="/static/js/bootstrap/extend/bootbox.js"></script>
	<script src="/static/js/bootstrap/extend/bootstrap-wysihtml5/js/wysihtml5-0.3.0_rc2.min.js"></script>
	<script src="/static/js/bootstrap/extend/bootstrap-wysihtml5/js/bootstrap-wysihtml5-0.0.2.js"></script>

	<!-- Cookie Plugin -->
	<script src="/static/js/theme/scripts/plugins/system/jquery.cookie.js"></script>
	
	<!-- Colors -->
	<script>
	var primaryColor = '#e25f39',
		dangerColor = '#bd362f',
		successColor = '#609450',
		warningColor = '#ab7a4b',
		inverseColor = '#45484d';
	</script>
	
	<!-- Themer -->
	<script>
	var themerPrimaryColor = primaryColor;
	</script>
		
	<script language="javascript" type="text/javascript" src="/static/js/flot/jquery.js"></script>
	<script language="javascript" type="text/javascript" src="/static/js/flot/jquery.flot.js"></script>
	<script language="javascript" type="text/javascript" src="/static/js/flot/jquery.flot.pie.js"></script>
	<script language="javascript" type="text/javascript" src="/static/js/flot/jquery.flot.time.js"></script>
	
   	<script src="/static/js/evam.visual.library.js"></script>
</head>
<body class="">
	
		<!-- Main Container Fluid -->
	<div class="container-fluid fluid menu-left">
		
		<!-- Top navbar -->
		<div class="navbar main hidden-print">
		
			<!-- Brand -->
			<a href="index.html?lang=en&amp;layout_type=fluid&amp;menu_position=menu-left&amp;style=style-light" class="appbrand pull-left"><span>Evam Panel<span></span></span></a>
			
						<!-- Menu Toggle Button -->
			<button type="button" class="btn btn-navbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<ul class="topnav pull-left tn1">
			</ul>
			<ul class="topnav pull-right">
			</ul>
						
		</div>
		<div id="wrapper">
		
		<!-- Sidebar Menu -->
		<div id="menu" class="hidden-phone hidden-print">
		
			<!-- Scrollable menu wrapper with Maximum height -->
			<div class="slim-scroll" data-scroll-height="800px">
			<ul>
			
								
								<!-- Menu Regular Item -->
				<li class="glyphicons display active"><a href="index.html?lang=en&amp;layout_type=fluid&amp;menu_position=menu-left&amp;style=style-light"><i></i><span>Dashboard</span></a></li>
			</ul>
			<div class="clearfix"></div>
			<div class="separator bottom"></div>
			</div>
			
		</div>
				
		<!-- Content -->
		<div id="content">
	<!-- Filters -->
<div class="filter-bar dark margin-bottom-none border-none">
	<form>
		
	</form>
</div>
<!-- // Filters END -->

<h3 class="heading-mosaic">Evam Charts</h3>
<div class="innerLR">
        <div class="widget widget-tabs widget-quick hidden-print">
        <div class="widget-head">
                <ul id="evamhead">
                </ul>
        </div>

        <div class="widget-body">
                <div class="tab-content" id="evambody">
        </div>
        </div>



</div>




	

</body>
</html>
