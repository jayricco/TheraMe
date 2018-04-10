
$(document).ready(function () {

    var nanoBar = new Nanobar();
    var baseRequestUrl = '/api/assignments/all';

    // Update list on page load
    getVideos(baseRequestUrl);

    function getVideos(requestUrl) {
        nanoBar.go(30);
        $.ajax({
            url: requestUrl,
            method: 'GET',
            cache: false,
            success: function(data) {
                clearVideoList();
                updateVideoList(data);
                nanoBar.go(100)
            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
                nanoBar.go(100);
            }
        });
    }

    function clearVideoList() {
        $('#video-container').empty();
    }

    function updateVideoList(assignments) {
        var template = $('#video-column-template');
        var videoContainer = $('#video-container');
        var currentRow;

        assignments.forEach(function(assignment, index) {
            if (index % 4 === 0) {
                currentRow = $('<div></div>', {class: 'row video-row'});
                videoContainer.append(currentRow);
            }

            var element = template.clone();
            element.removeClass('hidden');

            // Set thumbnail
            var thumbHref = assignment.exercise.mediaUrl + '/api/thumbnail?id=' + assignment.exercise.id;
            element.find('#video-thumbnail-template').attr('src', thumbHref);

            // Set title
            element.find('#video-title-template').html(assignment.exercise.title);

            // Set run time
            element.find('#video-timestamp-template').html(assignment.exercise.runTime);

            // Set href
            element.find('#video-href-template').attr('href', '/watch?v=' + assignment.exercise.id);

            currentRow.append(element);
        })
    }
});
