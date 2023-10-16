<%@ Page Title="" Language="C#" MasterPageFile="~/Master.Master" AutoEventWireup="true" CodeBehind="Mngecrowding.aspx.cs" Inherits="TicketingSystem.WebForm3" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
      <div class="container-fluid">
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
    </div>
</asp:Content>
