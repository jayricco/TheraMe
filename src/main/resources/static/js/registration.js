
$(document).ready(function () {

    var providers = [];

    updateProviders();

    function updateProviders() {
        var requestUrl = '/api/providers';

        $.ajax({
            url: requestUrl,
            method: 'GET',
            cache: false,
            success: function(providers) {
                var providerList = $('provider');

                providers.forEach(function(provider) {
                    providers.add(provider);

                    providerList.append('<option value="' + provider.id + '">' + provider.name + '</option>');
                })
            }
        });
    }

    $('form').submit(function(event) {
        event.preventDefault();

        // Reset error statuses before a new request is made
        $('.is-invalid').removeClass('is-invalid');

        var successMessage = $('#success-message');
        successMessage.html('').hide();

        var errorMessage = $('#error-message');
        errorMessage.html('').hide();

        if ($('#password').val() !== $('#repeat-password').val()) {
            errorMessage.show().html('Passwords don\'t match!');
            $(':password').addClass('is-invalid');
            return;
        }

        var formData = $('form').serialize();
        $.post("/api/register", formData, function() {
            successMessage.show().html('User registered successfully!');
        })
        .fail(function(response) {
            var errorString = '';
            response.responseJSON.errors.forEach(function(error) {
                $('#' + error.field).addClass('is-invalid');
                errorString += '- ' + error.defaultMessage + '\n';
            });
            errorMessage.show().html(errorString)
        })
    });
});