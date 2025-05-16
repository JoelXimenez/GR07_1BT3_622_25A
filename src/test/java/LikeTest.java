import org.apptrueque.model.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeTest {

    // === PRUEBAS BÁSICAS (Test 1 al 8) ===

    // Test 1: Verifica que un Like válido se cree con usuario, closet y fecha correctamente asignados
    @Test
    void dadoLikeValido_cuandoSeCrea_entoncesContieneUsuarioYCloset() {
        Usuario usuario = new Usuario("123", "Test", "test@test.com", "pass");
        usuario.setClosetActual(new Closet());
        Closet closet = new Closet();
        closet.setUsuario(new Usuario("456", "Owner", "owner@test.com", "pass"));
        closet.setPublicado(true);

        Like like = new Like(usuario, closet);

        assertThat(like.getUsuario()).isEqualTo(usuario);
        assertThat(like.getCloset()).isEqualTo(closet);
        assertThat(like.getFecha()).isNotNull();
    }

    // Test 2: Verifica que un usuario no pueda dar like a su propio closet
    @Test
    void dadoUsuario_cuandoLikeaSuPropioCloset_entoncesLanzaExcepcion() {
        Usuario usuario = new Usuario("123", "Test", "test@test.com", "pass");
        usuario.setClosetActual(new Closet());
        Closet closet = new Closet();
        closet.setUsuario(usuario);
        closet.setPublicado(true);

        assertThatThrownBy(() -> new Like(usuario, closet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No puedes dar like a tu propio closet");
    }

    // Test 3: Verifica que no se pueda likear un closet no publicado
    @Test
    void dadoClosetNoPublicado_cuandoSeLikea_entoncesLanzaExcepcion() {
        Usuario usuario = new Usuario("123", "Test", "test@test.com", "pass");
        usuario.setClosetActual(new Closet());
        Closet closet = new Closet();
        closet.setUsuario(new Usuario("456", "Owner", "owner@test.com", "pass"));
        closet.setPublicado(false);

        assertThatThrownBy(() -> new Like(usuario, closet))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se puede likear un closet no publicado");
    }

    // Test 4: Verifica que un usuario sin closet no pueda dar like
    @Test
    void dadoUsuarioSinCloset_cuandoDaLike_entoncesLanzaExcepcion() {
        Usuario usuario = new Usuario("123", "Test", "test@test.com", "pass");
        // No se asigna closet
        Closet closet = new Closet();
        closet.setUsuario(new Usuario("456", "Owner", "owner@test.com", "pass"));
        closet.setPublicado(true);

        assertThatThrownBy(() -> new Like(usuario, closet))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("El usuario no tiene un closet asignado");
    }

    // Test 5: Verifica que un Like recién creado esté activo
    @Test
    void dadoLikeRecienCreado_cuandoSeVerificaEstado_entoncesEstaActivo() {
        Usuario usuario = new Usuario("123", "Test", "test@test.com", "pass");
        usuario.setClosetActual(new Closet());
        Closet closet = new Closet();
        closet.setUsuario(new Usuario("456", "Owner", "owner@test.com", "pass"));
        closet.setPublicado(true);

        Like like = new Like(usuario, closet);

        assertThat(like.isActivo()).isTrue();
    }

    // Test 6: Verifica que un like activo se desactiva al quitarlo
    @Test
    void dadoLikeActivo_cuandoSeQuita_entoncesSeDesactiva() {
        Usuario usuario = new Usuario("123", "Test", "test@test.com", "pass");
        usuario.setClosetActual(new Closet());
        Closet closet = new Closet();
        closet.setUsuario(new Usuario("456", "Owner", "owner@test.com", "pass"));
        closet.setPublicado(true);

        Like like = new Like(usuario, closet);
        like.quitar();

        assertThat(like.isActivo()).isFalse();
    }

    // Test 7: Verifica que quitar un like ya inactivo devuelve false
    @Test
    void dadoLikeInactivo_cuandoSeIntentaQuitar_entoncesDevuelveFalse() {
        Usuario usuario = new Usuario("123", "Test", "test@test.com", "pass");
        usuario.setClosetActual(new Closet());
        Closet closet = new Closet();
        closet.setUsuario(new Usuario("456", "Owner", "owner@test.com", "pass"));
        closet.setPublicado(true);

        Like like = new Like(usuario, closet);
        like.quitar();
        boolean resultado = like.quitar();

        assertThat(resultado).isFalse();
    }

    // Test 8: Verifica que se pueda volver a dar like luego de quitarlo
    @Test
    void dadoLikeQuitado_cuandoSeVuelveADarLike_entoncesSeCreaNuevoLikeActivo() {
        Usuario usuario = new Usuario("123", "Test", "test@test.com", "pass");
        usuario.setClosetActual(new Closet());
        Closet closet = new Closet();
        closet.setUsuario(new Usuario("456", "Owner", "owner@test.com", "pass"));
        closet.setPublicado(true);

        Like like1 = new Like(usuario, closet);
        like1.quitar();
        Like like2 = new Like(usuario, closet);

        assertThat(like2.isActivo()).isTrue();
    }

    // === PRUEBAS CON MOCKS (Test 9 y 10) ===

    // Test 9: Verifica que se envía notificación al dueño cuando se da like (usa Mockito)
    @Test
    void dadoLikeValido_cuandoSeCrea_entoncesNotificaAlDueño() {
        NotificacionService mockNotif = mock(NotificacionService.class);
        Usuario owner = new Usuario("456", "Owner", "owner@test.com", "pass");
        Closet closet = new Closet();
        closet.setUsuario(owner);
        closet.setPublicado(true);
        Usuario liker = new Usuario("123", "Liker", "liker@test.com", "pass");
        liker.setClosetActual(new Closet());

        new Like(liker, closet, mockNotif);

        verify(mockNotif).notificar("Nuevo like en tu closet", "Owner");
    }

    // Test 10: Verifica que no se notifique más de una vez por el mismo like (usa Mockito)
    @Test
    void dadoLikeRepetido_cuandoSeCrea_entoncesNoNotificaDeNuevo() {
        NotificacionService mockNotif = mock(NotificacionService.class);
        Usuario owner = new Usuario("456", "Owner", "owner@test.com", "pass");
        Closet closet = new Closet();
        closet.setUsuario(owner);
        closet.setPublicado(true);
        Usuario liker = new Usuario("123", "Liker", "liker@test.com", "pass");
        liker.setClosetActual(new Closet());

        new Like(liker, closet, mockNotif);
        new Like(liker, closet, mockNotif);

        verify(mockNotif, times(1)).notificar(anyString(), anyString());
    }

    // === PRUEBAS PARAMETRIZADAS (Test 11 y 12) ===

    // Test 11: Verifica compatibilidad entre prendas según categoría
    @ParameterizedTest
    @CsvSource({
            "Mujer, Mujer, true",
            "Hombre, Unisex, true",
            "Mujer, Hombre, false",
            "Unisex, Mujer, true"
    })
    void dadasCategorias_cuandoSeVerificaCompatibilidad_entoncesRetornaResultadoEsperado(
            String cat1, String cat2, boolean esperado) {
        Prenda p1 = new Prenda();
        p1.setCategoria(cat1);
        p1.setIntercambiable(true);
        Prenda p2 = new Prenda();
        p2.setCategoria(cat2);
        p2.setIntercambiable(true);

        assertThat(Like.sonPrendasCompatibles(p1, p2)).isEqualTo(esperado);
    }

    // Test 12: Verifica que las prendas con la misma talla sean compatibles
    @ParameterizedTest
    @ValueSource(strings = {"S", "M", "L", "XL"})
    void dadoMismoTalla_cuandoSeVerificaCompatibilidad_entoncesRetornaTrue(String talla) {
        Prenda p1 = new Prenda();
        p1.setCategoria("Mujer");
        p1.setTalla(talla);
        p1.setIntercambiable(true);
        Prenda p2 = new Prenda();
        p2.setCategoria("Mujer");
        p2.setTalla(talla);
        p2.setIntercambiable(true);

        assertThat(Like.sonPrendasCompatibles(p1, p2)).isTrue();
    }
}
