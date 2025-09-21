      const token = localStorage.getItem("token");
      const user = localStorage.getItem("username");
      const nic = localStorage.getItem("nic");
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

      document.addEventListener("DOMContentLoaded", () => {
        loadData();
      });

      function loadData() {
        checkToken();
        loadLoggedInUserInfo();
        loadPendingSessionReqs();
        loadSchedule();
        loadTodayUpcomingSession();
      }

      async function loadTodayUpcomingSession() {
        try {
          const response = await fetch(
            `http://localhost:8080/instructor/loadUpcomingSession/${licenseId}`,
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (!response.ok) {
            throw new Error(`HTTP error ! status : ${response.status}`);
          }
          const data = await response.json();
          console.log(" Upcoming session data:", data);
          const scheduleListContainer = document.getElementById(
            "todays-schedule-list"
          );
          const todayDateElement = document.getElementById("today-date");

          const todayFormatted = new Date().toLocaleDateString("en-US", {
            weekday: "short",
            month: "short",
            day: "numeric",
          });
          todayDateElement.textContent = todayFormatted;

          scheduleListContainer.innerHTML = "";

          if (!data.data || data.data.length === 0) {
            scheduleListContainer.innerHTML = `
                <div class="no-schedule-today">
                    <i class="fas fa-calendar-check" style="font-size: 32px; margin-bottom: 8px;"></i>
                    <p>No upcoming sessions for today</p>
                </div>
            `;
            return;
          }

          data.data.forEach((session) => {
            let formattedTime = "N/A";
            let formattedDate = "N/A";

            if (session.date && session.time) {
              const dateTimeStr = `${session.date}T${session.time}`;
              const dateTimeObj = new Date(dateTimeStr);

              formattedTime = dateTimeObj.toLocaleTimeString("en-US", {
                hour: "2-digit",
                minute: "2-digit",
                hour12: true,
              });

              formattedDate = dateTimeObj.toLocaleDateString("en-US", {
                weekday: "short",
                month: "short",
                day: "numeric",
              });
            }

            const scheduleItem = document.createElement("div");
            scheduleItem.className = "todays-schedule-item";
            scheduleItem.innerHTML = `
                <div class="todays-schedule-time">${formattedTime}</div>
                <div class="todays-schedule-details">
                    ${session.courseName || "Driving Lesson"} <br>
                    <small>Vehicle: ${session.vehicleNumber || "N/A"}</small>
                </div>
            `;

            scheduleListContainer.appendChild(scheduleItem);
          });
        } catch (error) {
          console.error("Error loading today's upcoming session:", error);
          document.getElementById("todays-schedule-list").innerHTML = `
            <div class="no-schedule-today">
                <i class="fas fa-exclamation-triangle" style="font-size: 32px; margin-bottom: 8px;"></i>
                <p>Failed to load today's sessions</p>
            </div>
        `;
        }
      }

      async function loadSchedule() {
        try {
          const response = await fetch(
            `http://localhost:8080/instructor/loadSchedule/${licenseId}`,
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

          const scheduleResponse = await response.json();
          const scheduleData = scheduleResponse.data;

          const scheduleListContainer =
            document.querySelector(".schedule-list");
          scheduleListContainer.innerHTML = "";

          if (!scheduleData || scheduleData.length === 0) {
            scheduleListContainer.innerHTML = `
        <div class="no-lessons">
          <i class="fas fa-calendar-check" style="font-size: 48px; margin-bottom: 10px;"></i>
          <p>No scheduled lessons</p>
        </div>
      `;
            return;
          }

          scheduleData.forEach((session) => {
            const sessionDate = new Date(session.date);
            const formattedDate = sessionDate.toLocaleDateString("en-US", {
              weekday: "long",
              year: "numeric",
              month: "long",
              day: "numeric",
            });

            let formattedTime = "N/A";
            if (session.time) {
              const timeObj = new Date(`1970-01-01T${session.time}`);
              const hours = String(timeObj.getHours()).padStart(2, "0");
              const minutes = String(timeObj.getMinutes()).padStart(2, "0");
              formattedTime = `${hours}:${minutes}`;
            }

            const scheduleItem = document.createElement("div");
            scheduleItem.className = "schedule-item";
            scheduleItem.innerHTML = `
        <div class="student-info">
          <img
            src="https://ui-avatars.com/api/?name=Student"
            alt="Student Avatar"
            class="student-avatar"
          />
          <div class="student-details">
            <div class="student-name">Session ID: ${
              session.sessionTimeTableId
            }</div>
            <div class="lesson-type">Vehicle: ${
              session.vehicleNumber || "N/A"
            }</div>
          </div>
        </div>
        <div class="time-slot">${formattedTime} - ${formattedDate}</div>
      `;

            scheduleListContainer.appendChild(scheduleItem);
          });
        } catch (error) {
          console.error("Error loading schedule:", error);
          showAlert("Failed to load schedule: " + error.message, "error");
        }
      }

      async function loadPendingSessionReqs() {
        try {
          const response = await fetch(
            `http://localhost:8080/instructor/sessionLoad/${licenseId}`,
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
          console.log("Pending sessions API Response:", result);

          let pendingSessions = [];
          if (result.data && Array.isArray(result.data)) {
            pendingSessions = result.data;
          } else if (result.data) {
            pendingSessions = [result.data];
          } else if (Array.isArray(result)) {
            pendingSessions = result;
          }

          const pendingListContainer = document.querySelector(".pending-list");

          pendingListContainer.innerHTML = "";

          if (pendingSessions.length === 0) {
            pendingListContainer.innerHTML = `
                <div class="no-pending">
                    <i class="fas fa-calendar-check" style="font-size: 48px; margin-bottom: 10px;"></i>
                    <p>No pending schedule requests</p>
                </div>
            `;
            return;
          }

          pendingSessions.forEach((session) => {
            const sessionDate = new Date(session.date);
            

            const formattedDate = sessionDate.toLocaleDateString("en-US", {
              weekday: "long",
              year: "numeric",
              month: "long",
              day: "numeric",
            });

            const formattedTime = session.time || "Time not specified";

            const sessionItem = document.createElement("div");
            sessionItem.className = "pending-item";
            sessionItem.innerHTML = `
                <div class="pending-student-info">
                    
                    <div class="pending-student-details">
                           <div class="pending-session-id">${
                             session.sessionId
                           }</div>
                        <div class="pending-lesson-type">${
                          session.courseName || "Driving Lesson"
                        }</div>
                        <div class="student-name">${
                          session.vehicleNumber || "Student NIC"
                        }</div>
                    </div>
                </div>
                <div class="pending-time-slot">${formattedDate}, ${formattedTime}</div>
                <div class="pending-actions">
                 
                       <button class="accept-btn" onclick="viewRequest('${
                         session.sessionId
                       }', '${session.courseName}', '${session.date}', '${
              session.time
            }')">View</button>
                    
                </div>
            `;

            pendingListContainer.appendChild(sessionItem);
          });
        } catch (error) {
          alert("error " + error);
        }
      }

      async function loadLoggedInUserInfo() {
        try {
          const response = await fetch(
            `http://localhost:8080/instructor/loadDetails/${nic}`,
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

          console.log("API Response:", result);

          let instructor;
          if (result.data && Array.isArray(result.data)) {
            instructor = result.data[0];
          } else if (result.data) {
            instructor = result.data;
          } else {
            instructor = result;
          }

          if (!instructor) {
            throw new Error("No instructor data found in response");
          }

          document.getElementById("menubar-user").textContent =
            instructor.name || "Instructor";

          document.getElementById("username").textContent =
            instructor.name || "N/A";
          document.getElementById("address").textContent =
            instructor.address || "N/A";
          document.getElementById("contact").textContent =
            instructor.contact || "N/A";
          document.getElementById("email").textContent =
            instructor.gmail || instructor.email || "N/A";
        } catch (error) {
          console.error("Error loading instructor details:", error);
          showMessage(
            "Could not load instructor details: " + error.message,
            "error"
          );
        }
      }

      function editProfile() {
        const modal = document.getElementById("editModal");

        let username = document.getElementById("username").textContent;
        let address = document.getElementById("address").textContent;
        let contact = document.getElementById("contact").textContent;
        let email = document.getElementById("email").textContent;

        document.getElementById("edit-username").value = username;
        document.getElementById("edit-address").value = address;
        document.getElementById("edit-contact").value = contact;
        document.getElementById("edit-email").value = email;

        modal.style.display = "block";

        const closeBtn = document.querySelector(".close");

        closeBtn.onclick = function () {
          modal.style.display = "none";
        };

        window.onclick = function (event) {
          if (event.target == modal) {
            modal.style.display = "none";
          }
        };

        document.getElementById("editProfileForm").onsubmit = function (e) {
          e.preventDefault();
          saveEdit();
        };
      }

      async function saveEdit() {
        const updatedUsername = document.getElementById("edit-username").value;
        const updatedAddress = document.getElementById("edit-address").value;
        const updatedContact = document.getElementById("edit-contact").value;
        const updatedEmail = document.getElementById("edit-email").value;

        const updatedData = {
          nic: nic,
          address: updatedAddress,
          contact: updatedContact,
          email: updatedEmail,
          name: updatedUsername,
        };

        try {
          const response = await fetch(
            `http://localhost:8080/instructor/updateProfile/${nic}`,
            {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
              body: JSON.stringify(updatedData),
            }
          );

          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }

          const result = await response.json();

          document.getElementById("username").textContent = updatedUsername;
          document.getElementById("address").textContent = updatedAddress;
          document.getElementById("contact").textContent = updatedContact;
          document.getElementById("email").textContent = updatedEmail;

          document.getElementById("menubar-user").textContent = updatedUsername;

          document.getElementById("editModal").style.display = "none";

          showAlert("Profile updated successfully!", "success");
        } catch (error) {
          showAlert("Failed to update profile: " + error.message, "error");
          console.log(error.message);
        }
      }

      function showAlert(message, type) {
        const alertDiv = document.createElement("div");
        alertDiv.style.position = "fixed";
        alertDiv.style.top = "20px";
        alertDiv.style.right = "20px";
        alertDiv.style.padding = "15px 20px";
        alertDiv.style.borderRadius = "5px";
        alertDiv.style.color = "white";
        alertDiv.style.fontWeight = "bold";
        alertDiv.style.zIndex = "1000";
        alertDiv.style.boxShadow = "0 4px 8px rgba(0,0,0,0.2)";
        alertDiv.style.maxWidth = "300px";

        if (type === "success") {
          alertDiv.style.backgroundColor = "#28a745";
        } else if (type === "error") {
          alertDiv.style.backgroundColor = "#dc3545";
        } else {
          alertDiv.style.backgroundColor = "#17a2b8";
        }

        alertDiv.textContent = message;

        document.body.appendChild(alertDiv);

        setTimeout(() => {
          alertDiv.style.opacity = "0";
          alertDiv.style.transition = "opacity 0.5s ease-out";
          setTimeout(() => {
            document.body.removeChild(alertDiv);
          }, 500);
        }, 3000);
      }

      function viewRequest(sessionId, courseName, date, time) {
        console.log("Viewing session:", sessionId, courseName, date, time);

        let formattedTime = "N/A";
        if (time) {
          const timeObj = new Date(time);
          formattedTime = timeObj.toLocaleTimeString("en-US", {
            hour: "2-digit",
            minute: "2-digit",
            hour12: true,
          });
        }

        document.getElementById("view-session-id").textContent =
          sessionId || "N/A";
        document.getElementById("view-course-name").textContent =
          courseName || "N/A";
        document.getElementById("view-date").textContent = date || "N/A";
        document.getElementById("view-time").textContent =
          formattedTime || "N/A";

        document.getElementById("viewRequestModal").style.display = "block";
      }

      function closeViewModal() {
        document.getElementById("viewRequestModal").style.display = "none";
      }

      async function requestAction(action, sessionId) {
        try {
          const response = await fetch(
            `http://localhost:8080/instructor/sessionManagement/${action},${nic},${sessionId}`,
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (response.ok) {
            const data = await response.json();
            alert(`Request ${action} was successful ✅`);
            console.log("Response:", data);
            loadData();
            const modal = document.getElementById("requestModal");
            if (modal) modal.style.display = "none";
            document.getElementById("viewRequestModal").style.display = "block";
          } else {
            const errorText = await response.text();
            alert(`Failed to ${action}: ${errorText}`);
          }
        } catch (error) {
          alert("Error: " + error);
        }
      }
