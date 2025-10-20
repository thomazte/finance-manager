document.getElementById('formLogin').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senha').value;
    const erro = document.getElementById('erro');
    try {
        await API.post('api/auth/login', { email, senha });
        location.href = 'dashboard.html';
    } catch {
        erro.hidden = false;
    }
});