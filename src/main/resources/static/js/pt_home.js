$('form').submit(function(event) {
    event.preventDefault();

    var query = $('#query').val();

    if (!query) {
        return;
    }

    window.location.href = '/users?q=' + encodeURI(query);
});