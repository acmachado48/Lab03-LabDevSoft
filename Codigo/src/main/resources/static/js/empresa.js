const API_URL = 'http://localhost:8080/api';

const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado') || 'null');
if (!usuarioLogado || !usuarioLogado.token || usuarioLogado.tipo !== 'EMPRESA') {
    window.location.href = 'index.html';
}

const EMPRESA_ID = usuarioLogado.id;
let vantagensEmpresa = [];

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('empresa-nome').innerText = usuarioLogado.nome;
    carregarVantagensEmpresa();
});

async function carregarVantagensEmpresa() {
    const tbody = document.getElementById('tbody-vantagens-empresa');
    try {
        const res = await apiFetch(`${API_URL}/vantagens/empresa/${EMPRESA_ID}`);
        if (res.ok) {
            vantagensEmpresa = await res.json();
            if (vantagensEmpresa.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--text-muted);">Nenhuma vantagem cadastrada ainda.</td></tr>';
                return;
            }
            tbody.innerHTML = vantagensEmpresa.map(v => `
                <tr>
                    <td>${v.id}</td>
                    <td>
                        <div style="display:flex; align-items:center; gap:10px;">
                            <img src="${v.foto}" alt="img" style="width:40px; height:40px; object-fit:cover; border-radius:6px; border:1px solid #ccc;" onerror="this.src='https://via.placeholder.com/40?text=Foto'">
                            ${v.descricao}
                        </div>
                    </td>
                    <td><strong>${v.custoEmMoedas}</strong> moedas</td>
                    <td class="actions-cell">
                        <button class="btn btn-secondary btn-sm" onclick="abrirModalVantagem(${v.id})">Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="deletarVantagem(${v.id})">Excluir</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--danger);">Erro ao carregar vantagens.</td></tr>';
    }
}

function abrirModalVantagem(id = null) {
    limparErros();
    const form = document.getElementById('form-vantagem');
    form.reset();
    document.getElementById('vantagem-id').value = id || '';

    if (id) {
        document.getElementById('modal-vantagem-titulo').innerText = 'Editar Vantagem';
        const vant = vantagensEmpresa.find(v => v.id === id);
        if (vant) {
            document.getElementById('vantagem-descricao').value = vant.descricao;
            document.getElementById('vantagem-custo').value = vant.custoEmMoedas;
            document.getElementById('vantagem-foto').value = vant.foto;
        }
    } else {
        document.getElementById('modal-vantagem-titulo').innerText = 'Cadastrar Vantagem';
    }

    document.getElementById('modal-vantagem').classList.remove('hidden');
}

function fecharModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
}

document.getElementById('form-vantagem').addEventListener('submit', async (e) => {
    e.preventDefault();
    limparErros();

    const id = document.getElementById('vantagem-id').value;
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/vantagens/${id}` : `${API_URL}/vantagens`;

    const payload = {
        empresaId: EMPRESA_ID,
        descricao: document.getElementById('vantagem-descricao').value,
        custoEmMoedas: parseInt(document.getElementById('vantagem-custo').value),
        foto: document.getElementById('vantagem-foto').value
    };

    try {
        const res = await apiFetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            showToast(`Vantagem ${id ? 'atualizada' : 'cadastrada'} com sucesso!`);
            fecharModal('modal-vantagem');
            carregarVantagensEmpresa();
        } else {
            const data = await res.json().catch(() => null);
            showToast(data?.message || 'Erro ao salvar vantagem.', 'error');
        }
    } catch (e) {
        showToast('Erro de conexão ao salvar.', 'error');
    }
});

async function deletarVantagem(id) {
    if (!confirm('Tem certeza que deseja excluir esta vantagem?')) return;
    try {
        const res = await apiFetch(`${API_URL}/vantagens/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Vantagem excluída com sucesso');
            carregarVantagensEmpresa();
        } else {
            const data = await res.json().catch(() => null);
            showToast(data?.message || 'Erro ao excluir vantagem.', 'error');
        }
    } catch (e) {
        showToast('Erro de conexão ao excluir.', 'error');
    }
}

function limparErros() {
    document.querySelectorAll('.error-msg').forEach(el => el.innerText = '');
}

function showToast(mensagem, tipo = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = mensagem;
    toast.className = `toast ${tipo}`;
    setTimeout(() => toast.classList.add('hidden'), 3000);
}

function sair() {
    localStorage.removeItem('usuarioLogado');
    window.location.href = 'index.html';
}