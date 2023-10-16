using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace TicketingSystem
{
    public partial class Master : System.Web.UI.MasterPage
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void TmtblLnkBtn_Click(object sender, EventArgs e)
        {
            Response.Redirect("ViewTimeTable.aspx");
        }

        protected void SheduleLnkBtn_Click(object sender, EventArgs e)
        {
            Response.Redirect("AddSchedule.aspx");
        }

        protected void MngovrcrowdLnkBtn_Click(object sender, EventArgs e)
        {
            Response.Redirect("Mngecrowding.aspx");
        }

        protected void FnceLnkBtn_Click(object sender, EventArgs e)
        {
            Response.Redirect("Finance.aspx"); 
        }

        protected void RptLnkBtn_Click(object sender, EventArgs e)
        {
            Response.Redirect("Reports.aspx");
        }

        protected void TcktIsuLnkBtn_Click(object sender, EventArgs e)
        {
            Response.Redirect("TicketIssues.aspx");
        }
    }
}