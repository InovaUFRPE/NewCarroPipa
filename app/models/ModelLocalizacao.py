from app import db

class Localizacao(db.Model):
    __tablename__ = "localizacao"

    id_pessoa = db.Column(db.Integer, db.ForeignKey('pessoa.id_pessoa'), primary_key=True)
    latitude = db.Column(db.Integer)
    longitude = db.Column(db.Integer)

    pessoa = db.relationship('Pessoa', foreign_keys=id_pessoa)
    
    def __init__(self, id_pessoa, latitude, longitude):
        self.id_pessoa = id_pessoa
        self.latitude = latitude
        self.longitude = longitude

    def __repr__(self):
        return "<Localizacao %r>" % self.id_pessoa
