<%@ Page Title="" Language="C#" MasterPageFile="~/Master.Master" AutoEventWireup="true" CodeBehind="ViewTimeTable.aspx.cs" Inherits="TicketingSystem.WebForm2" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <link href="template/css/Canteen.css" rel="stylesheet" />
    <link href="datatables/css/cdn.datatables.net_1.13.6_css_jquery.dataTables.min.css" rel="stylesheet" />

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <%-- <script type="text/javascript" charset="utf-8" src=" https://cdn.datatables.net/searchbuilder/1.4.2/js/dataTables.searchBuilder.min.js"></script>--%>
    <script type="text/javascript">
        $(document).ready(function () {
            //$(".table").prepend($(<thead></thead>).append($(this).find("tr:first"))).dataTable();
            //$('#Train_schedule').DataTable();
            $("#Bus_schedule").dataTable();

            // Initialize the DataTable for the table with the id "Train_schedule"
            $("#Train_schedule").dataTable();

        });
    </script>

</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="container-fluid">

        <!-- Page Heading -->
    </div>



    <!-- DataTales Example -->
    <div class="card shadow mb-4">
        <div class="card-header">
            <div class="row">
                <div class="col-lg-4"></div>
                <div class="col-lg-4">
                    <h5 style="text-align: center" class="m-0 font-weight-bold text-primary"><b>Scheduled TimeTable</b></h5>
                </div>
                <div class="col-lg-4"></div>

            </div>

        </div>
        <div class="card-body p-1">
            <div class="table-responsive">
                <div id="dataTable_wrapper" class="dataTables_wrapper dt-bootstrap4">

                    <div class="row">
                        <div class="col-sm-12">
                            <div class="p-2">

                                <ul class="nav nav-tabs" id="myTabs" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active" id="bus-tab" data-toggle="tab" href="#bus" role="tab" aria-controls="bus" aria-selected="true">Bus</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="train-tab" data-toggle="tab" href="#train" role="tab" aria-controls="train" aria-selected="false">Train</a>
                                    </li>
                                </ul>
                            </div>

                            <div class="tab-content" id="myTabContent">
                                <div class="tab-pane fade show active" id="bus" role="tabpanel" aria-labelledby="bus-tab">
                                    <!-- DataTable for Bus goes here -->
                                    <table class="table table-bordered dataTable" id="Bus_schedule" runat="server" width="100%" cellspacing="0" aria-describedby="dataTable_info">
                                        <thead>
                                            <tr>
                                                <th>BusNo</th>
                                                <th>Date</th>
                                                <th>Day</th>
                                                <th>Start Time</th>
                                                <th>End Time</th>
                                                <th>Expiry Date</th>
                                                <th>Route No</th>
                                                <th>Start Location</th>
                                                <th>End Location</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>

                                        <tfoot>
                                            <tr>

                                                <th>BusNo</th>
                                                <th>Date</th>
                                                <th>Day</th>
                                                <th>Start Time</th>
                                                <th>End Time</th>
                                                <th>Expiry Date</th>
                                                <th>Route No</th>
                                                <th>Start Location</th>
                                                <th>End Location</th>
                                                <th>Action</th>

                                            </tr>
                                        </tfoot>
                                        <tbody id="tbodyBusScheduleData">
                                        </tbody>
                                    </table>
                                </div>
                                <div class="tab-pane fade" id="train" role="tabpanel" aria-labelledby="train-tab">
                                    <table class="table table-bordered dataTable" id="Train_schedule" runat="server" width="100%" cellspacing="0" aria-describedby="dataTable_info">
                                        <thead>
                                            <tr>
                                                <th class="Id" style="display:none">BSId</th>
                                                <th class="">Train No</th>
                                                <th>Date</th>
                                                <th>Day</th>
                                                <th>Start Time</th>
                                                <th>End Time</th>
                                                <th>Expiry Date</th>
                                                <th>Route Line</th>
                                                <th>Start Location</th>
                                                <th>End Location</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tfoot>
                                            <tr>
                                                <th style="display:none">TSId</th>
                                                <th>Train No</th>
                                                <th>Date</th>
                                                <th>Day</th>
                                                <th>Start Time</th>
                                                <th>End Time</th>
                                                <th>Expiry Date</th>
                                                <th>Route Line</th>
                                                <th>Start Location</th>
                                                <th>End Location</th>
                                                <th>Action</th>
                                            </tr>
                                        </tfoot>
                                        <tbody id="tbodyTrainScheduleData">
                                        </tbody>
                                    </table>

                                </div>
                            </div>
                        </div>


                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade show" id="updtBsSchdlMdl" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" style="max-width: 70%;" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title text-center" id="exampleModalLabel">Update Bus Schedule</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div>

                        <div class="user" method="get">
                            <div class="form-group row">
                                <div class="col-sm-12 mb-3 mb-sm-0">
                                    <asp:DropDownList CssClass="form-control" ID="updtDdlNum" runat="server">
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
                                    <asp:TextBox ID="updtTxtRouteNum" class="form-control form-control-user" Placeholder="Enter Bus Route Num" runat="server"></asp:TextBox>
                                </div>
                                <div class="col-sm-6">
                                    <asp:TextBox ID="updtTxtBookingDate" TextMode="Date" class="form-control form-control-user" runat="server"></asp:TextBox>
                                </div>
                            </div>
                            <label for="category">Select Time for schedule</label>
                            <div class="form-group row">
                                <div class="col-sm-6 mb-3 mb-sm-0">
                                    <div class="input-group clockpicker">
                                        <asp:TextBox ID="updtTxtFromTime" class="form-control form-control-user" runat="server" Placeholder="Enter Start Time"></asp:TextBox>
                                        <span class="input-group-append"><span class="input-group-text"><i class="fa fa-clock-o"></i></span></span>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <asp:TextBox ID="updtTxtToTime" class="form-control form-control-user" Placeholder="Enter End Time" runat="server"></asp:TextBox>
                                </div>
                            </div>
                            <div class="form-group row">
                                <div class="col-sm-6 mb-3 mb-sm-0">
                                    <asp:TextBox ID="updtTxtStartLoc" class="form-control form-control-user" Placeholder="Enter Start Location" runat="server"></asp:TextBox>
                                </div>
                                <div class="col-sm-6">
                                    <asp:TextBox ID="updtTxtEndLoc" class="form-control form-control-user" Placeholder="Enter End Location" runat="server"></asp:TextBox>
                                </div>
                            </div>
                        
                        <div class="modal-footer">
                            <button class="btn btn-secondary" id="btnDelete" type="button" data-dismiss="modal">Cancel</button>
                            <button class="btn btn-Update btn-primary" id="btnUpdate" type="button">Update</button>
                        </div>
                            </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <div class="modal fade show" id="DeleteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="DelModalLabel">Delete?</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">Are you sure you want to delete?</div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                    <button class="btn btn-delete btn-primary" id="btndelete" type="submit">Delete</button>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.js"></script>
    <script src="template/js/ViewTable.js"></script>
    <%--<script src="js/JarvisCustom/ViewCanteenInventory.js"></script>--%>
    <script>
        // Call the JavaScript function from the external file
        var bookId = "your-book-id";
        getBookDetails(bookId);
    </script>

</asp:Content>

