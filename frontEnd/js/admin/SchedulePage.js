      const token = localStorage.getItem("token");

      function checkToken() {
        if (!token || token === "undefined" || token === "null") {
          showMessage("Please log in to continue", "error");
          setTimeout(() => {
            window.location.href = "/pages/employeelogin.html";
          }, 1500);
          return;
        }
      }

      const timeInput = document.getElementById("start_time");

      timeInput.min = "08:30";
      timeInput.max = "18:00";

      timeInput.addEventListener("input", () => {
        const value = timeInput.value;
        if (value < timeInput.min) {
          timeInput.value = timeInput.min;
        } else if (value > timeInput.max) {
          timeInput.value = timeInput.max;
        }
      });

      function loadData() {
        checkToken();
        console.log(token)

        const dateInput = document.getElementById("date");

        const today = new Date();
        today.setDate(today.getDate() + 1);

        const yyyy = today.getFullYear();
        const mm = String(today.getMonth() + 1).padStart(2, "0");
        const dd = String(today.getDate()).padStart(2, "0");
        const minDate = `${yyyy}-${mm}-${dd}`;

        dateInput.min = minDate;

        reloadInstructors();
        reloadVehicles();
        reloadCourses();
        loadNextSessionId();
        loadSessionData();
      }

      document.addEventListener("DOMContentLoaded", async () => {
        loadData();
      });

      async function loadNextSessionId() {
        try {
          const response = await fetch(
            "http://localhost:8080/schedule/sessionId",
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

          if (result.data) {
            const sessionInput = document.getElementById("session_id");
            if (sessionInput) sessionInput.value = result.data.nextSessionId;

            const pendingCountEl = document.getElementById("pending-count");
            const approvedCountEl = document.getElementById("approved-count");
            const completedCountEl = document.getElementById("completed-count");

            if (pendingCountEl)
              pendingCountEl.textContent = result.data.pendingCount;
            if (approvedCountEl)
              approvedCountEl.textContent = result.data.approvedCount;
            if (completedCountEl)
              completedCountEl.textContent = result.data.completedCount || 0;
          }
        } catch (error) {
          console.error("Error fetching session info:", error);
        }
      }

      async function reloadVehicles() {
        try {
          const response = await fetch(
            "http://localhost:8080/schedule/getVehicle",
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );
          const result = await response.json();
          const list = document.getElementById("vehiclesList");
          list.innerHTML = "";

          if (result.data && result.data.length > 0) {
            result.data.forEach((vehicle) => {
              const li = document.createElement("li");
              li.classList.add("vehicle-item");

              const availabilityClass =
                vehicle.availability === "Available"
                  ? "available"
                  : "unavailable";

              li.innerHTML = `
              <div class="vehicle-icon">
                <i class="fas fa-car"></i>
              </div>
              <div class="vehicle-info">
                <div class="vehicle-model">${vehicle.vehicleNumber}</div>
                <div class="vehicle-details">
                  <i class="fas fa-tag"></i> ${vehicle.vehicleType}
                  <span class="availability-badge ${availabilityClass}">
                    ${vehicle.availability}
                  </span>
                </div>
              </div>
            `;
              li.addEventListener("click", () => selectVehicle(vehicle));

              list.appendChild(li);
            });
          } else {
            list.innerHTML = '<div class="no-data">No vehicles available</div>';
          }
        } catch (error) {
          console.error("Error loading vehicles:", error);
        }
      }

      async function reloadInstructors() {
        try {
          const response = await fetch(
            "http://localhost:8080/schedule/getInstructor",
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );
          if (!response.ok) {
            console.error("Failed to load sessions");
            return;
          }

          const result = await response.json();
          const list = document.getElementById("instructorsList");
          list.innerHTML = "";

          if (result.data && result.data.length > 0) {
            result.data.forEach((instructor) => {
              const li = document.createElement("li");
              li.classList.add("instructor-item");
              li.innerHTML = `
        <div class="instructor-avatar">${instructor.name.charAt(0)}</div>
        <div class="instructor-info">
          <div class="instructor-name">${instructor.name}</div>
          <div class="instructor-contact">
            <i class="fas fa-id-card"></i> ${instructor.licenseId}
          </div>
        </div>
      `;
              li.addEventListener("click", () => selectInstructor(instructor));
              list.appendChild(li);
            });
          } else {
            list.innerHTML = `
      <div class="no-data">
        <i class="fas fa-chalkboard-teacher"></i>
        <p>No instructors found</p>
      </div>
    `;
          }
        } catch (error) {
          console.error("Error fetching instructors:", error);
          showMessage("Error loading instructors", "error");
        }
      }

      async function reloadCourses() {
        try {
          const response = await fetch(
            "http://localhost:8080/schedule/getCourses",
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          const result = await response.json();
          const list = document.getElementById("coursesList");
          const courseDropdown = document.getElementById("course_name");
          list.innerHTML = "";
          courseDropdown.innerHTML = '<option value="">Select Course</option>';

          if (result.data && result.data.length > 0) {
            result.data.forEach((course) => {
              const li = document.createElement("li");
              li.classList.add("course-item");
              li.innerHTML = `
                <div class="course-icon">
                  <i class="fas fa-book"></i>
                </div>
                <div class="course-info">
                  <div class="course-name">${course.courseName}</div>
                  <div class="course-details">
                    <i class="fas fa-clock"></i> ${course.sessions} sessions
                    <span class="availability-badge available">
                      LKR.${course.courseFee}
                    </span>
                  </div>
                </div>
              `;
              li.addEventListener("click", () => selectCourse(course));
              list.appendChild(li);

              const option = new Option(course.courseName, course.courseName);
            });
          } else {
            list.innerHTML = '<div class="no-data">No courses available</div>';
          }
        } catch (error) {
          console.error("Error loading courses:", error);
          addSampleCourses();
        }
      }

      let selectLicenseId = null;

      function selectInstructor(instructor) {
        const instructorDropdown = document.getElementById("instructor_name");

        let option = [...instructorDropdown.options].find(
          (opt) => opt.value === instructor.name
        );
        if (!option) {
          option = new Option(instructor.name, instructor.name);
          instructorDropdown.add(option);
        }

        instructorDropdown.value = instructor.name;

        selectLicenseId = instructor.licenseId;

        console.log(selectLicenseId);
      }

      let selectedVehicleNumber = null;

      function selectVehicle(vehicle) {
        const vehicleDropdown = document.getElementById("vehicle_number");

        let option = [...vehicleDropdown.options].find(
          (opt) => opt.value === vehicle.vehicleNumber
        );
        if (!option) {
          option = new Option(vehicle.vehicleNumber, vehicle.vehicleNumber);
          vehicleDropdown.add(option);
        }

        vehicleDropdown.value = vehicle.vehicleNumber;

        selectedVehicleNumber = vehicle.vehicleNumber;
      }

      function selectCourse(course) {
        const courseDropdown = document.getElementById("course_name");

        let option = [...courseDropdown.options].find(
          (opt) => opt.value === course.courseName
        );
        if (!option) {
          option = new Option(course.courseName, course.courseName);
          courseDropdown.add(option);
        }

        courseDropdown.value = course.courseName;
      }

      document
        .getElementById("sessionForm")
        .addEventListener("submit", async (e) => {
          e.preventDefault();

          const sessionId = document.getElementById("session_id").value;
          const date = document.getElementById("date").value;
          const startTime = document.getElementById("start_time").value;
          const instructorName =
            document.getElementById("instructor_name").value;
          const vehicleNumber = document.getElementById("vehicle_number").value;
          const courseName = document.getElementById("course_name").value;

          if (
            !sessionId ||
            !date ||
            !startTime ||
            !selectLicenseId ||
            !vehicleNumber ||
            !courseName
          ) {
            alert("Please fill all required fields");
            return;
          }

          const sessionDate = document.getElementById("date").value;
          const startTime1 = document.getElementById("start_time").value;

          const dateTimeString = `${sessionDate}T${startTime1}:00`;
          const sessionTimestamp = new Date(dateTimeString).getTime();

          const payload = {
            sessionId: sessionId,
            date: date,
            time: startTime,
            licenseId: selectLicenseId,
            vehicleNumber: vehicleNumber,
            courseName: courseName,
            respond: "PENDING",
          };

          try {
            const response = await fetch(
              "http://localhost:8080/schedule/saveSession",
              {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(payload),
              }
            );

            const result = await response.json();

            if (response.ok) {
              alert(`Session scheduled successfully! ID: ${result.data}`);
              document.getElementById("sessionForm").reset();
              loadData();
            } else {
              alert(`Error: ${result.data || result.message}`);
            }
          } catch (error) {
            console.error("Error saving session:", error);
            alert("Failed to schedule session. See console for details.");
          }
        });

      function filterSessionsByResponse(status) {
        const tableBody = document.getElementById("sessionsList");
        const allRows = tableBody.querySelectorAll("tr");

        allRows.forEach((row) => {
          const responseCell = row.querySelector("td:nth-child(7) span");
          if (!responseCell) return;

          const responseText = responseCell.textContent.trim().toUpperCase();

          if (status === "ALL" || responseText === status) {
            row.style.display = "";
          } else {
            row.style.display = "none";
          }
        });
      }

      document
        .getElementById("responseFilter")
        .addEventListener("change", function () {
          filterSessionsByResponse(this.value);
        });

      async function loadSessionData() {
        try {
          const response = await fetch(
            "http://localhost:8080/schedule/getSession",
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

          const result = await response.json();

          if (response.ok) {
            console.log("Sessions:", result.data);

            let tableBody = document.getElementById("sessionsList");
            tableBody.innerHTML = "";

            result.data.forEach((session) => {
              let statusClass = "status-pending";
              if (session.respond === "ACCEPTED")
                statusClass = "status-confirmed";
              if (session.respond === "DECLINED")
                statusClass = "status-declined";
              if (session.respond === "CANCELED")
                statusClass = "status-declined";

              let cancelButton = "";
              if (session.respond !== "CANCELED") {
                cancelButton = `<button class="btn btn-secondary btn-cancel" onclick="cancelSession('${session.sessionId}')">Cancel</button>`;
              }

              let row = `<tr>
              <td>${session.sessionId}</td>
              <td>${session.date}</td>
              <td>${session.time}</td>
              <td>${session.instructorName}</td>
              <td>${session.vehicleNumber}</td>
              <td>${session.courseName || "N/A"}</td>
              <td><span class="status-badge ${statusClass}">${
                session.respond
              }</span></td>
              <td>${cancelButton}</td>
            </tr>`;
              tableBody.innerHTML += row;
            });
          } else {
            console.error("Error fetching sessions:", result.status);
            alert("Failed to load sessions!");
          }
        } catch (error) {
          console.error("Fetch error:", error);
          alert("Something went wrong while loading sessions.");
        }
      }

      async function cancelSession(sessionId) {
        const isConfirmed = confirm(
          "Are you sure you want to cancel this session?"
        );
        if (!isConfirmed) {
          return;
        }
        try {
          const response = await fetch(
            `http://localhost:8080/schedule/cancelSession/${sessionId}`,
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );
          if (response.ok) {
            alert(`Session ${sessionId} Cancelled successfully`);
            loadData();
          } else {
            alert("Failed to cancel session!");
          }
        } catch (error) {
          alert("Something went wrong while cancelling");
        }
      }

      document.querySelectorAll(".btn-cancel").forEach((button) => {
        button.addEventListener("click", cancelSession);
      });
