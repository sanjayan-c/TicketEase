<%@ Page Title="" Language="C#" MasterPageFile="~/Master.Master" AutoEventWireup="true" CodeBehind="AddSchedule.aspx.cs" Inherits="TicketingSystem.WebForm1" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="template/css/card-view.css" rel="stylesheet" />
    <script src="template/js/tabImageUpdater.js"></script>
    <script src="template/js/tab.js"></script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="container-fluid">
        <!-- Page Heading -->
    </div>
    <div class="row">
        <div class="col-1">
        </div>
        <div class="col-6">
            <div class="card o-hidden border-0 shadow-lg my-5">
                <div class="card-body p-0">
                    <!-- Nested Row within Card Body -->
                    <div class="row">
                        <!--  <div class="col-lg-5 d-none d-lg-block bg-register-image"></div>   -->
                        <div class="col-lg-12">
                            <div class="p-2">
                                <div class="card-body">
                                    <div class="menu-tabs">
                                        <ul id="myTab" role="tablist" class="nav nav-tabs">
                                            <li class="nav-item">
                                                <a id="divTabCombo" data-toggle="tab" href="#divContentCombo" role="tab" aria-controls="divContentCombo" aria-selected="true" aria-current="page" class="nav-link active" onclick="setActiveTab('Bus')">BUS</a>
                                            </li>
                                            <li class="nav-item">
                                                <a id="divTabPopcorn" data-toggle="tab" href="#divContentPopcorn" role="tab" aria-controls="divContentPopcorn" class="nav-link" onclick="setActiveTab('Train')">TRAIN</a>
                                            </li>
                                        </ul>
                                    </div>
                                    <br />
                                    <div id="menutabcontent" class="tab-content">
                                        <div id="divContentCombo" role="tabpanel" aria-labelledby="divTabCombo" class="tab-pane active show">
                                            <div class="user" method="get">
                                                <div class="form-group row">
                                                    <div class="col-sm-12 mb-3 mb-sm-0">
                                                        <asp:DropDownList CssClass="form-control" ID="ddlBusNum" runat="server">
                                                            <asp:ListItem Text="BusNo" Value="" />
                                                        </asp:DropDownList>
                                                        <%-- <input type="text" class="form-control form-control-user" name="BusNo" id="txtBusNo" required
                                                            placeholder="Bus Number">--%>
                                                    </div>

                                                </div>
                                                <label for="category">Select Day for schedule</label>
                                                <div class="form-group row">
                                                    <div class="col-sm-6 mb-3 mb-sm-0">
                                                        <%-- <select id="ddlDay" name="Day" class="custom-select custom-select form-control form-control-sm" required>
                                                            <option value="">--From--</option>
                                                            <option value="Monday">Monday</option>
                                                            <option value="Wednesday">Wednesday</option>
                                                            <option value="Thursday">Thursday</option>
                                                            <option value="Friday">Friday</option>
                                                            <option value="Saturday">Saturday</option>
                                                            <option value="Sunday">Sunday</option>
                                                        </select>--%>
                                                        <asp:TextBox ID="txtRouteNum" class="form-control form-control-user" Placeholder="Enter Bus Route Num" runat="server"></asp:TextBox>
                                                    </div>
                                                    <div class="col-sm-6">
                                                        <asp:TextBox ID="txtBookingDate" TextMode="Date" class="form-control form-control-user" runat="server"></asp:TextBox>
                                                    </div>
                                                </div>
                                                <label for="category">Select Time for schedule</label>
                                                <div class="form-group row">
                                                    <div class="col-sm-6 mb-3 mb-sm-0">
                                                        <div class="input-group clockpicker">
                                                            <asp:TextBox ID="txtfromBusTime" class="form-control form-control-user" runat="server" Placeholder="Enter Start Time"></asp:TextBox>
                                                            <span class="input-group-append"><span class="input-group-text"><i class="fa fa-clock-o"></i></span></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-6">
                                                        <asp:TextBox ID="textBusToTime" class="form-control form-control-user" Placeholder="Enter End Time" runat="server"></asp:TextBox>
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <div class="col-sm-6 mb-3 mb-sm-0">
                                                        <asp:TextBox ID="txtBusStartLoc" class="form-control form-control-user" Placeholder="Enter Start Location" runat="server"></asp:TextBox>
                                                    </div>
                                                    <div class="col-sm-6">
                                                        <asp:TextBox ID="txtBusEndLoc" class="form-control form-control-user" Placeholder="Enter End Location" runat="server"></asp:TextBox>
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <div class="col-sm-4 mb-3 mb-sm-0"></div>
                                                    <div class="col-sm-4 mb-3 mb-sm-0">
                                                        <asp:LinkButton type="button" ID="busAddSchecdule" OnClick="BusAddScheduleBtn_Click" runat="server" class="btn mb-1 rounded-pill btn-primary">Add Schedule</asp:LinkButton>
                                                    </div>
                                                    <div class="col-sm-4 mb-3 mb-sm-0"></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="divContentPopcorn" role="tabpanel" aria-labelledby="divTabPopcorn" class="tab-pane fade show">
                                            <div class="user" method="get">
                                                <div class="form-group row">
                                                    <div class="col-sm-12 mb-3 mb-sm-0">
                                                        <asp:DropDownList CssClass="form-control" ID="ddlTrainNo" runat="server">
                                                            <asp:ListItem Text="TrainNo" Value="" />
                                                        </asp:DropDownList>
                                                    </div>

                                                </div>

                                                <label for="category">Select Day for schedule</label>
                                                <div class="form-group row">
                                                    <div class="col-sm-6 mb-3 mb-sm-0">
                                                        <asp:TextBox ID="txtTrainRoute" class="form-control form-control-user" Placeholder="Enter Train Route" runat="server"></asp:TextBox>
                                                    </div>
                                                    <div class="col-sm-6">
                                                        <asp:TextBox ID="txtTrainBookDate" TextMode="Date" class="form-control form-control-user" runat="server"></asp:TextBox>

                                                    </div>
                                                </div>
                                                <label for="category">Select Time for schedule</label>
                                                <div class="form-group row">
                                                    <div class="col-sm-6 mb-3 mb-sm-0">
                                                        <asp:TextBox ID="txtTrainFromTime" class="form-control form-control-user" Placeholder="Enter Start Time" runat="server"></asp:TextBox>
                                                    </div>
                                                    <div class="col-sm-6">
                                                        <asp:TextBox ID="txtTrainToTime" class="form-control form-control-user" Placeholder="Enter End Time" runat="server"></asp:TextBox>
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <div class="col-sm-6 mb-3 mb-sm-0">
                                                        <asp:TextBox ID="txtTrainStartLoc" class="form-control form-control-user" Placeholder="Enter Start Location" runat="server"></asp:TextBox>
                                                    </div>
                                                    <div class="col-sm-6">
                                                        <asp:TextBox ID="txtTrainEndLoc" class="form-control form-control-user" Placeholder="Enter End Location" runat="server"></asp:TextBox>

                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <div class="col-sm-4 mb-3 mb-sm-0"></div>
                                                    <div class="col-sm-4 mb-3 mb-sm-0">
                                                         <asp:LinkButton ID="trainAddSchecdule" OnClick="TrainAddScheduleBtn_Click" runat="server" class="btn mb-1 rounded-pill btn-primary">Add Schedule</asp:LinkButton>
                                                    </div>
                                                    <div class="col-sm-4 mb-3 mb-sm-0"></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-4">
            <div class="card o-hidden border-0 shadow-lg my-5">
                <div class="card-body p-0">

                    <!-- Bootstrap Card -->
                    <div class="card">
                        <!-- Card Image -->
                        <!--<img id="tabImage" class="card-img-top" alt="Bus Image" style="height: 420px;">-->
                        <img id="tabImage" class="card-img-top" src="/Images/QRBusticket.png" alt="Default Image" style="height: 460px;">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <asp:HiddenField ID="activeTab" runat="server" />

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

    <script src="template/js/card-view.js"></script>
    <script src="template/js/tabImageUpdater.js"></script>
    <script>$(document).ready(function () {
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
    </script>

</asp:Content>
