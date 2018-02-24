
$(document).ready(function () {

    var userId = getParameterByName('id');
    var baseAssignmentsUrl = '/api/assignments/user?id=' + userId;

    getAssignments(baseAssignmentsUrl);

    searchExercises('');

    var timerid;
    $('#user-exercise-search-input').on('input', function() {
        var query = $(this).val();
        if($(this).data("lastval") != query) {

            $(this).data("lastval", query);
            clearTimeout(timerid);

            // Only request results after user stops typing
            // Default is 400ms after last edit
            timerid = setTimeout(function() {
                searchExercises(query);
            }, 400);
        }
    });

    function getParameterByName(name) {
        var url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }

    function searchExercises(query) {
        $.ajax({
            url: '/api/videos?q=' + query,
            method: 'GET',
            cache: false,
            success: function(data) {
                updateExercisesList(data, false);
            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
            }
        });
    }

    function updateExercisesList(exercises) {
        var resultsContainer = $('#user-exercise-search-container');
        resultsContainer.empty();

        var template = $('#user-exercise-search-entry-template');

        exercises.forEach(function (exercise) {
            var element = template.clone();
            element.removeClass('hidden');

            var thumbHref = exercise.mediaUrl + '/api/thumbnail?id=' + exercise.id;
            element.find('#user-exercise-result-img').attr('src', thumbHref);

            element.find('#user-exercise-result-name').html(exercise.title).attr('href', '/watch?v=' + exercise.id);

            element.find('#user-assign-exercise-button').click(function() {

                $.ajax({
                    url: '/api/assignments/add',
                    method: 'POST',
                    data: {
                        'userId': userId,
                        'exerciseId': exercise.id
                    },
                    success: function(data) {
                        updateAssignmentsList([data]);
                    },
                    error: function () {
                        // Need to actually notify the user at some point, maybe redirect to an error page?
                    }
                });
            });

            resultsContainer.append(element);
        });
    }

    function getAssignments(requestUrl) {
        $.ajax({
            url: requestUrl,
            method: 'GET',
            cache: false,
            success: function(data) {
                updateAssignmentsList(data, true);
            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
            }
        });
    }

    function updateAssignmentsList(assignments, shouldClear) {
        var assignmentsContainer = $('#user-exercises-list');

        if (shouldClear) {
            assignmentsContainer.empty();
        }

        var template = $('#user-exercise-entry-template');
        
        assignments.forEach(function (assignment) {
            var element = template.clone();
            element.removeClass('hidden');

            var thumbHref = assignment.exercise.mediaUrl + '/api/thumbnail?id=' + assignment.exercise.id;
            element.find('#user-exercise-img').attr('src', thumbHref);

            element.find('#user-exercise-name').html(assignment.exercise.title).attr('href', '/watch?v=' + assignment.exercise.id);

            element.find('#unassign-exercise-button').click(function() {
                $.ajax({
                    url: '/api/assignments/remove?id=' + assignment.id,
                    method: 'DELETE',
                    success: function() {
                        element.remove();
                    },
                    error: function () {
                        // Need to actually notify the user at some point, maybe redirect to an error page?
                    }
                });
            });

            assignmentsContainer.append(element);
        })
    }
});