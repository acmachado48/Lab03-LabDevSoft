const API_URL = 'http://localhost:8080/api';

// Estado Global
let empresasGlobais = [];
let vantagensGlobais = [];

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    carregarEmpresas();
    carregarVantagens();
});

// --- NAVEGAÇÃO ---
function switchView(view) {
    document.querySelectorAll('.view-section').forEach(sec => sec.classList.remove('active'));
    document.querySelectorAll('.nav-btn').forEach(btn => btn.classList.remove('active'));
    
    document.getElementById(`view-${view}`).classList.add('active');
    event.currentTarget.classList.add('active');
}

// --- INTERFACE (MODAIS E TOASTS) ---
function abrirModalEmpresa(id = null) {
    limparErros();
    const form = document.getElementById('form-empresa');
    form.reset();
    document.getElementById('empresa-id').value = id || '';
    
    if (id) {
        document.getElementById('modal-empresa-titulo').innerText = 'Editar Empresa';
        const emp = empresasGlobais.find(e => e.id === id);
        if(emp) {
            document.getElementById('empresa-nome').value = emp.nome;
            document.getElementById('empresa-cnpj').value = emp.cnpj;
            document.getElementById('empresa-email').value = emp.email;
            document.getElementById('empresa-telefone').value = emp.telefone || '';
            document.getElementById('empresa-login').value = emp.login;
        }
        document.getElementById('empresa-senha').required = false;
        document.getElementById('senha-hint').innerText = '(Deixe em branco para manter)';
    } else {
        document.getElementById('modal-empresa-titulo').innerText = 'Cadastrar Empresa';
        document.getElementById('empresa-senha').required = true;
        document.getElementById('senha-hint').innerText = '';
    }
    
    document.getElementById('modal-empresa').classList.remove('hidden');
}

function abrirModalVantagem(id = null) {
    limparErros();
    const form = document.getElementById('form-vantagem');
    form.reset();
    document.getElementById('vantagem-id').value = id || '';
    
    // Popular select de empresas
    const select = document.getElementById('vantagem-empresa');
    select.innerHTML = '<option value="">Selecione uma empresa...</option>';
    empresasGlobais.forEach(emp => {
        select.innerHTML += `<option value="${emp.id}">${emp.nome}</option>`;
    });

    if (id) {
        document.getElementById('modal-vantagem-titulo').innerText = 'Editar Vantagem';
        const vant = vantagensGlobais.find(v => v.id === id);
        if(vant) {
            document.getElementById('vantagem-empresa').value = vant.empresaId;
            document.getElementById('vantagem-empresa').disabled = true; // Não permite trocar o dono na edição
            document.getElementById('vantagem-descricao').value = vant.descricao;
            document.getElementById('vantagem-custo').value = vant.custoEmMoedas;
            document.getElementById('vantagem-foto').value = vant.foto;
        }
    } else {
        document.getElementById('modal-vantagem-titulo').innerText = 'Cadastrar Vantagem';
        document.getElementById('vantagem-empresa').disabled = false;
    }
    
    document.getElementById('modal-vantagem').classList.remove('hidden');
}

function fecharModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
}

function showToast(mensagem, tipo = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = mensagem;
    toast.className = `toast ${tipo}`;
    setTimeout(() => toast.classList.add('hidden'), 3000);
}

function limparErros() {
    document.querySelectorAll('.error-msg').forEach(el => el.innerText = '');
}

function processarErros(responseStatus, data) {
    if (responseStatus === 400 && data.fieldErrors) {
        for (const [campo, msg] of Object.entries(data.fieldErrors)) {
            const span = document.getElementById(`erro-${campo}`);
            if (span) span.innerText = msg;
        }
        showToast('Verifique os campos inválidos', 'error');
    } else if (responseStatus === 409) {
        showToast(data.message, 'error');
    } else {
        showToast(data.message || 'Erro inesperado', 'error');
    }
}

// --- CRUD EMPRESAS ---
async function carregarEmpresas() {
    try {
        const res = await fetch(`${API_URL}/empresas`);
        empresasGlobais = await res.json();
        const tbody = document.getElementById('tbody-empresas');
        tbody.innerHTML = empresasGlobais.map(emp => `
            <tr>
                <td>${emp.id}</td>
                <td><strong>${emp.nome}</strong></td>
                <td>${emp.cnpj}</td>
                <td>${emp.email}</td>
                <td class="actions-cell">
                    <button class="btn btn-secondary btn-sm" onclick="abrirModalEmpresa(${emp.id})">Editar</button>
                    <button class="btn btn-danger btn-sm" onclick="deletarEmpresa(${emp.id})">Excluir</button>
                </td>
            </tr>
        `).join('');
    } catch (e) {
        showToast('Erro ao carregar empresas', 'error');
    }
}

document.getElementById('form-empresa').addEventListener('submit', async (e) => {
    e.preventDefault();
    limparErros();
    
    const id = document.getElementById('empresa-id').value;
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/empresas/${id}` : `${API_URL}/empresas`;
    
    const payload = {
        nome: document.getElementById('empresa-nome').value,
        cnpj: document.getElementById('empresa-cnpj').value,
        email: document.getElementById('empresa-email').value,
        telefone: document.getElementById('empresa-telefone').value,
        login: document.getElementById('empresa-login').value,
        senha: document.getElementById('empresa-senha').value
    };

    const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        showToast(`Empresa ${id ? 'atualizada' : 'cadastrada'} com sucesso!`);
        fecharModal('modal-empresa');
        carregarEmpresas();
    } else {
        const data = await res.json();
        processarErros(res.status, data);
    }
});

async function deletarEmpresa(id) {
    if(!confirm('Tem certeza que deseja excluir esta empresa? Todas as vantagens dela serão perdidas.')) return;
    const res = await fetch(`${API_URL}/empresas/${id}`, { method: 'DELETE' });
    if (res.ok) {
        showToast('Empresa excluída com sucesso');
        carregarEmpresas();
        carregarVantagens(); // Atualiza a lista de vantagens
    } else {
        const data = await res.json();
        showToast(data.message || 'Erro ao excluir', 'error');
    }
}

// --- CRUD VANTAGENS ---
async function carregarVantagens() {
    try {
        const res = await fetch(`${API_URL}/vantagens`);
        vantagensGlobais = await res.json();
        const tbody = document.getElementById('tbody-vantagens');
        tbody.innerHTML = vantagensGlobais.map(v => `
            <tr>
                <td>${v.id}</td>
                <td>
                    <div style="display:flex; align-items:center; gap:10px;">
                        <img src="${v.foto}" alt="img" style="width:40px; height:40px; object-fit:cover; border-radius:6px; border:1px solid #ccc;">
                        ${v.descricao}
                    </div>
                </td>
                <td><strong>${v.custoEmMoedas}</strong> 🪙</td>
                <td>${v.nomeEmpresa}</td>
                <td class="actions-cell">
                    <button class="btn btn-secondary btn-sm" onclick="abrirModalVantagem(${v.id})">Editar</button>
                    <button class="btn btn-danger btn-sm" onclick="deletarVantagem(${v.id})">Excluir</button>
                </td>
            </tr>
        `).join('');
    } catch (e) {
        showToast('Erro ao carregar vantagens', 'error');
    }
}

document.getElementById('form-vantagem').addEventListener('submit', async (e) => {
    e.preventDefault();
    limparErros();
    
    const id = document.getElementById('vantagem-id').value;
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/vantagens/${id}` : `${API_URL}/vantagens`;
    
    const payload = {
        empresaId: id ? null : parseInt(document.getElementById('vantagem-empresa').value),
        descricao: document.getElementById('vantagem-descricao').value,
        custoEmMoedas: parseInt(document.getElementById('vantagem-custo').value),
        foto: document.getElementById('vantagem-foto').value
    };

    const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        showToast(`Vantagem ${id ? 'atualizada' : 'cadastrada'} com sucesso!`);
        fecharModal('modal-vantagem');
        carregarVantagens();
    } else {
        const data = await res.json();
        processarErros(res.status, data);
    }
});

async function deletarVantagem(id) {
    if(!confirm('Tem certeza que deseja excluir esta vantagem?')) return;
    const res = await fetch(`${API_URL}/vantagens/${id}`, { method: 'DELETE' });
    if (res.ok) {
        showToast('Vantagem excluída com sucesso');
        carregarVantagens();
    } else {
        showToast('Erro ao excluir vantagem', 'error');
    }
}