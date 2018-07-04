from app import db

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
