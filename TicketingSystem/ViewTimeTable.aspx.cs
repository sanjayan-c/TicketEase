using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using MySql.Data.MySqlClient;

namespace TicketingSystem
{
    public partial class WebForm2 : System.Web.UI.Page
    {

        string strcon = ConfigurationManager.ConnectionStrings["con"].ConnectionString;

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                LoadBusData();
                LoadTrainData();
            }
        }


        protected void LoadBusData()
        {
            using (MySqlConnection conn = new MySqlConnection(strcon))

            {
                conn.Open();
                string query = "SELECT * FROM Bus_schedule"; // Your SQL query to fetch Bus data

                using (MySqlCommand cmd = new MySqlCommand(query, conn))
                {
                    using (MySqlDataAdapter sda = new MySqlDataAdapter(cmd))
                    {
                        DataTable dt = new DataTable();
                        sda.Fill(dt);

                        // Clear the existing rows in the table
                        Bus_schedule.Rows.Clear();

                        // Create and add a table header row
                        HtmlTableRow headerRow = new HtmlTableRow();
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "BusNo" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "Date" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "FromTime" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "ToTime" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "RouteNo" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "StartLocation" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "EndLocation" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "Action" });
                        
                        // Add the other header cells here
                        Bus_schedule.Rows.Add(headerRow);

                        // Iterate through the DataTable and add rows
                        foreach (DataRow row in dt.Rows)
                        {
                            HtmlTableRow dataRow = new HtmlTableRow();
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["BusNo"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["Date"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["FromTime"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["ToTime"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["RouteNo"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["StartLocation"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["EndLocation"].ToString() });
                            
                            // Add buttons in the "Action" column
                            HtmlTableCell actionCell = new HtmlTableCell();
                                HtmlButton editButton = new HtmlButton();
                                editButton.Attributes.Add("class", "btn btn-success btn-circle btn-sm");
                                editButton.InnerHtml = "<i class='fas fa-pen'></i>";
                            HtmlButton deleteButton = new HtmlButton();
                            deleteButton.Attributes.Add("class", "btn btn-danger btn-circle btn-sm");
                            deleteButton.InnerHtml = "<i class='fas fa-trash'></i>";

                            // Attach JavaScript functions to buttons if needed
                            editButton.Attributes.Add("onclick", "editRow(" + row["BusNo"].ToString() + ")");
                            deleteButton.Attributes.Add("onclick", "deleteRow(" + row["BusNo"].ToString() + ")");

                            actionCell.Controls.Add(editButton);
                            actionCell.Controls.Add(deleteButton);
                            dataRow.Cells.Add(actionCell);

                            // Add other cells for the remaining columns
                            Bus_schedule.Rows.Add(dataRow);
                        }
                    }
                }
            }
        }

        protected void LoadTrainData()
        {
            string connectionString = ConfigurationManager.ConnectionStrings["con"].ConnectionString;
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                conn.Open();
                string query = "SELECT trainNo, Date, RouteLine, FromTime, ToTime, StartLocation, EndLocation, CONCAT(TrainPrefix, LPAD(TrainScheduleId, 3, '0')) AS TrainPrimaryKey\r\n FROM Train_schedule"; // Your SQL query to fetch Train data

                using (MySqlCommand cmd = new MySqlCommand(query, conn))
                {
                    using (MySqlDataAdapter sda = new MySqlDataAdapter(cmd))
                    {
                        DataTable dt = new DataTable();
                        sda.Fill(dt);

                        // Clear the existing rows in the table
                        Train_schedule.Rows.Clear();

                        // Create and add a table header row
                        HtmlTableRow headerRow = new HtmlTableRow();
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "TrainNo" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "Date" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "Day" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "FromTime" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "ToTime" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "RouteLine" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "StartLocation" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "EndLocation" });
                        headerRow.Cells.Add(new HtmlTableCell("th") { InnerText = "Action" });

                        // Add the other header cells here
                        Train_schedule.Rows.Add(headerRow);

                        // Iterate through the DataTable and add rows
                        foreach (DataRow row in dt.Rows)
                        {
                            HtmlTableRow dataRow = new HtmlTableRow();
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["TrainNo"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["Date"].ToString() });
                            DateTime dateValue = Convert.ToDateTime(row["Date"]);
                            string dayOfWeek = dateValue.ToString("dddd");

                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = dayOfWeek }); // Add day of the week
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["FromTime"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["ToTime"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["RouteLine"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["StartLocation"].ToString() });
                            dataRow.Cells.Add(new HtmlTableCell() { InnerText = row["EndLocation"].ToString() });

                            var trainPrimaryKeyCell = new HtmlTableCell();
                            trainPrimaryKeyCell.InnerText = row["TrainPrimaryKey"].ToString();
                            trainPrimaryKeyCell.Attributes.Add("class", "invisible-cell");
                            dataRow.Cells.Add(trainPrimaryKeyCell);

                            // Add buttons in the "Action" column
                            HtmlTableCell actionCell = new HtmlTableCell();
                            HtmlButton editButton = new HtmlButton();
                            editButton.Attributes.Add("class", "btn btn-success btn-circle btn-sm");
                            editButton.InnerHtml = "<i class='fas fa-pen'></i>";
                            HtmlButton deleteButton = new HtmlButton();
                            deleteButton.Attributes.Add("class", "btn btn-danger btn-circle btn-sm");
                            deleteButton.InnerHtml = "<i class='fas fa-trash'></i>";

                            // Attach JavaScript functions to buttons if needed
                            editButton.Attributes.Add("onclick", "editRow(" + row["TrainNo"].ToString() + ")");
                            deleteButton.Attributes.Add("onclick", "deleteRow(" + row["TrainNo"].ToString() + ")");

                            actionCell.Controls.Add(editButton);
                            actionCell.Controls.Add(deleteButton);
                            dataRow.Cells.Add(actionCell);

                            // Add other cells for the remaining columns
                            Train_schedule.Rows.Add(dataRow);
                        }
                    }
                }
            }
        }



        //void getBusDetails()
        //{
        //    try
        //    {
        //        MySqlConnection con = new MySqlConnection(strcon);
        //        if (con.State == ConnectionState.Closed)
        //        {
        //            con.Open();
        //        }
        //        MySqlCommand cmd = new MySqlCommand("SELECT * from book_master_tbl WHERE book_id='" + busId + "';", con);
        //        MySqlDataAdapter da = new MySqlDataAdapter(cmd);
        //        DataTable dt = new DataTable();
        //        da.Fill(dt);
        //        if (dt.Rows.Count >= 1)
        //        {
        //            TextBox2.Text = dt.Rows[0]["bus_no"].ToString();
                
                    
        //            DropDownList3.SelectedValue = dt.Rows[0]["bus_no"].ToString().Trim();

                  

        //        }
        //        else
        //        {
        //            Response.Write("<script>alert('Invalid Bus ID');</script>");
        //        }

        //    }
        //    catch (Exception ex)
        //    {

        //    }
        //}

    
    void updateBusSchedule()
        {
            MySqlConnection con = new MySqlConnection(strcon);
            MySqlCommand cmd = new MySqlCommand();

            try
            {

                con.Open();
                cmd.Connection = con;

                cmd.CommandText = "UPDATE Bus_schedule SET (busNo, Date, FromTime, ToTime, RouteNo, StartLocation, EndLocation, CreatedDateTime, UpdatedDateTime) Values(@busNo, @Date, @FromTime, @ToTime, @RouteNo, @StartLocation, @EndLocation, now(), now()) WHERE BusId=@BusId";
                cmd.Parameters.AddWithValue("@busNo", updtDdlNum.Text.Trim());
                cmd.Parameters.AddWithValue("@Date", updtTxtBookingDate.Text.Trim());
                cmd.Parameters.AddWithValue("@FromTime", updtTxtFromTime.Text.Trim());
                cmd.Parameters.AddWithValue("@ToTime", updtTxtToTime.Text.Trim());
                cmd.Parameters.AddWithValue("@RouteNo", updtTxtRouteNum.Text.Trim());
                cmd.Parameters.AddWithValue("@StartLocation", updtTxtStartLoc.Text.Trim());
                cmd.Parameters.AddWithValue("@EndLocation", updtTxtEndLoc.Text.Trim());

                cmd.ExecuteNonQuery();
                Response.Write("<script> alert('Bus Schedule updated successfully');</script>");



            }
            catch (Exception e)
            {
                Response.Write("Error: " + e.Message);
            }
            finally
            {
                con.Close();
            }

        }
    }
}