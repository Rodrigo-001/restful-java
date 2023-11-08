package br.com.fiap.megafarma.model.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.management.RuntimeErrorException;

public class ConnectionFactory {
	private static ConnectionFactory instance;
	private Connection conexao;
	private String url;
	private String user;
	private String pass;
	private String driver;
	
	//construtor com passagem de parâmetros
	public ConnectionFactory(String url, String user, String pass, String driver) {
		this.url = url;
		this.user = user;
		this.pass = pass;
		this.driver = driver;
	}

	//gets
	/*
	 * Desc: é um método estático que permite obter uma única instância de ConnectionFactory. 
	 * Ele carrega as informações de configuração do arquivo application.properties e cria uma única instância 
	 * da classe com base nesses valores. Se uma instância já existir, ela a retorna. 
	 * @author: Rodrigo
	 * */
	public static ConnectionFactory getInstance() {		
		ConnectionFactory result = instance;
		
		if (result != null) {
			return result;
		}
		Properties prop = new Properties();
		FileInputStream file = null;
		try {
			file = new FileInputStream("./src/main/resources/application.properties");
			prop.load(file);
			
			String url = prop.getProperty("datasource.url");
			String user = prop.getProperty("datasource.username");
			String pass = prop.getProperty("datasource.password");
			String driver = prop.getProperty("datasource.driver-class-name");
			file.close();
			
			if (instance == null) {
				instance = new ConnectionFactory(url, user, pass, driver);
			}
			return instance;	
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/*
	 * Método para tratamento de excessões da conexão ao banco de dados*/
	public Connection getConexao() {
		try {
			if (this.conexao != null && !this.conexao.isClosed()) {
				return this.conexao;
			}
			if (this.getDriver() == null || this.getDriver().equals("")) {
				System.out.println("Erro: nome da classe");
				throw new RuntimeException("Erro: Nome da Classe");				
			}
			if (this.getUrl() == null || this.getUrl().equals("")) {
				System.out.println("Erro de conexão");
				throw new RuntimeException("Erro: Url de conexão");	
			}
			if (this.getUrl() == null || this.getUser().equals("")) {
				throw new RuntimeException("Erro: Usuário incorreto");	
			}
			Class.forName(this.getDriver());
			this.conexao = DriverManager.getConnection(this.getUrl(), this.getUser(), this.getPass());
		} catch (ClassNotFoundException e) {
			System.out.println("Erro nome da classe: "+e.getMessage());
			System.exit(1);
		}catch (SQLException e) {
			System.out.println("Erro de SQL: "+e.getMessage());
			System.exit(1);
		}
		return conexao;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getDriver() {
		return driver;
	}
	
}
