/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinetudoproject.model.dao;

import cinetudoproject.model.database.DatabaseMySQL;
import cinetudoproject.model.domain.Ingresso;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;

/**
 *
 * @author Wellington
 */
public class IngressoDAO {

    Connection connection;
    DatabaseMySQL database;

    public IngressoDAO() {
        database = new DatabaseMySQL();
    }

    public void insert(Ingresso ingresso) {
        final String inserir = "INSERT INTO ingresso (preco, tipo, sessao_id, venda_id, assento) values(?,?,?,?,?)";
        try {
            //get the connection

            connection = database.connect();
            try (PreparedStatement salvar = connection.prepareStatement(inserir)) {
                salvar.setFloat(1, ingresso.getPreco());
                salvar.setInt(2, ingresso.getTipo());
                salvar.setInt(3, ingresso.getSessao_id());
                salvar.setInt(4, ingresso.getVenda_id());
                salvar.setString(5, ingresso.getNumAssento());
                
                salvar.executeUpdate();
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
        }
    }

    public ArrayList<Ingresso> listar() throws IOException, ParseException {
        final String sql = "SELECT * FROM ingresso";
        ArrayList<Ingresso> retorno = new ArrayList();
        
        try {
            connection = database.connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Ingresso ingresso;
                ingresso = buscaIngresso(resultado);
                retorno.add(ingresso);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngressoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Ingresso buscaIngresso(ResultSet resultadoBusca) throws SQLException, ParseException {
        Ingresso ingresso = new Ingresso();
        ingresso.setId(resultadoBusca.getInt("id"));
        ingresso.setPreco(resultadoBusca.getFloat("preco"), false);
        ingresso.setTipo(resultadoBusca.getInt("tipo"));
        ingresso.setSessao_id(resultadoBusca.getInt("sessao_id"));
        ingresso.setVenda_id(resultadoBusca.getInt("venda_id"));
        ingresso.setAssento(resultadoBusca.getString("assento"));
        return ingresso;
    }
    
    public void delete(int ingressoId) {
        final String delete = "delete from ingresso where id = ?";
        try {
            Connection conn = database.connect();
            PreparedStatement deletar = conn.prepareStatement(delete);
            deletar.setInt(1, ingressoId);
            deletar.executeUpdate();
            deletar.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Ingresso Cancelado com Sucesso");
       } catch (SQLException ex) {
            Logger.getLogger("Error on: " + FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro na remoção" + "\n" + ex.getMessage());
            //return false;
        }
    }
    
    public Ingresso buscarIngressoId(int ingressoId) throws IOException, ParseException {
        final String sql = "SELECT * FROM ingresso where id = ?";
        Ingresso ingresso = new Ingresso();
        
        try {
            Connection conn = database.connect();
            PreparedStatement buscar = conn.prepareStatement(sql);
            buscar.setInt(1, ingressoId);
            ResultSet resultadoBusca = buscar.executeQuery();
            resultadoBusca.next();
            ingresso = buscarIngresso(resultadoBusca);
            buscar.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingresso;
    }
    
    private Ingresso buscarIngresso(ResultSet resultadoBusca) throws SQLException, ParseException {
        Ingresso ingresso = new Ingresso();
        ingresso.setId(resultadoBusca.getInt("id"));
        ingresso.setPreco(resultadoBusca.getFloat("preco"));
        ingresso.setTipo(resultadoBusca.getInt("tipo"));
        ingresso.setSessao_id(resultadoBusca.getInt("sessao_id"));
        ingresso.setVenda_id(resultadoBusca.getInt("venda_id"));

        return ingresso;
    }
}
