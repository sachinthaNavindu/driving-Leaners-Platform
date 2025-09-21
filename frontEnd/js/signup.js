        document.getElementById("studentForm").addEventListener("submit", async function(e) {
            e.preventDefault();
            
            const submitBtn = document.getElementById('registerBtn');
            const originalText = submitBtn.innerHTML;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Registering...';
            submitBtn.disabled = true;
            
            const nic = document.getElementById("nic").value;
            const username = document.getElementById("username").value;
            const gmail = document.getElementById("gmail").value;
            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirmPassword").value;
            
            if (password !== confirmPassword) {
                showAlert('Passwords do not match!', 'error');
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
                return;
            }
            
            if (password.length < 8) {
                showAlert('Password must be at least 8 characters long', 'error');
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
                return;
            }
            
            if (!document.getElementById('terms').checked) {
                showAlert('Please accept the terms and conditions', 'error');
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
                return;
            }

            const data = {
                nic: nic,
                username: username,
                gmail: gmail,
                password: password
            };

            try {
                const response = await fetch("http://localhost:8080/auth/student/signup", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const result = await response.json();
                    showAlert('Registration successful! Redirecting to login...', 'success');
                    
                    document.getElementById('studentForm').reset();
                    document.getElementById('passwordStrengthBar').style.width = '0%';
                    
                    setTimeout(() => {
                        window.location.href = 'signin.html';
                    }, 2000);
                } else {
                    const error = await response.text();
                    showAlert('Error: ' + error, 'error');
                }
            } catch (err) {
                console.error("Request failed:", err);
                showAlert('Failed to connect to server. Please try again later.', 'error');
            } finally {
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            }
        });
        
        document.getElementById('password').addEventListener('input', function() {
            const password = this.value;
            const strengthBar = document.getElementById('passwordStrengthBar');
            let strength = 0;
            
            if (password.length >= 8) strength += 25;
            if (password.match(/[a-z]+/)) strength += 25;
            if (password.match(/[A-Z]+/)) strength += 25;
            if (password.match(/[0-9]+/) || password.match(/[^a-zA-Z0-9]+/)) strength += 25;
            
            strengthBar.style.width = strength + '%';
            
            if (strength < 50) {
                strengthBar.style.background = '#e74c3c';
            } else if (strength < 75) {
                strengthBar.style.background = '#f39c12';
            } else {
                strengthBar.style.background = '#2ecc71';
            }
        });
        
        function showAlert(message, type) {
            const alertBox = document.getElementById('alertBox');
            alertBox.textContent = message;
            alertBox.className = `alert alert-${type}`;
            alertBox.style.display = 'block';
            
            setTimeout(() => {
                alertBox.style.display = 'none';
            }, 5000);
        }
