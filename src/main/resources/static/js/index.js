var app = angular.module('carTrackerWebApp', [ 'uiSlider', 'ui.bootstrap' ]);

app.service('dataService', function($http) {
	this.getCarDetails = function() {
		return $http.get('/vehicles');
	};
});

app.controller('carTrackerCtrl',
		function($scope, dataService, $http, $timeout) {

			init();

			function init() {
				dataService.getCarDetails().then(function(response) {
					$scope.vehicles = response.data;
					console.log(response)
				});
			}
			$scope.hours=[];
			var sum=0;
			for(var i=0;i<2*24;i++){
				sum=sum+0.5;
				$scope.hours.push(sum)
			}
			//$scope.hours = [ 0.15,0.30,0.45,1, 1.15,1.30,1.45,2, 3, 4, 5,10 ];
			$scope.data = [];
			$scope.viewby = 10;
			//$scope.totalItems;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.viewby;
			$scope.maxSize = 5; // Number of pager buttons
								// to show

			/*
			 * $scope.propertyName = 'highAlert'; $scope.reverse = true;
			 * $scope.sortBy = function(propertyName) { $scope.reverse =
			 * ($scope.propertyName === propertyName) ? !$scope.reverse : false;
			 * $scope.propertyName = propertyName; };
			 */

			$scope.getHistoricalAlerts = function() {
				$http.get('/alerts/' + $scope.selectedVehicle).then(
						function(response) {
							$scope.data = response.data;
							$scope.totalItems = $scope.data.length;
							console.log($scope.data)
						});
			}

			$scope.getFuelVolumeData = function() {
				var node1 = document.getElementById("morris-bar-chart1");
				node1.innerHTML = '';
				var node2 = document.getElementById("morris-bar-chart2");
				node2.innerHTML = '';
				var node3 = document.getElementById("morris-area-chart");
				node3.innerHTML = '';
				$http.get(
						'/readings/' + $scope.selectedhours + '/'
								+ $scope.selectedVehicle_fuelVolume).then(
						function(response) {
							$scope.readings = response.data;
							console.log(response)

							var data1 = [];
							var data2 = [];
							var data3 = [];
							for (var i = 0; i < $scope.readings.length; i++) {
								var date = new Date(
										$scope.readings[i].timestamp);
								var hours = date.getHours();
								// Minutes part from the timestamp
								var minutes = "0" + date.getMinutes();
								// Seconds part from the timestamp
								var seconds = "0" + date.getSeconds();

								// Will display time in 10:30:23 format
								var formattedTime = hours + ':'
										+ minutes.substr(-2) + ':'
										+ seconds.substr(-2);
								var datevalues = date.getDate() + '/'
										+ (date.getMonth() + 1) + '/'
										+ date.getFullYear() + formattedTime;
								data1.push({
									"y1" : $scope.readings[i].fuelVolume,
									"x" : datevalues
								});
								data2.push({
									"y2" : $scope.readings[i].engineRpm,
									"x" : datevalues
								});
								data3.push({
									"engineRpm" : $scope.readings[i].engineRpm,
									"engineHp" : $scope.readings[i].engineHp,
									"fuelVolume" : $scope.readings[i].fuelVolume,
									"speed" : $scope.readings[i].speed,
									"x" : datevalues
								});
							}
							Morris.Bar({
								element : 'morris-bar-chart1',
								data : data1,
								xkey : 'x',
								ykeys : [ 'y1' ],
								labels : [ 'Fuel Volume' ],
								hideHover : 'auto',
								resize : false
							});
							Morris.Bar({
								element : 'morris-bar-chart2',
								data : data2,
								xkey : 'x',
								ykeys : [ 'y2' ],
								labels : [ 'Engine RPM' ],
								hideHover : 'auto',
								resize : false
							});
							

						    Morris.Area({
						        element: 'morris-area-chart',
						        data: data3,
						        xkey: 'x',
						        ykeys: ['engineRpm', 'engineHp', 'fuelVolume','speed'],
						        labels: ['engineRpm', 'engineHp', 'fuelVolume','speed'],
						        pointSize: 2,
						        hideHover: 'auto',
						        resize: true
						    });
						
						});
			}
			
			$scope.setPage = function(pageNo) {
				$scope.currentPage = pageNo;
			};

			$scope.pageChanged = function() {
				console.log('Page changed to: '
						+ $scope.currentPage);
			};

			$scope.setItemsPerPage = function(num) {
				$scope.itemsPerPage = num;
				$scope.currentPage = 1; //reset to first paghe
			}

		});

app.directive(
				"sort",
				function() {
					return {
						restrict : 'A',
						transclude : true,
						template : '<a ng-click="onClick()">'
								+ '<span ng-transclude></span>'
								+ '<i class="glyphicon" ng-class="{\'glyphicon-sort-by-order\' : order === by && !reverse,  \'glyphicon-sort-by-order-alt\' : order===by && reverse}"></i>'
								+ '</a>',
						scope : {
							order : '=',
							by : '=',
							reverse : '='
						},
						link : function(scope, element, attrs) {
							scope.onClick = function() {
								if (scope.order === scope.by) {
									scope.reverse = !scope.reverse
								} else {
									scope.by = scope.order;
									scope.reverse = false;
								}
							}
						}
					}
				});
