        document.addEventListener('DOMContentLoaded', function() {
            let startTime = null;
            let endTime = null;
            let intervalId = null;
            
            let presentCount = 0;
            let absentCount = 0;
            let pendingCount = 8;
            
            function updateCounters() {
                document.getElementById('present-count').textContent = presentCount;
                document.getElementById('absent-count').textContent = absentCount;
                document.getElementById('pending-count').textContent = pendingCount;
            }
            
            const presentButtons = document.querySelectorAll('.present-btn');
            const absentButtons = document.querySelectorAll('.absent-btn');
            
            presentButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const row = this.closest('tr');
                    const statusBadge = row.querySelector('.status-badge');
                    
                    if (statusBadge.classList.contains('status-pending')) {
                        statusBadge.className = 'status-badge status-present';
                        statusBadge.textContent = 'Present';
                        
                        pendingCount--;
                        presentCount++;
                        updateCounters();
                    }
                });
            });
            
            absentButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const row = this.closest('tr');
                    const statusBadge = row.querySelector('.status-badge');
                    
                    if (statusBadge.classList.contains('status-pending')) {
                        statusBadge.className = 'status-badge status-absent';
                        statusBadge.textContent = 'Absent';
                        
                        pendingCount--;
                        absentCount++;
                        updateCounters();
                    }
                });
            });
            
            const startBtn = document.querySelector('.start-btn');
            const endBtn = document.querySelector('.end-btn');
            
            startBtn.addEventListener('click', function() {
                if (!startTime) {
                    startTime = new Date();
                    document.getElementById('start-time').textContent = startTime.toLocaleTimeString();
                    startBtn.textContent = 'Session Started';
                    startBtn.disabled = true;
                    endBtn.disabled = false;
                    
                    intervalId = setInterval(updateDuration, 1000);
                }
            });
            
            endBtn.addEventListener('click', function() {
                if (startTime && !endTime) {
                    endTime = new Date();
                    document.getElementById('end-time').textContent = endTime.toLocaleTimeString();
                    endBtn.textContent = 'Session Ended';
                    endBtn.disabled = true;
                    
                    clearInterval(intervalId);
                    updateDuration();
                }
            });
            
            function updateDuration() {
                if (startTime) {
                    const now = endTime || new Date();
                    const diff = now - startTime;
                    
                    const hours = Math.floor(diff / 3600000);
                    const minutes = Math.floor((diff % 3600000) / 60000);
                    const seconds = Math.floor((diff % 60000) / 1000);
                    
                    document.getElementById('duration').textContent = 
                        `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
                }
            }
        });
