$(document).ready(function () {
    // Attach a click event handler to the divTabPopcorn
    $('#divTabPopcorn').on('click', function () {
        // Make an AJAX request to the server to fetch train numbers
        $.ajax({
            type: "POST",
            url: "AddSchedule.aspx/fillTrainNumberVal",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                // Clear the existing items in the ddlTrainNo dropdown
                $('#<%= ddlTrainNo.ClientID %>').empty();

                // Populate the dropdown with the retrieved train numbers
                $.each(data.d, function (index, item) {
                    $('#<%= ddlTrainNo.ClientID %>').append($('<option>', {
                        value: item,
                        text: item
                    }));
                });
            },
            error: function (data) {
                // Handle the error
                alert("Error fetching train numbers");
            }
        });
    });
});
