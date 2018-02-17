
$(document).ready(function () {
    $('form').submit(function(event) {
        event.preventDefault();

        var successMessage = $('#success-message');
        successMessage.html('').hide();

        var errorMessage = $('#error-message');
        errorMessage.html('').hide();

        var formData = new FormData($('form')[0]);

        $.ajax({
            url: '/api/upload',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            success: function() {
                successMessage.show().html('User registered successfully!');
            },
            error: function(response) {
                var errorString = '';
                response.responseJSON.errors.forEach(function(error) {
                    $('#' + error.field).addClass('is-invalid');
                    errorString += '- ' + error.defaultMessage + '\n';
                });
                errorMessage.show().html(errorString)
            }
        });
    });
});