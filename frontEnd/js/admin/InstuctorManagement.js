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

      async function reloadInstructors() {
        
        try {
          const response = await fetch(
            "http://localhost:8080/admin/getInstructors",
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
            result.data.forEach((instructor) => {
              const row = document.createElement("tr");
              row.innerHTML = `
                               <td>${instructor.nic}</td>
                               <td>${instructor.name}</td>
                               <td>${instructor.contact}</td>
                               <td>${instructor.email}</td>
                               <td>${instructor.address}</td>
                               <td>
                                   <div class="action-buttons">
                                       <div class="btn-icon btn-view"><i class="fas fa-eye"></i></div>
                                       <div class="btn-icon btn-edit"><i class="fas fa-edit"></i></div>
                                       <div class="btn-icon btn-delete"><i class="fas fa-trash"></i></div>
                                   </div>
                               </td>
                           `;
              tbody.appendChild(row);
            });
          } else {
            tbody.innerHTML = `
                           <tr>
                               <td colspan="6">
                                   <div class="empty-state">
                                       <i class="fas fa-chalkboard-teacher"></i>
                                       <p>No instructors found</p>
                                       <p>Add a new instructor to get started</p>
                                   </div>
                               </td>
                           </tr>
                       `;
          }
        } catch (error) {
          console.error("Error fetching instructors:", error);
          showMessage("Error loading instructors", "error");
        }
      }

      function showInstructorDetails(instructor) {
        document.getElementById("nic").value = instructor.nic;
        document.getElementById("name").value = instructor.name;
        document.getElementById("contact").value = instructor.contact;
        document.getElementById("email").value = instructor.email;
        document.getElementById("address").value = instructor.address;

        document.getElementById("nic").readOnly = true;
        document.getElementById("name").readOnly = true;
        document.getElementById("contact").readOnly = true;
        document.getElementById("email").readOnly = true;
        document.getElementById("address").readOnly = true;

        const registerButton = document.querySelector(".btn-register");
        registerButton.style.display = "none";

        const formHeader = document.querySelector(".form-header h2");
        formHeader.innerHTML =
          '<i class="fas fa-eye"></i> View Instructor Details';

        const cancelButton = document.querySelector(".btn-container button");
        if (cancelButton) {
          cancelButton.textContent = "Close";
          cancelButton.style.display = "block";
          cancelButton.onclick = function () {
            exitViewMode();
          };
        }

        document.getElementById("add-instructor").scrollIntoView({
          behavior: "smooth",
          block: "start",
        });
      }

      function exitViewMode() {
        document.getElementById("nic").readOnly = false;
        document.getElementById("name").readOnly = false;
        document.getElementById("contact").readOnly = false;
        document.getElementById("email").readOnly = false;
        document.getElementById("address").readOnly = false;

        const registerButton = document.querySelector(".btn-register");
        registerButton.style.display = "flex";

        const formHeader = document.querySelector(".form-header h2");
        formHeader.innerHTML =
          '<i class="fas fa-user-plus"></i> Add New Instructor';

        const cancelButton = document.querySelector(".btn-container button");
        if (cancelButton) {
          cancelButton.style.display = "none";
        }

        document.getElementById("instructorForm").reset();
      }

      document.addEventListener("DOMContentLoaded", async () => {
        checkToken();
        reloadInstructors();

        const form = document.getElementById("instructorForm");

        function showMessage(message, type = "success") {
          const messageBox = document.getElementById("message-box");
          messageBox.textContent = message;
          messageBox.className = `message-box message-${type}`;
          messageBox.style.display = "block";

          setTimeout(() => {
            messageBox.style.display = "none";
          }, 3000);
        }

        const registerButton = document.querySelector(".btn-register");
        const originalButtonText = registerButton.innerHTML;

        let isEditMode = false;
        let currentEditId = null;

        function populateFormForEditing(instructor) {
          document.getElementById("nic").value = instructor.nic;
          document.getElementById("name").value = instructor.name;
          document.getElementById("contact").value = instructor.contact;
          document.getElementById("email").value = instructor.email;
          document.getElementById("address").value = instructor.address;

          document.getElementById("nic").readOnly = true;
          document.getElementById("name").readOnly = false;
          document.getElementById("contact").readOnly = false;
          document.getElementById("email").readOnly = false;
          document.getElementById("address").readOnly = false;

          currentEditId = instructor.nic;

          enterEditMode();

          document.getElementById("add-instructor").scrollIntoView({
            behavior: "smooth",
            block: "start",
          });
        }

        function enterEditMode() {
          isEditMode = true;

          registerButton.innerHTML =
            '<i class="fas fa-sync-alt"></i> Update Instructor';
          registerButton.style.background =
            "linear-gradient(135deg, #ff8a4c, #b21f1f)";

          const formHeader = document.querySelector(".form-header h2");
          formHeader.innerHTML =
            '<i class="fas fa-user-edit"></i> Edit Instructor';
        }

        function exitEditMode() {
          isEditMode = false;
          currentEditId = null;

          document.getElementById("nic").readOnly = false;
          document.getElementById("name").readOnly = false;
          document.getElementById("contact").readOnly = false;
          document.getElementById("email").readOnly = false;
          document.getElementById("address").readOnly = false;

          registerButton.innerHTML = originalButtonText;
          registerButton.style.background =
            "linear-gradient(135deg, #1a2a6c, #b21f1f)";

          const formHeader = document.querySelector(".form-header h2");
          formHeader.innerHTML =
            '<i class="fas fa-user-plus"></i> Add New Instructor';
        }

        document.addEventListener("click", function (e) {
          if (
            e.target.closest(".btn-view") ||
            (e.target.classList.contains("fa-eye") &&
              e.target.closest(".btn-icon"))
          ) {
            const row = e.target.closest("tr");

            const instructor = {
              nic: row.cells[0].textContent,
              name: row.cells[1].textContent,
              contact: row.cells[2].textContent,
              email: row.cells[3].textContent,
              address: row.cells[4].textContent,
            };

            showInstructorDetails(instructor);
          }

          if (
            e.target.closest(".btn-edit") ||
            (e.target.classList.contains("fa-edit") &&
              e.target.closest(".btn-icon"))
          ) {
            const row = e.target.closest("tr");

            const instructor = {
              nic: row.cells[0].textContent,
              name: row.cells[1].textContent,
              contact: row.cells[2].textContent,
              email: row.cells[3].textContent,
              address: row.cells[4].textContent,
            };

            populateFormForEditing(instructor);
          }
        });

        form.addEventListener("submit", async (e) => {
          e.preventDefault();

          const instructorData = {
            nic: document.getElementById("nic").value.trim(),
            name: document.getElementById("name").value.trim(),
            contact: document.getElementById("contact").value.trim(),
            email: document.getElementById("email").value.trim(),
            address: document.getElementById("address").value.trim(),
          };

          try {
            let url, method;

            if (isEditMode) {
              url = `http://localhost:8080/admin/employeeUpdate/${currentEditId}`;
              method = "PUT";
            } else {
              url = "http://localhost:8080/admin/employeeRegister";
              method = "POST";
            }

            const response = await fetch(url, {
              method: method,
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
              body: JSON.stringify(instructorData),
            });

            const result = await response.json();

            if (response.ok) {
              showMessage(
                isEditMode
                  ? "Instructor updated successfully!"
                  : "Instructor registered successfully!",
                "success"
              );
              
              if (isEditMode) {
                exitEditMode();
              }

              form.reset();
              reloadInstructors();
            } else {
              showMessage(result.data || "Operation failed.", "error");
            }
          } catch (err) {
            showMessage("An error occurred.", "error");
          }
        });

        const btnContainer = document.querySelector(".btn-container");
        const cancelButton = document.createElement("button");
        cancelButton.textContent = "Cancel";
        cancelButton.type = "button";
        cancelButton.style.marginRight = "10px";
        cancelButton.style.padding = "12px 20px";
        cancelButton.style.borderRadius = "5px";
        cancelButton.style.border = "1px solid #ddd";
        cancelButton.style.background = "#f5f5f5";
        cancelButton.style.cursor = "pointer";
        cancelButton.style.display = "none";

        cancelButton.addEventListener("click", function () {
          if (isEditMode) {
            exitEditMode();
          } else {
            exitViewMode();
          }
          form.reset();
          cancelButton.style.display = "none";
        });

        btnContainer.prepend(cancelButton);

        function enterEditMode() {
          isEditMode = true;

          registerButton.innerHTML =
            '<i class="fas fa-sync-alt"></i> Update Instructor';
          registerButton.style.background =
            "linear-gradient(135deg, #ff8a4c, #b21f1f)";

          const formHeader = document.querySelector(".form-header h2");
          formHeader.innerHTML =
            '<i class="fas fa-user-edit"></i> Edit Instructor';

          cancelButton.style.display = "block";
        }

        function exitEditMode() {
          isEditMode = false;
          currentEditId = null;

          document.getElementById("nic").readOnly = false;
          document.getElementById("name").readOnly = false;
          document.getElementById("contact").readOnly = false;
          document.getElementById("email").readOnly = false;
          document.getElementById("address").readOnly = false;

          registerButton.innerHTML = originalButtonText;
          registerButton.style.background =
            "linear-gradient(135deg, #1a2a6c, #b21f1f)";

          const formHeader = document.querySelector(".form-header h2");
          formHeader.innerHTML =
            '<i class="fas fa-user-plus"></i> Add New Instructor';

          cancelButton.style.display = "none";
        }

        let currentDeleteNIC = null;

        function showDeleteModal(nic, name) {
          const modal = document.getElementById("deleteModal");
          const modalText = document.getElementById("deleteModalText");

          modalText.textContent = `Are you sure you want to delete instructor ${name} (NIC: ${nic})? This action cannot be undone.`;
          currentDeleteNIC = nic;
          modal.style.display = "flex";
        }

        function hideDeleteModal() {
          const modal = document.getElementById("deleteModal");
          modal.style.display = "none";
          currentDeleteNIC = null;
        }

        document.addEventListener("click", function (e) {
          if (
            e.target.closest(".btn-delete") ||
            (e.target.classList.contains("fa-trash") &&
              e.target.closest(".btn-icon"))
          ) {
            const row = e.target.closest("tr");
            const nic = row.cells[0].textContent;
            const name = row.cells[1].textContent;

            showDeleteModal(nic, name);
          }
        });

        document
          .getElementById("confirmDelete")
          .addEventListener("click", function () {
            if (currentDeleteNIC) {
              deleteInstructor(currentDeleteNIC);
              hideDeleteModal();
            }
          });

        document
          .getElementById("cancelDelete")
          .addEventListener("click", hideDeleteModal);

        document
          .getElementById("deleteModal")
          .addEventListener("click", function (e) {
            if (e.target.id === "deleteModal") {
              hideDeleteModal();
            }
          });

        async function deleteInstructor(nic) {
          try {
            const token = localStorage.getItem("token");
            const response = await fetch(
              `http://localhost:8080/admin/employeeDelete/${nic}`,
              {
                method: "DELETE",
                headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${token}`,
                },
              }
            );

            const result = await response.json();

            if (response.ok) {
              showMessage("Instructor deleted successfully!", "success");
              setTimeout(() => {
                reloadInstructors();
              }, 1500);
            } else {
              showMessage(
                result.data || "Failed to delete instructor.",
                "error"
              );
            }
          } catch (err) {
            showMessage(
              "An error occurred while deleting instructor.",
              "error"
            );
          }
        }
      });
