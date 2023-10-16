<%@ Page Title="" Language="C#" MasterPageFile="~/Master.Master" AutoEventWireup="true" CodeBehind="Dashboard.aspx.cs" Inherits="TicketingSystem.Dashboard" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="container-fluid">

        <div class="d-sm-flex align-items-center justify-content-between mb-4">
                        <h1 class="h3 mb-0 text-gray-800">Dashboard</h1>
                       
                    </div>
<div class="row">

                        <!-- Earnings (Monthly) Card Example -->
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-primary shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                                Earnings (Monthly)</div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800" ><asp:Label ID="monthlyearning" runat="server"></asp:Label>/=</div>
                                        </div>
                                        <div class="col-auto">
                                            <i class=" fas fa-dollar-sign fa-2x text-gray-300"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Earnings (Monthly) Card Example -->
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-success shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                                Total pasengers</div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"><asp:Label ID="totalPassenger" runat="server"></asp:Label></div>
                                        </div>
                                        <div class="col-auto">
                                            <i class=" fas fa-users fa-2x text-gray-300"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Earnings (Monthly) Card Example -->
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-info shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-info text-uppercase mb-1">Total Bus Count
                                            </div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"> <asp:Label ID="TotalBusCount" runat="server"></asp:Label></div>
                                        </div>
                                        <div class="col-auto">
                                            <i class="fas fa-bus fa-2x text-gray-300"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-warning shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                                Total Train Count</div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"><asp:Label ID="TotalTrainCount" runat="server"></asp:Label></div>
                                        </div>
                                        <div class="col-auto">
                                            <i class="fas fa-train fa-2x text-gray-300"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
    <br />
    <div class="row">

        <div class="col-xl-6 col-lg-7">
            <!-- Bar Chart -->

            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Total Revenue</h6>
                </div>
                <div class="card-body">
                    <div class="chart-bar">
                        <canvas id="myBarChart"></canvas>
                    </div>

                </div>
            </div>
        </div>

        <div class="col-xl-6 col-lg-7">
            <!-- Bar Chart -->

            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Over Crowding Routes</h6>
                </div>
                <div class="card-body">
                    <div class="row">
     <table class="table table-bordered dataTable" id="Bus_schedule" runat="server" width="100%" cellspacing="0" aria-describedby="dataTable_info">
                                        <thead>
                                            <tr>
                                                <th>BusRoute</th>
                                                <th>Date</th>
                                                <th>BusCount</th>
                                                <th>Passenger Count</th>
                                            </tr>
                                        </thead>
                                        <tbody id="tbodyBusCrwdData">
                                            <tr>
                                            <td>100</td>
                                            <td> 2023-10-29
                                            </td>
                                                <td>10</td>
                                                <td>150</td>
                                                </tr>
                                        </tbody>
                                    </table>
                        </div>
                      <div class="row">
                        </div>
                    </div>
                
                
            </div>
        </div>

        <div class="col-xl-5 col-lg-7">
        </div>
    </div>

        <div class="row">

        

        <div class="col-xl-6 col-lg-7">
            <!-- Bar Chart -->

        </div>

        <div class="col-xl-5 col-lg-7">
        </div>
    </div>
        </div>
    <script src="template/js/demo/chart-bar-demo.js"></script>
</asp:Content>
