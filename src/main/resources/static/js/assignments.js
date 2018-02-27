
$(document).ready(function () {

    var baseRequestUrl = '/api/assignments';
    var baseVideoUrl = '/api/video?id=';
    var assignments = [];

    getAssignments();

    $('video').on('ended',function() {
        playNextVideo(true);
    });

    function playNextVideo(autoPlay) {
        if (assignments.length <= 0) {
            return;
        }

        var assignment = assignments.shift();
        var videoPlayer = $('video');

        videoPlayer.find('source').attr('src', assignment.exercise.mediaUrl + baseVideoUrl + assignment.exercise.id);
        videoPlayer[0].load();

        if (autoPlay) {
            videoPlayer[0].play();
        }

        $('#video-player-title').html(assignment.exercise.title);
        $('#video-player-description').html(assignment.exercise.description);

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
        })
    }
});
