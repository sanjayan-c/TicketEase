using MySql.Data.MySqlClient;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace TicketingSystem
{
    public partial class Dashboard : System.Web.UI.Page
    {
        string strcon = ConfigurationManager.ConnectionStrings["con"].ConnectionString;
        protected void Page_Load(object sender, EventArgs e)
        {
            loadBusCount();
            loadTrainCount();
            loadPassengers();
            loadMonthlyEarnings();

        }

        void loadBusCount()
        {
            using (MySqlConnection conn = new MySqlConnection(strcon))
            {
                conn.Open();
                string query = "SELECT COUNT(*) FROM Bus";
                using (MySqlCommand command = new MySqlCommand(query, conn))
                {
                    // Execute the query
                    int busCount = Convert.ToInt32(command.ExecuteScalar());

                    // Close the database connection
                    conn.Close();
                    TotalBusCount.Text = busCount.ToString();


                }
            }
          
           
        }

        void loadTrainCount()
        {
            using (MySqlConnection conn = new MySqlConnection(strcon))
            {
                conn.Open();
                string query = "SELECT COUNT(*) FROM Train";
                using (MySqlCommand command = new MySqlCommand(query, conn))
                {
                    // Execute the query
                    int trainCount = Convert.ToInt32(command.ExecuteScalar());

                    // Close the database connection
                    conn.Close();
                    TotalTrainCount.Text = trainCount.ToString();


                }
            }
        }

        void loadPassengers()
        {
            using (MySqlConnection conn = new MySqlConnection(strcon))
            {
                conn.Open();
                string query = "SELECT COUNT(*) FROM customer";
                using (MySqlCommand command = new MySqlCommand(query, conn))
                {
                    // Execute the query
                    int passenger = Convert.ToInt32(command.ExecuteScalar());

                    // Close the database connection
                    conn.Close();
                    totalPassenger.Text = passenger.ToString();


                }
            }
        }

        void loadMonthlyEarnings()
        {
            using (MySqlConnection conn = new MySqlConnection(strcon))
            {
                string query = "SELECT SUM(price) AS MonthlyEarnings FROM CustomerPayment";
                using (MySqlCommand command = new MySqlCommand(query, conn))
                {
                    conn.Open();

                    object result = command.ExecuteScalar();

                    conn.Close();

                    if (result != DBNull.Value)
                    {
                        double monthlyEarnings = Convert.ToDouble(result);
                        monthlyearning.Text = monthlyEarnings.ToString();
                    }
                }
            }
        }



    }
}