
$(document).ready(function () {

    var baseRequestUrl = '/api/assignments';
    var baseVideoUrl = '/api/video?id=';
    var assignments = [];
    var currentAssignment;

    getAssignments();

    $('video').on('ended',function() {
        console.log('ender');
        recordHistory(currentAssignment);
        playNextVideo(true);
    });

    function recordHistory(assignment) {
        $.post("/api/history/add", {'assignmentId': assignment.id}, function() {
            // success
        })
    }

    function playNextVideo(autoPlay) {
        if (assignments.length <= 0) {
            showAllCompleteView();
            return;
        }

        currentAssignment = assignments.shift();
        var videoPlayer = $('video');

        videoPlayer.find('source').attr('src', currentAssignment.exercise.mediaUrl + baseVideoUrl + currentAssignment.exercise.id);
        videoPlayer[0].load();

        if (autoPlay) {
            videoPlayer[0].play();
        }

        $('#video-player-title').html(currentAssignment.exercise.title);
        $('#video-player-description').html(currentAssignment.exercise.description);

        updateAssignments(assignments);
    }

    function getAssignments() {
        $.ajax({
            url: baseRequestUrl,
            method: 'GET',
            cache: false,
            success: function(data) {
                updateAssignments(data);
                assignments = data;
                playNextVideo(false);
            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
            }
        });
    }

    function updateAssignments(assignments) {
        var entryContainer = $('#assignment-entry-container');
        entryContainer.empty();

        var template = $('#assignment-entry-template');

        assignments.forEach(function(assignment) {
            var element = template.clone();
            element.removeClass('hidden');

            var thumbHref = assignment.exercise.mediaUrl + '/api/thumbnail?id=' + assignment.exercise.id;
            element.find('#assignment-entry-img').attr('src', thumbHref);

            element.find('#assignment-entry-title').html(assignment.exercise.title);

            entryContainer.append(element);
        });
    }

    $('#submit-feedback-button').click(function() {
        var feedback = $('#feedback-input').val();

        var data = { feedback: feedback, exerciseId: currentAssignment.exercise.id };

        $.ajax({
            url: '/api/history/feedbackAdd',
            method: 'POST',
            data: data,
            success: function() {

            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
            }
        });
    });

    function showAllCompleteView() {
        $('.assignments-content-container').hide();
        $('#assign-complete-div').show();
    }
});
