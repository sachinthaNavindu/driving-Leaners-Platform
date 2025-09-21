      const token = localStorage.getItem("token");
      if (!token || token === "undefined" || token === "null") {
        window.location.href = "/pages/employeelogin.html";
      }

      const courseForm = document.getElementById("course-form");
      const tableBody = document.getElementById("coursesTableBody");
      const searchInput = document.getElementById("searchInput");
      const sortFilter = document.getElementById("sortFilter");
      const deleteModal = document.getElementById("deleteModal");
      const courseToDeleteNameEl =
        document.getElementById("courseToDeleteName");
      const cancelDeleteBtn = document.getElementById("cancelDelete");
      const courseIdDisplay = document.getElementById("courseIdDisplay");

      let coursesData = [];
      let courseToDeleteName = null;
      let isViewMode = false;
      let isEditMode = false;
      let currentViewCourseName = null;
      let currentEditCourseName = null;

      document.addEventListener("DOMContentLoaded", function () {
        loadCourses();

        searchInput.addEventListener("input", filterCourses);
        sortFilter.addEventListener("change", filterCourses);

        cancelDeleteBtn.addEventListener("click", closeDeleteModal);
      });

      courseForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        if (isViewMode) {
          exitViewMode();
          return;
        }

        if (isEditMode) {
          updateCourse();
          return;
        }

        const courseData = {
          courseName: document.getElementById("course_name").value.trim(),
          courseFee: parseFloat(document.getElementById("course_fee").value),
          sessions: parseInt(document.getElementById("sessions").value),
        };

        try {
          const response = await fetch(
            "http://localhost:8080/admin/saveCourse",
            {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
              body: JSON.stringify(courseData),
            }
          );

          if (response.ok) {
            const result = await response.json();
            showMessage("Course saved successfully!", "success");
            courseForm.reset();
            loadCourses();
            window.scrollTo({ top: 0, behavior: "smooth" });
          } else {
            showMessage("Failed to save course", "error");
          }
        } catch (error) {
          showMessage("Error saving course", "error");
          console.error("Error:", error);
        }
      });

      async function loadCourses() {
        try {
          const response = await fetch(
            "http://localhost:8080/admin/getCourseDetails",
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (response.ok) {
            const result = await response.json();
            coursesData = result.data || [];
            filterCourses();
          } else {
            console.error("Failed to load courses");
            showMessage("Failed to load courses", "error");
          }
        } catch (error) {
          console.error("Error:", error);
          showMessage("Error loading courses", "error");
        }
      }

      function filterCourses() {
        const searchTerm = searchInput.value.toLowerCase();
        const sortValue = sortFilter.value;

        let filteredCourses = coursesData;

        if (searchTerm) {
          filteredCourses = filteredCourses.filter((course) =>
            course.courseName.toLowerCase().includes(searchTerm)
          );
        }

        switch (sortValue) {
          case "name":
            filteredCourses.sort((a, b) =>
              a.courseName.localeCompare(b.courseName)
            );
            break;
          case "fee":
            filteredCourses.sort((a, b) => a.courseFee - b.courseFee);
            break;
          case "sessions":
            filteredCourses.sort((a, b) => b.sessions - a.sessions);
            break;
          case "newest":
          default:
            filteredCourses.sort((a, b) =>
              a.courseName.localeCompare(b.courseName)
            );
            break;
        }

        updateCourseTable(filteredCourses);
      }

      function updateCourseTable(courses) {
        tableBody.innerHTML = "";

        if (courses.length === 0) {
          tableBody.innerHTML = `
      <tr>
        <td colspan="4">
          <div class="empty-state">
            <i class="fas fa-book"></i>
            <p>No courses found</p>
            <p>Add a new course to get started</p>
          </div>
        </td>
      </tr>
    `;
          return;
        }

        courses.forEach((course) => {
          const row = document.createElement("tr");
          row.setAttribute("data-course-name", course.courseName);
          row.innerHTML = `
      <td>${course.courseName}</td>
      <td>${
        course.courseFee !== undefined && course.courseFee !== null
          ? Number(course.courseFee).toFixed(2)
          : "0.00"
      }</td>
      <td>${course.sessions}</td>
      <td>
        <div class="action-buttons">
          <div class="btn-icon btn-view" title="View Course" onclick="viewCourse('${course.courseName.replace(
            /'/g,
            "\\'"
          )}')">
            <i class="fas fa-eye"></i>
          </div>
          <div class="btn-icon btn-edit" title="Edit Course" onclick="editCourse('${course.courseName.replace(
            /'/g,
            "\\'"
          )}')">
            <i class="fas fa-edit"></i>
          </div>
          <div class="btn-icon btn-delete" title="Delete Course" onclick="showDeleteModal('${course.courseName.replace(
            /'/g,
            "\\'"
          )}')">
            <i class="fas fa-trash"></i>
          </div>
        </div>
      </td>
    `;
          tableBody.appendChild(row);
        });
      }

      function showDeleteModal(courseName) {
        courseToDeleteName = courseName;
        courseToDeleteNameEl.textContent = `Course: ${courseName}`;
        deleteModal.style.display = "flex";
      }

      function closeDeleteModal() {
        deleteModal.style.display = "none";
        courseToDeleteName = null;
      }

      async function deleteCourse() {
        if (!courseToDeleteName) return;

        try {
          const response = await fetch(
            `http://localhost:8080/admin/courseDelete/${encodeURIComponent(
              courseToDeleteName
            )}`,
            {
              method: "DELETE",
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (response.ok) {
            showMessage("Course deleted successfully!", "success");
            loadCourses();
          } else {
            showMessage("Failed to delete course", "error");
          }
        } catch (error) {
          showMessage("Error deleting course", "error");
          console.error("Error:", error);
        } finally {
          closeDeleteModal();
        }
      }

      function viewCourse(courseName) {
        const course = coursesData.find((c) => c.courseName === courseName);
        if (!course) return;

        enterViewMode(course);

        document.getElementById("add-course").scrollIntoView({
          behavior: "smooth",
          block: "start",
        });
      }

      function enterViewMode(course) {
        isViewMode = true;
        currentViewCourseName = course.courseName;

        document.getElementById("course_name").value = course.courseName;
        document.getElementById("course_fee").value = course.courseFee;
        document.getElementById("sessions").value = course.sessions;
        courseIdDisplay.textContent = course.courseName;

        document.getElementById("course_name").readOnly = true;
        document.getElementById("course_fee").readOnly = true;
        document.getElementById("sessions").readOnly = true;

        const formHeader = document.querySelector(".form-header h2");
        formHeader.innerHTML = '<i class="fas fa-eye"></i> View Course Details';

        const submitButton = document.querySelector(".btn-register");
        submitButton.innerHTML = '<i class="fas fa-times"></i> Close View';
        submitButton.style.background =
          "linear-gradient(135deg, #6c757d, #495057)";

        document.querySelector(".course-id-display").style.display = "block";
      }

      function exitViewMode() {
        isViewMode = false;
        currentViewCourseName = null;

        courseForm.reset();

        document.getElementById("course_name").readOnly = false;
        document.getElementById("course_fee").readOnly = false;
        document.getElementById("sessions").readOnly = false;

        const formHeader = document.querySelector(".form-header h2");
        formHeader.innerHTML =
          '<i class="fas fa-plus-circle"></i> Add New Course';

        const submitButton = document.querySelector(".btn-register");
        submitButton.innerHTML = '<i class="fas fa-save"></i> Save Course';
        submitButton.style.background =
          "linear-gradient(135deg, #1a2a6c, #b21f1f)";

        courseIdDisplay.textContent = "Will be automatically generated";
      }

      function editCourse(courseName) {
        const course = coursesData.find((c) => c.courseName === courseName);
        if (!course) return;

        enterEditMode(course);

        document.getElementById("add-course").scrollIntoView({
          behavior: "smooth",
          block: "start",
        });
      }

      function enterEditMode(course) {
        isEditMode = true;
        currentEditCourseName = course.courseName;

        document.getElementById("course_name").value = course.courseName;
        document.getElementById("course_fee").value = course.courseFee;
        document.getElementById("sessions").value = course.sessions;
        courseIdDisplay.textContent = course.courseName;

        document.getElementById("course_name").readOnly = false;
        document.getElementById("course_fee").readOnly = false;
        document.getElementById("sessions").readOnly = false;

        const formHeader = document.querySelector(".form-header h2");
        formHeader.innerHTML = '<i class="fas fa-edit"></i> Edit Course';

        const submitButton = document.querySelector(".btn-register");
        submitButton.innerHTML =
          '<i class="fas fa-sync-alt"></i> Update Course';
        submitButton.style.background =
          "linear-gradient(135deg, #ff8a4c, #b21f1f)";

        document.querySelector(".course-id-display").style.display = "block";
      }

      function exitEditMode() {
        isEditMode = false;
        currentEditCourseName = null;

        courseForm.reset();

        document.getElementById("course_name").readOnly = false;
        document.getElementById("course_fee").readOnly = false;
        document.getElementById("sessions").readOnly = false;

        const formHeader = document.querySelector(".form-header h2");
        formHeader.innerHTML =
          '<i class="fas fa-plus-circle"></i> Add New Course';

        const submitButton = document.querySelector(".btn-register");
        submitButton.innerHTML = '<i class="fas fa-save"></i> Save Course';
        submitButton.style.background =
          "linear-gradient(135deg, #1a2a6c, #b21f1f)";

        courseIdDisplay.textContent = "Will be automatically generated";
      }

      async function updateCourse() {
        const courseData = {
          courseName: document.getElementById("course_name").value.trim(),
          courseFee: parseFloat(document.getElementById("course_fee").value),
          sessions: parseInt(document.getElementById("sessions").value),
        };

        try {
          const response = await fetch(
            `http://localhost:8080/admin/courseUpdate/${encodeURIComponent(
              currentEditCourseName
            )}`,
            {
              method: "PUT",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
              body: JSON.stringify(courseData),
            }
          );

          if (response.ok) {
            const result = await response.json();
            showMessage("Course updated successfully!", "success");
            exitEditMode();
            loadCourses();
          } else {
            showMessage("Failed to update course", "error");
          }
        } catch (error) {
          showMessage("Error updating course", "error");
          console.error("Error:", error);
        }
      }

      function showMessage(message, type) {
        const messageBox = document.createElement("div");
        messageBox.className = `message-box message-${type}`;
        messageBox.textContent = message;
        document.body.appendChild(messageBox);

        messageBox.style.display = "block";

        setTimeout(() => {
          messageBox.remove();
        }, 3000);
      }
