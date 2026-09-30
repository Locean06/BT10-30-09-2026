const loginForm = document.getElementById('loginForm');

if (loginForm) {
    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const message = document.getElementById('message');

        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email, password})
        });

        const data = await response.json();
        if (!response.ok) {
            message.textContent = data.error || 'Đăng nhập thất bại';
            return;
        }

        localStorage.setItem('token', data.token);
        window.location.href = '/user/profile';
    });
}

const profile = document.getElementById('profile');
if (profile) {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login';
    } else {
        fetch('/users/me', {
            headers: {Authorization: 'Bearer ' + token}
        })
            .then(async response => {
                const data = await response.json();
                if (!response.ok) throw new Error(data.error || 'Không thể đọc profile');
                return data;
            })
            .then(user => {
                profile.innerHTML = `
                    <p><b>ID:</b> ${user.id}</p>
                    <p><b>Họ tên:</b> ${user.fullName}</p>
                    <p><b>Email:</b> ${user.email}</p>
                    <p><b>Ảnh:</b> ${user.images ?? ''}</p>
                `;
            })
            .catch(error => {
                localStorage.removeItem('token');
                profile.textContent = error.message;
            });
    }
}

const logoutBtn = document.getElementById('logoutBtn');
if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
        localStorage.removeItem('token');
        window.location.href = '/login';
    });
}
