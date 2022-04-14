package servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Candidato;

import java.io.InputStream;
import java.io.FileOutputStream;

import service.CandidatosService;

@MultipartConfig //Permite al servlet procesar objetos binarios (es decir, el contenido de un archivo)
@WebServlet("/AltaAction")
public class AltaAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Creamos un objeto correspondiente a la clase de la lógica de negocio:
		CandidatosService service = new CandidatosService(); 
		
		//Recuperamos los parámetros desde el cliente HTML:
		String nombre= request.getParameter("nombre");
		int edad= Integer.parseInt(request.getParameter("edad"));
		String puesto= request.getParameter("puesto");
		//Recuperamos el parámetro foto como objeto Part:
		Part foto = request.getPart("foto");
		String email = request.getParameter("email");
		
		//Muestra la lista de los encabezados de la petición :
		/*Collection<String> headers = foto.getHeaderNames();
		headers.forEach(h -> System.out.println(h+": "+ foto.getHeader(h)));*/
		
		String nombreFichero=obtenerNombreFichero(foto);
		
		//El constructor requiere que pongamos primero un int correspondiente al idCandidato, pero como es auto-numérico, se coloca un cero.
		Candidato nuevoCandidato= new Candidato(0,nombre,edad,puesto,nombreFichero,email);
		
		//Se llama al método de altaCandidato de la capa de servicio, para añadir el nuevo candidato a la BBDD:
		service.altaCandidato(nuevoCandidato);
		
		guardarFicheroEnServidor(request,foto,nombreFichero);
		
		//response.sendRedirect("menu.html"); Se encargará el FrontController de redireccionar a la vista.
	}
	
	private String obtenerNombreFichero(Part part) {
		for(String cd:part.getHeader("content-disposition").split(";")) {
			if(cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=')+1).trim().replace("\"", "");
				//return fileName.substring(fileName.lastIndexOf('/')+1).substring(fileName.lastIndexOf('\\'+1)); //MSIE fix.
				return fileName;
			}
		}
		return null;
	}
	
	private void guardarFicheroEnServidor(HttpServletRequest request, Part part, String nombreFichero) {
		String url = request.getServletContext().getRealPath("/");
		
		//Forma clásica para hacerlo:
		/*try(InputStream input=part.getInputStream();
				FileOutputStream output = new FileOutputStream(url+nombreFichero);){
			int leido = 0;
			leido = input.read();
			while(leido != -1) {
				output.write(leido);
				leido = input.read();
			}
		}catch(IOException ex) {
			ex.printStackTrace();
		}*/
		
		
		//Forma más sencilla:
		try {
			part.write(url+"/fotos/"+nombreFichero);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}