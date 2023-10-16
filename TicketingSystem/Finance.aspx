<%@ Page Title="" Language="C#" MasterPageFile="~/Master.Master" AutoEventWireup="true" CodeBehind="Finance.aspx.cs" Inherits="TicketingSystem.WebForm4" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div class="container-fluid">
        <div class="card shadow mb-4">
            <div class="card-body">                   
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="p-4">

                                    <ul class="nav nav-tabs" id="myTabs" role="tablist">
                                        <li class="nav-item">
                                            <a class="nav-link active" id="Rate-tab" data-toggle="tab" href="#rate" role="tab" aria-controls="rate" aria-selected="true">Bus</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="Revenue-tab" data-toggle="tab" href="#revenue" role="tab" aria-controls="revenue" aria-selected="false">Train</a>
                                        </li>
                                    </ul>
                                </div>

                                <div class="tab-content" id="myTabContent">
                                    <div class="tab-pane fade show active" id="rate" role="tabpanel" aria-labelledby="Rate-tab">

                                        <div class="p-4">
                                                     <div class="user" method="get">
                                            <div class="form-group row">
                                                <div class="col-sm-2 mb-3 mb-sm-0">
                                                    <asp:DropDownList CssClass="form-control" ID="DropDownList1" runat="server">
                                                        <asp:ListItem Text="BusNo" Value="" />
                                                    </asp:DropDownList>
                                                </div>
                                                <div class="col-sm-1 mb-3 mb-sm-0">
                                                </div>

                                                <div class="col-sm-2 mb-3 mb-sm-0">
                                                    <asp:DropDownList CssClass="form-control" ID="DropDownList2" runat="server">
                                                        <asp:ListItem Text="RouteNum" Value="" />
                                                    </asp:DropDownList>
                                                </div>
                                                <div class="col-sm-1 mb-3 mb-sm-0">
                                                </div>

                                                <div class="col-sm-2 mb-3 mb-sm-0">
                                                    <asp:TextBox ID="TextBox1" TextMode="SingleLine" class="form-control form-control-user" runat="server">Time</asp:TextBox>
                                                </div>
                                                <div class="col-sm-1 mb-3 mb-sm-0">
                                                </div>

                                                <div class="col-sm-2 mb-3 mb-sm-0">
                                                    <asp:TextBox ID="TextBox2" TextMode="Date" class="form-control form-control-user" runat="server"></asp:TextBox>
                                                </div>
                                                <div class="col-sm-1 mb-3 mb-sm-0">
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <div class="col-sm-4 mb-3 mb-sm-0"></div>
                                                <div class="col-sm-4 mb-3 mb-sm-0">
                                                  <a href="login.html" class="btn btn-primary btn-user">Confirm</a>
                                                </div>
                                                <div class="col-sm-4 mb-3 mb-sm-0"></div>
                                            </div>
                                        </div>
                                        </div>

                                    </div>
                                    <div class="tab-pane fade" id="revenue" role="tabpanel" aria-labelledby="Revenue-tab">
                                        <div class ="p-4">
                                        <div class="user" method="get">
                                            <div class="form-group row">
                                                <div class="col-sm-2 mb-3 mb-sm-0">
                                                    <asp:DropDownList CssClass="form-control" ID="ddlBusNum" runat="server">
                                                        <asp:ListItem Text="TrainNo" Value="" />
                                                    </asp:DropDownList>
                                                </div>
                                                <div class="col-sm-1 mb-3 mb-sm-0">
                                                </div>

                                                <div class="col-sm-2 mb-3 mb-sm-0">
                                                    <asp:DropDownList CssClass="form-control" ID="ddlRouteNo" runat="server">
                                                        <asp:ListItem Text="RouteLine" Value="" />
                                                    </asp:DropDownList>
                                                </div>
                                                <div class="col-sm-1 mb-3 mb-sm-0">
                                                </div>

                                                <div class="col-sm-2 mb-3 mb-sm-0">
                                                    <asp:TextBox ID="txtDate" TextMode="SingleLine" class="form-control form-control-user" runat="server">Time</asp:TextBox>
                                                </div>
                                                <div class="col-sm-1 mb-3 mb-sm-0">
                                                </div>

                                                <div class="col-sm-2 mb-3 mb-sm-0">
                                                    <asp:TextBox ID="txtTime" TextMode="Date" class="form-control form-control-user" runat="server"></asp:TextBox>
                                                </div>
                                                <div class="col-sm-1 mb-3 mb-sm-0">
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <div class="col-sm-4 mb-3 mb-sm-0"></div>
                                                <div class="col-sm-4 mb-3 mb-sm-0">
                                                  <a href="login.html" class="btn btn-primary btn-user">Confirm</a>
                                                </div>
                                                <div class="col-sm-4 mb-3 mb-sm-0"></div>
                                            </div>
                                        </div>
                                            </div>
                                    </div>
                                </div>
                            </div>


                        </div>
                <div class="row">
            <div class="col-xl-6">
            <!-- Bar Chart -->

            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Revenue Chart</h6>
                </div>
                <div class="card-body">
                    <div class="chart-bar">
                        <canvas id="myBarChart"></canvas>
                    </div>

                </div>
            </div>
                </div>

                
       <div class="col-xl-6">
    <!-- Bar Chart -->

    <div class="col-xl-10 col-md-6 mb-4">
                            <div class="card border-left-primary shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                                Total Income Earned</div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"><span id="ContentPlaceHolder1_monthlyearning">520</span>/=</div>
                                        </div>
                                        <div class="col-auto">
                                            <i class="fas fa-calendar fa-2x text-gray-300"></i>
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
    <script src="template/js/demo/chart-bar-demo.js"></script>
</asp:Content>
