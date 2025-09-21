      document
        .getElementById("instructorForm")
        .addEventListener("submit", async function (e) {
          e.preventDefault();

          const submitBtn = document.getElementById("loginBtn");
          const originalText = submitBtn.innerHTML;
          submitBtn.innerHTML =
            '<i class="fas fa-spinner fa-spin"></i> Logging in...';
          submitBtn.disabled = true;

          const nic = document.getElementById("licenseId").value.trim();
          const email = document.getElementById("email").value.trim();
          const password = document.getElementById("password").value.trim();

          if (!licenseId || !email || !password) {
            showAlert("Please fill in all fields", "error");
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
            return;
          }

          try {
            const response = await fetch(
              "http://localhost:8080/auth/employee/login",
              {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({
                  nic: nic,
                  email: email,
                  password: password,
                }),
              }
            );

            if (!response.ok) {
              throw new Error("Login failed");
            }

            const data = await response.json();
            console.log("Login Success:", data);

            if (data.data.accessToken) {
              localStorage.setItem("token", data.data.accessToken);
              localStorage.setItem("userRole", data.data.jobRole);
              localStorage.setItem("nic", data.data.nic);
              localStorage.setItem("username",data.data.username);
              localStorage.setItem("licenseId",data.data.licenseId);
            } 
            showAlert(
              "Login successful! Redirecting to dashboard...",
              "success"
            );

            if (data.data.jobRole === "ADMIN") {
              showAlert(
                "Login successful! Redirecting to Admin dashboard...",
                "success"
              );
              setTimeout(() => {
                console.log("ADMIN")
                window.location.href = "admin/home.html";
              }, 1500);
            } else if (data.data.jobRole === "INSTRUCTOR") {
              showAlert(
                "Login successful! Redirecting to Instructor dashboard...",
                "success"
              );
              setTimeout(() => {
                console.log("Instructor")
                window.location.href = "instructor/home.html";
              }, 1500);
            } else {
              showAlert("Unknown role. Please contact support.", "error");
            }
          } catch (error) {
            console.error("Error:", error);
            showAlert("Invalid credentials. Please try again.", "error");
          } finally {
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
          }
        });

      function showAlert(message, type) {
        const alertBox = document.getElementById("alertBox");
        alertBox.textContent = message;
        alertBox.className = `alert alert-${type}`;
        alertBox.style.display = "block";

        setTimeout(() => {
          alertBox.style.display = "none";
        }, 5000);
      }
