const API_URL = 'http://localhost:8080/api';

// Simulação de autenticação (Aluno ID 3 do data.sql)
const ALUNO_LOGADO_ID = 3;

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
        const res = await fetch(`${API_URL}/alunos`);
        if (res.ok) {
            const alunos = await res.json();
            const aluno = alunos.find(a => a.id === ALUNO_LOGADO_ID) || alunos[0];
            
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
            tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--text-muted);">Erro ao carregar o histórico.</td></tr>';
        }
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--text-muted);">Erro de conexão com o servidor.</td></tr>';
    }
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