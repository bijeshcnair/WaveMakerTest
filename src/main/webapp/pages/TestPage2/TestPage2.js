Application.$controller("TestPage2PageController", ["$scope", function($scope) {
    "use strict";

    /* perform any action on widgets/variables within this block */
    $scope.onPageReady = function() {
        /*
         * variables can be accessed through '$scope.Variables' property here
         * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
         * $scope.Variables.loggedInUser.getData()
         *
         * widgets can be accessed through '$scope.Widgets' property here
         * e.g. to get value of text widget named 'username' use following script
         * '$scope.Widgets.username.datavalue'
         */
    };


    $scope.HoursAccOnt1LoginDataonBeforeUpdate = function(variable, inputData) {



    };


    $scope.LoginFormonBeforeUpdate = function(variable, inputData) {

    };


    $scope.HoursAccOnt1EditLoginonBeforeUpdate = function(variable, inputData) {

        alert("set");
    };


    $scope.readLoginonBeforeUpdate = function(variable, inputData) {
        inputData.ID = {
            'value': $scope.Widgets.filterText.datavalue,
            'filterCondition': 'LESS_THAN_OR_EQUALS',
            'type': 'INTEGER'
        }
    };

}]);

Application.$controller("liveform2Controller", ["$scope",
	function($scope) {
		"use strict";
		$scope.ctrlScope = $scope;
	}
]);