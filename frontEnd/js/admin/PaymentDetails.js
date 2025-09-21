      const token = localStorage.getItem("token");
      let dataLoaded = false;

      function checkToken() {
        if (!token || token === "undefined" || token === "null") {
          showMessage("Please log in to continue", "error");
          setTimeout(() => {
            window.location.href = "/pages/employeelogin.html";
          }, 1500);
          return;
        }
      }

      function populatePaymentTable(payments) {
        const tableBody = document.getElementById("paymentTableBody");
        tableBody.innerHTML = "";

        payments.forEach((payment) => {
          const row = document.createElement("tr");

          row.innerHTML = `
            <td class="payment-id">${payment.paymentID}</td>
            <td class="payment-amount">${payment.paidAmount.toLocaleString(
              "en-US",
              {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
              }
            )}</td>
            <td>${payment.paymentDate || "N/A"}</td>
            <td>${payment.paidTime || "N/A"}</td>
            <td>${payment.courseName || "N/A"}</td>
            <td>${payment.studentNic || "N/A"}</td>
            <td>
              <button class="action-button print-btn" title="Print Receipt" data-payment='${JSON.stringify(
                payment
              )}'>
                <i class="fas fa-print"></i>
              </button>
              <button class="action-button delete-btn" title="Delete Payment">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          `;

          row
            .querySelector(".delete-btn")
            .addEventListener("click", function () {
              deletePayment(payment.paymentID, payment.studentNic);
            });
          tableBody.appendChild(row);
        });

        async function deletePayment(paymentId, nic) {
          console.log(paymentId);
          try {
            const response = await fetch(
              `http://localhost:8080/payments/deletePayment/${paymentId}`,
              {
                headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${token}`,
                },
              }
            );

            const result = await response.json();
            alert(result.data);

            const row = document
              .querySelector(`.delete-btn[data-id='${paymentId}']`)
              ?.closest("tr");
            if (row) row.remove();
          } catch (error) {
            alert("Delete failed: " + error);
          }
        }

        document.querySelectorAll(".print-btn").forEach((button) => {
          button.addEventListener("click", function () {
            const paymentData = JSON.parse(this.getAttribute("data-payment"));
            generatePaymentSlip(paymentData);
          });
        });

        attachSearchListener();
      }

      async function loadPaymentDetails() {
        try {
          document.getElementById("loadingIndicator").style.display = "block";
          document.getElementById("paymentDataTable").style.display = "none";
          document.getElementById("noResultsMessage").style.display = "none";

          const response = await fetch(
            "http://localhost:8080/payments/loadPayments",
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }

          const result = await response.json();
          console.log("Payment data:", result);

          if (result && result.data && result.data.length > 0) {
            populatePaymentTable(result.data);
            document.getElementById("paymentDataTable").style.display = "table";
            document.getElementById("loadingIndicator").style.display = "none";
          } else {
            document.getElementById("noResultsMessage").style.display = "block";
            document.getElementById("loadingIndicator").style.display = "none";
          }
        } catch (error) {
          console.error("Error loading payment data:", error);
          document.getElementById("noResultsMessage").textContent =
            "Error loading payment data. Please try again.";
          document.getElementById("noResultsMessage").style.display = "block";
          document.getElementById("loadingIndicator").style.display = "none";
        }
      }

      function performSearch() {
        const searchText = document
          .getElementById("searchInput")
          .value.toLowerCase();
        const tableRows = document.querySelectorAll("#paymentTableBody tr");

        let hasResults = false;

        tableRows.forEach((row) => {
          const cells = row.querySelectorAll("td");
          let found = false;

          cells.forEach((cell) => {
            if (cell.textContent.toLowerCase().includes(searchText)) {
              found = true;
            }
          });

          if (found) {
            row.style.display = "";
            hasResults = true;
          } else {
            row.style.display = "none";
          }
        });

        document.getElementById("noResultsMessage").style.display = hasResults
          ? "none"
          : "block";
      }

      function attachSearchListener() {
        const searchInput = document.getElementById("searchInput");
        searchInput.addEventListener("input", performSearch);
      }

      document.addEventListener("DOMContentLoaded", function () {
        checkToken();

        const toggleButton = document.getElementById("toggleTableBtn");
        const paymentTable = document.getElementById("paymentTable");
        const searchBtn = document.getElementById("searchBtn");

        toggleButton.addEventListener("click", function () {
          if (paymentTable.style.display === "none") {
            paymentTable.style.display = "block";
            toggleButton.innerHTML =
              '<i class="fas fa-eye-slash"></i> Hide Payment History';

            if (!dataLoaded) {
              loadPaymentDetails();
              dataLoaded = true;
            }
          } else {
            paymentTable.style.display = "none";
            toggleButton.innerHTML =
              '<i class="fas fa-money-bill-wave"></i> Cash Payment History';
          }
        });

        searchBtn.addEventListener("click", performSearch);

        if (typeof showMessage !== "function") {
          window.showMessage = function (message, type) {
            alert(`${type.toUpperCase()}: ${message}`);
          };
        }
      });

      let influenceDataLoaded = false;

      async function loadInfluencePayments() {
        try {
          document.getElementById("loadingInfluenceIndicator").style.display =
            "block";
          document.getElementById("influenceDataTable").style.display = "none";
          document.getElementById("noInfluenceResults").style.display = "none";

          const response = await fetch(
            "http://localhost:8080/payments/loadInfluencePayments",
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (!response.ok)
            throw new Error(`HTTP error! status: ${response.status}`);

          const result = await response.json();
          console.log("Influence payment data:", result);

          const tableBody = document.getElementById("influenceTableBody");
          tableBody.innerHTML = "";

          if (result && result.data && result.data.length > 0) {
            result.data.forEach((payment) => {
              const row = document.createElement("tr");
              row.innerHTML = `
          <td>${payment.paymentID || "N/A"}</td>
          <td>${payment.studentNic || "N/A"}</td>
          <td>${payment.paymentDate || "N/A"}</td>
          <td>${
            payment.paidAmount?.toLocaleString("en-US", {
              minimumFractionDigits: 2,
            }) || "0.00"
          }</td>
          <td>${
            payment.influentAmount?.toLocaleString("en-US", {
              minimumFractionDigits: 2,
            }) || "0.00"
          }</td>
        `;
              tableBody.appendChild(row);
            });

            document.getElementById("influenceDataTable").style.display =
              "table";
          } else {
            document.getElementById("noInfluenceResults").style.display =
              "block";
          }

          document.getElementById("loadingInfluenceIndicator").style.display =
            "none";
        } catch (error) {
          console.error("Error loading influential payments:", error);
          document.getElementById("noInfluenceResults").textContent =
            "Error loading data. Please try again.";
          document.getElementById("noInfluenceResults").style.display = "block";
          document.getElementById("loadingInfluenceIndicator").style.display =
            "none";
        }
      }

      document.addEventListener("DOMContentLoaded", function () {
        const influenceBtn = document.getElementById("toggleInfluenceBtn");
        const influenceTable = document.getElementById("influenceTable");

        influenceBtn.addEventListener("click", function () {
          if (influenceTable.style.display === "none") {
            influenceTable.style.display = "block";
            influenceBtn.innerHTML =
              '<i class="fas fa-eye-slash"></i> Hide Influential Payments';

            if (!influenceDataLoaded) {
              loadInfluencePayments();
              influenceDataLoaded = true;
            }
          } else {
            influenceTable.style.display = "none";
            influenceBtn.innerHTML =
              '<i class="fas fa-chart-line"></i> Influential Payments';
          }
        });
      });

      document
        .getElementById("exportInfluenceBtn")
        .addEventListener("click", function () {
          const { jsPDF } = window.jspdf;
          const doc = new jsPDF();

          const primaryColor = [26, 42, 108]; 
          const accentColor = [52, 152, 219]; 
          const textColor = [33, 37, 41]; 
          const lightGray = [248, 249, 250];

          doc.setFillColor(...primaryColor);
          doc.rect(0, 0, 210, 35, "F");

          doc.setTextColor(255, 255, 255);
          doc.setFontSize(20);
          doc.setFont("helvetica", "bold");
          doc.text("DRIVEMASTER", 105, 15, { align: "center" });

          doc.setFontSize(10);
          doc.setFont("helvetica", "normal");
          doc.text("Driving School - Payment Management System", 105, 25, {
            align: "center",
          });

          doc.setTextColor(...textColor);
          doc.setFontSize(16);
          doc.setFont("helvetica", "bold");
          doc.text("INFLUENTIAL PAYMENT RECORDS", 105, 50, { align: "center" });

          const currentDate = new Date();
          const formattedDate = currentDate.toLocaleDateString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
          });
          const formattedTime = currentDate.toLocaleTimeString("en-US", {
            hour: "2-digit",
            minute: "2-digit",
          });

          doc.setFontSize(9);
          doc.setFont("helvetica", "normal");
          doc.setTextColor(100, 100, 100);
          doc.text(
            `Generated on: ${formattedDate} at ${formattedTime}`,
            20,
            60
          );
          doc.text(`Document ID: RPT-${Date.now()}`, 20, 67);

          const tableData = [];
          let totalAmount = 0;
          let totalInfluentialAmount = 0;

          document.querySelectorAll("#influenceTableBody tr").forEach((row) => {
            const cells = row.querySelectorAll("td");
            if (cells.length >= 5) {
              const rowData = Array.from(cells).map((cell, index) => {
                let text = cell.textContent.trim();
                if (index === 3 || index === 4) {
                  const amount = parseFloat(text.replace(/[^0-9.-]/g, ""));
                  if (!isNaN(amount)) {
                    if (index === 3) totalAmount += amount;
                    if (index === 4) totalInfluentialAmount += amount;
                    return `LKR ${amount.toLocaleString("en-US", {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}`;
                  }
                }
                return text;
              });
              tableData.push(rowData);
            }
          });

          const headers = [
            [
              "Payment ID",
              "Student NIC",
              "Date",
              "Paid Amount",
              "Influential Amount",
            ],
          ];

          if (doc.autoTable) {
            doc.autoTable({
              head: headers,
              body: tableData,
              startY: 75,
              theme: "grid",
              headStyles: {
                fillColor: primaryColor,
                textColor: 255,
                fontSize: 10,
                fontStyle: "bold",
                halign: "center",
              },
              bodyStyles: {
                fontSize: 9,
                cellPadding: 4,
                overflow: "linebreak",
                minCellHeight: 10,
                lineWidth: 0.1,
              },
              alternateRowStyles: {
                fillColor: lightGray,
              },
              columnStyles: {
                0: {
                  halign: "center",
                  cellWidth: 30,
                  fontStyle: "bold",
                }, 
                1: {
                  halign: "center",
                  cellWidth: 35,
                },
                2: {
                  halign: "center",
                  cellWidth: 25,
                }, 
                3: {
                  halign: "right",
                  cellWidth: 40,
                }, 
                4: {
                  halign: "right",
                  cellWidth: 45,
                }, 
              },
              margin: { left: 10, right: 10 },
              styles: {
                overflow: "linebreak",
                cellPadding: 3,
                lineColor: [200, 200, 200],
                lineWidth: 0.1,
              },
              didDrawCell: function (data) {
                if (data.section === "body") {
                  doc.setFontSize(9);
                }
              },
              willDrawCell: function (data) {
                if (data.cell.raw.length > 15) {
                  data.cell.minReadableHeight = 15;
                }
              },
              didDrawPage: function (data) {
                const pageCount = doc.internal.getNumberOfPages();
                const pageSize = doc.internal.pageSize;
                const pageHeight = pageSize.height || pageSize.getHeight();

                doc.setFontSize(8);
                doc.setTextColor(150, 150, 150);
                doc.text(
                  `Page ${data.pageNumber} of ${pageCount}`,
                  pageSize.width - 15,
                  pageHeight - 10
                );
              },
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
            doc.text(
              `Total Paid Amount: LKR ${totalAmount.toLocaleString("en-US", {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
              })}`,
              15,
              finalY + 40
            );
            doc.text(
              `Total Influential Amount: LKR ${totalInfluentialAmount.toLocaleString(
                "en-US",
                { minimumFractionDigits: 2, maximumFractionDigits: 2 }
              )}`,
              15,
              finalY + 47
            );

            const pageHeight = doc.internal.pageSize.height;
            doc.setFillColor(...primaryColor);
            doc.rect(0, pageHeight - 20, 210, 20, "F");

            doc.setTextColor(255, 255, 255);
            doc.setFontSize(8);
            doc.text(
              "This is a computer-generated document. No signature required.",
              105,
              pageHeight - 12,
              { align: "center" }
            );
            doc.text(
              "For inquiries, contact: info@drivemaster.com | +94 11 234 5678",
              105,
              pageHeight - 6,
              { align: "center" }
            );
          } else {
            console.warn(
              "jsPDF autoTable plugin not found. Using basic table."
            );
            let y = 75;

            doc.setFillColor(...primaryColor);
            doc.rect(10, y - 5, 190, 10, "F");
            doc.setTextColor(255, 255, 255);
            doc.setFontSize(9);
            doc.setFont("helvetica", "bold");

            const colWidths = [35, 40, 30, 45, 50];
            let x = 15;
            headers[0].forEach((header, i) => {
              doc.text(header, x, y);
              x += colWidths[i];
            });

            y += 12;
            doc.setTextColor(...textColor);
            doc.setFont("helvetica", "normal");
            doc.setFontSize(8);

            tableData.forEach((row, index) => {
              if (index % 2 === 0) {
                doc.setFillColor(...lightGray);
                doc.rect(10, y - 4, 190, 8, "F");
              }

              x = 15;
              row.forEach((cell, i) => {
                const displayText = cell.toString();

                if (displayText.length > 15 && i !== 3 && i !== 4) {
                  const lines = doc.splitTextToSize(
                    displayText,
                    colWidths[i] - 5
                  );
                  doc.text(lines, x, y);
                  y += (lines.length - 1) * 4;
                } else {
                  doc.text(displayText, x, y);
                }

                x += colWidths[i];
              });
              y += 10;

              if (y > 250) {
                doc.addPage();
                y = 30;
              }
            });
          }

          const dateStr = currentDate.toISOString().slice(0, 10);
          const timeStr = currentDate
            .toTimeString()
            .slice(0, 5)
            .replace(":", "");
          doc.save(`Influential_Payments_Report_${dateStr}_${timeStr}.pdf`);
        });
