$(document).ready(function () {

    var token = getParameterByName('token');

    function getParameterByName(name) {
        var url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }

    $('form').submit(function (event) {
        event.preventDefault();

        // Reset error statuses before a new request is made
        $('.is-invalid').removeClass('is-invalid');

        var successMessage = $('#success-message');
        successMessage.html('').hide();

        var errorMessage = $('#error-message');
        errorMessage.html('').hide();

        var pass = $('#password').val();
        $.post("/api/confirm?token=" + token, { password: pass }, function () {
            window.location.href = '/login';
        })
        .fail(function (response) {
            var errorString = '';
            response.responseJSON.errors.forEach(function (error) {
                $('#' + error.field).addClass('is-invalid');
                errorString += '- ' + error.defaultMessage + '\n';
            });
            errorMessage.show().html(errorString)
        })
    });
});