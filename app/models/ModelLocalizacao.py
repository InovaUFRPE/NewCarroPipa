from app import db
        
class Localizacao(db.Model):
    __tablename__ = "localizacao"

    id_localizacao = db.Column(db.Integer, primary_key=True)
    latitude = db.Column(db.Float)
    longitude = db.Column(db.Float)
    id_pessoa = db.Column(db.Integer, db.ForeignKey('pessoa.id_pessoa'))

    pessoa = db.relationship('Pessoa', foreign_keys=id_pessoa)

    def __init__(self, lat, lng, p):
        self.latitude = lat
        self.longitude = lng
        self.id_pessoa = p

    def __repr__(self):
        return "nada"
