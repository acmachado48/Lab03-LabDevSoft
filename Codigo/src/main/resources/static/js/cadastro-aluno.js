const API_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    carregarInstituicoes();
});

// Busca as instituições no back-end para preencher o <select>
async function carregarInstituicoes() {
    const select = document.getElementById('aluno-instituicao');
    try {
        const res = await fetch(`${API_URL}/instituicoes`);
        if (res.ok) {
            const instituicoes = await res.json();
            select.innerHTML = '<option value="">Selecione sua instituição...</option>';
            instituicoes.forEach(inst => {
                select.innerHTML += `<option value="${inst.id}">${inst.nome}</option>`;
            });
        } else {
            select.innerHTML = '<option value="">Erro ao carregar instituições</option>';
        }
    } catch (e) {
        select.innerHTML = '<option value="">Erro de conexão. A API está rodando?</option>';
    }
}

document.getElementById('form-aluno').addEventListener('submit', async (e) => {
    e.preventDefault();
    limparErros();
    
    // Constrói o payload com base no modelo Aluno (e herança de Usuario)
    const payload = {
        nome: document.getElementById('aluno-nome').value,
        cpf: document.getElementById('aluno-cpf').value,
        rg: document.getElementById('aluno-rg').value,
        endereco: document.getElementById('aluno-endereco').value,
        curso: document.getElementById('aluno-curso').value,
        instituicaoId: parseInt(document.getElementById('aluno-instituicao').value),
        email: document.getElementById('aluno-email').value,
        login: document.getElementById('aluno-login').value,
        senha: document.getElementById('aluno-senha').value
    };

    try {
        const res = await fetch(`${API_URL}/alunos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const data = await res.json().catch(() => null);

        if (res.ok || res.status === 201) {
            // Volta para a tela inicial, que exibe a confirmação do cadastro
            window.location.href = 'index.html?cadastro=ok';
        } else if (res.status === 400 && data && data.fieldErrors) {
            for (const [campo, msg] of Object.entries(data.fieldErrors)) {
                const span = document.getElementById(`erro-${campo}`);
                if (span) span.innerText = msg;
            }
            showToast('Verifique os campos inválidos', 'error');
        } else if (res.status === 409 && data) {
            showToast(data.message, 'error'); // Ex: CPF, E-mail ou Login duplicados
        } else {
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