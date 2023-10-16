<%@ Page Title="" Language="C#" MasterPageFile="~/Master.Master" AutoEventWireup="true" CodeBehind="TicketIssues.aspx.cs" Inherits="TicketingSystem.WebForm5" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    
    <link href="template/css/bootstrap.min.css" rel="stylesheet" />
    <link href="template/css/style.css" rel="stylesheet" />
    <link href="template/css/helper.css" rel="stylesheet" />
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
	
				
	 <div class="container-fluid">
             <!-- /# row -->
          <div class="main-content">
            <div class="row">
              <div class="col-lg-12">
                <div class="card">
                  <div class="card-body">
                    <div class="compose-email">
                      <div class="mail-box">
                        <aside class="sm-side">
                         
                          <div class="inbox-body text-center">
                            
                            <!-- Modal -->
                            <div aria-hidden="true" role="dialog" tabindex="-1" id="myModal" class="modal fade">
                              <div class="modal-dialog">
                                <div class="modal-content text-left">
                                  <div class="modal-body">
                                    <form class="form-horizontal">
                                      <div class="form-group">
                                        <label class="col-lg-2 control-label">To</label>
                                        <div class="col-lg-10">
                                          <input type="text" placeholder="" id="inputEmail1" class="form-control">
                                        </div>
                                      </div>
                                      <div class="form-group">
                                        <label class="col-lg-2 control-label">Cc / Bcc</label>
                                        <div class="col-lg-10">
                                          <input type="text" placeholder="" id="cc" class="form-control">
                                        </div>
                                      </div>
                                      <div class="form-group">
                                        <label class="col-lg-2 control-label">Subject</label>
                                        <div class="col-lg-10">
                                          <input type="text" placeholder="" id="inputPassword1" class="form-control">
                                        </div>
                                      </div>
                                      <div class="form-group">
                                        <label class="col-lg-2 control-label">Message</label>
                                        <div class="col-lg-10">
                                          <textarea rows="10" cols="30" class="form-control" id="texarea1" name="texarea"></textarea>
                                        </div>
                                      </div>

                                      <div class="form-group">
                                        <div class="col-lg-offset-2 col-lg-10">
                                          <span class="btn green fileinput-button"><i class="fa fa-plus fa fa-white"></i>
																	<span>Attachment</span>
                                          <input type="file" name="files[]" multiple="">
                                          </span>
                                          <button class="btn btn-primary" type="submit">Send</button>
                                        </div>
                                      </div>
                                    </form>
                                  </div>
                                </div>
                                <!-- /.modal-content -->
                              </div>
                              <!-- /.modal-dialog -->
                            </div>
                            <!-- /.modal -->
                          </div>
                          <ul class="inbox-nav inbox-divider">
                            <li class="breadcrumb-item active">
                              <a href="#"><i class="fa fa-inbox"></i> Unresponded <span class="badge badge-success pull-right m-t-12">2</span></a>
                            </li>
                            <li>
                              <a href="#"><i class="fa fa-envelope-o"></i> Responded</a>
                            </li>
                            <li>
                              <a href="#"><i class=" fa fa-trash-o"></i> Trash</a>
                            </li>
                          </ul>


                        </aside>
                        <aside class="lg-side">
                          <div class="inbox-head">
                            <h3 class="input-text">Issues</h3>
                            <form action="#" class="pull-right position">
                              <div class="input-append inner-append">
                                <input type="text" class="sr-input" placeholder="Search Mail">
                                <button class="btn sr-btn append-btn" type="button"><i class="fa fa-search"></i></button>
                              </div>
                            </form>
                          </div>
                          <div class="mail-option">
                            <div class="chk-all chk-group">
                              <input type="checkbox" class="mail-checkbox mail-group-checkbox" id="checkAll" />
                              <div class="btn-group">
                                <a data-toggle="dropdown" href="#" class="btn mini all m-l-10" aria-expanded="false">All<i class="fa fa-angle-down "></i></a>
                                <ul class="card-option-dropdown dropdown-menu">
                                  <li><a href="#"> None</a></li>
                                  <li><a href="#"> Read</a></li>
                                  <li><a href="#"> Unread</a></li>
                                </ul>
                              </div>
                            </div>
                       
                            <div class="btn-group">
                              <a data-toggle="dropdown" href="#" class="btn mini blue">Move to<i class="fa fa-angle-down "></i></a>
                              <ul class="card-option-dropdown dropdown-menu">
                                <li><a href="#">Draft</a></li>
                                <li><a href="#">Spam</a></li>
                                <li><a href="#"> Delete</a></li>
                              </ul>
                            </div>

                            <ul class="unstyled inbox-pagination pagination-list">
                              <li><span>1-50 of 234</span></li>
                              <li>
                                <a class="np-btn" href="#"><i class="fa fa-angle-left  pagination-left"></i></a>
                              </li>
                              <li>
                                <a class="np-btn" href="#"><i class="fa fa-angle-right pagination-right"></i></a>
                              </li>
                            </ul>
                          </div>
                          <div class="table-responsive">
                            <table class="table table-inbox table-hover table-responsive">
                              <tbody>
                       
                              </tbody>
                            </table>
                          </div>

                        </aside>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
        
          </div>
        </div>
  
			
</asp:Content>
