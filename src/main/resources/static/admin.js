document.addEventListener('DOMContentLoaded', () => {
  const loggedIn = localStorage.getItem('adminLoggedIn');
  toggleDashboard(loggedIn === 'true');

  document.getElementById('adminLogin')?.addEventListener('submit', async function (e) {
    e.preventDefault();
    const username = document.getElementById('adminUsername').value;
    const password = document.getElementById('adminPassword').value;

    const response = await fetch('/api/admin/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });

    if (response.ok) {
      localStorage.setItem('adminLoggedIn', 'true');
      toggleDashboard(true);
    } else {
      document.getElementById('loginError').classList.remove('hidden');
    }
  });
});

function toggleDashboard(show) {
  document.getElementById('loginForm').classList.toggle('hidden', show);
  document.getElementById('adminDashboard').classList.toggle('hidden', !show);
  if (show) loadUsers();
}

function logout() {
  localStorage.removeItem('adminLoggedIn');
  toggleDashboard(false);
}

// CRUD Actions
async function loadUsers() {
  const res = await fetch('/api/admin/users');
  const users = await res.json();

  const container = document.getElementById('adminResult');
  container.innerHTML = '';

  users.forEach(user => {
    const div = document.createElement('div');
    div.className = 'result-box';
    div.innerHTML = `
      <strong>${user.userId}</strong><br>
      Strikes: ${user.strikeCount} — Status: ${user.banned ? '❌ BANNED' : '✅ Active'}<br>
      <button onclick="banUser('${user.userId}')">Ban</button>
      <button onclick="unbanUser('${user.userId}')">Unban</button>
      <button onclick="resetStrikes('${user.userId}')">Reset</button>
      <button onclick="deleteUser('${user.userId}')" class="danger">Delete</button>
    `;
    container.appendChild(div);
  });
}

async function banUser(id) {
  await fetch(`/api/admin/ban/${id}`, { method: 'POST' }); loadUsers();
}
async function unbanUser(id) {
  await fetch(`/api/admin/unban/${id}`, { method: 'POST' }); loadUsers();
}
async function resetStrikes(id) {
  await fetch(`/api/admin/user/${id}/reset`, { method: 'POST' }); loadUsers();
}
async function deleteUser(id) {
  await fetch(`/api/admin/user/${id}`, { method: 'DELETE' }); loadUsers();
}

async function loadUsers() {
  const res = await fetch('/api/admin/users');
  const users = await res.json();
  const container = document.getElementById('adminResult');
  container.innerHTML = ''; // clear previous

  users.forEach(user => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${user.userId}</td>
      <td>${user.strikeCount}</td>
      <td>${user.banned ? '<span style="color:red;">Banned</span>' : 'Active'}</td>
      <td>
        <button onclick="banUser('${user.userId}')" class="action-btn">Ban</button>
        <button onclick="unbanUser('${user.userId}')" class="action-btn">Unban</button>
        <button onclick="resetStrikes('${user.userId}')" class="action-btn">Reset</button>
        <button onclick="deleteUser('${user.userId}')" class="action-btn danger">Delete</button>
      </td>
    `;
    container.appendChild(tr);
  });
}