      const token = localStorage.getItem("token");
      let selectedCourseName = "";
      if (!token || token === "undefined" || token === "null") {
        console.log("No valid token found");
        window.location.href = "/pages/employeelogin.html";
      } else {
        console.log("Token exists:", token);
      }

      document
        .getElementById("course_id")
        .addEventListener("change", function () {
          selectedCourseName = this.options[this.selectedIndex].text;
          console.log("Selected course name:", selectedCourseName);
        });

      document.addEventListener("DOMContentLoaded", function () {
        loadData();

        const viewApplicationsBtn = document.getElementById(
          "viewApplicationsBtn"
        );
        const applicationsModal = document.getElementById("applicationsModal");
        const closeApplicationsModal = document.getElementById(
          "closeApplicationsModal"
        );

        viewApplicationsBtn.addEventListener("click", function () {
          applicationsModal.style.display = "flex";
          viewApplication();
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

      function loadData() {
        loadRegisteredStudents();
        getCurrentDateAndTime();
        loadAvailableCourses();
      }

      async function loadAvailableCourses() {
        try {
          const response = await fetch(
            "http://localhost:8080/admin/getCourseDetails",
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );
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
          alert("Error", error);
        }
      }

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

      async function loadRegisteredStudents() {
        try {
          const response = await fetch(
            "http://localhost:8080/admin/getRegisteredStudents",
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

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

      function showMessage(message, type = "success") {
        const messageBox = document.getElementById("message-box");
        messageBox.textContent = message;
        messageBox.className = `message-box message-${type}`;
        messageBox.style.display = "block";

        setTimeout(() => {
          messageBox.style.display = "none";
        }, 3000);
      }

      function editStudent(nic) {
        alert(`Edit student with NIC: ${nic}`);
      }

      function viewStudent(nic, name, email, contact, address) {
        const studentData = {
          nic: nic,
          name: name,
          email: email,
          contact: contact,
          address: address,
        };

        document.getElementById("detail_nic").value = studentData.nic;
        document.getElementById("detail_name").value = studentData.name;
        document.getElementById("detail_email").value = studentData.email;
        document.getElementById("detail_contact").value = studentData.contact;
        document.getElementById("detail_address").value = studentData.address;

        document.getElementById("studentDetailModal").style.display = "flex";
      }

      document.getElementById("closeModal").addEventListener("click", () => {
        document.getElementById("studentDetailModal").style.display = "none";
      });

      document.getElementById("cancelEdit").addEventListener("click", () => {
        document.getElementById("studentDetailModal").style.display = "none";
      });

      document
        .getElementById("modeToggle")
        .addEventListener("change", function () {
          const modalBody = document.getElementById("modalBody");
          if (this.checked) {
            modalBody.classList.remove("view-mode");
            modalBody.classList.add("edit-mode");

            document
              .querySelectorAll("#studentDetailForm input")
              .forEach((input) => {
                if (input.id !== "detail_nic") {
                  input.removeAttribute("readonly");
                }
              });
          } else {
            modalBody.classList.remove("edit-mode");
            modalBody.classList.add("view-mode");

            document
              .querySelectorAll("#studentDetailForm input")
              .forEach((input) => {
                input.setAttribute("readonly", true);
              });
          }
        });

      async function deleteStudent(nic) {
        if (
          confirm(`Are you sure you want to delete student with NIC: ${nic}?`)
        ) {
          try {
            const response = await fetch(
              `http://localhost:8080/admin/studentDelete/${nic}`,
              {
                headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${token}`,
                },
              }
            );
            const result = await response.json();
            if (!response.ok) {
              alert(
                "Deletion failed: " + (result.message || JSON.stringify(result))
              );
              return;
            }
            showMessage(
              `Student with NIC: ${nic} deleted successfully.`,
              "success"
            );
            loadData();
          } catch (error) {
            alert("Delete failed", error);
          }
        }
      }

      document
        .getElementById("studentRegistrationForm")
        .addEventListener("submit", async (e) => {
          e.preventDefault();

          console.log("submit");

          const studentData = {
            nic: document.getElementById("nic").value.trim(),
            name: document.getElementById("name").value.trim(),
            email: document.getElementById("email").value.trim(),
            contact: document.getElementById("contact").value.trim(),
            address: document.getElementById("address").value.trim(),
          };

          const paymentData = {
            paidAmount: document.getElementById("payment_amount").value.trim(),
            courseName: selectedCourseName,
            studentNic: document.getElementById("nic").value.trim(),
            paymentDate: document.getElementById("payment_date").value.trim(),
            paidTime: document.getElementById("payment_time").value.trim(),
          };

          const payload = {
            studentDTO: studentData,
            paymentDTO: paymentData,
          };

          console.log(payload);
          try {
            const response = await fetch(
              "http://localhost:8080/admin/studentRegister",
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
              showMessage(
                result.data || "Student registered successfully!",
                "success"
              );
              document.getElementById("studentRegistrationForm").reset();
              getCurrentDateAndTime();
              loadRegisteredStudents();
            } else {
              showMessage(
                result.data || "Failed to register student.",
                "error"
              );
              console.log(result);
            }
          } catch (err) {
            console.error(err);
            showMessage(
              "An error occurred while registering the student.",
              "error"
            );
          }
        });

      document
        .getElementById("studentDetailForm")
        .addEventListener("submit", async function (e) {
          e.preventDefault();

          const nic = document.getElementById("detail_nic").value;
          const name = document.getElementById("detail_name").value;
          const email = document.getElementById("detail_email").value;
          const contact = document.getElementById("detail_contact").value;
          const address = document.getElementById("detail_address").value;

          const studentData = {
            nic: nic,
            name: name,
            email: email,
            address: address,
            contact: contact,
          };

          try {
            const response = await fetch(
              "http://localhost:8080/admin/studentUpdate",
              {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(studentData),
              }
            );

            if (!response.ok) {
              throw new Error(`HTTP error! status: ${response.status}`);
            }

            const result = await response.json();
            console.log("Update successful:", result);

            const modal = document.getElementById("studentDetailModal");
            if (modal) {
              modal.style.display = "none";
            }

            alert("Student details updated successfully!");
            loadData();
          } catch (error) {
            console.error("Error updating student:", error);
            loadData();
            alert("Failed to update student. Check console for details.");
          }
        });

         let applicationsData = [];
      async function viewApplication() {
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
          console.log(result);

           

          const tbody = document.getElementById("applicationsTableBody");
          tbody.innerHTML = "";

          let applications = [];
          if (result.data) {
            if (Array.isArray(result.data)) {
              applications = result.data;
            } else if (typeof result.data === "object") {
              applications = Object.values(result.data);
            }
          }

            applicationsData = applications;

          if (applications.length > 0) {
            applications.forEach((app) => {
              const row = document.createElement("tr");
              row.innerHTML = `
                    <td>${app.address}</td>
                    <td>${app.contact}</td>
                    <td>${app.email}</td>
                    <td>${app.name}</td>
                    <td>${app.nic}</td>
                    <td>${app.paidAmount || "-"}</td>
                    <td>${app.courseName || "-"}</td>
                    <td>
                        <button class="action-btn view-btn" onclick="viewApplicationDetail(${
                          app.id
                        })">
                            <i class="fas fa-eye"></i>
                        </button>
                    </td>
                `;
              tbody.appendChild(row);
            });
          } else {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="empty-table-message">
                        <i class="fas fa-info-circle" style="font-size: 48px; margin-bottom: 15px"></i>
                        <p>No applications found.</p>
                    </td>
                </tr>
            `;
          }

          document.getElementById("applicationsModal").style.display = "flex";
        } catch (error) {
          alert("Error loading applications: " + error);
        }
      }
     
      let currentApplicationId = null;
      function viewApplicationDetail(id) {
         currentApplicationId = id; 
        document.getElementById("applicationsModal").style.display = "none";
        console.log("working");
        const app = applicationsData.find((a) => a.id === id);
        if (!app) return;
       
        document.getElementById("detailName").value = app.name;
        document.getElementById("detailNic").value = app.nic;
        document.getElementById("detailEmail").value = app.email;
        document.getElementById("detailContact").value = app.contact;
        document.getElementById("detailAddress").value = app.address;
        document.getElementById("detailCourse").value = app.courseName;
        document.getElementById("detailPaidAmount").value = app.paidAmount;

          document.getElementById(
    "nicFrontPreview"
  ).src = app.nicFrontUrl
    ? `http://localhost:8080/application/imageDB/imagesDB/${app.nicFrontUrl.split("/").pop()}`
    : "";

  document.getElementById(
    "nicBackPreview"
  ).src = app.nicBackUrl
    ? `http://localhost:8080/application/imageDB/imagesDB/${app.nicBackUrl.split("/").pop()}`
    : "";

  document.getElementById(
    "paymentSlipPreview"
  ).src = app.paymentSlip
    ? `http://localhost:8080/application/imageDB/imagesDB/${app.paymentSlip.split("/").pop()}`
    : "";
        document.getElementById("applicationDetailModal").style.display =
          "flex";
      }

      function closeApplicationModal() {
        document.getElementById("applicationDetailModal").style.display =
          "none";
      }

     async function processApplication(actionTaken){
      console.log( currentApplicationId);
      try{
        const response = await fetch(
          `http://localhost:8080/admin/approve/${currentApplicationId}/${actionTaken}`,
          {
            headers:{
              "Content-Type":"application/json",
              Authorization:`Bearer ${token}`,
            }
          }
        );

            if (!response.ok) {
      throw new Error("Failed to update application");
    }

        const result = await response.json();
        console.log(result);

        if(actionTaken === "DENIED"){
          closeApplicationModal();
          return;
        }
            if (actionTaken === "APPROVED") {
      closeApplicationModal();

      const app = applicationsData.find((a) => a.id === currentApplicationId);
      if (app) {
        document.getElementById("payment_amount").value = app.paidAmount || "";
        document.getElementById("nic").value = app.nic || "";
        document.getElementById("name").value = app.name || "";
        document.getElementById("email").value = app.email || "";
        document.getElementById("contact").value = app.contact || "";
        document.getElementById("address").value = app.address || "";

        const courseDropdown = document.getElementById("course_id");
        for (let option of courseDropdown.options) {
          if (option.text === app.courseName || option.value === app.courseId) {
            option.selected = true;
            break;
          }
        }

        document.getElementById("payment_date").value =
          new Date().toISOString().split("T")[0];
        document.getElementById("payment_time").value = new Date()
          .toTimeString()
          .split(" ")[0]
          .substring(0, 5);
      }
    }

      }catch(error){
        alert(err)
      }
      }