const API_URL = 'http://localhost:8080/api';

// Usuário guardado pela tela inicial (index.html) após o login
const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado') || 'null');
if (!usuarioLogado || usuarioLogado.tipo !== 'ALUNO') {
    window.location.href = 'index.html';
}
const ALUNO_LOGADO_ID = usuarioLogado?.id;

let saldoAlunoAtual = 0;
let vantagensGlobais = [];

document.addEventListener('DOMContentLoaded', () => {
    carregarDadosAluno();
    carregarVantagens();
    carregarExtratoAluno();
});

// 1. Carrega Perfil e Saldo do Aluno
async function carregarDadosAluno() {
    try {
        const res = await fetch(`${API_URL}/alunos/${ALUNO_LOGADO_ID}`);
        if (res.ok) {
            const aluno = await res.json();
            document.getElementById('aluno-nome').innerText = aluno.nome;
            document.getElementById('aluno-curso').innerText = aluno.curso;
            document.getElementById('aluno-instituicao').innerText = aluno.nomeInstituicao;
            document.getElementById('aluno-saldo').innerText = aluno.saldoMoedas;
            saldoAlunoAtual = aluno.saldoMoedas;
        } else if (res.status === 404) {
            // Aluno da sessão não existe mais no banco
            sair();
        }
    } catch (e) {
        showToast('Erro ao carregar dados do aluno', 'error');
    }
}

// 2. Carrega as Vantagens do Catálogo
async function carregarVantagens() {
    const grid = document.getElementById('grid-vantagens');
    try {
        const res = await fetch(`${API_URL}/vantagens`);
        if (res.ok) {
            vantagensGlobais = await res.json();

            if (vantagensGlobais.length === 0) {
                grid.innerHTML = '<p style="color: var(--text-muted); grid-column: span 3;">Nenhuma vantagem cadastrada até o momento.</p>';
                return;
            }

            // Passa APENAS o ID no onclick para evitar erros de sintaxe
            grid.innerHTML = vantagensGlobais.map(v => `
                <div class="vantagem-card">
                    <img src="${v.foto}" alt="${v.descricao}" class="vantagem-foto" onerror="this.src='https://via.placeholder.com/300x160?text=Sem+Foto'">
                    <div class="vantagem-body">
                        <span class="vantagem-empresa">${v.nomeEmpresa}</span>
                        <h3 class="vantagem-titulo">${v.descricao}</h3>
                        <div class="vantagem-footer">
                            <span class="custo-tag">${v.custoEmMoedas} 🪙</span>
                            <button class="btn btn-primary btn-sm" onclick="resgatarVantagem(${v.id})">
                                Resgatar
                            </button>
                        </div>
                    </div>
                </div>
            `).join('');
        }
    } catch (e) {
        grid.innerHTML = '<p style="color: var(--danger);">Erro de conexão ao carregar vantagens.</p>';
    }
}

window.switchTab = function(tab) {
    document.querySelectorAll('.tab-content').forEach(el => el.style.display = 'none');
    document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));

    document.getElementById(`tab-${tab}`).style.display = 'block';
    event.currentTarget.classList.add('active');
};

// 3. Ação de Resgatar Vantagem (Segura por ID)
window.resgatarVantagem = async function(id) {
    const vantagem = vantagensGlobais.find(v => v.id === id);
    if (!vantagem) return;

    if (vantagem.custoEmMoedas > saldoAlunoAtual) {
        alert(`Saldo insuficiente! Você possui ${saldoAlunoAtual} moedas e a vantagem custa ${vantagem.custoEmMoedas}.`);
        return;
    }

    if (!confirm(`Confirma o resgate de "${vantagem.descricao}" por ${vantagem.custoEmMoedas} moedas?`)) return;

    try {
        const res = await fetch(`${API_URL}/resgatar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                alunoId: ALUNO_LOGADO_ID,
                vantagemId: id
            })
        });

        if (res.ok) {
            showToast('Vantagem resgatada com sucesso! O cupom foi enviado ao seu e-mail.');
            carregarDadosAluno();    // Atualiza o saldo atualizado
            carregarExtratoAluno();  // Recarrega o extrato com o novo registo
        } else {
            const data = await res.json().catch(() => null);
            showToast(data?.message || 'Erro ao processar o resgate.', 'error');
        }
    } catch (e) {
        showToast('Erro de conexão ao tentar resgatar.', 'error');
    }
};

// 4. Carrega o Extrato do Aluno
async function carregarExtratoAluno() {
    const tbody = document.getElementById('tbody-extrato-aluno');
    if (!tbody) return;

    try {
        // Busca as moedas recebidas dos professores e as vantagens resgatadas
        const [resTransacoes, resResgates] = await Promise.all([
            fetch(`${API_URL}/transacoes/aluno/${ALUNO_LOGADO_ID}`),
            fetch(`${API_URL}/resgates/aluno/${ALUNO_LOGADO_ID}`)
        ]);
        if (!resTransacoes.ok || !resResgates.ok) {
            tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--danger);">Erro ao carregar o extrato.</td></tr>';
            return;
        }

        const recebimentos = (await resTransacoes.json()).map(t => ({
            data: t.dataTransacao,
            descricao: `${t.motivo} <br><small style="color: var(--text-muted);">Prof. ${t.nomeProfessor}</small>`,
            tipo: '<span style="color: var(--success); font-weight: 600;">+ Recebido</span>',
            valor: `<span style="color: var(--success); font-weight: bold;">+ ${t.valor} 🪙</span>`
        }));
        const trocas = (await resResgates.json()).map(r => ({
            data: r.dataResgate,
            descricao: `${r.descricaoVantagem} (${r.nomeEmpresa}) <br><small style="color: var(--text-muted);">Cupom: <strong>${r.codigoCupom}</strong></small>`,
            tipo: '<span style="color: var(--danger); font-weight: 600;">- Resgate</span>',
            valor: `<span style="color: var(--danger); font-weight: bold;">- ${r.valor} 🪙</span>`
        }));
        const movimentacoes = [...recebimentos, ...trocas].sort((a, b) => new Date(b.data) - new Date(a.data));

        if (movimentacoes.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--text-muted);">Nenhuma movimentação registrada no seu histórico.</td></tr>';
            return;
        }

        tbody.innerHTML = movimentacoes.map(m => `
            <tr>
                <td>${new Date(m.data).toLocaleDateString('pt-BR')}</td>
                <td>${m.descricao}</td>
                <td>${m.tipo}</td>
                <td>${m.valor}</td>
            </tr>
        `).join('');
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--danger);">Erro de conexão ao carregar o extrato.</td></tr>';
    }
}

// 4. Ação de Resgatar Vantagem
async function resgatarVantagem(id, custo, descricao) {
    if (custo > saldoAlunoAtual) {
        showToast(`Saldo insuficiente! Você precisa de ${custo} moedas para este resgate.`, 'error');
        return;
    }

    if (!confirm(`Confirma o resgate de "${descricao}" por ${custo} moedas?`)) return;

    try {
        const res = await fetch(`${API_URL}/resgates`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                alunoId: ALUNO_LOGADO_ID,
                vantagemId: id
            })
        });

        if (res.ok) {
            const resgate = await res.json();
            showToast(`Vantagem resgatada! Código do cupom: ${resgate.codigoCupom} (também disponível no extrato)`);
            carregarDadosAluno(); // Atualiza saldo na tela
            carregarExtratoAluno();
        } else {
            const data = await res.json().catch(() => null);
            showToast(data?.message || 'Erro ao resgatar vantagem.', 'error');
        }
    } catch (e) {
        showToast('Erro de conexão ao tentar resgatar.', 'error');
    }
}

function sair() {
    localStorage.removeItem('usuarioLogado');
    window.location.href = 'index.html';
}

// Alternate entre as abas
function switchTab(tab) {
    document.querySelectorAll('.tab-content').forEach(el => el.style.display = 'none');
    document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));

    document.getElementById(`tab-${tab}`).style.display = 'block';
    event.currentTarget.classList.add('active');
}

function showToast(mensagem, tipo = 'success') {
    const toast = document.getElementById('toast');
    if (!toast) {
        alert(mensagem);
        return;
    }
    toast.textContent = mensagem;
    toast.className = `toast ${tipo}`;
    setTimeout(() => toast.classList.add('hidden'), 3500);
}