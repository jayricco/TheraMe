
$(document).ready(function () {
    $('form').submit(function(event) {
        event.preventDefault();

        var submitButton = $('#upload-submit');

        // Make sure the user doesn't submit another while already uploading
        if (submitButton.hasClass('disabled')) {
            return;
        }

        var successMessage = $('#success-message');
        successMessage.html('').hide();

        var errorMessage = $('#error-message');
        errorMessage.html('').hide();

        var progressBar = $('#upload-progress');
        progressBar.removeClass('hidden');

        var progressIndicator = progressBar.find('.progress-bar');

        submitButton.addClass('disabled');

        var formData = new FormData($('form')[0]);

        $.ajax({
            url: '/api/upload',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            xhr: function() {
                var xhr = $.ajaxSettings.xhr();
                xhr.upload.addEventListener('progress', function(event) {
                    if (event.lengthComputable) {
                        var percentComplete = (event.loaded / event.total) * 100;
                        progressIndicator.css({ width: percentComplete + '%' });
                        progressIndicator.attr('aria-valuenow', percentComplete);
                    }
                });
                return xhr;
            },
            success: function() {
                successMessage.show().html('Video uploaded successfully!');
                submitButton.removeClass('disabled');
            },
            error: function(response) {
                var errorString = '';

                if (response.responseJSON) {
                    if (response.responseJSON.errors) {
                        response.responseJSON.errors.forEach(function (error) {
                            $('#' + error.field).addClass('is-invalid');
                            errorString += '- ' + error.defaultMessage + '\n';
                        });
                    } else if (response.responseJSON.message) {
                        errorString = '- ' + response.responseJSON.message;
                    }
                }

                errorMessage.show().html(errorString);
                submitButton.removeClass('disabled');
            }
        });
    });
});