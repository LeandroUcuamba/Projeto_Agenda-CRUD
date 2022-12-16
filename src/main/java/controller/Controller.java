package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

//SÃo as requisições que vem do formulario (por parte do click do usuario);
@WebServlet(urlPatterns= {"/Controller", "/main", "/insert", "/select", "/update", "/delete", "/report"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();
       
    
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
        String action = request.getServletPath();
        System.out.println(action);
        if(action.equals("/main")) {
        	contactos(request, response);
        } else if(action.equals("/insert")) {
        	novoContato(request, response);
        } else if(action.equals("/update")) {
        	editarContato(request, response);
        } else if(action.equals("/delete")) {
        	removerContato(request, response);
        } else if(action.equals("/report")) {
        	gerarRelatorio(request, response);
        } else if(action.equals("/select")) {
        	listarContato(request, response);
        } 
        else {
        	response.sendRedirect("index.html");
        }
        
	}
        
    //Listar contactos   
	protected void contactos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  //Criar um objecto que ira receber os dados JavaBeans;
	  ArrayList<JavaBeans> lista = dao.listarContactos();
	  /* //teste de recebimento da lista;
	  for(int i=0; i< lista.size(); i++) {
		  System.out.println(lista.get(i).getIdcon());
		  System.out.println(lista.get(i).getNome());
		  System.out.println(lista.get(i).getFone());
		  System.out.println(lista.get(i).getEmail());
	  }  */
	  
	  //Encaminhar a lista ao documento agenda.jsp;
	  request.setAttribute("contatos", lista);
	  RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
	  rd.forward(request, response);
	  
	  
	}
	
	//Novo contato
	protected void novoContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Setar as variaveis JavaBeans
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		//invocar o metodo inserirContato passando o objecto contato;
		dao.inserirContato(contato);
		//redirecionar para "agenda.jsp"
		response.sendRedirect("main");
	}
	
	//Editar contacto
	protected void listarContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Recebimento do id do contacto que será editado;
		String idcon = request.getParameter("idcon");
		//setar a variavel JavaBeans;
		contato.setIdcon(idcon);
		//Exeutar o metodo selecionar contato (DAO)
		dao.selecionarContato(contato);
		//setar o conteudo do formulario com o conteúdo JavaBeans;
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());
		//Encaminhar ao documento editar.jsp;
		RequestDispatcher rd = request.getRequestDispatcher("editar.jsp");
		rd.forward(request, response);
			
	}
	
	protected void editarContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//setar as variaveis JavaBeans;
		contato.setIdcon(request.getParameter("idcon"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		//Executar o metodo alterarContato;
		dao.alterarContato(contato);
		//Redirecionar para o documento agenda.jsp (com dados já actualizados);
		response.sendRedirect("main");
		
	}
	
	
	//Remover um contacto;
	protected void removerContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Recebimento do id do contato a ser removido (validador.js);
		String idcon = request.getParameter("idcon");
		//setar as variaveis idcon do JavaBeans;
		contato.setIdcon(idcon);
		//Executar um metodo deletar contato (DAO) passando o objecto contato;
		dao.deletarContato(contato);
		//Redirecionar para o documento agenda.jsp (com dados já actualizados);
		response.sendRedirect("main");
		
	}
	
	
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Document documento = new Document();
		try {
			//Tipo de conteudo;
			response.setContentType("apllicatio/pdf");
			//Nome do documento;
			response.addHeader("Content-Disposition", "inline; filename="+"Contactos.pdf");
			//Criar o documento;
			PdfWriter.getInstance(documento, response.getOutputStream());
			//Abrir o documento para gerar o conteudo;
			documento.open();
			documento.add(new Paragraph("Lista de contactos:"));
			documento.add(new Paragraph(" "));
			//Criar uma tabela;
			PdfPTable tabela = new PdfPTable(3); //tabela de 3 colunas;
			//Cabeçario;
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Fone"));
			PdfPCell col3 = new PdfPCell(new Paragraph("E-mail"));
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			//Popular a tabela com os contatos;
			ArrayList<JavaBeans> lista = dao.listarContactos();
			for(int i=0; i < lista.size() ; i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}
			documento.add(tabela);
			documento.close();
		}catch(Exception e) {
			System.out.println(e);
			documento.close();
		}
		
	}
	
	
  }
