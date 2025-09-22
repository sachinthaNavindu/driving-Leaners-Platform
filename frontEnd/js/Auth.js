
export function getAccessToken() {
  return localStorage.getItem("token");
}

export function getRefreshToken() {
  return localStorage.getItem("refreshToken");
}

export async function refreshAccessToken() {
  const refreshToken = getRefreshToken();
  if (!refreshToken) {
    redirectToLogin();
    return null;
  }

  try {
    const response = await fetch("http://localhost:8080/auth/refresh", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ refreshToken }),
    });

    if (!response.ok) {
      redirectToLogin();
      return null;
    }

    const data = await response.json();
    localStorage.setItem("token", data.data.accessToken);
    return data.data.accessToken;
  } catch (error) {
    console.error("Error refreshing token:", error);
    redirectToLogin();
    return null;
  }
}

export function redirectToLogin() {
  localStorage.clear();
  window.location.href = "/pages/employeelogin.html";
}

export async function fetchWithAuth(url, options = {}) {
  let token = getAccessToken();

  if (!options.headers) options.headers = {};
  options.headers["Authorization"] = `Bearer ${token}`;

  let response = await fetch(url, options);

  if (response.status === 401) {
    token = await refreshAccessToken();
    if (!token) return null;

    options.headers["Authorization"] = `Bearer ${token}`;
    response = await fetch(url, options);
  }

  return response;
}

export function checkToken() {
  const token = getAccessToken();
  if (!token || token === "undefined" || token === "null") {
    redirectToLogin();
    return;
  }

  const userName = localStorage.getItem("username");
  document.getElementById("menubar-user").innerText = userName;
}
