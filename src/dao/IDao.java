package dao;

import java.util.List;

public interface IDao<T> {
    boolean create(T o); // Méthode permettant d'ajouter un objet o de type T.
    List<T> findAll(); // Méthode permettant de renvoyer la liste des objets de type T.
    T findById(int id); // Méthode permettant de renvoyer un objet dont id est passé en paramètre.
    boolean delete(T o); // Méthode permettant de supprimer un objet o de type T.
    boolean update(T o); // Méthode permettant de modifier un objet o de type T.
}
