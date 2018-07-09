from app import db

class Endereco(db.Model):
    __tablename__ = "endereco"

    id_pessoa = db.Column(db.Integer, db.ForeignKey('pessoa.id_pessoa'), primary_key=True)
    logradouro = db.Column(db.String)
    complemento = db.Column(db.String)
    bairro = db.Column(db.String)
    cidade = db.Column(db.String)
    cep = db.Column(db.String)
    uf = db.Column(db.String)

    pessoa = db.relationship('Pessoa', foreign_keys=id_pessoa)

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
