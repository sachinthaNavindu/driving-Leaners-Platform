import { getAccessToken, fetchWithAuth, checkToken, redirectToLogin } from '../Auth.js';

let dataLoaded = false;
let influenceDataLoaded = false;

document.addEventListener("DOMContentLoaded", async function () {
  checkToken();

  const toggleButton = document.getElementById("toggleTableBtn");
  const paymentTable = document.getElementById("paymentTable");
  const searchBtn = document.getElementById("searchBtn");
  const influenceBtn = document.getElementById("toggleInfluenceBtn");
  const influenceTable = document.getElementById("influenceTable");
  const exportInfluenceBtn = document.getElementById("exportInfluenceBtn");

  toggleButton.addEventListener("click", async function () {
    if (paymentTable.style.display === "none") {
      paymentTable.style.display = "block";
      toggleButton.innerHTML =
        '<i class="fas fa-eye-slash"></i> Hide Payment History';

      if (!dataLoaded) {
        await loadPaymentDetails();
        dataLoaded = true;
      }
    } else {
      paymentTable.style.display = "none";
      toggleButton.innerHTML =
        '<i class="fas fa-money-bill-wave"></i> Cash Payment History';
    }
  });

  searchBtn.addEventListener("click", performSearch);

  influenceBtn.addEventListener("click", async function () {
    if (influenceTable.style.display === "none") {
      influenceTable.style.display = "block";
      influenceBtn.innerHTML =
        '<i class="fas fa-eye-slash"></i> Hide Influential Payments';

      if (!influenceDataLoaded) {
        await loadInfluencePayments();
        influenceDataLoaded = true;
      }
    } else {
      influenceTable.style.display = "none";
      influenceBtn.innerHTML =
        '<i class="fas fa-chart-line"></i> Influential Payments';
    }
  });

  exportInfluenceBtn.addEventListener("click", async () => {
    if (!influenceDataLoaded) {
      await loadInfluencePayments();
      influenceDataLoaded = true;
    }
    exportInfluentialPayments();
  });

  if (typeof showMessage !== "function") {
    window.showMessage = function (message, type) {
      alert(`${type.toUpperCase()}: ${message}`);
    };
  }
});

async function loadPaymentDetails() {
  try {
    document.getElementById("loadingIndicator").style.display = "block";
    document.getElementById("paymentDataTable").style.display = "none";
    document.getElementById("noResultsMessage").style.display = "none";

    const response = await fetchWithAuth("http://localhost:8080/payments/loadPayments", {
      headers: { "Content-Type": "application/json" },
    });

    if (!response || !response.ok) throw new Error(`HTTP error! status: ${response?.status}`);

    const result = await response.json();

    if (result?.data?.length) {
      populatePaymentTable(result.data);
      document.getElementById("paymentDataTable").style.display = "table";
    } else {
      document.getElementById("noResultsMessage").style.display = "block";
    }
  } catch (error) {
    console.error("Error loading payment data:", error);
    document.getElementById("noResultsMessage").textContent =
      "Error loading payment data. Please try again.";
    document.getElementById("noResultsMessage").style.display = "block";
  } finally {
    document.getElementById("loadingIndicator").style.display = "none";
  }
}

function populatePaymentTable(payments) {
  const tableBody = document.getElementById("paymentTableBody");
  tableBody.innerHTML = "";

  payments.forEach((payment) => {
    const row = document.createElement("tr");

    row.innerHTML = `
      <td class="payment-id">${payment.paymentID}</td>
      <td class="payment-amount">${payment.paidAmount.toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</td>
      <td>${payment.paymentDate || "N/A"}</td>
      <td>${payment.paidTime || "N/A"}</td>
      <td>${payment.courseName || "N/A"}</td>
      <td>${payment.studentNic || "N/A"}</td>
      <td>
        <button class="action-button print-btn" title="Print Receipt" data-payment='${JSON.stringify(payment)}'>
          <i class="fas fa-print"></i>
        </button>
        <button class="action-button delete-btn" title="Delete Payment">
          <i class="fas fa-trash"></i>
        </button>
      </td>
    `;

    row.querySelector(".delete-btn").addEventListener("click", () => deletePayment(payment.paymentID));
    row.querySelector(".print-btn").addEventListener("click", () => generatePaymentSlip(payment));

    tableBody.appendChild(row);
  });

  attachSearchListener();
}

async function deletePayment(paymentId) {
  try {
    const response = await fetchWithAuth(`http://localhost:8080/payments/deletePayment/${paymentId}`, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
    });

    const result = await response.json();
    alert(result.data);

    const row = Array.from(document.querySelectorAll("#paymentTableBody tr"))
      .find(r => r.querySelector(".payment-id").textContent == paymentId);
    if (row) row.remove();
  } catch (error) {
    alert("Delete failed: " + error);
  }
}

function performSearch() {
  const searchText = document.getElementById("searchInput").value.toLowerCase();
  const tableRows = document.querySelectorAll("#paymentTableBody tr");

  let hasResults = false;
  tableRows.forEach((row) => {
    const found = Array.from(row.querySelectorAll("td")).some(td => td.textContent.toLowerCase().includes(searchText));
    row.style.display = found ? "" : "none";
    if (found) hasResults = true;
  });

  document.getElementById("noResultsMessage").style.display = hasResults ? "none" : "block";
}

function attachSearchListener() {
  document.getElementById("searchInput").addEventListener("input", performSearch);
}

async function loadInfluencePayments() {
  try {
    document.getElementById("loadingInfluenceIndicator").style.display = "block";
    document.getElementById("influenceDataTable").style.display = "none";
    document.getElementById("noInfluenceResults").style.display = "none";

    const response = await fetchWithAuth("http://localhost:8080/payments/loadInfluencePayments", {
      headers: { "Content-Type": "application/json" },
    });

    if (!response || !response.ok) throw new Error(`HTTP error! status: ${response?.status}`);

    const result = await response.json();
    const tableBody = document.getElementById("influenceTableBody");
    tableBody.innerHTML = "";

    if (result?.data?.length) {
      result.data.forEach((payment) => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${payment.paymentID || "N/A"}</td>
          <td>${payment.studentNic || "N/A"}</td>
          <td>${payment.paymentDate || "N/A"}</td>
          <td>${payment.paidAmount?.toLocaleString("en-US", { minimumFractionDigits: 2 }) || "0.00"}</td>
          <td>${payment.influentAmount?.toLocaleString("en-US", { minimumFractionDigits: 2 }) || "0.00"}</td>
        `;
        tableBody.appendChild(row);
      });

      document.getElementById("influenceDataTable").style.display = "table";
    } else {
      document.getElementById("noInfluenceResults").style.display = "block";
    }
  } catch (error) {
    console.error("Error loading influential payments:", error);
    document.getElementById("noInfluenceResults").textContent =
      "Error loading data. Please try again.";
    document.getElementById("noInfluenceResults").style.display = "block";
  } finally {
    document.getElementById("loadingInfluenceIndicator").style.display = "none";
  }
}

function exportInfluentialPayments() {
  const doc = new window.jspdf.jsPDF(); // use window.jspdf.jsPDF

  const primaryColor = [26, 42, 108];
  const lightGray = [248, 249, 250];
  const textColor = [33, 37, 41];

  doc.setFillColor(...primaryColor);
  doc.rect(0, 0, 210, 35, "F");
  doc.setTextColor(255, 255, 255);
  doc.setFontSize(20);
  doc.setFont("helvetica", "bold");
  doc.text("DRIVEMASTER", 105, 15, { align: "center" });
  doc.setFontSize(10);
  doc.setFont("helvetica", "normal");
  doc.text("Driving School - Payment Management System", 105, 25, { align: "center" });

  doc.setTextColor(...textColor);
  doc.setFontSize(16);
  doc.setFont("helvetica", "bold");
  doc.text("INFLUENTIAL PAYMENT RECORDS", 105, 50, { align: "center" });

  const currentDate = new Date();
  const formattedDate = currentDate.toLocaleDateString("en-US", { year: "numeric", month: "long", day: "numeric" });
  const formattedTime = currentDate.toLocaleTimeString("en-US", { hour: "2-digit", minute: "2-digit" });

  doc.setFontSize(9);
  doc.setFont("helvetica", "normal");
  doc.setTextColor(100, 100, 100);
  doc.text(`Generated on: ${formattedDate} at ${formattedTime}`, 20, 60);
  doc.text(`Document ID: RPT-${Date.now()}`, 20, 67);

  const tableData = [];
  let totalAmount = 0;
  let totalInfluentialAmount = 0;
  document.querySelectorAll("#influenceTableBody tr").forEach(row => {
    const cells = row.querySelectorAll("td");
    if (cells.length >= 5) {
      const paid = parseFloat(cells[3].textContent.replace(/[^0-9.-]/g, "")) || 0;
      const influent = parseFloat(cells[4].textContent.replace(/[^0-9.-]/g, "")) || 0;
      totalAmount += paid;
      totalInfluentialAmount += influent;
      tableData.push([
        cells[0].textContent,
        cells[1].textContent,
        cells[2].textContent,
        `LKR ${paid.toLocaleString("en-US", { minimumFractionDigits: 2 })}`,
        `LKR ${influent.toLocaleString("en-US", { minimumFractionDigits: 2 })}`
      ]);
    }
  });

  const headers = [["Payment ID", "Student NIC", "Date", "Paid Amount", "Influential Amount"]];

  if (doc.autoTable) {
    doc.autoTable({
      head: headers,
      body: tableData,
      startY: 75,
      theme: "grid",
      headStyles: { fillColor: primaryColor, textColor: 255, fontSize: 10, fontStyle: "bold", halign: "center" },
      bodyStyles: { fontSize: 9, cellPadding: 4, minCellHeight: 10 },
      alternateRowStyles: { fillColor: lightGray },
      columnStyles: { 0: { halign: "center" }, 1: { halign: "center" }, 2: { halign: "center" }, 3: { halign: "right" }, 4: { halign: "right" } }
    });

    const finalY = doc.lastAutoTable.finalY || 150;
    doc.setFillColor(...lightGray);
    doc.rect(10, finalY + 15, 190, 35, "F");
    doc.setDrawColor(...primaryColor);
    doc.setLineWidth(0.5);
    doc.rect(10, finalY + 15, 190, 35);

    doc.setTextColor(...textColor);
    doc.setFontSize(11);
    doc.setFont("helvetica", "bold");
    doc.text("SUMMARY", 15, finalY + 25);

    doc.setFont("helvetica", "normal");
    doc.setFontSize(9);
    doc.text(`Total Records: ${tableData.length}`, 15, finalY + 33);
    doc.text(`Total Paid Amount: LKR ${totalAmount.toLocaleString("en-US", { minimumFractionDigits: 2 })}`, 15, finalY + 40);
    doc.text(`Total Influential Amount: LKR ${totalInfluentialAmount.toLocaleString("en-US", { minimumFractionDigits: 2 })}`, 15, finalY + 47);

    const pageHeight = doc.internal.pageSize.height;
    doc.setFillColor(...primaryColor);
    doc.rect(0, pageHeight - 20, 210, 20, "F");
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(8);
    doc.text("This is a computer-generated document. No signature required.", 105, pageHeight - 12, { align: "center" });
    doc.text("For inquiries, contact: info@drivemaster.com | +94 11 234 5678", 105, pageHeight - 6, { align: "center" });
  }

  const dateStr = currentDate.toISOString().slice(0, 10);
  const timeStr = currentDate.toTimeString().slice(0, 8).replace(/:/g, "-");
  doc.save(`Influential_Payments_${dateStr}_${timeStr}.pdf`);
}