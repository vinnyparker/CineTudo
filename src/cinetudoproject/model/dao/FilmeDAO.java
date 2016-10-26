/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinetudoproject.model.dao;

import cinetudoproject.model.database.DatabaseMySQL;
import cinetudoproject.model.domain.Filme;
import cinetudoproject.model.domain.Genero;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;

/**
 *
 * @author mateus
 */
public class FilmeDAO {
    Connection connection;
    DatabaseMySQL database;

    public FilmeDAO() {
        database = new DatabaseMySQL();
    }
    
    public void insert(Filme filme) throws FileNotFoundException {
        final String inserir = "INSERT INTO filme(titulo, diretor, ator, duracao, genero_id, classificacao, image, cinema_id) values(?,?,?,?,?,?,?,?)";	
      
        try {
            //to keep image on database
            FileInputStream fis = new FileInputStream(filme.getImageFile());
            //get the connection
            Connection conn = database.connect();
            PreparedStatement salvar = conn.prepareStatement(inserir);
            salvar.setString(1, filme.getTitulo());
            salvar.setString(2, filme.getDiretor());
            salvar.setString(3, filme.getAtorPrincipal());
            salvar.setInt(4, filme.getDuracao());
            salvar.setInt(5, filme.getGenero().getId());
            salvar.setInt(6, filme.getClassEtaria());
            salvar.setBinaryStream(7, fis, (int) filme.getImageFile().length());
            salvar.setInt(8, filme.getCinema_id());
            
            salvar.executeUpdate();
            
            database.desconnect(conn);
              
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Sucesso");
            alert.setContentText("Cadastrado com sucesso!");
            alert.showAndWait();
        
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Erro");
            alert.setContentText("Erro no cadastro: \n"+ ex.getMessage());
            alert.showAndWait();
        }
    }
    
    public Filme buscaFilme(String nomeFilme) {
        Filme filme = null;
        
        final String busca = "SELECT titulo, diretor, ator, duracao, genero_id, classificacao, image FROM filme WHERE titulo = ?";
        try {
            Connection conn = database.connect();
            PreparedStatement buscar = conn.prepareStatement(busca);
            buscar.setString(1, nomeFilme);
            ResultSet resultadoBusca = buscar.executeQuery();
            resultadoBusca.next();
            filme = buscaFilme(resultadoBusca);
            buscar.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filme;
    }
    
    private Filme buscaFilme(ResultSet resultadoBusca) throws SQLException, ParseException {
        Genero genero = null;
        GeneroDAO generoDAO = new GeneroDAO();
        
        Filme filme = new Filme();
        filme.setTitulo(resultadoBusca.getString(1));
        filme.setDiretor(resultadoBusca.getString(2));
        filme.setAtorPrincipal(resultadoBusca.getString(3));
        filme.setDuracao(resultadoBusca.getInt(4));
        
        int generoId = resultadoBusca.getInt(5);
        genero = generoDAO.buscaGenero(generoId);
        filme.setGenero(genero);
        
        filme.setClassEtaria(resultadoBusca.getInt(6));
        
        File imageFile = new File(resultadoBusca.getString(7));
        filme.setImageFile(imageFile);
        return filme;
    }
}
