
$(document).ready(function () {

    var userId = getParameterByName('id');
    var baseAssignmentsUrl = '/api/assignments/user?id=' + userId;
    var baseFeedbackUrl = '/api/history/feedback?patient_id=' + userId;
    var baseHistoryUrl = '/api/history/specificpatient?id=' + userId;

    var daysOfTheWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

    getAssignments(baseAssignmentsUrl);
    getFeedback(baseFeedbackUrl);
    getHistory(baseHistoryUrl);

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

    function getFeedback(requestUrl){
        $.ajax({
            url: requestUrl,
            method: 'GET',
            cache: false,
            success: function(data){
                updateFeedback(data);
            },
            error: function(){

            }
        })
    }

    function getHistory(requestUrl){
        $.ajax({
            url: requestUrl,
            method: 'GET',
            cache: false,
            success: function(data){
                updateHistory(data);
            },
            error: function(){

            }
        })
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

    function updateFeedback(feedback) {
        var feedbackContainer = $('#issue-table-body');
        var template = $('#issue-entry-template');
        feedback.forEach(function (feedback){
            var element = template.clone();
            element.removeClass('hidden');
            element.find('#issue-exercise-name').html(feedback.exercise.title);
            element.find('#issue-text-body').html(feedback.comments);
            feedbackContainer.append(element);
        })
    }

    function updateHistory(report) {
        var tuples = [];

        Object.keys(report.history).forEach(function(date) {
            tuples.push({ date: new Date(date + ' 00:00'), complete: report.history[date] });
        });

        // Make sure they're sorted correctly
        tuples.sort(function (a, b) {
            return a.date - b.date;
        });

        var entryContainer = $('#user-history-body');
        var completions = 0;

        tuples.forEach(function(tuple, index) {
            var element = entryContainer.find('#user-history-day' + index);

            if (tuple.complete) {
                element.html('<i class=\"far fa-check-square\"></i><br>' + daysOfTheWeek[tuple.date.getDay()]);
                completions++;
            } else {
                element.html('<i class=\"far fa-square\"></i><br>' + daysOfTheWeek[tuple.date.getDay()]);
            }
        });
    }

    $('form').submit(function(event) {
        event.preventDefault();

        var formData = $('form').serialize();

        var successMessage = $('#success-message');
        successMessage.html('').hide();

        var button = $('#deactivate');
        button.html('').hide();

        $.ajax({
            url: '/api/deactivate?id='+userId,
            method: 'POST',
            cache: false,
            success: function (data) {
                successMessage.show().html('User Deactivated Successfully!');
                button.classList.add("disabled");
            },
            error: function () {

            }
        })
    });
});