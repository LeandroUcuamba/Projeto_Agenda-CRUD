<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="model.JavaBeans" %>
<%@ page import="java.util.ArrayList" %>
<% 

   @ SuppressWarnings ("unchecked") //para tirar o alerta que exibiu em baixo;
   ArrayList<JavaBeans> lista = (ArrayList<JavaBeans>) 
request.getAttribute("contatos");
/*
// Teste de recebimento da lista;
for(int i=0; i< lista.size(); i++) {
	  out.println(lista.get(i).getIdcon());
	  out.println(lista.get(i).getNome());
	  out.println(lista.get(i).getFone());
	  out.println(lista.get(i).getEmail());
} */
%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="ISO-8859-1">
<link rel="icon" href="imagens/person.PNG">
<link rel="stylesheet" href="style.css">
<title>Agenda de contactos</title>
</head>
<body>
   <h1>Agenda de contactos</h1>
   <a href="novo.html" class="botao1">Novo contacto</a>
   <a href="report" class="botao2">Relatorio</a>
   <table id="tabela">
     <thead>
       <tr>
         <th>Idcon</th>
         <th>Nome</th>
         <th>Fone</th>
         <th>Email</th>
         <th>Opções</th>
       </tr>
     <thead>
     <tbody>
      <%for(int i = 0; i < lista.size(); i++){ %>
          <tr>
             <td><%=lista.get(i).getIdcon()%></td>
             <td><%=lista.get(i).getNome()%></td>
             <td><%=lista.get(i).getFone()%></td>
             <td><%=lista.get(i).getEmail()%></td>
             <td><a href="select?idcon=<%=lista.get(i).getIdcon()%>" class="botao1">editar</a>
                 <a href="javascript: confirmar(<%=lista.get(i).getIdcon()%>)" class="botao2">apagar</a></td>
             </td>
          </tr>
       <%} %>    
     </tbody>
   </table>
   
   <script src="scripts/confirmador.js"></script>
</body>
</html>