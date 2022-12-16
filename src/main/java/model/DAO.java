package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAO {
	/** Modulo de conexao **/
	// Parametros de Conexao
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
	private String user = "root";
	private String password= "";
	
	// Metodo de conexão
	private Connection conectar() {
		
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
	}
	
	//Teste de Conexão
	@SuppressWarnings("unused")
	public void testarConexao() {
		try {
			Connection con = conectar();
			System.out.println(con);
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/* CRUD CREATE */
	public void inserirContato(JavaBeans contato) {
		String create = "Insert into contactos (nome,fone,email) values (?,?,?)";
		try {
			//Abrir conexão
			Connection con = conectar();
			//Preparar a query para a execucao no Banco de Dados;
			PreparedStatement pst = con.prepareStatement(create);
			//Substituir as ?, pelas variaveis que estao na classe JavaBeans;
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			//Executacao da query
			pst.executeUpdate();
			//Encerrar a conexao;
			con.close();
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/** CRUD READ **/
    public ArrayList<JavaBeans> listarContactos(){
    	//criando um obj para aceder as variaveis da classe JavaBeans; 
    	ArrayList<JavaBeans> contatos = new ArrayList<>();
    	
    	String read = "select * from contactos order by nome";
    	try {
    		//Abrir conexão
			Connection con = conectar();
			//Preparar a query para a execucao no Banco de Dados;
			PreparedStatement pst = con.prepareStatement(read);
			ResultSet rs = pst.executeQuery(); //Armazena temporariamente o retorno dos dados da bd num objecto;
			//O laço abaixo será executado quando houver contactos;
			while(rs.next()) {
				//variaveis de apoio que recebem os dados que vem do banco de dados;
				String idcon = rs.getString(1);
				String nome = rs.getString(2);
				String fone = rs.getString(3);
				String email = rs.getString(4);
				//populando o ArrayList;
				contatos.add(new JavaBeans(idcon, nome, fone, email));
			}
		  con.close();
		  return contatos;
			
    	} catch(Exception e) {
    		System.out.println(e);
    		return null;
    	}
    	
    }
	
    
    /** CRUD READ **/
    public void selecionarContato(JavaBeans contato) {
    	String read2 = "select * from contactos where idcon=?";
    	try {
    		//Abrir conexão
			Connection con = conectar();
			//Preparar a query para a execucao no Banco de Dados;
			PreparedStatement pst = con.prepareStatement(read2);
			pst.setString(1,contato.getIdcon());
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				//setar as variaveis JavaBeans;
				contato.setIdcon(rs.getString(1));
				contato.setNome(rs.getString(2));
				contato.setFone(rs.getString(3));
				contato.setEmail(rs.getString(4));
			}
			con.close();
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    	
    }
    
    //editar o contato;
    public void alterarContato(JavaBeans contato) {
    	String create = "update contactos set nome=?, fone=?, email=? where idcon=?";
    	try {
    		Connection con = conectar();
    		PreparedStatement pst = con.prepareStatement(create);
    		pst.setString(1, contato.getNome());
    		pst.setString(2, contato.getFone());
    		pst.setString(3, contato.getEmail());
    		pst.setString(4, contato.getIdcon());
    		pst.executeUpdate();
    		con.close();
    		
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    	
    }
    
    
    /* CRUD DELETE */
    public void deletarContato(JavaBeans contato) {
    	String delete = "delete from contactos where idcon=?";
    	try {
    		Connection con = conectar();
    		PreparedStatement pst = con.prepareStatement(delete);
    		pst.setString(1, contato.getIdcon());
    		pst.executeUpdate();
    		con.close();
    		
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }

}
