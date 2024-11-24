const API_AUTH_URL = '/api/auth';

async function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    try {
        const response = await fetch(`${API_AUTH_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();

            // Lưu token và role vào LocalStorage
            localStorage.setItem('token', data.token);
            localStorage.setItem('role', data.role);

            // Điều hướng dựa trên role
            if (data.role === 'ADMIN') {
                window.location.href = '/admin.html';
            } else if (data.role === 'USER') {

                window.location.href = '/user.html';
            } else {
                alert('Role is not recognized');
            }
        } else {
            alert('Invalid credentials');
        }
    } catch (error) {
        console.error('Error during login:', error);
    }
}


async function handleRegister(event) {
    event.preventDefault();
    const username = document.getElementById('register-username').value;
    const password = document.getElementById('register-password').value;

    try {
        const response = await fetch(`${API_AUTH_URL}/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            alert('Registration successful');
            window.location.href = 'login.html';
        } else {
            alert('Registration failed');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

const API_PERSON_URL = '/api/auth';

async function changePassword(event) {
    event.preventDefault();
    const API_URL = '/api/user/change-password';
    const oldPassword = document.getElementById('old-password').value;
    const newPassword = document.getElementById('new-password').value;

    const response = await apiRequest(API_URL, 'POST', { oldPassword, newPassword });
    alert('Password changed successfully!');
}

async function updatePersonalInfo() {
    const API_URL = '/user/personal-info';
    const fullName = document.getElementById('full-name').value;
    const email = document.getElementById('email').value;
    const address = document.getElementById('address').value;

    const response = await apiRequest(API_URL, 'PUT', { fullName, email, address });
    alert('Personal info updated successfully!');
}

// Gắn sự kiện cho nút Logout
document.addEventListener('DOMContentLoaded', () => {
    const logoutButton = document.getElementById('logout-button');
    if (logoutButton) {
        logoutButton.addEventListener('click', handleLogout);
    }
});

// Logout logic
function handleLogout() {
    // Xóa token và role khỏi LocalStorage
    localStorage.removeItem('token');
    localStorage.removeItem('role');

    // Điều hướng về trang đăng nhập
    window.location.href = '/login.html';
}


const API_URL_ADMIN = '/admin/books';
let editingBookId = null;

// API Helper function
async function apiRequest(url, method = 'GET', body = null) {
    const token = localStorage.getItem('token');
    if (!token) {
        alert('You must be logged in!');
        window.location.href = '/';
        return;
    }

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };

    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);

    const response = await fetch(url, options);
    if (!response.ok) {
        if (response.status === 401) {
            alert('Unauthorized! Please login again.');
            localStorage.removeItem('token');
            window.location.href = '/';
        }
        throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    return response.json();
}

// Show the selected tab and reload its data
function showTab(tabId) {
    // Ẩn tất cả các nội dung tab
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => {
        if (tab.id === tabId) {
            tab.style.display = 'block'; // Hiển thị tab được chọn
        } else {
            tab.style.display = 'none'; // Ẩn các tab khác
        }
    });

    // Loại bỏ lớp "active" trên tất cả các nút tab
    const tabButtons = document.querySelectorAll('.tab-button');
    tabButtons.forEach(button => {
        button.classList.remove('active');
    });

    // Thêm lớp "active" vào nút tab hiện tại
    const currentButton = document.querySelector(`.tab-button[onclick="showTab('${tabId}')"]`);
    if (currentButton) {
        currentButton.classList.add('active');
    }

    // Reload dữ liệu cho tab được chọn
    reloadCurrentTab(tabId);
}

// Reload current tab's data
function reloadCurrentTab(tabId) {
    if (tabId === 'manage-books') fetchBooks();
    if (tabId === 'manage-authors') fetchAuthors();
    if (tabId === 'manage-categories') fetchCategories();
    if (tabId === 'manage-borrowers') fetchBorrowers();
}

// Open modal
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'flex';

    // Load dropdown options if it's the Book modal
    if (modalId === 'book-modal') {
        fetchDropdownOptions();
    }
}

// Close modal
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';

    // Reset form when modal is closed
    const form = document.querySelector(`#${modalId} form`);
    if (form) form.reset();
}
