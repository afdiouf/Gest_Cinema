package sn.dioufy.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import sn.dioufy.cinema.model.Film;

@RepositoryRestResource
@CrossOrigin(origins = "*")
public interface FilmRepository extends JpaRepository<Film, Long> {

}
