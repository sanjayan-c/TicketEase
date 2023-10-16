using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using MySql.Data.MySqlClient;
using System.Diagnostics;
using System.Drawing;

namespace TicketingSystem
{
    public partial class WebForm1 : System.Web.UI.Page
    {
        string strcon = ConfigurationManager.ConnectionStrings["con"].ConnectionString;

        protected void Page_Load(object sender, EventArgs e)
        {

            if (!IsPostBack)
            {
                string activeTabValue = activeTab.Value;

                if (activeTabValue == "Bus")
                {
                    fillBusNumberVal();
                }
                else if (activeTabValue == "Train")
                {
                    
                }                
                else
                {
                    // Default case (e.g., on initial page load)
                    fillBusNumberVal();
                }

                // Add "BusNo" as the default item
                ddlBusNum.Items.Insert(0, new ListItem("BusNo", ""));

                // Add "TrainNo" as the default item for the train dropdown
                ddlTrainNo.Items.Insert(0, new ListItem("TrainNo", ""));
            }


        }
        //Add BusScheduleLink
        protected void BusAddScheduleBtn_Click(object sender, EventArgs e)
        {
            addBusSchedule();
        }

        protected void TrainAddScheduleBtn_Click(object sender, EventArgs e)
        {
            addTrainSchedule();
        }



        void fillBusNumberVal()
        {
            MySqlConnection con = new MySqlConnection(strcon);
            try
               
            {
                
                if (con.State == ConnectionState.Closed)
                {
                    con.Open();
                }
                MySqlCommand cmd = new MySqlCommand("Select busNo from Bus;",con);
                MySqlDataAdapter da = new MySqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);
                ddlBusNum.DataTextField = "busNo";
                ddlBusNum.DataSource= dt;
                ddlBusNum.DataValueField = "busNo";
                ddlBusNum.DataBind();
                    

                }
            catch(Exception e) {
                Debug.WriteLine("Exception: " + e.Message);
            }
            finally
            {
                con.Close(); // Close the connection in the finally block
            }
        }


        void addBusSchedule()
        {
            MySqlConnection con = new MySqlConnection(strcon);
            MySqlCommand cmd = new MySqlCommand();

            try
            {
               
                    con.Open();
                    cmd.Connection = con;

                cmd.CommandText = "INSERT INTO Bus_schedule (busNo, Date, FromTime, ToTime, RouteNo, StartLocation, EndLocation, CreatedDateTime, UpdatedDateTime) Values(@busNo, @Date, @FromTime, @ToTime, @RouteNo, @StartLocation, @EndLocation, now(), now())";
                cmd.Parameters.AddWithValue("@busNo", ddlBusNum.Text.Trim());
                cmd.Parameters.AddWithValue("@Date", txtBookingDate.Text.Trim());
                cmd.Parameters.AddWithValue("@FromTime", txtfromBusTime.Text.Trim());
                cmd.Parameters.AddWithValue("@ToTime", textBusToTime.Text.Trim());
                cmd.Parameters.AddWithValue("@RouteNo", txtRouteNum.Text.Trim());
                cmd.Parameters.AddWithValue("@StartLocation", txtBusStartLoc.Text.Trim());
                cmd.Parameters.AddWithValue("@EndLocation", txtBusEndLoc.Text.Trim());

                cmd.ExecuteNonQuery();
                Response.Write("<script> alert('Bus Schedule added successfully');</script>");



            }
            catch (Exception e){
                Response.Write("Error: " + e.Message);
            }
            finally
            {
                con.Close();
            }

        }


        void fillTrainNumberVal()
        {
            MySqlConnection con = new MySqlConnection(strcon);
            try
            {
                con.Open();
                MySqlCommand cmd = new MySqlCommand("Select trainNo from Train;", con);
                using (MySqlDataAdapter da = new MySqlDataAdapter(cmd))
                {
                    DataTable dt = new DataTable();
                    da.Fill(dt);
                    ddlTrainNo.DataTextField = "trainNo";
                    ddlTrainNo.DataSource = dt;
                    ddlTrainNo.DataBind();
                }
            }
            catch (Exception e)
            {
                Debug.WriteLine("Exception: " + e.Message);
            }
            finally
            {
                con.Close(); // Close the connection in the finally block
            }
        }


        void addTrainSchedule()
        {
            MySqlConnection con = new MySqlConnection(strcon);
            MySqlCommand cmd = new MySqlCommand();

            try
            {

                con.Open();
                cmd.Connection = con;

                cmd.CommandText = "INSERT INTO Train_schedule (trainNo, Date, FromTime, ToTime, RouteLine, StartLocation, EndLocation, CreatedDateTime, UpdatedDateTime) Values(@trainNo, @Date, @FromTime, @ToTime, @RouteLine, @StartLocation, @EndLocation, now(), now())";
                cmd.Parameters.AddWithValue("@trainNo", ddlTrainNo.Text.Trim());
                cmd.Parameters.AddWithValue("@Date", txtTrainBookDate.Text.Trim());
                cmd.Parameters.AddWithValue("@FromTime", txtTrainFromTime.Text.Trim());
                cmd.Parameters.AddWithValue("@ToTime", txtTrainToTime.Text.Trim());
                cmd.Parameters.AddWithValue("@Routeline", txtTrainRoute.Text.Trim());
                cmd.Parameters.AddWithValue("@StartLocation", txtTrainStartLoc.Text.Trim());
                cmd.Parameters.AddWithValue("@EndLocation", txtTrainEndLoc.Text.Trim());

                cmd.ExecuteNonQuery();
                Response.Write("<script> alert('Bus Schedule added successfully');</script>");



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