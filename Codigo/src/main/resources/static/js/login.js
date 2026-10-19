const API_URL = 'http://localhost:8080/api';

// Painel aberto para cada tipo de usuário após o login
const PAINEIS = {
    ALUNO: 'aluno.html',
    PROFESSOR: 'professor.html',
    EMPRESA: 'admin.html'
};

document.getElementById('form-login').addEventListener('submit', async (e) => {
    e.preventDefault();
    limparErros();

    const payload = {
        login: document.getElementById('login').value.trim(),
        senha: document.getElementById('senha').value
    };

    try {
        const res = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const data = await res.json().catch(() => null);

        if (res.ok) {
            // Guarda o usuário logado para os painéis saberem quem está acessando
            localStorage.setItem('usuarioLogado', JSON.stringify(data));
            window.location.href = PAINEIS[data.tipo] || 'index.html';
        } else if (res.status === 400 && data && data.fieldErrors) {
            for (const [campo, msg] of Object.entries(data.fieldErrors)) {
                const span = document.getElementById(`erro-${campo}`);
                if (span) span.innerText = msg;
            }
        } else {
            showToast(data?.message || 'Erro inesperado ao entrar.', 'error');
        }
    } catch (error) {
        showToast('Erro de conexão. Verifique se o servidor está rodando.', 'error');
    }
});

// Mostra a mensagem deixada pela tela de cadastro, se houver
const mensagemCadastro = new URLSearchParams(window.location.search).get('cadastro');
if (mensagemCadastro === 'ok') {
    showToast('Cadastro realizado com sucesso! Faça login para continuar.', 'success');
}

function limparErros() {
    document.querySelectorAll('.error-msg').forEach(el => el.innerText = '');
}

function showToast(mensagem, tipo = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = mensagem;
    toast.className = `toast ${tipo}`;
    setTimeout(() => toast.classList.add('hidden'), 4000);
}
