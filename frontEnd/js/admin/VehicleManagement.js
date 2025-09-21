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

      document.addEventListener("DOMContentLoaded", () => {
        loadData();
      });

      function loadData() {
        loadTableAndOtherStats();
        checkToken();
      }

      async function loadTableAndOtherStats() {
        try {
          const response = await fetch(
            "http://localhost:8080/admin/getVehicles",
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );

          if (!response.ok) throw new Error("Failed to fetch vehicle data");

          const result = await response.json();
          const data = result.data;

          document
            .querySelectorAll(".stat-card")[0]
            .querySelector(".stat-card-value").textContent =
            data.availableVehicleCount;
          document
            .querySelectorAll(".stat-card")[1]
            .querySelector(".stat-card-value").textContent =
            data.unavailableVehicleCount;

          const tbody = document.querySelector(".vehicle-table tbody");
          tbody.innerHTML = "";

          data.vehicles.forEach((vehicle) => {
            const tr = document.createElement("tr");

            const tdNumber = document.createElement("td");
            tdNumber.textContent = vehicle.vehicleNumber;
            tr.appendChild(tdNumber);

            const tdAvailability = document.createElement("td");
            tdAvailability.textContent = vehicle.availability;
            tdAvailability.classList.add(
              "status-badge",
              vehicle.availability === "Available"
                ? "status-available"
                : "status-unavailable"
            );
            tr.appendChild(tdAvailability);

            const tdType = document.createElement("td");
            tdType.textContent = vehicle.vehicleType;
            tr.appendChild(tdType);

            const tdActions = document.createElement("td");
            tdActions.classList.add("action-buttons");

            const viewBtn = document.createElement("button");
            viewBtn.innerHTML = '<i class="fas fa-eye"></i>';
            viewBtn.classList.add("action-btn", "btn-view");
            viewBtn.title = "View Vehicle Details";
            viewBtn.addEventListener("click", () => {
              loadVehicleToForm(vehicle, false);
            });

            const editBtn = document.createElement("button");
            editBtn.innerHTML = '<i class="fas fa-edit"></i>';
            editBtn.classList.add("action-btn", "btn-edit");
            editBtn.title = "Edit Vehicle";
            editBtn.addEventListener("click", () => {
              loadVehicleToForm(vehicle, true);
            });

            const deleteBtn = document.createElement("button");
            deleteBtn.innerHTML = '<i class="fas fa-trash"></i>';
            deleteBtn.classList.add("action-btn", "btn-delete");
            deleteBtn.title = "Delete Vehicle";

            deleteBtn.addEventListener("click", () => {
              showDeleteConfirmation(vehicle.vehicleNumber);
            });

            tdActions.appendChild(viewBtn);
            tdActions.appendChild(editBtn);
            tdActions.appendChild(deleteBtn);

            tr.appendChild(tdActions);
            tbody.appendChild(tr);
          });
        } catch (error) {
          console.error("Error loading vehicle data:", error);
          showMessage(
            "Failed to load vehicle data. Please try again.",
            "error"
          );
        }
      }

      function showDeleteConfirmation(vehicleNumber) {
        const modal = document.getElementById("confirmModal");
        modal.classList.remove("hidden");

        const yesBtn = document.getElementById("confirmYes");
        const noBtn = document.getElementById("confirmNo");

        yesBtn.onclick = () => {
          deleteVehicle(vehicleNumber);
          modal.classList.add("hidden");
        };

        noBtn.onclick = () => {
          modal.classList.add("hidden");
        };
      }

      function loadVehicleToForm(vehicle, isEditable = false) {
        const vehicleNumberInput = document.getElementById("vehicle_number");
        const availabilitySelect = document.getElementById("availability");
        const vehicleTypeInput = document.getElementById("vehicle_type");
        const submitBtn = document.querySelector(".btn-submit");
        const cancelBtn = document.querySelector(".btn-cancel");

        vehicleNumberInput.value = vehicle.vehicleNumber;
        availabilitySelect.value = vehicle.availability;
        vehicleTypeInput.value = vehicle.vehicleType;

        vehicleNumberInput.readOnly = true;
        vehicleTypeInput.readOnly = !isEditable;
        availabilitySelect.disabled = !isEditable;

        if (isEditable) {
          submitBtn.style.display = "inline-flex";
          submitBtn.innerHTML =
            '<i class="fas fa-sync-alt"></i> Update Vehicle';
          submitBtn.style.background =
            "linear-gradient(135deg, #1a2a6c, #b21f1f)";
          submitBtn.onclick = (e) => {
            e.preventDefault();
            updateVehicle(vehicle.vehicleNumber);
          };
          cancelBtn.textContent = "Cancel";
          cancelBtn.onclick = () => resetFormToAddMode();
        } else {
          submitBtn.style.display = "none";
          cancelBtn.textContent = "Close";
          cancelBtn.onclick = () => resetFormToAddMode();
        }
      }

      async function updateVehicle(vehicleNumber) {
        const availability = document.getElementById("availability").value;
        const vehicleType = document.getElementById("vehicle_type").value;

        console.log(vehicleNumber, availability, vehicleType);
        try {
          const response = await fetch(
            `http://localhost:8080/admin/updateVehicle/${vehicleNumber}`,
            {
              method: "PUT",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
              body: JSON.stringify({
                vehicleNumber,
                availability,
                vehicleType,
              }),
            }
          );

          const result = await response.json();

          if (!response.ok) {
            showMessage("Update failed: " + result.message, "error");
            return;
          }

          showMessage("Vehicle updated successfully!", "success");
          loadData();
          resetFormToAddMode();
        } catch (error) {
          console.error("Error updating vehicle:", error);
          showMessage("Error updating vehicle. Try again.", "error");
        }
      }

      function resetFormToAddMode() {
        const vehicleNumberInput = document.getElementById("vehicle_number");
        const availabilitySelect = document.getElementById("availability");
        const vehicleTypeInput = document.getElementById("vehicle_type");
        const submitBtn = document.querySelector(".btn-submit");
        const cancelBtn = document.querySelector(".btn-cancel");

        document.getElementById("addVehicleForm").reset();

        vehicleNumberInput.readOnly = false;
        availabilitySelect.disabled = false;
        vehicleTypeInput.readOnly = false;

        submitBtn.style.display = "inline-flex";
        submitBtn.innerHTML = '<i class="fas fa-plus"></i> Add Vehicle';
        submitBtn.style.background =
          "linear-gradient(135deg, #1a2a6c, #b21f1f)";
        submitBtn.onclick = null;

        cancelBtn.textContent = "Clear Form";
        cancelBtn.onclick = () =>
          document.getElementById("addVehicleForm").reset();
      }

      async function deleteVehicle(vehicleNumber) {
        try {
          const response = await fetch(
            `http://localhost:8080/admin/vehicleDelete/${vehicleNumber}`,
            {
              method: "DELETE",
              headers: { Authorization: `Bearer ${token}` },
            }
          );

          if (!response.ok) throw new Error("Failed to delete vehicle");

          showMessage("Vehicle deleted successfully!", "success");
          loadTableAndOtherStats();
        } catch (error) {
          console.error("Error deleting vehicle:", error);
          showMessage("Error deleting vehicle. Try again.", "error");
        }
      }

      function showMessage(text, type = "success") {
        const box = document.getElementById("messageBox");
        box.textContent = text;
        box.className = type === "success" ? "success" : "error";
        box.classList.remove("hidden");

        setTimeout(() => box.classList.add("hidden"), 3000);
      }

      document
        .getElementById("addVehicleForm")
        .addEventListener("submit", async (e) => {
          e.preventDefault();
          const vehicleNumber = document
            .getElementById("vehicle_number")
            .value.trim();
          const availability = document.getElementById("availability").value;
          const vehicleType = document
            .getElementById("vehicle_type")
            .value.trim();

          if (!vehicleNumber || !availability || !vehicleType) {
            showMessage("All fields are required!", "error");
            return;
          }

          try {
            const response = await fetch(
              "http://localhost:8080/admin/vehicleSave",
              {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({
                  vehicleNumber,
                  availability,
                  vehicleType,
                }),
              }
            );

            if (!response.ok) {
              const result = await response.json().catch(() => ({}));
              showMessage(result.message || "Process failed..!", "error");
              return;
            }

            showMessage("Vehicle added successfully!", "success");

            setTimeout(() => {
              document.getElementById("addVehicleForm").reset();
              loadTableAndOtherStats();
            }, 50);
          } catch (error) {
            console.error("Error saving vehicle:", error);
            showMessage("Error saving vehicle. Try again.", "error");
          }
        });
