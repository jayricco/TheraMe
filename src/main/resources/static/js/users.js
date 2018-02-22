
$(document).ready(function () {

    var nanoBar = new Nanobar();
    var baseRequestUrl = '/api/users?types=PATIENT';
    const baseUserHref = '/user/';

    // Update list on page load
    getUsers(baseRequestUrl);

    $('form').submit(function(event) {
        event.preventDefault();

        var requestUrl = baseRequestUrl;
        var query = $('#query').val();

        if (query) {
            requestUrl += '&q=' + query;
        }

        clearUserList();
        getUsers(requestUrl);
    });

    function getUsers(requestUrl) {
        nanoBar.go(30);
        $.ajax({
            url: requestUrl,
            method: 'GET',
            cache: false,
            success: function(data) {
                clearUserList();
                updateUserList(data);
                nanoBar.go(100)
            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
                nanoBar.go(100);
            }
        });
    }

    function clearUserList() {
        $('#user-table-body').empty();
    }

    function updateUserList(users) {
        var template = $('#user-entry-template');
        var userTable = $('#user-table-body');

        users.forEach(function(user) {
            var element = template.clone();

            element.find('#user-entry-name').html(user.firstName + ' ' + user.lastName).attr('href', baseUserHref + user.id);

            if (user.therapist) {
                element.find('#user-entry-ptname').html(user.therapist.firstName + ' ' + user.therapist.lastName).attr('href', baseUserHref + user.therapist.id);
            } else {
                element.find('#user-entry-ptname').html('N/A');
            }

            element.find('#user-entry-status').html('Active');

            userTable.append(element);
        })
    }
});
