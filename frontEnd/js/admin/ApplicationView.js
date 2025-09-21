      const token = localStorage.getItem("token");

      function checkToken() {
        if (!token || token === "undefined" || token === "null") {
          showMessage("Please log in to continue", "error");
          setTimeout(() => {
            window.location.href = "/pages/employeelogin.html";
          }, 1500);
          return;
        }
        const userName = localStorage.getItem("username");
        document.getElementById("menubar-user").innerText = userName;
      }

      document.addEventListener("DOMContentLoaded", function () {
        checkToken();
        loadPendingApplications();
      });

      async function loadPendingApplications() {
        try {
          const response = await fetch(
            "http://localhost:8080/application/getApply",
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          const result = await response.json();
          console.log("Applications:", result);

          const grid = document.querySelector(".application-grid");
          grid.innerHTML = "";

          result.data.forEach((app) => {
            const card = document.createElement("div");
            card.classList.add("application-card");

            card.innerHTML = `
        <div class="card-header">
          <div class="application-id">APP-${app.id}</div>
        </div>
        <div class="card-body">
          <div class="student-info">
            <div class="info-item"><i class="fas fa-user"></i> <span>${
              app.name
            }</span></div>
            <div class="info-item"><i class="fas fa-id-card"></i> <span>${
              app.nic
            }</span></div>
            <div class="info-item"><i class="fas fa-phone"></i> <span>${
              app.contact
            }</span></div>
            <div class="info-item"><i class="fas fa-envelope"></i> <span>${
              app.email
            }</span></div>
            <div class="info-item"><i class="fas fa-map-marker-alt"></i> <span>${
              app.address
            }</span></div>
          </div>
        </div>
        <div class="card-footer">
          <div class="application-date">
            <i class="far fa-calendar-alt"></i> Applied: ${app.date || "N/A"}
          </div>
          <div class="actions">
            <button class="action-btn btn-view">
              <i class="fas fa-eye"></i> View
            </button>
          </div>
        </div>
      `;

            card.querySelector(".btn-view").addEventListener("click", () => {
              showApplication(app);
            });

            grid.appendChild(card);
          });
        } catch (error) {
          console.error("Error loading applications:", error);
        }
      }

      let currentApplication = null;

      function showApplication(application) {
        currentApplication = application;

        document.getElementById("appId").value = application.id;
        document.getElementById("appStatus").value =
          application.respond || "PENDING";
        document.getElementById("appDate").value = application.date || "N/A";

        document.getElementById("studentName").value = application.name;
        document.getElementById("studentNIC").value = application.nic;
        document.getElementById("studentEmail").value = application.email;
        document.getElementById("studentContact").value = application.contact;
        document.getElementById("studentAddress").value = application.address;

        document.getElementById(
          "nicFrontPreview"
        ).src = `http://localhost:8080/application/imageDB/imagesDB/${application.nicFrontUrl
          .split("/")
          .pop()}`;

        document.getElementById(
          "nicBackPreview"
        ).src = `http://localhost:8080/application/imageDB/imagesDB/${application.nicBackUrl
          .split("/")
          .pop()}`;

        document.getElementById("applicationModal").style.display = "block";
      }
      function closeModal() {
        document.getElementById("applicationModal").style.display = "none";
      }

      async function approvalApplication() {
        if (!currentApplication) {
          alert("No application selected!");
          return;
        }

        const appId = currentApplication.id;

        
      }
