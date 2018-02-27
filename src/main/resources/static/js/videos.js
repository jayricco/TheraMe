String.prototype.escapeSpecialChars = function() {
    return this.replace(/\\n/g, "\\n")
        .replace(/\\'/g, "\\'")
        .replace(/\\"/g, '\\"')
        .replace(/\\&/g, "\\&")
        .replace(/\\r/g, "\\r")
        .replace(/\\t/g, "\\t")
        .replace(/\\b/g, "\\b")
        .replace(/\\f/g, "\\f");
};

$(document).ready(function () {

    var nanoBar = new Nanobar();
    var baseRequestUrl = '/api/videos';

    $('#w-input-search').autocomplete({
        serviceUrl: baseRequestUrl,
        paramName: 'q',
        delimiter: ",",
        transformResult: function(response) {
            return {
                //must convert json to javascript object before processing
                suggestions: [$.map($.parseJSON(response), function(item) {
                    printf("%s", item);
                    return { value: item, data: item.id };
                })]
            };
        }
    });



    // Update list on page load
    getVideos(baseRequestUrl);

    $('form').submit(function(event) {
        event.preventDefault();

        var requestUrl = baseRequestUrl;
        var query = $('#query').val();
        if (query) {
            requestUrl += '?q=' + query;
        }

        clearVideoList();
        getVideos(requestUrl);
    });

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

    function updateVideoList(videos) {
        var template = $('#video-column-template');
        var videoContainer = $('#video-container');
        var currentRow;

        videos.forEach(function(video, index) {
            if (index % 4 === 0) {
                currentRow = $('<div></div>', {class: 'row'});
                videoContainer.append(currentRow);
            }

            var element = template.clone();
            element.removeClass('hidden');

            // Set thumbnail
            var thumbHref = video.mediaUrl + '/api/thumbnail?id=' + video.id;
            element.find('#video-thumbnail-template').attr('src', thumbHref);

            // Set title
            element.find('#video-title-template').html(video.title);

            // Set run time
            element.find('#video-timestamp-template').html(video.runTime);

            // Set href
            element.find('#video-href-template').attr('href', '/watch?v=' + video.id);

            currentRow.append(element);
        })
    }
});
