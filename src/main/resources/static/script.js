// --- Constants (Shared) ---
const API_BASE = "/api/v1/weather";
const LOGIN_ENDPOINT = '/api/v1/auth';
const SIGNUP_ENDPOINT = '/api/v1/user';

// --- Pagination State ---
let currentPage = 0;
const PAGE_SIZE = 12;

// --- Home Page Logic ---
async function fetchAndRenderWeather(page = 0) {
    const grid = document.getElementById('weatherGrid');
    if (!grid) return;

    grid.innerHTML = '<p style="text-align: center; color: #BB86FC;">Loading weather data...</p>';

    const data = await fetch(`${API_BASE}?page=${page}&size=${PAGE_SIZE}`)
        .then(r => r.json());

    grid.innerHTML = '';

    data.content.forEach(cityData => {
        const card = document.createElement('div');
        card.className = 'card';
        card.onclick = () => {
            window.location.href = `city.html?city=${encodeURIComponent(cityData.cityName)}`;
        };

        const temp = cityData.tempC !== 'N/A' ? `${Math.round(cityData.tempC)}°C` : 'N/A';
        const iconHtml = cityData.icon
            ? `<img src="${cityData.icon}" alt="Weather Icon" style="width: 100%; height: 100%; object-fit: cover; border-radius: 5px;"/>`
            : `<div class="icon-placeholder">☀️</div>`;

        card.innerHTML = `
            <div class="condition-icon">
                <h2>${cityData.cityName}</h2>
                <div style="width: 50px; height: 50px;">${iconHtml}</div>
            </div>
            <div class="temp">${temp}</div>
            <div class="condition">Condition: ${cityData.condition}</div>
        `;
        grid.appendChild(card);
    });

    document.getElementById('prev').disabled = page === 0;
    document.getElementById('next').disabled = page === data.totalPages - 1;
    document.getElementById('pageInfo').textContent = `Page ${page + 1} of ${data.totalPages}`;
    currentPage = page;
}

// --- City Page Logic ---
function getCityNameFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('city');
}

async function fetchAndRenderCityWeather() {
    const container = document.getElementById('weatherContainer');
    if (!container) return;

    const cityName = getCityNameFromUrl();
    const title = document.getElementById('pageTitle');
    const h1 = document.getElementById('cityNameDisplay');

    if (!cityName) {
        h1.textContent = 'Error: No City Specified';
        container.innerHTML = '<p>Please return to the dashboard and select a city.</p>';
        return;
    }

    h1.textContent = cityName;
    title.textContent = `${cityName} Detailed Weather`;

    try {
        const response = await fetch(`${API_BASE}/city?city=${encodeURIComponent(cityName)}`);

        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        const data = await response.json();
        const fmt = (num) => typeof num === 'number' ? num.toFixed(num % 1 === 0 ? 0 : 1) : 'N/A';

        container.innerHTML = `
            <h1 id="cityNameDisplay">${data.cityName}</h1>
            <p class="country">${data.country}</p>
            <div class="temp-main">
                <div class="temp">${fmt(data.tempC)}°C</div>
                <div class="icon-container">
                    <img src="${data.icon}" alt="Weather Icon"/>
                </div>
            </div>
            <p class="condition">${data.condition}</p>
            <div class="details-grid">
                <div class="detail-item">
                    <span class="detail-label">Fahrenheit</span>
                    ${fmt(data.tempF)}°F
                </div>
                <div class="detail-item">
                    <span class="detail-label">Feels Like</span>
                    ${fmt(data.feelsLike)}°C
                </div>
                <div class="detail-item">
                    <span class="detail-label">Humidity</span>
                    ${data.humidity}%
                </div>
                <div class="detail-item">
                    <span class="detail-label">Wind Speed</span>
                    ${fmt(data.windSpeed)} km/h
                </div>
                <div class="detail-item">
                    <span class="detail-label">Wind Direction</span>
                    ${data.windDirection}
                </div>
                <div class="detail-item">
                    <span class="detail-label">Last Updated</span>
                    ${new Date(data.lastUpdated).toLocaleString()}
                </div>
            </div>
        `;

    } catch (error) {
        console.error("Failed to fetch weather data:", error);
        container.innerHTML = `
            <h1>${cityName}</h1>
            <p class="country">Error</p>
            <p style="color: #E94560;">Could not load detailed weather data. Please check the backend service.</p>
        `;
    }
}

// --- Login Page Logic ---
function setupLoginForm() {
    const loginForm = document.getElementById('loginForm');
    if (!loginForm) return;

    loginForm.addEventListener('submit', async function(event) {
        event.preventDefault();
        const form = event.target;
        const messageDiv = document.getElementById('message');
        messageDiv.textContent = 'Logging in...';
        messageDiv.style.color = '#BB86FC';

        const credentials = {
            name: form.name.value,
            password: form.password.value
        };

        try {
            const response = await fetch(LOGIN_ENDPOINT, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials)
            });

            if (response.ok) {
                const responseData = await response.json();
                messageDiv.textContent = 'Login Successful! Redirecting to dashboard...';
                messageDiv.style.color = '#03DAC6';
                window.location.href = 'home.html';
            } else {
                const errorData = await response.json().catch(() => ({ message: 'Unknown error' }));
                messageDiv.textContent = `Login failed: ${errorData.message || response.statusText || 'Invalid credentials'}`;
                messageDiv.style.color = '#CF6679';
            }
        } catch (error) {
            console.error('Network or parsing error during login:', error);
            messageDiv.textContent = 'An unexpected error occurred. Check server connection.';
            messageDiv.style.color = '#CF6679';
        }
    });
}

// --- Sign Up Page Logic ---
function setupSignupForm() {
    const signupForm = document.getElementById('signupForm');
    if (!signupForm) return;

    signupForm.addEventListener('submit', async function(event) {
        event.preventDefault();
        const form = event.target;
        const messageDiv = document.getElementById('message');
        messageDiv.textContent = 'Submitting...';
        messageDiv.style.color = '#BB86FC';

        const userData = {
            name: form.name.value,
            email: form.email.value,
            password: form.password.value
        };

        try {
            const response = await fetch(SIGNUP_ENDPOINT, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData)
            });

            if (response.ok) {
                messageDiv.textContent = 'Registration Successful! Redirecting to login...';
                messageDiv.style.color = '#03DAC6';
                setTimeout(() => { window.location.href = 'login.html'; }, 2000);
            } else if (response.status === 409) {
                messageDiv.textContent = 'Registration failed: Username or Email already in use.';
                messageDiv.style.color = '#CF6679';
            } else {
                const errorData = await response.json().catch(() => ({ message: response.statusText }));
                messageDiv.textContent = `Registration failed: ${errorData.message || response.statusText}`;
                messageDiv.style.color = '#CF6679';
            }
        } catch (error) {
            messageDiv.textContent = 'An error occurred during registration. Check your connection.';
            messageDiv.style.color = '#CF6679';
            console.error('Fetch error:', error);
        }
    });
}

// --- Initialization ---
document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname.split('/').pop();

    if (path === 'home.html' || path === '') {
        fetchAndRenderWeather();
        document.getElementById('prev').onclick = () => fetchAndRenderWeather(currentPage - 1);
        document.getElementById('next').onclick = () => fetchAndRenderWeather(currentPage + 1);
    } else if (path === 'city.html') {
        fetchAndRenderCityWeather();
    } else if (path === 'login.html') {
        setupLoginForm();
    } else if (path === 'signup.html') {
        setupSignupForm();
    }
});