import { Pessoa } from './domain/Pessoa.js';
import { Endereco } from './domain/Endereco.js';
import { Documento } from './domain/Documento.js';
import { CadastroPessoaRequest } from './dto/CadastroPessoaRequest.js';
import { CadastroPessoaCompletoRequest } from './dto/CadastroPessoaCompletoRequest.js';
import { PessoaService } from './service/PessoaService.js';

const pessoaService = new PessoaService('http://localhost:8080/api/pessoas');

const resultado = document.getElementById('resultado');
const listaPessoas = document.getElementById('listaPessoas');

document.getElementById('formPessoaSimples').addEventListener('submit', async (event) => {
    event.preventDefault();

    try {
        const pessoa = new Pessoa(
            valor('simplesNome'),
            valor('simplesIdade'),
            valor('simplesEmail'),
            valor('simplesTipoSanguineo'));

        const request = new CadastroPessoaRequest(pessoa);
        const response = await pessoaService.cadastrarSimples(request);

        mostrarResultado(response);
        event.target.reset();
        document.getElementById('simplesIdade').value = 18;
        await listarPessoas();
    } catch (erro) {
        mostrarErro(erro);
    }
});

document.getElementById('formPessoaCompleta').addEventListener('submit', async (event) => {
    event.preventDefault();

    try {
        const pessoa = new Pessoa(
            valor('completoNome'),
            valor('completoIdade'),
            valor('completoEmail'),
            valor('completoTipoSanguineo'));

        const endereco = new Endereco(
            valor('enderecoRua'),
            valor('enderecoNumero'),
            valor('enderecoCidade'));

        const documento = new Documento(
            valor('documentoTipo'),
            valor('documentoValor'),
            valor('documentoDemaisDados'));

        const enderecos = endereco.estaPreenchido() ? [endereco] : [];
        const documentos = documento.estaPreenchido() ? [documento] : [];
        const request = new CadastroPessoaCompletoRequest(pessoa, enderecos, documentos);
        const response = await pessoaService.cadastrarCompleto(request);

        mostrarResultado(response);
        event.target.reset();
        document.getElementById('completoIdade').value = 18;
        await listarPessoas();
    } catch (erro) {
        mostrarErro(erro);
    }
});

document.getElementById('formBuscarPessoa').addEventListener('submit', async (event) => {
    event.preventDefault();

    try {
        const pessoa = await pessoaService.buscarPorId(valor('buscarId'));
        preencherFormularioAtualizacao(pessoa);
        mostrarResultado(pessoa);
    } catch (erro) {
        mostrarErro(erro);
    }
});

document.getElementById('formAtualizarPessoa').addEventListener('submit', async (event) => {
    event.preventDefault();

    try {
        const pessoa = new Pessoa(
            valor('atualizarNome'),
            valor('atualizarIdade'),
            valor('atualizarEmail'),
            valor('atualizarTipoSanguineo'));

        const request = new CadastroPessoaRequest(pessoa);
        const response = await pessoaService.atualizar(valor('atualizarId'), request);

        mostrarResultado(response);
        await listarPessoas();
    } catch (erro) {
        mostrarErro(erro);
    }
});

document.getElementById('btnExcluir').addEventListener('click', async () => {
    try {
        const id = valor('atualizarId');

        if (!id) {
            throw new Error('Informe o ID para excluir.');
        }

        await pessoaService.excluir(id);
        mostrarResultado({ mensagem: 'Pessoa excluida logicamente', id });
        limparFormularioAtualizacao();
        await listarPessoas();
    } catch (erro) {
        mostrarErro(erro);
    }
});

document.getElementById('btnListar').addEventListener('click', listarPessoas);

async function listarPessoas() {
    try {
        const pessoas = await pessoaService.listar();
        listaPessoas.innerHTML = '';

        for (const pessoa of pessoas) {
            const item = document.createElement('div');
            item.className = 'item-pessoa';
            item.innerHTML = `
                <strong>${pessoa.nome}</strong><br>
                ID: ${pessoa.id}<br>
                Idade: ${pessoa.idade}<br>
                Email: ${pessoa.email}<br>
                Tipo sanguineo: ${pessoa.tipoSanguineo || ''}
            `;
            item.addEventListener('click', () => preencherFormularioAtualizacao(pessoa));
            listaPessoas.appendChild(item);
        }

        mostrarResultado({ total: pessoas.length, pessoas });
    } catch (erro) {
        mostrarErro(erro);
    }
}

function preencherFormularioAtualizacao(pessoa) {
    document.getElementById('atualizarId').value = pessoa.id || '';
    document.getElementById('atualizarNome').value = pessoa.nome || '';
    document.getElementById('atualizarIdade').value = pessoa.idade || '';
    document.getElementById('atualizarEmail').value = pessoa.email || '';
    document.getElementById('atualizarTipoSanguineo').value = pessoa.tipoSanguineo || '';
}

function limparFormularioAtualizacao() {
    document.getElementById('formAtualizarPessoa').reset();
}

function valor(id) {
    return document.getElementById(id).value.trim();
}

function mostrarResultado(data) {
    resultado.textContent = JSON.stringify(data, null, 2);
}

function mostrarErro(erro) {
    resultado.textContent = 'Erro: ' + erro.message;
}
