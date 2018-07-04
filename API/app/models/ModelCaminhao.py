from app import db

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
