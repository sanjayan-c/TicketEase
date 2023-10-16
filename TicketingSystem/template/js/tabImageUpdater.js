document.addEventListener('DOMContentLoaded', function () {
    // Get references to the tab and image elements
    var tabs = document.querySelectorAll('#myTab a[data-toggle="tab"]');
    var tabImage = document.getElementById('tabImage');

    // Set the default image to QRBusticket.png
    tabImage.src = '/Images/QRBusticket.png';

    // Add click event listeners to each tab
    tabs.forEach(function (tab) {
        tab.addEventListener('click', function (event) {
            var activeTab = event.target;
            // Check the ID of the active tab and change the image accordingly
            if (activeTab.id === 'divTabCombo') {
                tabImage.src = '/Images/QRBusticket.png';
            } else if (activeTab.id === 'divTabPopcorn') {
                tabImage.src = '/Images/QRtrainticket.jpg';
            }
        });
    });

    // Continue with your existing code here
    var pObj = new Object();
    // ...
    pObj.ExpiryDate = $("#txtExpiryDate").val();

    // Get the expiry date and current date as Date objects
    var expiryDate = new Date(pObj.ExpiryDate);
    var currentDate = new Date();

    // Compare the expiry date with the current date
    if (expiryDate < currentDate) {
        pObj.Status = false; // Update the status as 'Expired' (boolean value)
    } else {
        pObj.Status = true; // Update the status as 'Valid' (boolean value)
    }

    // ...

    $("#imageInput").change(function () {
        readURL(this);
    });

    $(document).ready(function () {
        var dtToday = new Date();
        var month = dtToday.getMonth() + 1;
        var day = dtToday.getDate();
        var year = dtToday.getFullYear();
        if (month < 10)
            month = '0' + month.toString();

        if (day < 10)
            day = '0' + day.toString();

        var maxDate = year + '-' + month + '-' + day;
        $('#txtExpiryDate').attr('min', maxDate);
    });

    // ...
});
