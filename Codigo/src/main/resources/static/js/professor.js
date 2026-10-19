const API_URL = 'http://localhost:8080/api';

// Usuário guardado pela tela inicial (index.html) após o login
const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado') || 'null');
if (!usuarioLogado || usuarioLogado.tipo !== 'PROFESSOR') {
    window.location.href = 'index.html';
}
const PROFESSOR_LOGADO_ID = usuarioLogado?.id;

document.addEventListener('DOMContentLoaded', () => {
    carregarDadosProfessor();
    carregarAlunos();
    carregarExtrato();
});

// 1. Carrega os dados do Professor (Nome e Saldo)
async function carregarDadosProfessor() {
    try {
        // Nota: O back-end precisará ter este endpoint (GET /api/professores/{id})
        const res = await fetch(`${API_URL}/professores/${PROFESSOR_LOGADO_ID}`);
        if (res.ok) {
            const professor = await res.json();
            document.getElementById('nome-prof').innerText = professor.nome;
            document.getElementById('saldo-prof').innerText = professor.saldoMoedas;
        } else {
            console.error('Erro ao buscar dados do professor');
        }
    } catch (e) {
        console.error('Erro de conexão ao buscar professor', e);
    }
}

// 2. Carrega a lista de alunos para o <select>
async function carregarAlunos() {
    const select = document.getElementById('select-aluno');
    try {
        // Nota: O back-end precisará ter este endpoint (GET /api/alunos)
        const res = await fetch(`${API_URL}/alunos`);
        if (res.ok) {
            const alunos = await res.json();
            select.innerHTML = '<option value="">Selecione um aluno...</option>';
            alunos.forEach(aluno => {
                // Idealmente, filtra-se apenas alunos da mesma instituição do professor
                select.innerHTML += `<option value="${aluno.id}">${aluno.nome} (${aluno.curso})</option>`;
            });
        } else {
            select.innerHTML = '<option value="">Erro ao carregar alunos</option>';
        }
    } catch (e) {
        select.innerHTML = '<option value="">Erro de conexão.</option>';
    }
}

// 3. Carrega o Histórico de Transações (Extrato)
async function carregarExtrato() {
    const tbody = document.getElementById('tbody-extrato');
    try {
        // Nota: O back-end precisará ter este endpoint (GET /api/transacoes/professor/{id})
        const res = await fetch(`${API_URL}/transacoes/professor/${PROFESSOR_LOGADO_ID}`);
        if (res.ok) {
            const transacoes = await res.json();
            
            if (transacoes.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">Nenhuma transação realizada ainda.</td></tr>';
                return;
            }

            tbody.innerHTML = transacoes.map(t => {
                const dataFormatada = new Date(t.dataTransacao).toLocaleDateString('pt-BR');
                return `
                    <tr>
                        <td>${dataFormatada}</td>
                        <td><strong>${t.nomeAluno}</strong></td>
                        <td>${t.motivo}</td>
                        <td style="color: var(--danger); font-weight: bold;">- ${t.valor} 🪙</td>
                    </tr>
                `;
            }).join('');
        }
    } catch (e) {
        console.error('Erro ao carregar extrato', e);
        tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: red;">Erro ao carregar o extrato.</td></tr>';
    }
}

// 4. Submete o formulário de Envio de Moedas
document.getElementById('form-doacao').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const alunoId = document.getElementById('select-aluno').value;
    const valor = parseInt(document.getElementById('qtd-moedas').value);
    const motivo = document.getElementById('motivo').value;

    // Validação básica de saldo no front-end
    const saldoAtual = parseInt(document.getElementById('saldo-prof').innerText);
    if (valor > saldoAtual) {
        alert('Erro: Você não possui saldo suficiente para esta doação.');
        return;
    }

    const payload = {
        professorId: PROFESSOR_LOGADO_ID,
        alunoId: parseInt(alunoId),
        valor: valor,
        motivo: motivo
    };

    try {
        const res = await fetch(`${API_URL}/transacoes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            alert('Reconhecimento enviado com sucesso!');
            
            // Atualiza a tela
            document.getElementById('form-doacao').reset();
            
            // Atualiza o saldo visualmente de imediato
            document.getElementById('saldo-prof').innerText = saldoAtual - valor;
            
            // Recarrega o extrato
            carregarExtrato();
        } else {
            const data = await res.json().catch(() => null);
            alert(data?.message || 'Erro ao enviar moedas. Verifique as regras de negócio.');
        }
    } catch (error) {
        alert('Erro de conexão. Verifique se o servidor está a correr.');
    }
});