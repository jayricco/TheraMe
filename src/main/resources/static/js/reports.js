
$(document).ready(function () {

    var baseRequestUrl = '/api/history/allpatients';
    var daysOfTheWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

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

    function updateReports(reports) {

        var container = $('#report-table-body');
        var template = $('#report-entry-template');

        if (!reports.length) {
            // If we have no reports to show, tell the user that
            container.html('<tr><td>No Results</td></tr>');

            return;
        }

        reports.forEach(function(report) {
            var row = template.clone();
            row.removeClass('hidden');

            var tuples = [];

            Object.keys(report.history).forEach(function(date) {
                tuples.push({ date: new Date(date + ' 00:00'), complete: report.history[date] });
            });

            // Make sure they're sorted correctly
            tuples.sort(function (a, b) {
                return a.date - b.date;
            });

            var completions = 0;

            row.find('#report-entry-name').html(report.user.firstName + ' ' + report.user.lastName);

            tuples.forEach(function(tuple, index) {
                var element = row.find('#report-entry-day' + index);

                if (tuple.complete) {
                    element.html('<i class=\"far fa-check-square\"></i><br>' + daysOfTheWeek[tuple.date.getDay()]);
                    completions++;
                } else {
                    element.html('<i class=\"far fa-square\"></i><br>' + daysOfTheWeek[tuple.date.getDay()]);
                }
            });

            container.append(row);
        });
    }
});
