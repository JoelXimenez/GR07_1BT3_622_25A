import org.apptrueque.model.Closet;
import org.apptrueque.model.Prenda;
import org.apptrueque.model.Publicacion;
import org.apptrueque.model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class PublicacionTest {

    // Test 1: Devuelve lista vacía si no hay publicaciones
    @Test
    void givenListaPublicacionesVacia_whenObtenerPublicaciones_thenRetornaListaVacia() {
        List<Publicacion> publicaciones = List.of();
        List<Publicacion> resultado = Publicacion.filtrarPublicaciones(publicaciones, null);
        assertThat(resultado).isEmpty();
    }

    // Test 2: Devuelve solo publicaciones activas
    @Test
    void givenPublicacionesActivasEInactivas_whenFiltrarPublicaciones_thenRetornaSoloActivas() {
        Publicacion activa = new Publicacion(new Usuario(), new Closet());
        Publicacion inactiva = new Publicacion(new Usuario(), new Closet());
        inactiva.setActiva(false);

        List<Publicacion> resultado = Publicacion.filtrarPublicaciones(List.of(activa, inactiva), null);
        assertThat(resultado).containsExactly(activa);
    }

    // Test 3: Devuelve solo publicaciones de otros usuarios
    @Test
    void givenPublicacionesDeOtrosYPropias_whenFiltrarPublicaciones_thenExcluyePropias() {
        Usuario usuarioActual = new Usuario("1", "Ana", "ana@email.com", "pass");
        Publicacion propia = new Publicacion(usuarioActual, new Closet());
        Publicacion deOtro = new Publicacion(new Usuario("2", "Luis", "luis@email.com", "pass"), new Closet());

        List<Publicacion> resultado = Publicacion.filtrarPublicaciones(List.of(propia, deOtro), usuarioActual);
        assertThat(resultado).containsExactly(deOtro);
    }

    // Test 4: Devuelve prendas filtradas por nombre
    @Test
    void givenClosetConPrendas_whenFiltrarPorNombre_thenDevuelvePrendasCorrectas() {
        Closet closet = new Closet();
        Prenda p1 = new Prenda("Vestido rojo", "Elegante vestido", "M", "nuevo", "mujer", "url");
        Prenda p2 = new Prenda("Zapatos negros", "Zapatos formales", "42", "nuevo", "hombre", "url");
        closet.agregarPrenda(p1);
        closet.agregarPrenda(p2);

        List<Prenda> resultado = Publicacion.filtrarPorNombre("vestido");

        assertThat(resultado).containsExactly(p1);
    }

    // Test 5: Devuelve prendas filtradas por categoría
    @Test
    void givenClosetConPrendas_whenFiltrarPorCategoria_thenDevuelvePrendasCorrectas() {
        Closet closet = new Closet();
        Prenda p1 = new Prenda("Vestido rojo", "Elegante vestido", "M", "nuevo", "mujer", "url");
        Prenda p2 = new Prenda("Zapatos negros", "Zapatos formales", "42", "nuevo", "hombre", "url");
        closet.agregarPrenda(p1);
        closet.agregarPrenda(p2);

        List<Prenda> resultado = Publicacion.filtrarPorCategoria(closet, "mujer");

        assertThat(resultado).containsExactly(p1);
    }

    // Test 6: Las publicaciones están ordenadas por fecha descendente
    @Test
    void givenPublicacionesDesordenadas_whenFiltrarPublicaciones_thenRetornaOrdenadasDesc() {
        Publicacion p1 = new Publicacion(new Usuario(), new Closet());
        p1.setFechaPublicacion(LocalDateTime.now().minusDays(1));
        Publicacion p2 = new Publicacion(new Usuario(), new Closet());
        p2.setFechaPublicacion(LocalDateTime.now());

        List<Publicacion> resultado = Publicacion.filtrarPublicaciones(List.of(p1, p2), null);
        assertThat(resultado).containsExactly(p2, p1);
    }

    // Test 7: Devuelve nombre del usuario en la publicación
    @Test
    void givenPublicacionDeUsuario_whenObtenerNombreUsuario_thenRetornaNombreCorrecto() {
        Usuario usuario = new Usuario("1", "Ana", "ana@email.com", "pass");
        Publicacion pub = new Publicacion(usuario, new Closet());
        assertThat(pub.getUsuario().getNombre()).isEqualTo("Ana");
    }

    // Test 8: Cada publicación contiene un ID único
    @Test
    void givenPublicaciones_whenCompararIds_thenTodosSonUnicos() {
        Publicacion p1 = new Publicacion(new Usuario(), new Closet());
        Publicacion p2 = new Publicacion(new Usuario(), new Closet());
        assertThat(p1.getId()).isNotEqualTo(p2.getId());
    }


}
