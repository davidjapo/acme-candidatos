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


public class CandidatosService {
	
	private static EntityManager em;
	
	static {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("empresaPU");
		em=factory.createEntityManager();
	}
	
	public void altaCandidato(Candidato candidato) {
		EntityTransaction tx=em.getTransaction();
		tx.begin();
			em.persist(candidato);
		tx.commit();
	}
	
	public void eliminarCandidato(int idCandidato) {
		Candidato candidato = em.find(Candidato.class, idCandidato);
		EntityTransaction tx=em.getTransaction();
		tx.begin();
			if(candidato!=null) {
				em.remove(candidato);
			}
		tx.commit();
	}
	
	public List<Candidato> recuperarCandidatos(){
		//Se utiliza el método createNamedQuery del objeto EntityManager para acceder a la consulta nominada:
		TypedQuery<Candidato> query = em.createNamedQuery("Candidato.findAll", Candidato.class);
		return query.getResultList();
	}
	
	
	/*
	 * Si la lista no tiene datos, no devuelve un null, devuelve la lista con tamaño cero.
	 */
	public List<Candidato> recuperarCandidatosByPuesto(String puesto){
		TypedQuery<Candidato> query = em.createNamedQuery("Candidato.findByPuesto", Candidato.class);
		query.setParameter(1, puesto);
		return query.getResultList();
	}
	
	
	//Otro ejemplo de funcionalidad, pero en caso de que no lo encuentre, que me devuelva null:
	public Candidato buscarPorEmail(String email) {
		TypedQuery<Candidato> query = em.createNamedQuery("Candidato.findByEmail", Candidato.class);
		query.setParameter(1, email);
		//Si no encuentra el dato requerido, la lista estará vacía:
		List<Candidato> candidatos = query.getResultList(); 
		return !candidatos.isEmpty()?candidatos.get(0):null;
		
		/*Otra forma de hacerlo, utilizando programación funcional:
		return query.getResultList().stream().findFirst().orElse(null);
		*/
	}
	
	
	//Mismo ejemplo de funcionalidad anterior, pero de otro forma.
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
	
	
	//Otra funcionalidad, utilizando una sentencia JPQL de acción:
	public void eliminarCandidatoPorEmail(String email) {
		EntityTransaction tx = em.getTransaction();
		tx.begin(); //Siempre que sea una consulta de acción, hay que iniciar la transacción.
		Query query = em.createNamedQuery("Candidato.deleteByEmail");
		query.setParameter(1, email);
		query.executeUpdate();
		tx.commit();
	}
	
}
