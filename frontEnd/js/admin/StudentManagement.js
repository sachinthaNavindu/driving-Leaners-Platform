      const token = localStorage.getItem("token");
      if (!token || token === "undefined" || token === "null") {
        console.log("No valid token found");
        window.location.href = "/pages/employeelogin.html";
      } else {
        console.log("Token exists:", token);
      }

      document.addEventListener("DOMContentLoaded", function () {
        loadData();
      });

      async function loadData() {
        try {
          const response = await fetch(
            "http://localhost:8080/admin/getStudentDetails",
            {
              headers:{
                "Content-Type":"application/json",
                Authorization:`Bearer ${token}`
              },
            }
          );

          const result = await response.json(); 

            console.log(result);

            const studentCount = result.data.registeredStudentCount;
            const activeStudentCount = result.data.activeStudentCount;
            const newApplications = result.data.newApplicationCount;

           document.getElementById("studentCount").innerText = studentCount;
           document.getElementById("activeStudentCount").innerText = activeStudentCount;
           document.getElementById("pendingApplications").innerText = newApplications;

        } catch (error) {
          alert("something went wrong", error);
        }
      }
