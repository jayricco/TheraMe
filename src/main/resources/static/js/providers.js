
$(document).ready(function () {

    var nanoBar = new Nanobar();
    var baseRequestUrl = '/api/providers';

    // Update list on page load
    getProviders();

    $('form').submit(function(event) {
        event.preventDefault();

        clearProviderList();
        getProviders();
    });

    function getProviders() {
        nanoBar.go(30);

        var requestUrl = baseRequestUrl;
        var query = $('#query').val();

        if (query) {
            requestUrl += '?q=' + query;
        }

        $.ajax({
            url: requestUrl,
            method: 'GET',
            cache: false,
            success: function(data) {
                clearProviderList();
                updateUserList(data);
                nanoBar.go(100)
            },
            error: function () {
                // Need to actually notify the user at some point, maybe redirect to an error page?
                nanoBar.go(100);
            }
        });
    }

    function clearProviderList() {
        $('#provider-table-body').empty();
    }

    function updateUserList(users) {
        var template = $('#provider-entry-template');
        var providerTable = $('#provider-table-body');

        users.forEach(function(provider) {
            var element = template.clone();

            element.find('#provider-entry-name').html(provider.name);
            element.find('#provider-entry-address').html(provider.address);
            element.find('#provider-entry-phone').html(provider.phone);
            element.find('#provider-entry-hours').html(provider.hours);

            providerTable.append(element);
        })
    }
});
