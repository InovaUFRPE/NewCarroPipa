from app import db

class Usuario(db.Model):
    __tablename__ = "usuario"

    id_usuario = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String, unique=True)
    senha = db.Column(db.String)

    def __init__(self, email, senha):
        self.email = email
        self.senha = senha

    def __repr__(self):
        return "<Usuario %r>" % self.email
        
class Pessoa(db.Model):
    __tablename__ = "pessoa"

    id_pessoa = db.Column(db.Integer, primary_key=True)
    nomerazaosocial = db.Column(db.String)
    telefone = db.Column(db.String)
    id_usuario = db.Column(db.Integer, db.ForeignKey('usuario.id_usuario'))
    tipoPessoa = db.Column(db.String)
    cpfCnpj = db.Column(db.String, unique=True)

    usuario = db.relationship('Usuario', foreign_keys=id_usuario)

    def __init__(self, nomerazaosocial, telefone, id_usuario, tipoPessoa, cpfCnpj):
        self.nomerazaosocial = nomerazaosocial
        self.telefone = telefone
        self.id_usuario = id_usuario
        self.tipoPessoa = tipoPessoa
        self.cpfCnpj = cpfCnpj

    def __repr__(self):
        return "<Pessoa %r>" % self.nomerazaosocial

class Endereco(db.Model):
    __tablename__ = "endereco"

    id_pessoa = db.Column(db.Integer, primary_key=True)
    logradouro = db.Column(db.String)
    complemento = db.Column(db.String)
    bairro = db.Column(db.String)
    cidade = db.Column(db.String)
    cep = db.Column(db.String)
    uf = db.Column(db.String)

    def __init__(self, id_pessoa, logradouro, complemento, bairro, cidade, cep, uf):
        self.id_pessoa = id_pessoa
        self.logradouro = logradouro
        self.complemento = complemento
        self.bairro = bairro
        self.cidade = cidade
        self.cep = cep
        self.uf = uf

    def __repr__(self):
        return "<Endereco %r>" % self.logradouro

class Empresa(db.Model):
    __tablename__ = "empresa"

    id_pessoa_emp = db.Column(db.Integer, primary_key=True)

    def __init__(self, id_pessoa_emp):
        self.id_pessoa_emp = id_pessoa_emp

    def __repr__(self):
        return "<Empresa %r>" % self.id_pessoa_emp

class Cliente(db.Model):
    __tablename__ = "cliente"

    id_pessoa = db.Column(db.Integer, primary_key=True)

    def __init__(self, id_pessoa):
        self.id_pessoa = id_pessoa

    def __repr__(self):
        return "<Cliente %r>" % self.id_pessoa

class Caminhao(db.Model):
    __tablename__ = "caminhao"

    id_caminhao = db.Column(db.Integer, primary_key=True)
    placa = db.Column(db.Integer)
    capacidade = db.Column(db.String)
    modelo = db.Column(db.String)
    id_pessoa_emp = db.Column(db.Integer, db.ForeignKey('empresa.id_pessoa_emp'))

    empresa = db.relationship('Empresa', foreign_keys=id_pessoa_emp)

    def __init__(self, placa, capacidade, modelo, id_pessoa_emp):
        self.placa = placa
        self.capacidade = capacidade
        self.modelo = modelo
        self.id_pessoa_emp = id_pessoa_emp

    def __repr__(self):
        return "<Caminhao %r>" % self.placa

class Motorista(db.Model):
    __tablename__ = "motorista"

    id_pessoa = db.Column(db.Integer, primary_key=True)
    id_caminhao = db.Column(db.Integer, db.ForeignKey('caminhao.id_caminhao'))
    id_pessoa_emp = db.Column(db.Integer, db.ForeignKey('empresa.id_pessoa_emp'))

    caminhao = db.relationship('Caminhao', foreign_keys=id_caminhao)
    empresa = db.relationship('Empresa', foreign_keys=id_pessoa_emp)

    def __init__(self, id_pessoa, id_caminhao, id_pessoa_emp):
        self.id_pessoa = id_pessoa
        self.id_caminhao = id_caminhao
        self.id_pessoa_emp = id_pessoa_emp

    def __repr__(self):
        return "<Motorista %r>" % self.id_pessoa

class Pedido(db.Model):
    __tablename__ = "pedido"

    id_pedido = db.Column(db.Integer, primary_key=True)
    id_pessoa_cli = db.Column(db.Integer, db.ForeignKey('cliente.id_pessoa'))
    id_pessoa_mot = db.Column(db.Integer, db.ForeignKey('motorista.id_pessoa'))
    valor = db.Column(db.Float)
    dataHora = db.Column(db.DateTime)
    checkIn = db.Column(db.String)
    imediatoProgramado = db.Column(db.String)
    confirmadoProgramado = db.Column(db.String)

    cliente = db.relationship('Cliente', foreign_keys=id_pessoa_cli)
    motorista = db.relationship('Motorista', foreign_keys=id_pessoa_mot)

    def __init__(self, id_pessoa_cli, id_pessoa_mot, valor, dataHora, checkIn, imediatoProgramado, confirmadoProgramado):
        self.id_pessoa_cli = id_pessoa_cli
        self.id_pessoa_mot = id_pessoa_mot
        self.valor = valor
        self.dataHora = dataHora
        self.checkIn = checkIn
        self.imediatoProgramado = imediatoProgramado
        self.confirmadoProgramado = confirmadoProgramado

    def __repr__(self):
        return "<Pedido %r>" % self.id_pessoa_cli

class Ranking(db.Model):
    __tablename__ = "ranking"

    id_pessoa_deu = db.Column(db.Integer, primary_key=True)
    id_pedido = db.Column(db.Integer)
    nota = db.Column(db.Integer)
    comentario = db.Column(db.String)

    def __init__(self, id_pessoa_deu, id_pedido, nota, comentario):
        self.id_pessoa_deu = id_pessoa_deu
        self.id_pedido = id_pedido
        self.nota = nota
        self.comentario = comentario

    def __repr__(self):
        return "<Ranking %r>" % self.nota

class FormaPagto(db.Model):
    __tablename__ = "formapagto"

    id_formapagto = db.Column(db.Integer, primary_key=True)
    descricao = db.Column(db.String)

    def __init__(self, descricao):
        self.descricao = descricao

    def __repr__(self):
        return "<FormaPagto %r>" % self.descricao

class Pagamento(db.Model):
    __tablename__ = "pagamento"

    id_pagamento = db.Column(db.Integer, primary_key=True)
    id_formapagto = db.Column(db.Integer, db.ForeignKey('pagamento.id_formapagto'))
    valor = db.Column(db.Float)
    id_pedido = db.Column(db.Integer, db.ForeignKey('pedido.id_pedido'))
    dataHora = db.Column(db.DateTime)

    formapagto = db.relationship('FormaPagto', foreign_keys=id_formapagto)
    pedido = db.relationship('Pedido', foreign_keys=id_pedido)

    def __init__(self, id_formapagto, valor, id_pedido, dataHora):
        self.id_formapagto = id_formapagto
        self.valor = valor
        self.id_pedido = id_pedido
        self.dataHora = dataHora

    def __repr__(self):
        return "<Pagamento %r>" % self.valor
