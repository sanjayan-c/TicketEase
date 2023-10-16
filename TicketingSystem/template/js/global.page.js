var GlobalJARVIS = {

    PrintErrorList: function (errList, elementId) {

        if (errList)
            var msg = '';

        for (var er of errList) {
            msg += "<li class='list-group-item list-group-item-danger'>" + er + "</li>";
        }

        var top =
            "<ul class='list-group'>" + msg + "</ul>";

        var element = document.getElementById(elementId);
        element.className = "";

        element.innerHTML = top;
    },

    ApiResponseMessageAlert: function (data, bottomMsgId = "") {

        var objectData = data;
        if ($.type(data) === "string")
            objectData = JSON.parse(data);

        if (objectData.StatusCode == 401) {
            window.location.href = "Logins.aspx";
        }
        else {

            if (objectData.ModelValid) {
                if (bottomMsgId != "") {
                    var element = document.getElementById(bottomMsgId);
                    element.className = "";
                    element.innerHTML = "";
                }
            }
            else {
                if (bottomMsgId != "") {
                    GlobalJARVIS.PrintErrorList(objectData.Data, bottomMsgId);
                }
                else {
                    alert("Bottom Message Id missing.");
                }
            }
        }
    },

    ApiResponseMessageAddSeat: function (data, bottomMsgId = "") {

        var objectData = data;
        if ($.type(data) === "string")
            objectData = JSON.parse(data);

        if (objectData.StatusCode == 401) {
            window.location.href = "Logins.aspx";
        }
        else {

            if (objectData.ModelValid) {
                if (bottomMsgId != "") {
                    var element = document.getElementById(bottomMsgId);
                    element.className = "";
                    element.innerHTML = "";
                }
            }
            else {
                if (bottomMsgId != "") {
                    GlobalJARVIS.PrintErrorList(objectData.Data, bottomMsgId);


                }
                else {
                    alert("Bottom Message Id missing.");
                }
            }
        }
    }
}
