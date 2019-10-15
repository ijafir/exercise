'use strict';

angular.module('myApp').factory('PersonService', ['$http', '$q', function ($http, $q) {

    var REST_SERVICE_URI = 'http://localhost:8443/api/v1/persons/';

    var factory = {
        fetchAllPersons: fetchAllPersons,
        createPerson: createPerson,
        updatePerson: updatePerson,
        deletePerson: deletePerson
    };

    return factory;

    function fetchAllPersons() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI)
            .then(
                function (response) {
                    console.log(response.data);
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while fetching Persons');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function createPerson(person) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI, person)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    alert('Error while creating Person');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function updatePerson(person, id) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI + id, person)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    alert('Error while updating Person');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deletePerson(id) {
        var deferred = $q.defer();
        $http.delete(REST_SERVICE_URI + id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while deleting Person');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

}]);