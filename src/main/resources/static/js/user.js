
$(document).ready(function () {

    var userId = getParameterByName('id');
    var baseAssignmentsUrl = '/api/assignments/user?id=' + userId;
    var baseFeedbackUrl = '/api/history/feedback?patient_id='+userId;
    var baseHistoryUrl = '/api/history/specificpatient?id='+userId;

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

    function updateFeedback(feedback){
        var feedbackContainer = $('#issue-table-body');
        var template = $('#issue-entry-template');
        feedback.forEach(function (feedback){
            var element = template.clone();
            element.removeClass('hidden');
            element.find('#issue-exercise-name').html(feedback.assignment.exercise.title);
            element.find('#issue-text-body').html(feedback.comments);
            feedbackContainer.append(element);
        })
    }

    function updateHistory(history) {
        console.log(history);
        var weekday = new Array(7);
        weekday[0] = "Sunday";
        weekday[1] = "Monday";
        weekday[2] = "Tuesday";
        weekday[3] = "Wednesday";
        weekday[4] = "Thursday";
        weekday[5] = "Friday";
        weekday[6] = "Saturday";
        var entryContainer = $('#user-history-body');
        entryContainer.empty();
        var template = $('#user-history-template');
        var users = "";
        var today = new Date();
        today.setHours(0, 0, 0, 0);
        var weekAgo = new Date(today.getTime() - 6 * 24 * 60 * 60 * 1000);
        var arr = [];
        history.forEach(function (history) {
            arr.push(history.patientId.id);
        });
        var users = [];
        $.each(arr, function (i, el) {
            if ($.inArray(el, users) === -1) users.push(el);
        });
        for (var i = 0; i < users.length; i++) {
            var element = template.clone();
            element.removeClass('hidden');
            var date = weekAgo;
            var day = 0;
            var total = 0;
            history.forEach(function (history) {
                console.log("Week ago: " + weekAgo);
                if (history.patientId.id == users[i]) {
                    var exerciseDate = new Date(history.timeStart);
                    exerciseDate = new Date(exerciseDate.setHours(0, 0, 0, 0));
                    while (date < exerciseDate) {
                        element.find('#user-history-day' + day).html("<i class=\"far fa-square\"></i><br>" + weekday[date.getDay()]);
                        date = new Date(date.getTime() + 1 * 24 * 60 * 60 * 1000);
                        day++;
                    }
                    console.log(date.getTime());
                    console.log(exerciseDate.getTime());
                    if (date.getTime() == exerciseDate.getTime()) {
                        if (history.completed) {
                            element.find('#user-history-day' + day).html("<i class=\"far fa-check-square\"></i><br>" + weekday[date.getDay()]);
                            total++;
                        }
                        else {
                            element.find('#user-history-day' + day).html("<i class=\"far fa-square\"></i><br>" + weekday[date.getDay()]);
                        }
                        day++;
                        date = new Date(date.getTime() + 1 * 24 * 60 * 60 * 1000);
                    }
                }
            });
            entryContainer.append(element);
        }
    }
});