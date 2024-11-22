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

async function handleChangePassword(event) {
    event.preventDefault();
    const username = document.getElementById('change-username').value;
    const oldPassword = document.getElementById('old-password').value;
    const newPassword = document.getElementById('new-password').value;

    try {
        const response = await fetch(`${API_AUTH_URL}/change-password`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, oldPassword, newPassword })
        });

        if (response.ok) {
            alert('Password changed successfully');
            window.location.href = 'login.html';
        } else {
            alert('Change password failed');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Logout logic
function handleLogout() {
    // Xóa token và role khỏi LocalStorage
    localStorage.removeItem('token');
    localStorage.removeItem('role');

    // Điều hướng về trang đăng nhập
    window.location.href = '/login.html';
}

// Gắn sự kiện cho nút Logout
document.addEventListener('DOMContentLoaded', () => {
    const logoutButton = document.getElementById('logout-button');
    if (logoutButton) {
        logoutButton.addEventListener('click', handleLogout);
    }
});


const API_URL_ADMIN = '/admin/books';
let editingBookId = null;

// Fetch and display books
async function fetchBooks() {
    const response = await apiRequest(API_URL_ADMIN, 'GET');
    const bookList = document.getElementById('book-list');
    bookList.innerHTML = '';

    response.forEach(book => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${book.id}</td>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.publishedYear || ''}</td>
            <td>${book.price ? `$${book.price.toFixed(2)}` : ''}</td>
            <td>${book.quantity}</td>
            <td>
                <button onclick="editBook(${book.id}, '${book.title}', '${book.author}', ${book.publishedYear}, ${book.price}, ${book.quantity})">Edit</button>
                <button onclick="deleteBook(${book.id})">Delete</button>
            </td>
        `;
        bookList.appendChild(row);
    });
}

// Add or update book
async function addOrUpdateBook(event) {
    event.preventDefault();
    const book = {
        title: document.getElementById('book-title').value,
        author: document.getElementById('book-author').value,
        publishedYear: parseInt(document.getElementById('book-year').value, 10) || null,
        price: parseFloat(document.getElementById('book-price').value) || null,
        quantity: parseInt(document.getElementById('book-quantity').value, 10),
    };

    if (editingBookId) {
        await apiRequest(`${API_URL_ADMIN}/${editingBookId}`, 'PUT', book);
        editingBookId = null;
    } else {
        await apiRequest(API_URL_ADMIN, 'POST', book);
    }

    document.getElementById('book-form').reset();
    fetchBooks();
}

// Delete book
async function deleteBook(id) {
    await apiRequest(`${API_URL_ADMIN}/${id}`, 'DELETE');
    fetchBooks();
}

// Edit book
function editBook(id, title, author, year, price, quantity) {
    editingBookId = id;
    document.getElementById('book-title').value = title;
    document.getElementById('book-author').value = author;
    document.getElementById('book-year').value = year || '';
    document.getElementById('book-price').value = price || '';
    document.getElementById('book-quantity').value = quantity;
}

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



