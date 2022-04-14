<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="model.Candidato, java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Candidatos registrados </title>
<style type="text/css">
	table,thead,tr,td,tbody{
		border: 1px solid pink;
	}
</style>
</head>
<body>


<%List<Candidato> candidatos = (List<Candidato>)request.getAttribute("candidatos"); 

if(!candidatos.isEmpty()){%>
	<br>
  	<br>
	<table align="center">
		<thead>
			<tr>
				<td>Nombre</td>
				<td>Edad</td>
				<td>Puesto</td>
				<td>Foto</td>
				<td>E-mail</td>
				<td></td>
			</tr>
		</thead>
		<tbody>
			<%for(int i=0; i<candidatos.size(); i++){ %>
				<tr>
					<td><%=candidatos.get(i).getNombre()%></td>
					<td><%=candidatos.get(i).getEdad()%></td>
					<td><%=candidatos.get(i).getPuesto()%></td>
					<td><img alt="foto-candidato" width="50px" height="70px" src="fotos/<%=candidatos.get(i).getFoto()%>"/></td>
					<td><%=candidatos.get(i).getEmail()%></td>
					<td><a href="FrontController?option=doEliminarId&idCandidato=<%=candidatos.get(i).getIdCandidato()%>">Eliminar</a></td>
				</tr>	
			<%}%>
		</tbody>
	</table>
	<br>
	<p align="center"><a href="FrontController?option=toMenu">Volver</a></p>
	
  <%}else {
		response.sendRedirect("sinresultados.html");  	
}%>


</body>
</html>