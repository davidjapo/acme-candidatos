package service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import model.Candidato;

//En esta clase se encuentra toda la lógica de negocio correspondiente al acceso a datos:
public class CandidatosService {
	
	private static EntityManager em;
	
	//Se hace uso de la unidad de persistencia para envolver los datos correspondientes a la conexión con la BBDD:
	static {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("empresaPU");
		em=factory.createEntityManager();
	}
	
	//Método que añade una tupla en la BBDD correspondiente a un candidato en concreto:
	public void altaCandidato(Candidato candidato) {
		EntityTransaction tx=em.getTransaction();
		tx.begin();
			em.persist(candidato);
		tx.commit();
	}
	
	//Método que elimina un candidato correspondiente al id del Candidato:
	public void eliminarCandidato(int idCandidato) {
		Candidato candidato = em.find(Candidato.class, idCandidato);
		EntityTransaction tx=em.getTransaction();
		tx.begin();
			if(candidato!=null) {
				em.remove(candidato);
			}
		tx.commit();
	}
	
	//Metodo que se encarga de obtener los candidatos registrados en format lista de objetos Candidato:
	public List<Candidato> recuperarCandidatos(){
		//Se utiliza el método createNamedQuery del objeto EntityManager para acceder a la consulta nominada:
		TypedQuery<Candidato> query = em.createNamedQuery("Candidato.findAll", Candidato.class);
		return query.getResultList();
	}

	//¡OJO! Se producen excepciones que hay controlar en caso de que no existan resultados para dicha condición,
	//o exista más de un resultado con la misma condición.
	//Se hace uso de try/catch (multicatch) para capturar la excepción:
	//TODAS LAS EXCEPCIONES EN JPA SON RUNTIME, NO OBLIGA A CAPTURARLAS, PERO SE DEBE DE CONTROLAR:
	public Candidato buscarByEmail(String email) {
		TypedQuery<Candidato> query = em.createNamedQuery("Candidato.findByEmail", Candidato.class);
		query.setParameter(1, email);
		Candidato encontrado;
		try {
			encontrado = query.getSingleResult();
		}catch(NoResultException | NonUniqueResultException e) {
			//e.printStackTrace();
			return null;
		}
		return encontrado;
	}
		
	//Otra funcionalidad, utilizando una sentencia JPQL de acción.
	//Este método se encarga de eliminar un candidato mediante el email facilitado:
	public void eliminarCandidatoPorEmail(String email) {
		EntityTransaction tx = em.getTransaction();
		tx.begin(); //Siempre que sea una consulta de acción, hay que iniciar la transacción.
		Query query = em.createNamedQuery("Candidato.deleteByEmail");
		query.setParameter(1, email);
		query.executeUpdate();
		tx.commit();
	}	
}
