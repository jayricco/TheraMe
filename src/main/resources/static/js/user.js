
$(document).ready(function () {

    var userId = getParameterByName('id');
    var baseAssignmentsUrl = '/api/assignments/user?id=' + userId;

    getAssignments(baseAssignmentsUrl);

    function getParameterByName(name) {
        var url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }

    function getAssignments(requestUrl) {
        $.ajax({
            url: requestUrl,
            method: 'GET',
            cache: false,
            success: function(data) {
                updateAssignmentsList(data);
            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
            }
        });
    }

    function updateAssignmentsList(assignments) {
        var assignmentsContainer = $('#user-exercises-list');
        assignmentsContainer.empty();

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
                    cache: false,
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