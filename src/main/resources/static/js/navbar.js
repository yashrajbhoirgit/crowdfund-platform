/**
 * navbar.js — Shared navigation bar for CrowdFund Platform
 */
(function () {
    const STYLES = `
        #shared-navbar {
            position: sticky;
            top: 0;
            left: 0;
            right: 0;
            width: 100%;
            z-index: 9999;
            background: rgba(15, 23, 42, 0.95);
            backdrop-filter: blur(16px);
            -webkit-backdrop-filter: blur(16px);
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
            padding: 0 5%;
            height: 68px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            box-sizing: border-box;
            font-family: 'Outfit', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        }

        #shared-navbar * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        #shared-navbar .nb-brand {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            text-decoration: none;
            font-size: 1.35rem;
            font-weight: 800;
            letter-spacing: -0.02em;
            background: linear-gradient(135deg, #60a5fa, #c084fc);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            cursor: pointer;
        }

        #shared-navbar .nb-brand-dot {
            width: 12px;
            height: 12px;
            background: #60a5fa;
            border-radius: 50%;
            display: inline-block;
            box-shadow: 0 0 10px #60a5fa;
        }

        #shared-navbar .nb-nav-list {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            list-style: none;
        }

        #shared-navbar .nb-link {
            padding: 0.5rem 1rem;
            border-radius: 0.5rem;
            text-decoration: none;
            font-size: 0.92rem;
            font-weight: 500;
            color: #94a3b8;
            transition: all 0.2s ease;
            cursor: pointer;
        }

        #shared-navbar .nb-link:hover,
        #shared-navbar .nb-link.active {
            color: #f8fafc;
            background: rgba(255, 255, 255, 0.08);
        }

        #shared-navbar .nb-auth-group {
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        #shared-navbar .nb-btn {
            padding: 0.5rem 1.15rem;
            border-radius: 0.6rem;
            text-decoration: none;
            font-size: 0.9rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
            display: inline-flex;
            align-items: center;
            gap: 0.4rem;
            border: none;
            font-family: inherit;
        }

        #shared-navbar .nb-btn-outline {
            background: transparent;
            border: 1px solid rgba(255, 255, 255, 0.15);
            color: #f8fafc;
        }

        #shared-navbar .nb-btn-outline:hover {
            border-color: #3b82f6;
            background: rgba(59, 130, 246, 0.1);
            color: #60a5fa;
        }

        #shared-navbar .nb-btn-primary {
            background: linear-gradient(135deg, #3b82f6, #6366f1);
            color: #ffffff;
            box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
        }

        #shared-navbar .nb-btn-primary:hover {
            box-shadow: 0 6px 18px rgba(59, 130, 246, 0.45);
            transform: translateY(-1px);
        }

        #shared-navbar .nb-btn-danger {
            background: rgba(239, 68, 68, 0.1);
            border: 1px solid rgba(239, 68, 68, 0.3);
            color: #f87171;
        }

        #shared-navbar .nb-btn-danger:hover {
            background: rgba(239, 68, 68, 0.2);
            color: #ffffff;
        }

        #shared-navbar .nb-user-chip {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.35rem 0.85rem 0.35rem 0.4rem;
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
            border-radius: 9999px;
            text-decoration: none;
            color: #f8fafc;
            font-size: 0.9rem;
            font-weight: 600;
            transition: all 0.2s;
        }

        #shared-navbar .nb-user-chip:hover {
            background: rgba(255, 255, 255, 0.12);
        }

        #shared-navbar .nb-avatar {
            width: 28px;
            height: 28px;
            border-radius: 50%;
            background: linear-gradient(135deg, #3b82f6, #8b5cf6);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 0.8rem;
            font-weight: 700;
            color: #ffffff;
        }

        @media (max-width: 860px) {
            #shared-navbar .nb-nav-list {
                display: none;
            }
        }
    `;

    function buildNavbar() {
        if (document.getElementById('shared-navbar')) return;

        const style = document.createElement('style');
        style.textContent = STYLES;
        document.head.appendChild(style);

        const nav = document.createElement('nav');
        nav.id = 'shared-navbar';

        const rawUser = localStorage.getItem('user');
        const token = localStorage.getItem('token');
        let user = null;
        if (token && rawUser) {
            try {
                user = JSON.parse(rawUser);
            } catch (e) {
                user = null;
            }
        }

        const currentPage = window.location.pathname.split('/').pop() || 'index.html';

        let authButtonsHtml = '';
        if (user) {
            const initial = (user.name || user.email || 'U')[0].toUpperCase();
            const displayName = user.name ? user.name.split(' ')[0] : 'Profile';
            const isAdmin = user.role === 'ADMIN';

            authButtonsHtml = `
                ${isAdmin ? `<a href="admin-dashboard.html" class="nb-btn nb-btn-outline">Admin Panel</a>` : ''}
                <a href="my-donations.html" class="nb-btn nb-btn-outline">My Donations</a>
                <a href="profile.html" class="nb-user-chip">
                    <div class="nb-avatar">${initial}</div>
                    <span>${displayName}</span>
                </a>
                <button onclick="window.sharedLogout()" class="nb-btn nb-btn-danger">Logout</button>
            `;
        } else {
            authButtonsHtml = `
                <a href="login.html" class="nb-btn nb-btn-outline">Sign In</a>
                <a href="register.html" class="nb-btn nb-btn-primary">Get Started</a>
            `;
        }

        nav.innerHTML = `
            <a href="index.html" class="nb-brand" title="Home">
                <span class="nb-brand-dot"></span>
                <span>CrowdHope</span>
            </a>

            <ul class="nb-nav-list">
                <li><a href="index.html" class="nb-link ${currentPage === 'index.html' || currentPage === '' ? 'active' : ''}">Home</a></li>
                <li><a href="campaigns.html" class="nb-link ${currentPage === 'campaigns.html' ? 'active' : ''}">Explore</a></li>
                <li><a href="create-campaign.html" class="nb-link ${currentPage === 'create-campaign.html' ? 'active' : ''}">Start Campaign</a></li>
            </ul>

            <div class="nb-auth-group">
                ${authButtonsHtml}
            </div>
        `;

        if (document.body) {
            document.body.insertBefore(nav, document.body.firstChild);
        }
    }

    window.sharedLogout = function () {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = 'index.html';
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', buildNavbar);
    } else {
        buildNavbar();
    }
})();
