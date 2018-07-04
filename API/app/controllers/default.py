from flask import render_template, jsonify, request
from flask_sqlalchemy import SQLAlchemy, BaseQuery
from app import app, db

from app.models.ModelUsuario import Usuario
from app.models.ModelPessoa import Pessoa
from app.models.ModelEndereco import Endereco
from app.models.ModelEmpresa import Empresa
from app.models.ModelCliente import Cliente
from app.models.ModelCaminhao import Caminhao
from app.models.ModelMotorista import Motorista
from app.models.ModelRanking import Ranking
from app.models.ModelFormaPagto import FormaPagto
from app.models.ModelPagamento import Pagamento


@app.route("/index/<user>")
@app.route("/", defaults={'user': None})
def index(user):
    return render_template('index.html',
                           user=user)


'''----------------------------Usuario--------------------------------'''

@app.route("/usuario/add/<email>,<senha>")
def usuario_add(email,senha):
    i = Usuario(email,senha)
    db.session.add(i)
    db.session.commit()
    return "Usuário \"" + i.email + "\" incluido com sucesso!"

@app.route("/usuario/delete/<id_usuario>")
def usuario_delete(id_usuario):
    d = Usuario.query.get(id_usuario)
    db.session.delete(d)
    db.session.commit()
    return "Usuário \"" + d.email + "\" excluído com sucesso!"

@app.route("/usuario/get/<id_usuario>")
def usuario_get(id_usuario):
    g = Usuario.query.get(id_usuario)
    return "Usuário \"" + g.email

@app.route("/usuario/update/<id_usuario>,<email>,<senha>")
def usuario_update(id_usuario,email,senha):
    u = Usuario.query.get(id_usuario)
    u.email = email
    u.senha = senha
    db.session.commit()
    return "Usuário \"" + u.email + "\" alterado com sucesso!"

'''----------------------------Pessoa--------------------------------'''

@app.route("/pessoa/add/<nomerazaosocial>,<telefone>,<id_usuario>,<tipopessoa>,<cpfcnpj>")
def pessoa_add(nomerazaosocial,telefone,id_usuario,tipopessoa,cpfcnpj):
    i = Pessoa(nomerazaosocial,telefone,id_usuario,tipopessoa,cpfcnpj)
    db.session.add(i)
    db.session.commit()
    return "Pessoa \"" + i.nomerazaosocial + "\" incluida com sucesso!"

@app.route("/pessoa/delete/<id_pessoa>")
def pessoa_delete(id_pessoa):
    d = Pessoa.query.get(id_pessoa)
    db.session.delete(d)
    db.session.commit()
    return "Pessoa \"" + d.nomerazaosocial + "\" excluída com sucesso!"

@app.route("/pessoa/get/<id_pessoa>")
def pessoa_get(id_pessoa):
    g = Pessoa.query.get(id_pessoa)
    return "Pessoa \"" + g.nomerazaosocial

@app.route("/pessoa/update/<id_pessoa>,<nomerazaosocial>,<telefone>,<id_usuario>,<tipopessoa>,<cpfcnpj>")
def pessoa_update(id_pessoa,nomerazaosocial,telefone,id_usuario,tipopessoa,cpfcnpj):
    u = Pessoa.query.get(id_pessoa)
    u.nomerazaosocial = nomerazaosocial
    u.telefone = telefone
    u.id_usuario = id_usuario
    u.tipopessoa = tipopessoa
    u.cpfcnpj = cpfcnpj
    db.session.commit()
    return "Pessoa \"" + u.nomerazaosocial + "\" alterada com sucesso!"

'''----------------------------Endereco--------------------------------'''

@app.route("/endereco/add/<id_pessoa>,<logradouro>,<complemento>,<bairro>,<cidade>,<cep>,<uf>")
def endereco_add(id_pessoa,logradouro,complemento,bairro,cidade,cep,uf):
    i = Endereco(id_pessoa,logradouro,complemento,bairro,cidade,cep,uf)
    db.session.add(i)
    db.session.commit()
    return "Endereco \"" + i.logradouro + "\" incluido com sucesso!"

@app.route("/endereco/delete/<id_endereco>")
def endereco_delete(id_endereco):
    d = Endereco.query.get(id_endereco)
    db.session.delete(d)
    db.session.commit()
    return "Endereco \"" + d.logradouro + "\" excluído com sucesso!"

@app.route("/endereco/get/<id_endereco>")
def endereco_get(id_endereco):
    g = Endereco.query.get(id_endereco)
    return "Endereco \"" + g.logradouro

@app.route("/endereco/update/<id_pessoa>,<logradouro>,<complemento>,<bairro>,<cidade>,<cep>,<uf>")
def endereco_update(id_pessoa,logradouro,complemento,bairro,cidade,cep,uf):
    u = Endereco.query.get(id_endereco)
    u.logradouro = logradouro
    u.complemento = complemento
    u.bairro = bairro
    u.cidade = cidade
    u.cep = cep
    u.uf = uf
    db.session.commit()
    return "Endereco \"" + u.logradouro + "\" alterado com sucesso!"

'''----------------------------Empresa--------------------------------'''

@app.route("/empresa/add/<id_pessoa_emp>")
def empresa_add(id_pessoa_emp):
    i = Empresa(id_pessoa_emp)
    db.session.add(i)
    db.session.commit()
    return "Empresa \"" + str(i.id_pessoa_emp) + "\" incluida com sucesso!"

@app.route("/empresa/delete/<id_pessoa_emp>")
def empresa_delete(id_pessoa_emp):
    d = Empresa.query.get(id_pessoa_emp)
    db.session.delete(d)
    db.session.commit()
    return "Empresa \"" + str(d.id_pessoa_emp) + "\" excluída com sucesso!"

@app.route("/empresa/get/<id_pessoa_emp>")
def empresa_get(id_pessoa_emp):
    g = Empresa.query.get(id_pessoa_emp)
    return "Empresa \"" + str(g.id_pessoa_emp)

'''----------------------------Cliente--------------------------------'''

@app.route("/cliente/add/<id_pessoa>")
def cliente_add(id_pessoa):
    i = Cliente(id_pessoa)
    db.session.add(i)
    db.session.commit()
    return "Cliente \"" + str(i.id_pessoa) + "\" incluida com sucesso!"

@app.route("/cliente/delete/<id_pessoa>")
def cliente_delete(id_pessoa):
    d = Cliente.query.get(id_pessoa)
    db.session.delete(d)
    db.session.commit()
    return "Cliente \"" + str(d.id_pessoa) + "\" excluída com sucesso!"

@app.route("/cliente/get/<id_pessoa>")
def cliente_get(id_pessoa):
    g = Cliente.query.get(id_pessoa)
    return "Cliente \"" + str(g.id_pessoa)

'''----------------------------Caminhao--------------------------------'''

@app.route("/caminhao/add/<placa>,<capacidade>,<modelo>,<id_pessoa_emp>")
def caminhao_add(placa,capacidade,modelo,id_pessoa_emp):
    i = Caminhao(placa,capacidade,modelo,id_pessoa_emp)
    db.session.add(i)
    db.session.commit()
    return "Caminhao \"" + i.placa + "\" incluido com sucesso!"

@app.route("/caminhao/delete/<id_caminhao>")
def caminhao_delete(id_caminhao):
    d = Caminhao.query.get(id_caminhao)
    db.session.delete(d)
    db.session.commit()
    return "Caminhao \"" + d.placa + "\" excluído com sucesso!"

@app.route("/caminhao/get/<id_caminhao>")
def caminhao_get(id_caminhao):
    g = Caminhao.query.get(id_caminhao)
    return "Caminhao \"" + g.placa

@app.route("/caminhao/update/<id_caminhao>,<placa>,<capacidade>,<modelo>,<id_pessoa_emp>")
def caminhao_update(id_caminhao,placa,capacidade,modelo,id_pessoa_emp):
    u = Caminhao.query.get(id_caminhao)
    u.placa = placa
    u.capacidade = capacidade
    u.modelo = modelo
    u.id_pessoa_emp = id_pessoa_emp
    db.session.commit()
    return "Caminhao \"" + u.placa + "\" alterado com sucesso!"

'''----------------------------Motorista--------------------------------'''

@app.route("/motorista/add/<id_pessoa>,<id_caminhao>,<id_pessoa_emp>")
def motorista_add(id_pessoa,id_caminhao,id_pessoa_emp):
    i = Motorista(id_pessoa,id_caminhao,id_pessoa_emp)
    db.session.add(i)
    db.session.commit()
    return "Motorista \"" + str(i.id_pessoa) + "\" incluido com sucesso!"

@app.route("/motorista/delete/<id_pessoa>")
def motorista_delete(id_pessoa):
    d = Motorista.query.get(id_pessoa)
    db.session.delete(d)
    db.session.commit()
    return "Motorista \"" + str(d.id_pessoa) + "\" excluído com sucesso!"

@app.route("/motorista/get/<id_pessoa>")
def motorista_get(id_pessoa):
    g = Motorista.query.get(id_pessoa)
    return "Motorista \"" + str(g.id_pessoa)

@app.route("/motorista/update/<id_pessoa>,<id_caminhao>,<id_pessoa_emp>")
def motorista_update(id_pessoa,id_caminhao,id_pessoa_emp):
    u = Motorista.query.get(id_pessoa)
    u.id_caminhao = id_caminhao
    u.id_pessoa_emp = id_pessoa_emp
    db.session.commit()
    return "Motorista \"" + str(u.id_pessoa) + "\" alterado com sucesso!"

'''----------------------------Pedido--------------------------------'''

@app.route("/pedido/add/<id_pessoa_cli>,<id_pessoa_mot>,<valor>,<datahora>,<checkin>,<imediatoprogramado>,<confirmaprogramado>")
def pedido_add(id_pessoa_cli,id_pessoa_mot,valor,datahora,checkin,imediatoprogramado,confirmaprogramado):
    i = Pedido(id_pessoa_cli,id_pessoa_mot,valor,datahora,checkin,imediatoprogramado,confirmaprogramado)
    db.session.add(i)
    db.session.commit()
    return "Pedido \"" + str(i.id_pedido) + "\" incluido com sucesso!"

@app.route("/pedido/delete/<id_pedido>")
def pedido_delete(id_pedido):
    d = Pedido.query.get(id_pedido)
    db.session.delete(d)
    db.session.commit()
    return "Pedido \"" + str(d.id_pedido) + "\" excluído com sucesso!"

@app.route("/pedido/get/<id_pedido>")
def pedido_get(id_pedido):
    g = Pedido.query.get(id_pedido)
    return "Pedido \"" + str(g.id_pedido)

@app.route("/pedido/update/<id_pedido>,<id_pessoa_cli>,<id_pessoa_mot>,<valor>,<datahora>,<checkin>,<imediatoprogramado>,<confirmaprogramado>")
def pedido_update(id_pedido,id_pessoa_cli,id_pessoa_mot,valor,datahora,checkin,imediatoprogramado,confirmaprogramado):
    u = Pedido.query.get(id_pedido)
    u.id_pessoa_cli = id_pessoa_cli
    u.id_pessoa_mot = id_pessoa_mot
    u.valor = valor
    u.datahora = datahora
    u.checkin = checkin
    u.imediatoprogramado = imediatoprogramado
    u.confirmaprogramado = confirmaprogramado
    db.session.commit()
    return "Pedido \"" + str(u.id_pedido) + "\" alterado com sucesso!"

'''----------------------------Ranking--------------------------------'''

@app.route("/ranking/add/<id_pessoa_deu>,<id_pedido>,<nota>,<comentario>")
def ranking_add(id_pessoa_deu,id_pedido,nota,comentario):
    i = Ranking(id_pessoa_deu,id_pedido,nota,comentario)
    db.session.add(i)
    db.session.commit()
    return "Ranking \"" + str(i.id_pessoa_deu) + "\" incluido com sucesso!"

@app.route("/ranking/delete/<id_pessoa_deu>")
def ranking_delete(id_pessoa_deu):
    d = Ranking.query.get(id_pessoa_deu)
    db.session.delete(d)
    db.session.commit()
    return "Ranking \"" + str(d.id_pessoa_deu) + "\" excluído com sucesso!"

@app.route("/ranking/get/<id_pessoa_deu>")
def ranking_get(id_pessoa_deu):
    g = Ranking.query.get(id_pessoa_deu)
    return "Ranking \"" + str(g.id_pessoa_deu)

@app.route("/ranking/update/<id_pessoa_deu>,<id_pedido>,<nota>,<comentario>")
def ranking_update(id_pessoa_deu,id_pedido,nota,comentario):
    u = Ranking.query.get(id_pessoa_deu)
    u.nota = nota
    u.comentario = comentario
    db.session.commit()
    return "Ranking \"" + str(u.id_pessoa_deu) + "\" alterado com sucesso!"

'''----------------------------FormaPagto--------------------------------'''

@app.route("/formapagto/add/<descricao>")
def formapagto_add(descricao):
    i = FormaPagto(descricao)
    db.session.add(i)
    db.session.commit()
    return "FormaPagto \"" + i.descricao + "\" incluido com sucesso!"

@app.route("/formapagto/delete/<id_formapagto>")
def formapagto_delete(id_formapagto):
    d = FormaPagto.query.get(id_formapagto)
    db.session.delete(d)
    db.session.commit()
    return "FormaPagto \"" + d.descricao + "\" excluído com sucesso!"

@app.route("/formapagto/get/<id_formapagto>")
def formapagto_get(id_formapagto):
    g = FormaPagto.query.get(id_formapagto)
    return "FormaPagto \"" + g.descricao

@app.route("/formapagto/update/<id_formapagto>,<descricao>")
def formapagto_update(id_formapagto,descricao):
    u = FormaPagto.query.get(id_formapagto)
    u.descricao = descricao
    db.session.commit()
    return "FormaPagto \"" + u.descricao + "\" alterado com sucesso!"

'''----------------------------Pagamento--------------------------------'''

@app.route("/pagamento/add/<id_formapagto>,<valor>,<id_pedido>,<datahora>")
def pagamento_add(id_formapagto,valor,id_pedido,datahora):
    i = Pagamento(id_formapagto,valor,id_pedido,datahora)
    db.session.add(i)
    db.session.commit()
    return "Pagamento \"" + str(i.id_pagamento) + "\" incluido com sucesso!"

@app.route("/pagamento/delete/<id_pagamento>")
def pagamento_delete(id_pagamento):
    d = Pagamento.query.get(id_pagamento)
    db.session.delete(d)
    db.session.commit()
    return "Pagamento \"" + str(d.id_pagamento) + "\" excluído com sucesso!"

@app.route("/pagamento/get/<id_pagamento>")
def pagamento_get(id_pagamento):
    g = Pagamento.query.get(id_pagamento)
    return "Pagamento \"" + str(g.id_pagamento)

@app.route("/pagamento/update/<id_pagamento>,<id_formapagto>,<valor>,<id_pedido>,<datahora>")
def pagamento_update(id_pagamento,id_formapagto,valor,id_pedido,datahora):
    u = Pagamento.query.get(id_pagamento)
    u.id_formapagto = id_formapagto
    u.valor = valor
    u.id_pedido = id_pedido
    u.datahora = datahora
    db.session.commit()
    return "Pagamento \"" + str(u.id_pagamento) + "\" alterado com sucesso!"
