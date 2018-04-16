$(document).ready(function () {

    var daysOfTheWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    var baseRequestUrl = '/api/history/currentuser';

    getReports();

    function getReports() {
        $.ajax({
            url: baseRequestUrl,
            method: 'GET',
            cache: false,
            success: function(data) {
                updateReports(data);
            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
            }
        });
    }

    function updateReports(report) {
        var tuples = [];

        Object.keys(report.history).forEach(function(date) {
            tuples.push({ date: new Date(date + ' 00:00'), complete: report.history[date] });
        });

        // Make sure they're sorted correctly
        tuples.sort(function (a, b) {
            return a.date - b.date;
        });

        var entryContainer = $('#completion-days-body');
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

        entryContainer.find('#user-history-title').html('You\'ve completed your exercises ' + completions + ' of the last 7 days.');

    }
});
