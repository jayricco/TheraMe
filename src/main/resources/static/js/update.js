
$(document).ready(function () {
    console.log("updating for user");
    var userId = new

    $('form').submit(function(event) {
        event.preventDefault();

        // Reset error statuses before a new request is made
        $('.is-invalid').removeClass('is-invalid');

        var successMessage = $('#success-message');
        successMessage.html('').hide();

        var errorMessage = $('#error-message');
        errorMessage.html('').hide();

        var formData = $('form').serialize();
        console.log(formData);
        $.get("/api/updateInfo?id=", formData, function() {
            successMessage.show().html('User Information Changed Successfully!');
        })
    });
});