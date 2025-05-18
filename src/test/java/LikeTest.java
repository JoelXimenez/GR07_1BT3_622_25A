import org.apptrueque.model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;


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

}