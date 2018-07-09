from app import db

class Empresa(db.Model):
    __tablename__ = "empresa"

    id_pessoa = db.Column(db.Integer, db.ForeignKey('pessoa.id_pessoa'), primary_key=True)

    pessoa = db.relationship('Pessoa', foreign_keys=id_pessoa)

    def __init__(self, id_pessoa):
        self.id_pessoa = id_pessoa

    def __repr__(self):
        return "<Empresa %r>" % self.id_pessoa
