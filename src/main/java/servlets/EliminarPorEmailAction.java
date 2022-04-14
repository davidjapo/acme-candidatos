package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.CandidatosService;
import model.Candidato;


@WebServlet("/EliminarPorEmailAction")
public class EliminarPorEmailAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CandidatosService service = new CandidatosService();
		String email = request.getParameter("email");
		Candidato encontrado = service.buscarByEmail(email);
		if(encontrado!=null) {
			service.eliminarCandidatoPorEmail(email);
			request.setAttribute("resultado", true);
			//response.sendRedirect("menu.html"); Se encargará el FrontController.
		}else {
			//response.sendRedirect("emailNoEncontrado.html"); Se encargará el FrontController.
			request.setAttribute("resultado", false);
		}
	}

}
