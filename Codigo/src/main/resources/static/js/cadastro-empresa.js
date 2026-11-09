const API_URL = 'http://localhost:8080/api';

document.getElementById('form-empresa').addEventListener('submit', async (e) => {
    e.preventDefault();
    limparErros();
    
    const payload = {
        nome: document.getElementById('emp-nome').value,
        cnpj: document.getElementById('emp-cnpj').value,
        telefone: document.getElementById('emp-telefone').value,
        email: document.getElementById('emp-email').value,
        login: document.getElementById('emp-login').value,
        senha: document.getElementById('emp-senha').value
    };

    try {
        const res = await fetch(`${API_URL}/empresas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const data = await res.json().catch(() => null);

        // Se cadastrou com sucesso (200 ou 201), redireciona pro login com mensagem
        if (res.ok || res.status === 201) {
            window.location.href = 'index.html?cadastro=ok';
        } 
        // Tratamento de erros de validação (campos vazios, CNPJ inválido, etc)
        else if (res.status === 400 && data && data.fieldErrors) {
            for (const [campo, msg] of Object.entries(data.fieldErrors)) {
                const span = document.getElementById(`erro-${campo}`);
                if (span) span.innerText = msg;
            }
            showToast('Verifique os campos inválidos', 'error');
        } 
        // Tratamento de conflito (CNPJ, Email ou Login já existem)
        else if (res.status === 409 && data) {
            showToast(data.message, 'error'); 
        } 
        else {
            showToast(data?.message || 'Erro inesperado ao cadastrar.', 'error');
        }
    } catch (error) {
        showToast('Erro de conexão. Verifique se o servidor está rodando.', 'error');
    }
});

function limparErros() {
    document.querySelectorAll('.error-msg').forEach(el => el.innerText = '');
}

function showToast(mensagem, tipo = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = mensagem;
    toast.className = `toast ${tipo}`;
    setTimeout(() => toast.classList.add('hidden'), 4000);
}