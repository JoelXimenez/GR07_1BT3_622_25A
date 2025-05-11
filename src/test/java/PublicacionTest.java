import org.apptrueque.model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
        Prenda p1 = new Prenda();
        p1.setNombre("Vestido rojo");
        closet.getPrendas().add(p1);

        List<Prenda> resultado = Publicacion.filtrarPorNombre(closet, "vestido");
        assertThat(resultado).extracting(Prenda::getNombre).containsExactly("Vestido rojo");
    }

    // Test 5: Devuelve prendas filtradas por categoría
    @Test
    void givenClosetConPrendas_whenFiltrarPorCategoria_thenDevuelvePrendasCorrectas() {
        Closet closet = new Closet();
        Prenda p1 = new Prenda();
        p1.setCategoria("Mujer"); // Nota: Mayúscula inicial
        closet.getPrendas().add(p1);

        List<Prenda> resultado = Publicacion.filtrarPorCategoria(closet, "mujer"); // minúsculas
        assertThat(resultado)
                .hasSize(1)
                .extracting(Prenda::getCategoria)
                .containsExactly("Mujer"); // Respeta capitalización original
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
    // Test 9 Mock: Notifica al autor cuando otro usuario comenta
    @Test
    void givenComentarioDeOtroUsuario_whenAgregarComentario_thenNotificaAlAutor() {
        NotificacionService mockService = mock(NotificacionService.class);
        Usuario autor = new Usuario("1", "Carlos", "carlos@email.com", "pass");
        Closet closet = new Closet();
        Publicacion publicacion = new Publicacion(autor, closet);
        publicacion.setNotificacionService(mockService);

        publicacion.agregarComentario("Interesado!", "Ana");

        verify(mockService).notificar("Nuevo comentario en tu publicación", "Carlos");
    }

    // Test 10: No notifica si el autor comenta
    @Test
    void givenComentarioDelMismoAutor_whenAgregarComentario_thenNoNotifica() {
        NotificacionService mockService = mock(NotificacionService.class);
        Usuario autor = new Usuario("1", "Carlos", "carlos@email.com", "pass");
        Publicacion publicacion = new Publicacion(autor, new Closet());
        publicacion.setNotificacionService(mockService);

        publicacion.agregarComentario("Hola", "Carlos");

        verify(mockService, never()).notificar(anyString(), anyString());
    }

    // Test 11: Retorna true para estados activos (con diferentes mayúsculas)
    @ParameterizedTest
    @ValueSource(strings = {"activa", "ACTIVA", "Activa"})
    void givenEstadosActivos_whenVerificarEstado_thenRetornaTrue(String estado) {
        Usuario autor = new Usuario("1", "Carlos", "carlos@email.com", "pass");
        Closet closet = new Closet();
        Publicacion publicacion = new Publicacion(autor, closet);
        publicacion.setEstado(estado);

        assertThat(publicacion.estaActiva()).isTrue();
    }

    // Test 12: Retorna false para estados no activos
    @ParameterizedTest
    @ValueSource(strings = {"inactiva", "pendiente", "Ya intercambiada"})
    void givenEstadosNoActivos_whenVerificarEstado_thenRetornaFalse(String estado) {
        Usuario autor = new Usuario("1", "Carlos", "carlos@email.com", "pass");
        Closet closet = new Closet();
        Publicacion publicacion = new Publicacion(autor, closet);
        publicacion.setEstado(estado);

        assertThat(publicacion.estaActiva()).isFalse();
    }

}
