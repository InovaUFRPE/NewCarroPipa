package com.inovaufrpe.carropipa.model;

public class Pedido {
    public int idPedido;
    public int idCliente;
    public int idMotorista;
    public int dataHora;
    public double valorAgua;
    public double valorFrete;
    public String checkin;
    public boolean imediatoProgramado;
    public boolean confirmaProgramado;
    public Pedido (int id, int idC, int idM, int dH, double vAg, double vFr, String chk, boolean iP, boolean cP) {
        this.idPedido = id;
        this.idCliente = idC;
        this.idMotorista = idM;
        this.dataHora = dH;
        this.valorAgua = vAg;
        this.valorFrete = vFr;
        this.checkin = chk;
        this.imediatoProgramado = iP;
        this.confirmaProgramado = cP;
    }
}
