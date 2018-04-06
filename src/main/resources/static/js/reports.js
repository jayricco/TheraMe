
$(document).ready(function () {

    var baseRequestUrl = '/api/history/allpatients?therapistId=';
    var reports = [];
    const baseUserHref = '/user?id=';

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
        var weekday = new Array(7);
        weekday[0] =  "Sunday";
        weekday[1] = "Monday";
        weekday[2] = "Tuesday";
        weekday[3] = "Wednesday";
        weekday[4] = "Thursday";
        weekday[5] = "Friday";
        weekday[6] = "Saturday";
        var entryContainer = $('#report-table-body');
        entryContainer.empty();
        var template = $('#report-entry-template');
        var users = "";
        var today = new Date();
        today.setHours(0, 0, 0, 0);
        var weekAgo = new Date(today.getTime() - 6 * 24 * 60 * 60 * 1000);
        var arr = [];
        reports.forEach(function(report) {
            arr.push(report.patientId.id);
        })
        var users = [];
        $.each(arr, function(i, el){
            if($.inArray(el, users) === -1) users.push(el);
        });
        for (var i = 0; i<users.length; i++){
            var element = template.clone();
            element.removeClass('hidden');
            var date = weekAgo;
            var count = 0;
            var day = 0;
            reports.forEach(function(report){
                if (report.patientId.id == users[i]){
                    if (count == 0){
                        element.find('#report-entry-name').html(report.patientId.firstName+' '+report.patientId.lastName).attr('href', baseUserHref + report.patientId.id);
                        count++;
                    }
                    var exerciseDate = new Date(report.timeStart);
                    exerciseDate = new Date(exerciseDate.setHours(0, 0, 0, 0));
                    while (date < exerciseDate){
                        var elementToFind = '#report-entry-day'.concat(day);
                        element.find('#report-entry-day'+day).html("<i class=\"far fa-square\"></i><br>"+weekday[date.getDay()]);
                        date = new Date(date.getTime() + 1 * 24 * 60 * 60 * 1000);
                        day++;
                    }
                    if (date.getTime() == exerciseDate.getTime())
                    {
                        if (report.completed){
                            element.find('#report-entry-day'+day).html("<i class=\"far fa-check-square\"></i><br>"+weekday[date.getDay()]);
                        }
                        else{
                            element.find('#report-entry-day'+day).html("<i class=\"far fa-square\"></i><br>"+weekday[date.getDay()]);
                        }
                        day++;
                        date = new Date(date.getTime() + 1 * 24 * 60 * 60 * 1000);
                    }
                }
            })
            while(date <= today && day < 7){
                element.find('#report-entry-day'+day).html("<i class=\"far fa-square\"></i><br>"+weekday[date.getDay()]);
                date = new Date(date.getTime() + 1 * 24 * 60 * 60 * 1000);
                day++;
            }
            entryContainer.append(element);
        }
    }
});
