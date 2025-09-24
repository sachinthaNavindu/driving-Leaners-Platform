      document
        .getElementById("loginForm")
        .addEventListener("submit", async function (e) {
          e.preventDefault();

          const submitBtn = this.querySelector("button");
          const originalText = submitBtn.innerHTML;
          submitBtn.innerHTML =
            '<i class="fas fa-spinner fa-spin"></i> Logging in...';
          submitBtn.disabled = true;

          const nic = document.getElementById("nic").value;
          const gmail = document.getElementById("gmail").value;
          const password = document.getElementById("password").value;

          if (!nic || !gmail || !password) {
            showAlert("Please fill in all fields", "error");
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
            return;
          }

          try {
            const response = await fetch(
              "http://localhost:8080/auth/student/login",
              {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({
                  nic: nic,
                  gmail: gmail,
                  password: password,
                }),
              }
            );

            if (!response.ok) {
              throw new Error("Login failed");
            }

            const res = await response.json();
            console.log("Login Success:", res);

            if (res.data && res.data.accessToken) {
              localStorage.setItem("token", res.data.accessToken);
              localStorage.setItem("nic", res.data.nic);
              localStorage.setItem("role", res.data.jobRole);
              localStorage.setItem("licenseId", res.data.licenseId || "");
              localStorage.setItem("refreshToken",res.data.refreshToken);
            }

            showAlert("Login successful! Redirecting...", "success");

            setTimeout(() => {
              window.location.href = 'student/studentDashboard.html';
              console.log(data);
            }, 1500);
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
