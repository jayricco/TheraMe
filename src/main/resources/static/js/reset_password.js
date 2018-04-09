$(document).ready(function () {

    $('form').submit(function (event) {
        event.preventDefault();

        // Reset error statuses before a new request is made
        $('.is-invalid').removeClass('is-invalid');

        var successMessage = $('#success-message');
        successMessage.html('').hide();

        var errorMessage = $('#error-message');
        errorMessage.html('').hide();

        var email = $('#email').val();
        $.post("/api/resetPassword", { email: email }, function () {
            successMessage.show().html('Please check your email for a reset link.');
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