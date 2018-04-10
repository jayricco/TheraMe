
$(document).ready(function () {

    $('form').submit(function(event) {
        event.preventDefault();

        // Reset error statuses before a new request is made
        $('.is-invalid').removeClass('is-invalid');

        var successMessage = $('#success-message');
        successMessage.html('').hide();

        var errorMessage = $('#error-message');
        errorMessage.html('').hide();

        var formData = $('form').serialize();
        console.log("Form data is: "+formData);
        $.post("/api/change", formData, function() {
            successMessage.show().html('User Information Changed Successfully!');
        })
    });
});