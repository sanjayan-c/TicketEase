$(document).ready(function () {
    // Function to load data into the DataTable
    function loadDataTable(tabId, tableId, dataUrl, columns) {
        $(tabId).on('shown.bs.tab', function (e) {
            $.ajax({
                url: dataUrl, // Replace with your actual PHP file for data
                dataType: 'json',
                success: function (data) {
                    $(tableId).DataTable({
                        data: data,
                        columns: columns
                    });
                }
            });
        });
    }

    // Load Bus Data
    loadDataTable('#bus-tab', '#Bus_schedule', 'getBusScheduleData.php', [
        { data: 'BusNo' },
        { data: 'Date' },
        { data: 'Day' },
        { data: 'StartTime' },
        { data: 'EndTime' },
        { data: 'ExpiryDate' },
        { data: 'RouteNo' },
        { data: 'StartLocation' },
        { data: 'EndLocation' },
        { data: null, render: function () { return 'Action'; } }
    ]);

    // Load Train Data
    loadDataTable('#train-tab', '#Train_schedule', 'getTrainScheduleData.php', [
        { data: 'TrainNo' },
        { data: 'Date' },
        { data: 'Day' },
        { data: 'StartTime' },
        { data: 'EndTime' },
        { data: 'ExpiryDate' },
        { data: 'RouteLine' },
        { data: 'StartLocation' },
        { data: 'EndLocation' },
        { data: null, render: function () { return 'Action'; } }
    ]);

// When the "bus" tab is activated, show the Bus table and hide the Train table
$('#bus-tab').on('shown.bs.tab', function (e) {
    $('#Bus_schedule').show();
    $('#Train_schedule').hide();
});

// When the "train" tab is activated, show the Train table and hide the Bus table
$('#train-tab').on('shown.bs.tab', function (e) {
    $('#Bus_schedule').hide();
    $('#Train_schedule').show();
});


    function getBusDetails(bookId) {
        // Perform an AJAX request to your server
        // You can use JavaScript Fetch or jQuery AJAX for this
        // Here's an example using jQuery AJAX

        $.ajax({
            type: "POST",
            url: "YourApiEndpointOrPage.aspx",
            data: { bookId: busId }, // Pass the book ID as a parameter
            success: function (data) {
                // Handle the response data here
                // Update your HTML elements with the book details
                // Example: $('#TextBox1').val(data.book_name);
            },
            error: function (xhr, status, error) {
                console.log("Error: " + error);
            }
        });
    }

    // Example of triggering the function when a button is clicked
    $("button.get-details").on("click", function () {
        var busId = $(this).data("busid"); // Get the book ID from data attribute
        getBusDetails(busId);
    });

});
