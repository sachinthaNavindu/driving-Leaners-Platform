      const sessionData = [];

      const token = localStorage.getItem("token");
      if (!token || token === "undefined" || token === "null") {
        console.log("No valid token found");
        window.location.href = "/pages/employeelogin.html";
      } else {
        console.log("Token exists:", token);
      }

      const user = localStorage.getItem("username");
      const nic = localStorage.getItem("nic");
      const licenseId = localStorage.getItem("licenseId");

      if (user) {
        document.getElementById("menubar-user").textContent = user;
      }

      document.addEventListener("DOMContentLoaded", function () {
        loadCalendar();
        setupEventListeners();
        loadData();
      });

      let currentDate = new Date();

      function loadCalendar() {
        renderCalendar(currentDate);
      }

      function setupEventListeners() {
        document.getElementById("prev-month").addEventListener("click", () => {
          currentDate.setMonth(currentDate.getMonth() - 1);
          renderCalendar(currentDate);
        });

        document.getElementById("next-month").addEventListener("click", () => {
          currentDate.setMonth(currentDate.getMonth() + 1);
          renderCalendar(currentDate);
        });

        document.getElementById("today").addEventListener("click", () => {
          currentDate = new Date();
          renderCalendar(currentDate);
        });
      }

      function renderCalendar(date) {
        const daysGrid = document.getElementById("days-grid");
        daysGrid.innerHTML = "";

        const year = date.getFullYear();
        const month = date.getMonth();

        const monthNames = [
          "January",
          "February",
          "March",
          "April",
          "May",
          "June",
          "July",
          "August",
          "September",
          "October",
          "November",
          "December",
        ];
        document.getElementById(
          "current-date-display"
        ).textContent = `${monthNames[month]} ${year}`;

        const firstDay = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();

        const daysInPrevMonth = new Date(year, month, 0).getDate();

        for (let i = firstDay - 1; i >= 0; i--) {
          const dayElement = document.createElement("div");
          dayElement.classList.add("day", "other-month");
          dayElement.innerHTML = `
            <div class="day-number">${daysInPrevMonth - i}</div>
            <div class="events"></div>
          `;
          daysGrid.appendChild(dayElement);
        }

        const today = new Date();
        for (let i = 1; i <= daysInMonth; i++) {
          const dayElement = document.createElement("div");
          const isToday =
            today.getDate() === i &&
            today.getMonth() === month &&
            today.getFullYear() === year;

          if (isToday) {
            dayElement.classList.add("day", "today");
          } else {
            dayElement.classList.add("day");
          }

          dayElement.innerHTML = `
            <div class="day-number">${i}</div>
            <div class="events" id="day-${i}-events"></div>
            <div class="add-event"><i class="fas fa-plus"></i></div>
          `;

          daysGrid.appendChild(dayElement);

          const dateStr = `${year}-${String(month + 1).padStart(
            2,
            "0"
          )}-${String(i).padStart(2, "0")}`;
          const daySessions = sessionData.filter(
            (session) => session.date === dateStr
          );

          const eventsContainer = document.getElementById(`day-${i}-events`);
          if (daySessions.length > 0 && eventsContainer) {
            daySessions.forEach((session) => {
              const eventElement = document.createElement("div");
              eventElement.classList.add("event", session.status);
              eventElement.title = `${session.time} - (${session.type})`;
              eventElement.innerHTML = `
  <div style="font-size: 12px; font-weight: 700; color: #ffd700; margin-bottom: 3px;">
    ${session.course}  
  </div>
  <div><strong>${session.time}</strong> - ${session.details}</div>
`;

              eventsContainer.appendChild(eventElement);
            });
          }
        }

        const totalCells = firstDay + daysInMonth;
        const nextMonthDays = totalCells % 7 === 0 ? 0 : 7 - (totalCells % 7);

        for (let i = 1; i <= nextMonthDays; i++) {
          const dayElement = document.createElement("div");
          dayElement.classList.add("day", "other-month");
          dayElement.innerHTML = `
            <div class="day-number">${i}</div>
            <div class="events"></div>
          `;
          daysGrid.appendChild(dayElement);
        }
      }

      function loadData() {
        loadSessionData();
      }

      async function loadSessionData() {
        console.log(licenseId);
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

          if (response.ok) {
            const result = await response.json();
            console.log(result);
            sessionData.length = 0;

            (result.data || []).forEach((item) => {
              sessionData.push({
                date: item.date,
                time: new Date(item.time).toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit",
                }),
                details: item.vehicleNumber,
                course: item.courseName,
                type: "Driving Lesson",
                status: "Accepted",
              });
            });

            renderCalendar(currentDate);
          }
        } catch (error) {
          console.error("Error loading session data:", error);
        }
      }
