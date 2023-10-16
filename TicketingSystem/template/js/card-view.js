var endpointUrl = "Controller/JarvisApi.ashx?call=";

function navigateToPage(url) {
    window.location.href = url;
}

function goBack() {
    window.history.back();
}


$("#btnCanteenMenuAdd").click(function () {

    var pObj = new Object();
    pObj.ProductName = $("#txtProductName").val();
    pObj.Category = $("#ddlSelectCategory").val();
    pObj.Size = $("#txtSize").val();
    pObj.AdditionalDescription = $("#AdditionalInf").val();
    pObj.UnitPrice = $("#txtUnitPrice").val();
    pObj.MenuImage = document.getElementById('b64').innerText;
    var bottomMsgId = "divValidationError";


    jQuery.ajax({
        url: endpointUrl + "AddCanteenMenu",
        type: 'POST',
        datatype: 'json',
        data: JSON.stringify(pObj),
        success: function (data) {
            data = JSON.parse(data);
            if (data.StatusCode == 200) {
                alert("Saved Succesfully");
            }
            else {
                GlobalJARVIS.ApiResponseMessageAlert(data, bottomMsgId);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
});

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#menuImg').attr('src', e.target.result);
            $('#menuImg').show(); // Show the image
            document.getElementById('b64').innerText = e.target.result;
        }

        reader.readAsDataURL(input.files[0]);
    }
}


$("#imageInput").change(function () {
    readURL(this);
});

function ViewAllCanteenMenu() {
    var pObj = new Object();
    jQuery.ajax({
        url: endpointUrl + "ViewAllCanteenMenu",
        type: 'POST',
        datatype: 'json',
        data: JSON.stringify(pObj),
        success: function (data) {
            data = JSON.parse(data);
            if (data.StatusCode == 200) {
                $.each(data.Data, function (i, item) {
                    var Category = data.Data[i].Category;

                    if (Category == "COMBO") {
                        var tabbuild = "<div class='select-food-row'> <div class='row'><div class='menu-id' style='display:none'>" + data.Data[i].MenuId + "</div><div class='pCategory' style='display:none'>" + data.Data[i].Category + "</div><div class='col-lg-2 image-container'><div class='fill'><img alt='image' id='menuImg' class='img-fluid mImage' src='" + data.Data[i].MenuImage + "' style='border: 1px solid #D6D6D6; border-radius: 12px;'runat='server'/></div></div><div class='col-4'><div class='menu-name-price pName'>" + data.Data[i].ProductName + "</div><div class='menu-name-price uPrice'>" + data.Data[i].UnitPrice + "</div><div class='menu-name-price addInf'>" + data.Data[i].AdditionalDescription + "</div></div><div class='col-1.5'><label for='name'>Available Quantity </label><div class='menu-name-price-box1'><div class='size-quantity" + (data.Data[i].AvailableQuantity < 75 ? " low-quantity" : "") + "'>" + data.Data[i].AvailableQuantity + "</div></div><label style='text-align: center' for='name'>Size </label><div class='menu-name-price-box1'><div class='size-quantity size'>" + data.Data[i].Size + "</div></div></div><div class='col-2'><a class='action-menu btn-cant-edit btn btn-success btn-icon-split' data-toggle='modal' data-target='#MenuModal' data-id='" + data.Data[i].MenuId + "'><span class='icon text-white-50'><i class='fas fa-pencil-alt'></i></span><span class='text'>Edit</span></a></div><div class='col-2'><a href='#' class='action-menu btn-cant-delete btn btn-danger btn-icon-split' data-toggle='modal' data-target='#DeleteModal'><span class='icon text-white-50'><i class='fas fa-trash-alt'></i></span><span class='text'>Delete</span></a></div></div></div>"

                         $("#divContentCombo").append(tabbuild);
                    }
                    else if (Category == "POPCORN") {
                        var tabbuild = "<div class='select-food-row'> <div class='row'><div class='menu-id' style='display:none'>" + data.Data[i].MenuId + "</div><div class='pCategory' style='display:none'>" + data.Data[i].Category + "</div><div class='col-lg-2 image-container'><div class='fill'><img alt='image' id='menuImg' class='img-fluid mImage src='" + data.Data[i].MenuImage + "' style='border: 1px solid #D6D6D6; border-radius: 12px;''runat='server'/></div></div><div class='col-4'><div class='menu-name-price pName'>" + data.Data[i].ProductName + "</div><div class='menu-name-price uPrice'>" + data.Data[i].UnitPrice + "</div><div class='menu-name-price addInf'>" + data.Data[i].AdditionalDescription + "</div></div><div class='col-1.5'><label for='name'>Available Quantity </label><div class='menu-name-price-box1'><div class='size-quantity'>" + data.Data[i].AvailableQuantity + "</div></div><label style='text-align: center' for='name'>Size </label><div class='menu-name-price-box1'><div class='size-quantity size'>" + data.Data[i].Size + "</div></div></div><div class='col-2'><a class='action-menu btn-cant-edit btn btn-success btn-icon-split' data-toggle='modal' data-target='#MenuModal' data-id='" + data.Data[i].MenuId + "'><span class='icon text-white-50'><i class='fas fa-pencil-alt'></i></span><span class='text'>Edit</span></a></div><div class='col-2'><a href='#' class='action-menu btn-cant-delete btn btn-danger btn-icon-split' data-toggle='modal' data-target='#DeleteModal'><span class='icon text-white-50'><i class='fas fa-trash-alt'></i></span><span class='text'>Delete</span></a></div></div></div>"

                        $("#divContentPopcorn").append(tabbuild);
                    }
                    else if (Category == "SOFT DRINKS") {

                        var tabbuild = "<div class='select-food-row'> <div class='row'><div class='menu-id' style='display:none'>" + data.Data[i].MenuId + "</div><div class='pCategory' style='display:none'>" + data.Data[i].Category + "</div><div class='col-lg-2 image-container'><div class='fill'><img alt='image' id='menuImg' class='img-fluid mImage' src='" + data.Data[i].MenuImage + "' style='border: 1px solid #D6D6D6; border-radius: 12px;''runat='server'/></div></div><div class='col-4'><div class='menu-name-price pName'>" + data.Data[i].ProductName + "</div><div class='menu-name-price uPrice'>" + data.Data[i].UnitPrice + "</div><div class='menu-name-price addInf'>" + data.Data[i].AdditionalDescription + "</div></div><div class='col-1.5'><label for='name'>Available Quantity </label><div class='menu-name-price-box1'><div class='size-quantity'>" + data.Data[i].AvailableQuantity + "</div></div><label style='text-align: center' for='name'>Size </label><div class='menu-name-price-box1'><div class='size-quantity size'>" + data.Data[i].Size + "</div></div></div><div class='col-2'><a class='action-menu btn-cant-edit btn btn-success btn-icon-split' data-toggle='modal' data-target='#MenuModal' data-id='" + data.Data[i].MenuId + "'><span class='icon text-white-50'><i class='fas fa-pencil-alt'></i></span><span class='text'>Edit</span></a></div><div class='col-2'><a href='#' class='action-menu btn-cant-delete btn btn-danger btn-icon-split' data-toggle='modal' data-target='#DeleteModal'><span class='icon text-white-50'><i class='fas fa-trash-alt'></i></span><span class='text'>Delete</span></a></div></div></div>"

                        $("#divContentSoftDrinks").append(tabbuild);
                    }
                    else if (Category == "COFFEE") {

                        var tabbuild = "<div class='select-food-row'> <div class='row'><div class='menu-id' style='display:none'>" + data.Data[i].MenuId + "</div><div class='pCategory' style='display:none'>" + data.Data[i].Category + "</div><div class='col-lg-2 image-container'><div class='fill'><img alt='image' id='menuImg' class='img-fluid mImage' src='" + data.Data[i].MenuImage + "' style='border: 1px solid #D6D6D6; border-radius: 12px;''runat='server'/></div></div><div class='col-4'><div class='menu-name-price pName'>" + data.Data[i].ProductName + "</div><div class='menu-name-price uPrice'>" + data.Data[i].UnitPrice + "</div><div class='menu-name-price addInf'>" + data.Data[i].AdditionalDescription + "</div></div><div class='col-1.5'><label for='name'>Available Quantity </label><div class='menu-name-price-box1'><div class='size-quantity'>" + data.Data[i].AvailableQuantity + "</div></div><label style='text-align: center' for='name'>Size </label><div class='menu-name-price-box1'><div class='size-quantity size'>" + data.Data[i].Size + "</div></div></div><div class='col-2'><a class='action-menu btn-cant-edit btn btn-success btn-icon-split' data-toggle='modal' data-target='#MenuModal' data-id='" + data.Data[i].MenuId + "'><span class='icon text-white-50'><i class='fas fa-pencil-alt'></i></span><span class='text'>Edit</span></a></div><div class='col-2'><a href='#' class='action-menu btn-cant-delete btn btn-danger btn-icon-split' data-toggle='modal' data-target='#DeleteModal'><span class='icon text-white-50'><i class='fas fa-trash-alt'></i></span><span class='text'>Delete</span></a></div></div></div>"

                        $("#divContentCoffee").append(tabbuild);
                    }
                    else {
                        var tabbuild = "<div class='select-food-row'> <div class='row'><div class='menu-id' style='display:none'>" + data.Data[i].MenuId + "</div><div class='pCategory' style='display:none'>" + data.Data[i].Category + "</div><div class='col-lg-2 image-container'><div class='fill'><img alt='image' id='menuImg' class='img-fluid mImage' src='" + data.Data[i].MenuImage + "' style='border: 1px solid #D6D6D6; border-radius: 12px;''runat='server'/></div></div><div class='col-4'><div class='menu-name-price pName'>" + data.Data[i].ProductName + "</div><div class='menu-name-price uPrice'>" + data.Data[i].UnitPrice + "</div><div class='menu-name-price addInf'>" + data.Data[i].AdditionalDescription + "</div></div><div class='col-1.5'><label for='name'>Available Quantity </label><div class='menu-name-price-box1'><div class='size-quantity'>" + data.Data[i].AvailableQuantity + "</div></div><label style='text-align: center' for='name'>Size </label><div class='menu-name-price-box1'><div class='size-quantity size'>" + data.Data[i].Size + "</div></div></div><div class='col-2'><a class='action-menu btn-cant-edit btn btn-success btn-icon-split' data-toggle='modal' data-target='#MenuModal' data-id='" + data.Data[i].MenuId + "'><span class='icon text-white-50'><i class='fas fa-pencil-alt'></i></span><span class='text'>Edit</span></a></div><div class='col-2'><a href='#' class='action-menu btn-cant-delete btn btn-danger btn-icon-split' data-toggle='modal' data-target='#DeleteModal'><span class='icon text-white-50'><i class='fas fa-trash-alt'></i></span><span class='text'>Delete</span></a></div></div></div>"

                        $("#divContentChocolates").append(tabbuild);
                    }
                });
                console.log(data);
            }
            else {
                alert("Not Saved");
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
};

////$('.action-menu.btn-cant-edit').click(function () {
//    $(document).on('click', '.action-menu.btn-cant-edit', function () {
//        var menuId = $(this).data('id');

////$("#tbodyCanteenInventoryData").on("click", ".btn-edit", function () {
//    //var $row = $(this).closest("tr");    // Find the row
//    //var text = $row.find(".cid").text(); // Find the text
//        var productName = $(this).closest('.row').find('.pName').text();
//        var Category = $(this).closest('.row').find('.pCategory').text();
//        var Size = $(this).closest('.row').find('.size').text();
//        var AdditionalDescription = $(this).closest('.row').find('.addInf').text();
//        var UnitPrice = $(this).closest('.row').find('.uPrice').text();
//        var MenuImage = $(this).closest('.row').find('.mImage').attr('src'); // Use .attr('src') to get the image source

//        $("#txtUpdatedProductName").val(productName);
//        $("#ddlUpdatedSelectCategory").val(Category);
//        $("#txtUpdatedSize").val(Size);
//        $("#txtUpdatedAdditionalInf").val(AdditionalDescription);
//        $("#txtUpdatedUnitPrice").val(UnitPrice);
//        $("#menuImg").attr('src', MenuImage).show();


//        $("#btnMenuUpdate").on("click", function () {
//        var iObj = {
//            MenuId: menuId,
//            ProductName: $("#txtUpdatedProductName").val(),
//            Category: $("#ddlUpdatedSelectCategory").val(),
//            Size: $("#txtUpdatedSize").val(),
//            AdditionalDescription: $("#txtUpdatedAdditionalInf").val(),
//            UnitPrice: $("#txtUpdatedUnitPrice").val(),
//            MenuImage: $('#b64').text()// Use the variable containing the image source

//        };


//            $.ajax({
//                url: endpointUrl + "UpdateCanteenMenu",
//                type: 'POST',
//                dataType: 'json',
//                contentType: 'application/json',
//                data: JSON.stringify(iObj),
//                success: function (data) {
//                    if (data.StatusCode == 200) {
//                        alert("Data updated successfully!");
//                        // Refresh the page or update the row with the updated data
//                        // based on your application's requirements
//                    } else {
//                        alert("Update failed");
//                    }
//                },
//                error: function (xhr, textStatus, errorThrown) {
//                    console.log(xhr.responseText);
//                    console.log(textStatus);
//                    console.log(errorThrown);
//                    alert("Update failed");
//                }
//            });
//        });
//    });
//<img alt = 'image' id = 'menuImg' class='img-fluid mImage' src = '" + data.Data[i].MenuImage + "' style = 'border: 1px solid #D6D6D6; border-radius: 12px;'runat = 'server' />

$(document).on('click', '.action-menu.btn-cant-edit', function () {
    var menuId = $(this).data('id');
    var productName = $(this).closest('.row').find('.pName').text();
    var Category = $(this).closest('.row').find('.pCategory').text();
    var Size = $(this).closest('.row').find('.size').text();
    var AdditionalDescription = $(this).closest('.row').find('.addInf').text();
    var UnitPrice = $(this).closest('.row').find('.uPrice').text();
    var MenuImage = $(this).closest('.row').find('.mImage').attr('src');

    $("#txtUpdatedProductName").val(productName);
    $("#ddlUpdatedSelectCategory").val(Category);
    $("#txtUpdatedSize").val(Size);
    $("#txtUpdatedAdditionalInf").val(AdditionalDescription);
    $("#txtUpdatedUnitPrice").val(UnitPrice);
/*    $("#menuImg").attr('src', MenuImage).show();*/

    if (MenuImage.startsWith("data:image")) {
        // If MenuImage is base64 data
        $("#menuImg").attr('src', MenuImage).show();
    } else {
        // If MenuImage is a URL
        $("#menuImg").attr('src', MenuImage).show();
    }

    console.log("MenuImage:", MenuImage);
    $("#btnAddImageSubmit").click(function () {
        $("#fileInput").click();
    });

    $("#fileInput").change(function () {
        readURL(this);
    });

    $("#btnMenuUpdate").on("click", function () {
        var iObj = {
            MenuId: menuId,
            ProductName: $("#txtUpdatedProductName").val(),
            Category: $("#ddlUpdatedSelectCategory").val(),
            Size: $("#txtUpdatedSize").val(),
            AdditionalDescription: $("#txtUpdatedAdditionalInf").val(),
            UnitPrice: $("#txtUpdatedUnitPrice").val(),
            MenuImage: $('#b64').text()
        };

        $.ajax({
            url: endpointUrl + "UpdateCanteenMenu",
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(iObj),
            success: function (data) {
                if (data.StatusCode == 200) {
                    alert("Data updated successfully!");
                    // Refresh the page or update the row with the updated data
                    // based on your application's requirements
                } else {
                    alert("Update failed");
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                console.log(textStatus);
                console.log(errorThrown);
                alert("Update failed");
            }
        });
    });
});

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#menuImg').attr('src', e.target.result);
            document.getElementById('b64').innerText = e.target.result;
        }

        reader.readAsDataURL(input.files[0]);
    }
}

$(document).on('click', '.btn-cant-delete', function () {
    var menuId = $(this).closest('.select-food-row').find('.menu-id').text();
    var data = { MenuId: menuId };

       // Handle delete button click
    $('#btndelete').on('click', function () {
        $.ajax({
            type: 'POST',
            url: endpointUrl + 'DelCantMenu',
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (data) {
                if (data.StatusCode == 200) {
                    alert('Deleted successful   ly!');
                    $('.select-food-row .menu-id').filter(function () {
                        return $(this).text() === menuId;
                    }).closest('.select-food-row').remove();
                }
            },
            error: function (xhr, status, error) {
                // Handle error response from server
                alert('Delete failed'); // Replace with your desired action
            }
        });
    });
});