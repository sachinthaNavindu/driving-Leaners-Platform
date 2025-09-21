      const token = localStorage.getItem("token");
      const nic = localStorage.getItem("nic");
      const role = localStorage.getItem("role");
      const licenseId = localStorage.getItem("licenseId");

      function checkToken() {
        if (!token || token === "undefined" || token === "null") {
          showMessage("Please log in to continue", "error");
          setTimeout(() => {
            window.location.href = "/pages/employeelogin.html";
          }, 1500);
          return;
        }
      }

      document.addEventListener("DOMContentLoaded", function () {
        checkToken();
        loadData();
      });

      async function loadData() {
        console.log(nic);
        try {
          const response = await fetch(
            `http://localhost:8080/student/loadData/${nic}`,
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          const result = await response.json();
          console.log("Student Data:", result.data);

          if (!result.data) return;

          const student = result.data.student;
          const sessions = result.data.upcomingSession;
          const sessionsForUser = result.data.sessionsForUser || [];

          document.getElementById("profile-name").innerText = student.name;
          document.getElementById("menubar-user").innerHTML = student.name;
          document.getElementById("profile-nic").innerText = student.nic;
          document.getElementById("profile-address").innerText =
            student.address;
          document.getElementById("profile-contact").innerText =
            student.contact;
          document.getElementById("profile-email").innerText = student.email;

          const sessionCard = document.querySelector(".session-card");

          if (sessions && sessions.length > 0) {
            const session = sessions[0];

            document.querySelector(
              ".session-details .session-row:nth-child(1) .session-value"
            ).innerText = session.sessionId;

            document.querySelector(
              ".session-details .session-row:nth-child(2) .session-value"
            ).innerText = new Date(session.date).toLocaleDateString("en-US", {
              month: "long",
              day: "numeric",
              year: "numeric",
            });

            document.querySelector(
              ".session-details .session-row:nth-child(3) .session-value"
            ).innerText = session.time;

            document.querySelector(
              ".session-details .session-row:nth-child(4) .instructor-badge"
            ).innerText = `👨‍🏫 ${session.instructorName}`;

            document.querySelector(
              ".session-details .session-row:nth-child(5) .session-value"
            ).innerText = session.vehicleNumber;

            document.querySelector(
              ".session-details .session-row:nth-child(6) .session-value"
            ).innerText = session.courseName;

            const btn = document.getElementById("assignBtn");
            btn.dataset.sessionId = session.sessionId;
            btn.onclick = function () {
              confirmSession(this.dataset.sessionId, nic);
            };

            sessionCard.style.display = "block";
          } else {
            sessionCard.style.display = "none";
          }

          populateSessionsTable(sessionsForUser);

        } catch (error) {
          console.error("Error loading data:", error);
        }
      }

      function populateSessionsTable(sessions) {
        const tableBody = document.getElementById("sessions-table-body");
        tableBody.innerHTML = "";

        if (!sessions || sessions.length === 0) {
          tableBody.innerHTML = `
            <tr>
              <td colspan="4" class="no-sessions">No sessions found</td>
            </tr>
        `;
          return;
        }

        sessions.forEach((session) => {
          const row = document.createElement("tr");

          const formattedDate = new Date(session.date).toLocaleDateString(
            "en-US",
            {
              month: "short",
              day: "numeric",
              year: "numeric",
            }
          );

          let formattedTime = "N/A";
          if (session.time) {
            const [hours, minutes, seconds] = session.time.split(":");
            const date = new Date();
            date.setHours(hours, minutes, seconds);
            formattedTime = date.toLocaleTimeString("en-US", {
              hour: "2-digit",
              minute: "2-digit",
              hour12: true,
            });
          }

          row.innerHTML = `
            <td>${session.sessionId || "N/A"}</td>
            <td>${formattedDate}</td>
            <td>${formattedTime}</td>
     
        `;

          tableBody.appendChild(row);
        });
      }

      async function confirmSession(sessionId, nic) {
        console.log(sessionId, nic);
        try {
          const response = await fetch(
            `http://localhost:8080/student/confirm/${sessionId}/${nic}`,
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );
          const result = await response.json();
          if (response.ok) {
            loadData();
            alert("Session confirmed successfully!");
          } else {
            alert("Failed to confirm session: " + result.message);
          }
        } catch (error) {
          alert("error", error);
        }
      }

      const editProfileBtn = document.querySelector(".edit-btn");
      const modal = document.getElementById("editProfileModal");
      const closeBtn = modal.querySelector(".close-btn");
      const editForm = document.getElementById("editProfileForm");

      editProfileBtn.addEventListener("click", () => {
        document.getElementById("edit-name").value =
          document.getElementById("profile-name").innerText;
        document.getElementById("edit-nic").value =
          document.getElementById("profile-nic").innerText;
        document.getElementById("edit-address").value =
          document.getElementById("profile-address").innerText;
        document.getElementById("edit-contact").value =
          document.getElementById("profile-contact").innerText;
        document.getElementById("edit-email").value =
          document.getElementById("profile-email").innerText;

        modal.style.display = "block";
      });

      closeBtn.addEventListener("click", () => {
        modal.style.display = "none";
      });
      window.addEventListener("click", (e) => {
        if (e.target == modal) modal.style.display = "none";
      });

      editForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const updatedData = {
          name: document.getElementById("edit-name").value,
          nic: document.getElementById("edit-nic").value,
          address: document.getElementById("edit-address").value,
          contact: document.getElementById("edit-contact").value,
          email: document.getElementById("edit-email").value,
        };

        try {
          const response = await fetch(
            `http://localhost:8080/student/updateProfile/${nic}`,
            {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
              body: JSON.stringify(updatedData),
            }
          );
          const result = await response.json();
          if (response.ok) {
            document.getElementById("profile-name").innerText =
              updatedData.name;
            document.getElementById("profile-nic").innerText = updatedData.nic;
            document.getElementById("profile-address").innerText =
              updatedData.address;
            document.getElementById("profile-contact").innerText =
              updatedData.contact;
            document.getElementById("profile-email").innerText =
              updatedData.email;

            modal.style.display = "none";
            alert("Profile updated successfully!");
          } else {
            alert("Failed to update profile: " + result.message);
          }
        } catch (error) {
          alert("Error updating profile");
        }
      });
