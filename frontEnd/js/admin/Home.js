
import { getAccessToken, getRefreshToken, fetchWithAuth, checkToken, redirectToLogin } from '../Auth.js';

document.addEventListener("DOMContentLoaded", async function () {
  checkToken();   
  await loadData(); 

  const modal = document.getElementById("modelPapersModal");
  const addBtn = document.getElementById("addModelPapersBtn");
  const closeBtn = document.getElementsByClassName("close")[0];
  const cancelBtn = document.getElementById("cancelBtn");
  const form = document.getElementById("modelPapersForm");

  addBtn.onclick = () => { modal.style.display = "block"; };
  closeBtn.onclick = () => { modal.style.display = "none"; };
  cancelBtn.onclick = () => { modal.style.display = "none"; };

  window.onclick = (event) => {
    if (event.target == modal) modal.style.display = "none";
  };

  form.onsubmit = (e) => {
    e.preventDefault();
    const fileInput = document.getElementById("paperFile");
    const file = fileInput.files[0];

    if (file && file.type !== "application/pdf") {
      alert("Please select a PDF file");
      return;
    }

    alert("Model paper uploaded successfully!");
    modal.style.display = "none";
    form.reset();
  };
});

async function loadData() {
  try {
    const response = await fetchWithAuth("http://localhost:8080/admin/loadData", {
      headers: { "Content-Type": "application/json" },
    });

    if (!response || !response.ok) {
      alert("Error loading data");
      console.log(response);
      return;
    }

    const result = await response.json();
    const data = result.data;

    document.getElementById("totalStudents").innerText =
      data.studentCount.registeredStudentCount;
    document.getElementById("activeCourse").innerText = data.courseCount;
    document.getElementById("todaySession").innerText =
      data.todaysSessionCount;

    renderUpcomingSessions(data.upcomingSessions);
    renderTodaySessions(data.scheduleSessions);
  } catch (error) {
    console.error("Error:", error);
    alert("Error loading data");
  }
}

function renderTodaySessions(sessions) {
  const container = document.getElementById("todaySessionsList");
  container.innerHTML = "";

  if (!sessions || sessions.length === 0) {
    container.innerHTML = `<li class="activity-item">No sessions today</li>`;
    return;
  }

  sessions.forEach((s, index) => {
    let statusClass = "status-pending";
    if (s.respond === "ACCEPTED") statusClass = "status-confirmed";
    if (s.respond === "DECLINED" || s.respond === "CANCELED")
      statusClass = "status-declined";

    const colors = ["#1a2a6c", "#b21f1f", "#3498db", "#2ecc71", "#9b59b6"];
    const bgColor = colors[index % colors.length];

    const li = document.createElement("li");
    li.classList.add("activity-item");

    li.innerHTML = `
      <div class="activity-icon" style="background-color: ${bgColor}">
        <i class="fas fa-car"></i>
      </div>
      <div class="activity-content">
        <div class="activity-title">
          ${s.courseName || "Driving Session"}
          <span class="session-status ${statusClass}">${s.respond}</span>
        </div>
        <div class="activity-desc">
          Instructor: ${s.licenseId || "Not Assigned"} |
          Vehicle: ${s.vehicleNumber || "Not Assigned"}
        </div>
        <div class="activity-time">
          ${s.date}, ${s.time}
        </div>
      </div>
    `;

    container.appendChild(li);
  });
}

function renderUpcomingSessions(sessions) {
  const container = document.getElementById("upcomingSessionsList");
  container.innerHTML = "";

  if (!sessions || sessions.length === 0) {
    container.innerHTML = `<li class="activity-item">No upcoming sessions</li>`;
    return;
  }

  const colors = ["#1a2a6c", "#b21f1f", "#3498db", "#2ecc71", "#9b59b6"];

  sessions.forEach((s, index) => {
    const bgColor = colors[index % colors.length];

    const li = document.createElement("li");
    li.classList.add("activity-item");

    li.innerHTML = `
      <div class="activity-icon" style="background-color: ${bgColor}">
        <i class="fas fa-car"></i>
      </div>
      <div class="activity-content">
        <div class="activity-title">
          ${s.courseName || "Driving Session"}
        </div>
        <div class="activity-desc">
          Instructor: ${s.licenseId || "Not Assigned"} |
          Vehicle: ${s.vehicleNumber || "Not Assigned"}
        </div>
        <div class="activity-time">
          ${formatDate(s.date)} | ${formatTime(s.time)}
        </div>
      </div>
    `;

    container.appendChild(li);
  });
}

function formatDate(dateStr) {
  const date = new Date(dateStr);
  return date.toLocaleDateString("en-US", {
    weekday: "short",
    month: "short",
    day: "numeric",
  });
}

function formatTime(timeStr) {
  if (!timeStr) return "Time not specified";
  const [hour, minute] = timeStr.split(":");
  const date = new Date();
  date.setHours(hour, minute);
  return date.toLocaleTimeString("en-US", {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true,
  });
}
