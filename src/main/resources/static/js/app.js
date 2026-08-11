/**
 * app.js
 * Shared JavaScript utilities for Crowdfund Platform
 */

const API_BASE_URL = '/api';

/**
 * JWT Token Management
 */
const TokenService = {
    getToken() {
        return localStorage.getItem('jwt_token');
    },
    
    setToken(token) {
        localStorage.setItem('jwt_token', token);
    },
    
    removeToken() {
        localStorage.removeItem('jwt_token');
    },

    decodeToken() {
        const token = this.getToken();
        if (!token) return null;
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));
            return JSON.parse(jsonPayload);
        } catch (error) {
            console.error("Error decoding token", error);
            return null;
        }
    }
};

/**
 * Authentication Checkers
 */
function isLoggedIn() {
    const token = TokenService.getToken();
    if (!token) return false;
    
    const decoded = TokenService.decodeToken();
    if (!decoded) return false;
    
    // Check expiration
    const currentTime = Date.now() / 1000;
    if (decoded.exp < currentTime) {
        logout();
        return false;
    }
    return true;
}

function getCurrentUser() {
    if (!isLoggedIn()) return null;
    return TokenService.decodeToken();
}

function requireAuth() {
    if (!isLoggedIn()) {
        window.location.href = '/login.html';
    }
}

function logout() {
    TokenService.removeToken();
    window.location.href = '/';
}

/**
 * API Fetch Wrapper
 */
async function fetchWithAuth(url, options = {}) {
    const token = TokenService.getToken();
    
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const config = {
        ...options,
        headers
    };

    try {
        const response = await fetch(`${API_BASE_URL}${url}`, config);
        
        if (response.status === 401) {
            // Unauthorized, token might be expired
            logout();
            throw new Error('Session expired. Please login again.');
        }
        
        return response;
    } catch (error) {
        console.error('Fetch Error:', error);
        throw error;
    }
}

/**
 * UI Utilities
 */

// Initialize Toast Container
document.addEventListener('DOMContentLoaded', () => {
    if (!document.getElementById('toast-container')) {
        const container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
});

function showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    let icon = '💬';
    if (type === 'success') icon = '✅';
    if (type === 'error') icon = '❌';
    if (type === 'warning') icon = '⚠️';

    toast.innerHTML = `<span>${icon}</span> <span>${message}</span>`;
    
    container.appendChild(toast);

    // Trigger reflow for animation
    void toast.offsetWidth;
    toast.classList.add('show');

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            container.removeChild(toast);
        }, 300); // Wait for transition to finish
    }, 3000);
}

/**
 * Format Helpers
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        maximumFractionDigits: 0
    }).format(amount);
}

function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}

function calculateProgress(raised, goal) {
    if (!goal || goal === 0) return 0;
    const percentage = (raised / goal) * 100;
    return Math.min(Math.round(percentage), 100);
}

function updateProgress(elementId, raised, goal) {
    const element = document.getElementById(elementId);
    if (element) {
        const percentage = calculateProgress(raised, goal);
        element.style.width = `${percentage}%`;
    }
}

// Update UI based on auth state (useful for navbars)
function updateAuthUI() {
    const authLinks = document.getElementById('auth-links');
    const userLinks = document.getElementById('user-links');
    
    if (isLoggedIn()) {
        if (authLinks) authLinks.style.display = 'none';
        if (userLinks) userLinks.style.display = 'flex';
        
        const user = getCurrentUser();
        const userNameElem = document.getElementById('nav-username');
        if (userNameElem && user) {
            userNameElem.textContent = user.sub; // Or however username is stored in JWT
        }
    } else {
        if (authLinks) authLinks.style.display = 'flex';
        if (userLinks) userLinks.style.display = 'none';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    updateAuthUI();
});
