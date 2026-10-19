document.getElementById('form-empresa').addEventListener('submit', async function(event) {
    event.preventDefault(); // Impede o recarregamento da página

    // 1. Limpar mensagens anteriores
    limparErros();

    // 2. Capturar os dados do formulário
    const formData = {
        nome: document.getElementById('nome').value,
        cnpj: document.getElementById('cnpj').value,
        telefone: document.getElementById('telefone').value,
        email: document.getElementById('email').value,
        login: document.getElementById('login').value,
        senha: document.getElementById('senha').value
    };

    try {
        // 3. Fazer o pedido HTTP (Ajuste a porta caso o Spring Boot esteja em outra)
        const response = await fetch('http://localhost:8080/api/empresas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        const data = await response.json().catch(() => null);

        // 4. Tratar a resposta com base no HTTP Status
        if (response.status === 201) {
            // Sucesso
            exibirMensagemGlobal('Empresa parceira cadastrada com sucesso!', 'sucesso');
            document.getElementById('form-empresa').reset();

        } else if (response.status === 400 && data && data.fieldErrors) {
            // Erros de validação do DTO (MethodArgumentNotValidException)
            // Preenche os spans correspondentes a cada campo inválido
            for (const [campo, mensagem] of Object.entries(data.fieldErrors)) {
                const erroSpan = document.getElementById(`erro-${campo}`);
                if (erroSpan) {
                    erroSpan.textContent = mensagem;
                }
            }

        } else if (response.status === 409 && data) {
            // Erros de regra de negócio (BusinessException), ex: CNPJ duplicado
            exibirMensagemGlobal(data.message, 'erro');

        } else {
            // Outros erros (500 Internal Server Error, etc.)
            exibirMensagemGlobal(data?.message || 'Ocorreu um erro inesperado. Tente novamente.', 'erro');
        }

    } catch (error) {
        exibirMensagemGlobal('Erro de conexão com o servidor. Verifique se a API está a correr.', 'erro');
    }
});

// Funções auxiliares
function limparErros() {
    // Limpa a mensagem global
    const msgGlobal = document.getElementById('mensagem-global');
    msgGlobal.className = 'mensagem';
    msgGlobal.textContent = '';

    // Limpa as mensagens específicas de cada campo
    const errosCampos = document.querySelectorAll('.erro-campo');
    errosCampos.forEach(span => span.textContent = '');
}

function exibirMensagemGlobal(mensagem, tipo) {
    const msgGlobal = document.getElementById('mensagem-global');
    msgGlobal.textContent = mensagem;
    msgGlobal.className = `mensagem ${tipo}`;
}