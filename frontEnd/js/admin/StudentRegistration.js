import { getAccessToken, getRefreshToken, fetchWithAuth, checkToken, redirectToLogin } from '../Auth.js';

let token = getAccessToken();
let selectedCourseName = "";

if (!token || token === "undefined" || token === "null") {
  console.log("No valid token found");
  redirectToLogin();
} else {
  console.log("Token exists:", token);
}

document.getElementById("course_id").addEventListener("change", function () {
  selectedCourseName = this.options[this.selectedIndex].text;
  console.log("Selected course name:", selectedCourseName);
});

document.addEventListener("DOMContentLoaded", function () {
  loadData();

  const viewApplicationsBtn = document.getElementById("viewApplicationsBtn");
  const applicationsModal = document.getElementById("applicationsModal");
  const closeApplicationsModal = document.getElementById("closeApplicationsModal");

  // Show applications modal and load applications
  viewApplicationsBtn.addEventListener("click", function () {
    applicationsModal.style.display = "flex";
    viewApplication(); // now function exists
  });

  closeApplicationsModal.addEventListener("click", function () {
    applicationsModal.style.display = "none";
  });

  applicationsModal.addEventListener("click", function (e) {
    if (e.target === applicationsModal) {
      applicationsModal.style.display = "none";
    }
  });
});

async function loadData() {
  loadRegisteredStudents();
  getCurrentDateAndTime();
  loadAvailableCourses();
}

// Load courses into select dropdown
async function loadAvailableCourses() {
  try {
    const response = await fetchWithAuth("http://localhost:8080/admin/getCourseDetails", {
      headers: { "Content-Type": "application/json" }
    });

    const result = await response.json();
    console.log(result);

    const courseSelect = document.getElementById("course_id");
    courseSelect.innerHTML = `<option value="">Select a course</option>`;

    if (result.data && result.data.length > 0) {
      result.data.forEach((course) => {
        const option = document.createElement("option");
        option.value = course.courseId;
        option.textContent = course.courseName;
        courseSelect.appendChild(option);
      });
    }
  } catch (error) {
    alert("Error loading courses", error);
  }
}

// Get current date & time for form
function getCurrentDateAndTime() {
  const now = new Date();

  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  const formattedDate = `${year}-${month}-${day}`;

  const hours = String(now.getHours()).padStart(2, "0");
  const minutes = String(now.getMinutes()).padStart(2, "0");
  const formattedTime = `${hours}:${minutes}`;

  document.getElementById("payment_date").value = formattedDate;
  document.getElementById("payment_time").value = formattedTime;
}

// Load all registered students
async function loadRegisteredStudents() {
  try {
    const response = await fetchWithAuth("http://localhost:8080/admin/getRegisteredStudents", {
      headers: { "Content-Type": "application/json" }
    });

    const result = await response.json();

    const tbody = document.querySelector(".table-container tbody");
    tbody.innerHTML = "";

    if (result.data && result.data.length > 0) {
      result.data.forEach((student) => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${student.nic}</td>
          <td>${student.name}</td>
          <td>${student.email}</td>
          <td>${student.contact}</td>
          <td>${student.address}</td>
          <td>
            <div class="action-buttons">
              <button class="action-btn view-btn" title="View Details" onclick="viewStudent('${student.nic}','${student.name}','${student.email}','${student.contact}','${student.address}')">
                <i class="fas fa-eye"></i>
              </button>
              <button class="action-btn edit-btn" title="Edit Student" onclick="viewStudent('${student.nic}','${student.name}','${student.email}','${student.contact}','${student.address}')">
                <i class="fas fa-edit"></i>
              </button>
              <button class="action-btn delete-btn" title="Delete Student" onclick="deleteStudent('${student.nic}')">
                <i class="fas fa-trash"></i>
              </button>
            </div>
          </td>
        `;
        tbody.appendChild(row);
      });
    } else {
      tbody.innerHTML = `
        <tr>
          <td colspan="6" class="empty-table-message">
            <i class="fas fa-info-circle" style="font-size: 48px; margin-bottom: 15px"></i>
            <p>No students registered yet. Register a student to see them listed here.</p>
          </td>
        </tr>
      `;
    }
  } catch (err) {
    console.error(err);
    showMessage("Failed to load students.", "error");
  }
}

async function viewApplication() {
  try {
    const response = await fetchWithAuth("http://localhost:8080/application/getApply", {
      headers: { "Content-Type": "application/json" }
    });
    const result = await response.json();

    const tbody = document.getElementById("applicationsTableBody");
    tbody.innerHTML = "";

    if (result.data && result.data.length > 0) {
      result.data.forEach((app) => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${app.address}</td>
          <td>${app.contact}</td>
          <td>${app.email}</td>
          <td>${app.name}</td>
          <td>${app.nic}</td>
          <td>${app.paidAmount}</td>
          <td>${app.courseName}</td>
          <td>
            <button onclick="processApplication('${app.nic}', 'APPROVED')">Approve</button>
            <button onclick="processApplication('${app.nic}', 'DENIED')">Deny</button>
          </td>
        `;
        tbody.appendChild(row);
      });
    } else {
      tbody.innerHTML = `<tr><td colspan="8">No applications found.</td></tr>`;
    }
  } catch (err) {
    console.error(err);
    alert("Failed to load applications.");
  }
}