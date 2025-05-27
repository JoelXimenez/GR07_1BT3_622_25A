import org.apptrueque.model.Match;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)


class MatchTest {
    @Mock
    private Match matchMock;
    // 1. Verificar que Match crea fecha por defecto
    @Test
    void crearMatchDebeTenerFecha() {
        Match match = new Match();
        assertNotNull(match.getFechaMatch());
    }
    // 2. Verificar que usuarioA puede asignarse
    @Test
    void asignarUsuarioA() {
        Match match = new Match();
        match.setUsuarioA("usuarioA@example.com");
        assertEquals("usuarioA@example.com", match.getUsuarioA());
    }
    // 3. Verificar que usuarioB puede asignarse
    @Test
    void asignarUsuarioB() {
        Match match = new Match();
        match.setUsuarioB("usuarioB@example.com");
        assertEquals("usuarioB@example.com", match.getUsuarioB());
    }
    // 4. Verificar que se puede cambiar la fecha
    @Test
    void cambiarFechaMatch() {
        Match match = new Match();
        LocalDateTime nuevaFecha = LocalDateTime.of(2020, 1, 1, 12, 0);
        match.setFechaMatch(nuevaFecha);
        assertEquals(nuevaFecha, match.getFechaMatch());
    }
    // 5. Verificar que dos usuarios iguales se pueden asignar
    @Test
    void asignarMismosUsuarios() {
        Match match = new Match();
        match.setUsuarioA("test@example.com");
        match.setUsuarioB("test@example.com");
        assertEquals("test@example.com", match.getUsuarioA());
        assertEquals("test@example.com", match.getUsuarioB());
    }
    // 6. Verificar que getId() retorna null al inicio
    @Test
    void idInicialEsNull() {
        Match match = new Match();
        assertNull(match.getId());
    }
    // 7. Verificar que usuarioA puede ser null
    @Test
    void usuarioANoDebeSerNull() {
        Match match = new Match();
        match.setUsuarioA(null);
        assertNull(match.getUsuarioA());
    }
    // 8. Verificar que usuarioB puede ser null
    @Test
    void usuarioBNoDebeSerNull() {
        Match match = new Match();
        match.setUsuarioB(null);
        assertNull(match.getUsuarioB());
    }
    // 9. MOCK: Simular acceso a usuarioA
    @Test
    void mockUsuarioAFalla() {
        when(matchMock.getUsuarioA()).thenReturn("mock@a.com");
        assertEquals("otro@a.com", matchMock.getUsuarioA());
    }
    // 10. MOCK: Simular acceso a usuarioB
    @Test
    void mockUsuarioBFalla() {
        when(matchMock.getUsuarioB()).thenReturn("mock@b.com");
        assertEquals("otro@b.com", matchMock.getUsuarioB());
    }
    // 11. PARAMETRIZADA: Asignar múltiples usuarios A
    @ParameterizedTest
    @ValueSource(strings = {"a1@example.com", "a2@example.com",
            "a3@example.com"})
    void asignarVariosUsuariosA(String email) {
        Match match = new Match();
        match.setUsuarioA(email);
        assertEquals(email, match.getUsuarioA());
    }
    // 12. PARAMETRIZADA: Comparar fechas asignadas
    @ParameterizedTest
    @CsvSource({
            "2022,1,1,10,0",
            "2023,5,15,15,30"
    })
    void asignarFechasMatch(int y, int m, int d, int h, int min) {
        Match match = new Match();
        LocalDateTime fecha = LocalDateTime.of(y, m, d, h, min);
        match.setFechaMatch(fecha);
        assertEquals(fecha, match.getFechaMatch());
    }
}