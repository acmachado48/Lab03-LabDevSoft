const API_URL = 'http://localhost:8080/api';

// Simulação de autenticação (Aluno ID 3 pré-cadastrado no data.sql)
const ALUNO_LOGADO_ID = 3;

let saldoAlunoAtual = 0;

document.addEventListener('DOMContentLoaded', () => {
    carregarDadosAluno();
    carregarVantagens();
    carregarExtratoAluno();
});

// 1. Carrega Perfil e Saldo do Aluno
async function carregarDadosAluno() {
    try {
        const res = await fetch(`${API_URL}/alunos`);
        if (res.ok) {
            const alunos = await res.json();
            const aluno = alunos.find(a => a.id === ALUNO_LOGADO_ID);
            
            if (aluno) {
                document.getElementById('aluno-nome').innerText = aluno.nome;
                document.getElementById('aluno-curso').innerText = aluno.curso;
                document.getElementById('aluno-saldo').innerText = aluno.saldoMoedas;
                saldoAlunoAtual = aluno.saldoMoedas;
            }
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
            const vantagens = await res.json();

            if (vantagens.length === 0) {
                grid.innerHTML = '<p style="color: var(--text-muted); grid-column: span 3;">Nenhuma vantagem cadastrada pelas empresas parceiras até o momento.</p>';
                return;
            }

            grid.innerHTML = vantagens.map(v => `
                <div class="vantagem-card">
                    <img src="${v.foto}" alt="${v.descricao}" class="vantagem-foto" onerror="this.src='https://via.placeholder.com/300x160?text=Sem+Foto'">
                    <div class="vantagem-body">
                        <span class="vantagem-empresa">${v.nomeEmpresa}</span>
                        <h3 class="vantagem-titulo">${v.descricao}</h3>
                        <div class="vantagem-footer">
                            <span class="custo-tag">${v.custoEmMoedas} 🪙</span>
                            <button class="btn btn-primary btn-sm" onclick="resgatarVantagem(${v.id}, ${v.custoEmMoedas}, '${v.descricao.replace(/'/g, "\\'")}')">
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

// 3. Carrega o Extrato do Aluno
async function carregarExtratoAluno() {
    const tbody = document.getElementById('tbody-extrato-aluno');
    try {
        // Busca transações recebidas do professor
        const res = await fetch(`${API_URL}/transacoes/aluno/${ALUNO_LOGADO_ID}`);
        if (res.ok) {
            const transacoes = await res.json();

            if (transacoes.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--text-muted);">Nenhuma movimentação registrada no seu histórico.</td></tr>';
                return;
            }

            tbody.innerHTML = transacoes.map(t => {
                const dataFormatada = new Date(t.dataTransacao).toLocaleDateString('pt-BR');
                return `
                    <tr>
                        <td>${dataFormatada}</td>
                        <td>${t.motivo}</td>
                        <td><span style="color: var(--success); font-weight: 600;">+ Recebido</span></td>
                        <td style="color: var(--success); font-weight: bold;">+ ${t.valor} 🪙</td>
                    </tr>
                `;
            }).join('');
        } else {
            tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--text-muted);">Nenhuma doação recebida ainda.</td></tr>';
        }
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--text-muted);">Nenhuma doação recebida ainda.</td></tr>';
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
        // Envia requisição de resgate ao backend (Será processado se a rota de resgate estiver ativa)
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

// Alternate entre as abas
function switchTab(tab) {
    document.querySelectorAll('.tab-content').forEach(el => el.style.display = 'none');
    document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));

    document.getElementById(`tab-${tab}`).style.display = 'block';
    event.currentTarget.classList.add('active');
}

function showToast(mensagem, tipo = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = mensagem;
    toast.className = `toast ${tipo}`;
    setTimeout(() => toast.classList.add('hidden'), 3500);
}