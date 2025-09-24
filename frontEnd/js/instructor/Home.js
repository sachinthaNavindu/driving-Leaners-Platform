import { getAccessToken, getRefreshToken, fetchWithAuth, checkToken, redirectToLogin } from '../Auth.js';

const token = getAccessToken();
const nic = localStorage.getItem("nic");
const licenseId = localStorage.getItem("licenseId");

document.addEventListener("DOMContentLoaded", async () => {
    checkToken();   
    await loadData();
});

async function loadData() {
    loadLoggedInUserInfo();
    loadPendingSessionReqs();
    loadSchedule();
    loadTodayUpcomingSession();
}

async function loadTodayUpcomingSession() {
    try {
        const response = await fetchWithAuth(`http://localhost:8080/instructor/loadUpcomingSession/${licenseId}`, {
            headers: { "Content-Type": "application/json" }
        });

        if (!response || !response.ok) throw new Error(`HTTP error! status: ${response?.status}`);

        const data = await response.json();
        const scheduleListContainer = document.getElementById("todays-schedule-list");
        const todayDateElement = document.getElementById("today-date");

        todayDateElement.textContent = new Date().toLocaleDateString("en-US", {
            weekday: "short",
            month: "short",
            day: "numeric",
        });

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

        data.data.forEach(session => {
            let formattedTime = "N/A";
            let formattedDate = "N/A";

            if (session.date && session.time) {
                const dateTimeObj = new Date(`${session.date}T${session.time}`);
                formattedTime = dateTimeObj.toLocaleTimeString("en-US", {
                    hour: "2-digit",
                    minute: "2-digit",
                    hour12: true
                });
                formattedDate = dateTimeObj.toLocaleDateString("en-US", {
                    weekday: "short",
                    month: "short",
                    day: "numeric"
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
        const response = await fetchWithAuth(`http://localhost:8080/instructor/loadSchedule/${licenseId}`, {
            headers: { "Content-Type": "application/json" }
        });

        if (!response || !response.ok) throw new Error(`HTTP error! status: ${response?.status}`);

        const scheduleData = (await response.json()).data;
        const scheduleListContainer = document.querySelector(".schedule-list");
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

        scheduleData.forEach(session => {
            const sessionDate = new Date(session.date);
            const formattedDate = sessionDate.toLocaleDateString("en-US", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric"
            });

            const formattedTime = session.time || "N/A";

            const scheduleItem = document.createElement("div");
            scheduleItem.className = "schedule-item";
            scheduleItem.innerHTML = `
                <div class="student-info">
                    <img src="https://ui-avatars.com/api/?name=Student" alt="Student Avatar" class="student-avatar"/>
                    <div class="student-details">
                        <div class="student-name">Session ID: ${session.sessionTimeTableId}</div>
                        <div class="lesson-type">Vehicle: ${session.vehicleNumber || "N/A"}</div>
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
        const response = await fetchWithAuth(`http://localhost:8080/instructor/sessionLoad/${licenseId}`, {
            headers: { "Content-Type": "application/json" }
        });

        if (!response || !response.ok) throw new Error(`HTTP error! status: ${response?.status}`);

        const result = await response.json();
        let pendingSessions = result.data && Array.isArray(result.data) ? result.data : result.data ? [result.data] : [];

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

        pendingSessions.forEach(session => {
            const sessionDate = new Date(session.date);
            const formattedDate = sessionDate.toLocaleDateString("en-US", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric"
            });

            const formattedTime = session.time || "Time not specified";

            const sessionItem = document.createElement("div");
            sessionItem.className = "pending-item";
            sessionItem.innerHTML = `
                <div class="pending-student-info">
                    <div class="pending-student-details">
                        <div class="pending-session-id">${session.sessionId}</div>
                        <div class="pending-lesson-type">${session.courseName || "Driving Lesson"}</div>
                        <div class="student-name">${session.vehicleNumber || "Student NIC"}</div>
                    </div>
                </div>
                <div class="pending-time-slot">${formattedDate}, ${formattedTime}</div>
                <div class="pending-actions">
                    <button class="accept-btn" onclick="viewRequest('${session.sessionId}', '${session.courseName}', '${session.date}', '${session.time}')">View</button>
                </div>
            `;
            pendingListContainer.appendChild(sessionItem);
        });
    } catch (error) {
        console.error("Error loading pending sessions:", error);
    }
}

async function loadLoggedInUserInfo() {
    try {
        const response = await fetchWithAuth(`http://localhost:8080/instructor/loadDetails/${nic}`, {
            headers: { "Content-Type": "application/json" }
        });

        if (!response || !response.ok) throw new Error(`HTTP error! status: ${response?.status}`);

        const result = await response.json();
        const instructor = result.data && Array.isArray(result.data) ? result.data[0] : result.data;

        if (!instructor) throw new Error("No instructor data found");

        document.getElementById("menubar-user").textContent = instructor.name || "Instructor";
        document.getElementById("username").textContent = instructor.name || "N/A";
        document.getElementById("address").textContent = instructor.address || "N/A";
        document.getElementById("contact").textContent = instructor.contact || "N/A";
        document.getElementById("email").textContent = instructor.gmail || instructor.email || "N/A";
    } catch (error) {
        console.error("Error loading instructor details:", error);
        showMessage("Could not load instructor details: " + error.message, "error");
    }
}
