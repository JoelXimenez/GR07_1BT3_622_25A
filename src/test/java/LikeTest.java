import org.apptrueque.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


class LikeTest {

    // Test 1: Verifica que se asigna correctamente el closet y el email
    @Test
    void cuandoSeCreaLike_entoncesAsignaClosetYEmail() {
        Closet closet = new Closet();
        String email = "usuario@test.com";

        Like like = new Like();
        like.setCloset(closet);
        like.setUsuarioEmail(email);

        assertThat(like.getCloset()).isEqualTo(closet);
        assertThat(like.getUsuarioEmail()).isEqualTo(email);
    }

    // Test 2: Verifica que la fecha de creación se asigna al crear el objeto
    @Test
    void cuandoSeCreaLike_entoncesTieneFechaAsignada() {
        Like like = new Like();

        assertThat(like.getFecha()).isNotNull();
        assertThat(like.getFecha()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    // Test 3: Verifica que se puede cambiar la fecha manualmente
    @Test
    void dadoLike_cuandoSeModificaFecha_entoncesFechaCambia() {
        Like like = new Like();
        LocalDateTime nuevaFecha = LocalDateTime.of(2023, 1, 1, 12, 0);
        like.setFecha(nuevaFecha);

        assertThat(like.getFecha()).isEqualTo(nuevaFecha);
    }

    // Test 4: Verifica que se puede asignar un closet después de creado
    @Test
    void dadoLikeVacio_cuandoAsignaCloset_entoncesSeAsignaCorrectamente() {
        Closet closet = new Closet();
        Like like = new Like();
        like.setCloset(closet);

        assertThat(like.getCloset()).isSameAs(closet);
    }

    // Test 5: Verifica que el usuario email puede ser cambiado después de creado
    @Test
    void dadoLike_cuandoSeCambiaEmail_entoncesSeActualiza() {
        Like like = new Like();
        like.setUsuarioEmail("original@test.com");
        like.setUsuarioEmail("nuevo@test.com");

        assertThat(like.getUsuarioEmail()).isEqualTo("nuevo@test.com");
    }

    // Test 6: Verifica que un Like no es válido si no se asigna un usuarioEmail
    @Test
    void dadoLikeSinUsuarioEmail_cuandoSeConsulta_entoncesRetornaNull() {
        Like like = new Like();
        assertThat(like.getUsuarioEmail()).isNull();
    }


    // Test 7: Verifica que se puede crear un like sin errores si todos los campos están correctamente asignados
    @Test
    void cuandoSeAsignaClosetYEmail_entoncesLikeEsValido() {
        Closet closet = new Closet();
        Like like = new Like();
        like.setCloset(closet);
        like.setUsuarioEmail("valido@test.com");

        assertThat(like.getCloset()).isNotNull();
        assertThat(like.getUsuarioEmail()).isEqualTo("valido@test.com");
    }

    // Test 8: Verifica que el ID es null al crear el objeto y se puede setear (si tuvieras un setter)
    @Test
    void cuandoSeCreaLike_entoncesIdEsNull() {
        Like like = new Like();
        assertThat(like.getId()).isNull();
    }

// === TESTS CON MOCKS ===

    // Test 9/Mock 1: Verifica que el email del usuario que da like no es nulo
    @Test
    void dadoLike_cuandoSeObtieneEmail_entoncesDebeSerElEsperado() {
        Like like = new Like();
        like.setUsuarioEmail("test@ejemplo.com");
        assertThat(like.getUsuarioEmail()).isEqualTo("test@ejemplo.com");
    }

    // Test 10/Mock 2: Verifica que el like se asocia al closet correctamente
    @Test
    void dadoLike_cuandoSeAsignaCloset_entoncesDebeCoincidir() {
        Closet closet = new Closet();
        Like like = new Like();
        like.setCloset(closet);
        assertThat(like.getCloset()).isSameAs(closet);
    }


    // === TESTS PARAMETRIZADOS ===

    //Test 11:
    @ParameterizedTest
    @CsvSource({
            "user1@test.com, user1@test.com, true",
            "user1@test.com, user2@test.com, false"
    })
    void dadoUsuarioEmail_cuandoSeCompara_entoncesResultadoEsperado(String emailSeteado, String emailEsperado, boolean esperado) {
        Like like = new Like();
        like.setUsuarioEmail(emailSeteado);
        boolean resultado = like.getUsuarioEmail().equals(emailEsperado);
        assertThat(resultado).isEqualTo(esperado);
    }

    //Test 12:
    @ParameterizedTest
    @CsvSource({
            "2024-01-01T10:00, true",
            "2022-12-31T23:59, true"
    })
    void dadaFechaAsignada_cuandoSeConsulta_entoncesDebeCoincidir(LocalDateTime fecha, boolean esperado) {
        Like like = new Like();
        like.setFecha(fecha);
        boolean resultado = fecha.equals(like.getFecha());
        assertThat(resultado).isEqualTo(esperado);
    }
}