# Report Dashboard

Simple visualization dashboard over data connectors (database, constant data, rest api, etc.)
You can add line charts, bar charts, pie charts and tables over data store and design layouts via simple configuration.

Connfiguration is simple by creating a layout and data source xml.
Example:

```xml
<?xml version="1.0" encoding="utf-8" ?>
<dashboard>
	
	<tab>
		<key>bar_charts</key>
		<title>Bar Charts</title>
	
		<portlet>
			<key>scenario_values_bar</key>
			<title>Bar Chart String vs Number</title>
			<type>barchart</type>
			<width>50</width>			
			<x-value-type>string</x-value-type>			
			<y-value-type>number</y-value-type> 
			<brush>true</brush>
			<data-source></data-source>
		</portlet>

		<portlet>
			<key>scenario_values_groupbar1</key>
			<title>Group Bar Chart String vs Number</title>
			<type>groupbarchart</type>
			<width>50</width>			
			<x-value-type>string</x-value-type>			
			<y-value-type>number</y-value-type> 
			<brush>true</brush>
			<data-source></data-source>
		</portlet>

	</tab>

	<tab>
		<key>scenario_view2</key>
		<title>Circular Charts</title>
		
		<portlet>
			<key>scenario_values_piechart2</key>
			<title>Pie DB</title>
			<type>piechart</type>
			<width>33</width>
			<data-source>
				<type>database</type><
				<query>select to_char(log_time, 'DD.MM.YYYY'), count(*) from (select * from int_log_table order by id asc limit 500000 ) as q group by to_char(log_time, 'DD.MM.YYYY')</query>
			</data-source>
		</portlet>

		<portlet>
			<key>scenario_values_donut2</key>
			<title>Donut DB</title>
			<type>donutchart</type>
			<width>50</width>
			<data-source>
				<type>database</type><!-- Olası değerler: database, constant -->
				<query>select to_char(log_time, 'DD.MM.YYYY'), count(*) from (select * from int_log_table order by id asc limit 500000 ) as q group by to_char(log_time, 'DD.MM.YYYY')</query>
			</data-source>
		</portlet>

	</tab>
	
	<tab>
		<key>scenario_view3</key>
		<title>Interaction</title>
		
		<portlet>
			<key>scenario_name_select_box</key>
			<title>Campaign Names</title>
			<type>selectbox</type>
			<width>50</width>
			<data-source>
				<type>database</type>
				<query>select scenario_name as key, scenario_name as val from int_scenario_def</query>
			</data-source>
			<interactions>
				<interaction>
					<tab-key>scenario_view3</tab-key>
					<portlet-key>scenario_values_table_short</portlet-key>
					<variable>scenario_def</variable>
					<trigger>true</trigger>
				</interaction>
				<interaction>
					<tab-key>scenario_view3</tab-key>
					<portlet-key>scenario_values_piechart99</portlet-key>
					<variable>scenario_def</variable>
					<trigger>true</trigger>
				</interaction>
				<interaction>
					<tab-key>scenario_view3</tab-key>
					<portlet-key>scenario_values_donutchart99</portlet-key>
					<variable>scenario_def</variable>
					<trigger>true</trigger>
				</interaction>
				<interaction>
					<tab-key>scenario_view3</tab-key>
					<portlet-key>scenario_values_multiseries_99</portlet-key>
					<variable>scenario_def</variable>
					<trigger>true</trigger>
				</interaction>
			</interactions>			
		</portlet>
		
		<portlet>
			<key>scenario_name_select_box2</key>
			<title>Campaign Fields</title>
			<type>selectbox</type>
			<width>50</width>
			<data-source>
				<type>constant</type>
				<values>
					<delimiter>,</delimiter>
					<value>iom_key,Key</value>
					<value>offer_cycle_count,Count</value>
					<value>last_state,State</value>					
				</values>
			</data-source>
			<interactions>
				<interaction>
					<tab-key>scenario_view3</tab-key>
					<portlet-key>scenario_values_table_short</portlet-key>
					<variable>order_field</variable>
					<trigger>true</trigger>
				</interaction>
			</interactions>
		</portlet>
		
		<portlet>
			<key>scenario_values_table_short</key>
			<title>Campaigns Table</title>
			<type>datatable</type>
			<width>100</width>
			<autoStart>false</autoStart>
			<data-source>
				<type>database</type><!-- Olası değerler: database, constant -->
				<query>select * from ${scenario_def}_table order by ${order_field} limit 100</query>
			</data-source>
		</portlet>
		
		<portlet>
			<key>scenario_values_piechart99</key>
			<title>Campaigns PieChart</title>
			<type>piechart</type>
			<width>50</width>
			<autoStart>false</autoStart>
			<data-source>
				<type>database</type><!-- Olası değerler: database, constant -->
				<query>select category, count(*) from ${scenario_def}_table group by category</query>
			</data-source>
		</portlet>
		
		<portlet>
			<key>scenario_values_donutchart99</key>
			<title>Campaigns DonuChart</title>
			<type>donutchart</type>
			<width>50</width>
			<autoStart>false</autoStart>
			<data-source>
				<type>database</type><!-- Olası değerler: database, constant -->
				<query>select country, count(*) from ${scenario_def}_table group by country</query>
			</data-source>
		</portlet>
		
		<portlet>
			<key>scenario_values_multiseries_99</key>
			<title>Multi Series Line Chart DB</title>
			<type>multiserieslinechart</type>
			<width>75</width>		
			<autoStart>false</autoStart>	
			<x-value-type>string</x-value-type>	
			<y-value-type>number</y-value-type> 
			<brush>true</brush>
			<interpolate>cardinal</interpolate>
			<data-source>
				<type>database</type>
				<query>select category, count(*), country from ${scenario_def}_table group by country, category order by country, category</query>
			</data-source>
		</portlet>
	</tab>
</dashboard>

```
