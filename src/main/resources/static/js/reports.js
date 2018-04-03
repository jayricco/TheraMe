
$(document).ready(function () {

    console.log("test");

    var baseRequestUrl = '/api/history/allpatients';
    var reports = [];

    getReports();


    function getReports() {
        $.ajax({
            url: baseRequestUrl,
            method: 'GET',
            cache: false,
            success: function(data) {
                updateReports(data);
                reports = data;
            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
            }
        });
    }

    function updateReports(reports) {
        console.log("testing reports update");
        var entryContainer = $('#report-entry-container');
        entryContainer.empty();

        var template = $('#report-table-body');

        reports.forEach(function(report) {
            var element = template.clone();
            element.removeClass('hidden');

            element.find('#assignment-entry-img').attr('src', thumbHref);

            element.find('#assignment-entry-title').html(assignment.exercise.title);

            entryContainer.append(element);
        })
    }
});
