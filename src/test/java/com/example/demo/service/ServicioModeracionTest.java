package com.example.demo.service;

import com.example.demo.dto.DenunciaDeContenidoDTO;
import com.example.demo.entity.Denuncia;
import com.example.demo.entity.EstadoDePublicacion;
import com.example.demo.entity.EstadoDenuncia;
import com.example.demo.entity.Publicacion;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.denuncias;
import com.example.demo.repository.publicaciones;
import com.example.demo.repository.usuarios;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioModeracionTest {

    @Mock
    private ServicioNotificacion notificationService;

    @Mock
    private usuarios usuarioRepo;

    @Mock
    private publicaciones publicacionRepo;

    @Mock
    private denuncias denunciaRepo;

    @Mock
    private ServicioPost postService;

    @InjectMocks
    private ServicioModeracion servicioModeracion;

    @Test
    void registrarDenuncia_usuarioNoExiste_lanzaExcepcion() {
        DenunciaDeContenidoDTO dto = new DenunciaDeContenidoDTO(10L, "spam", 1L);

        when(usuarioRepo.findById(1L)).thenReturn(Optional.empty());

        // Aquí probamos el comportamiento real del código actual.
        // Como usa orElseThrow() sin mensaje, salta NoSuchElementException.
        assertThrows(NoSuchElementException.class,
                () -> servicioModeracion.registrarDenuncia(dto));

        verify(publicacionRepo, never()).findById(any());
        verify(denunciaRepo, never()).save(any());
        verify(notificationService, never()).notificar();
    }

    @Test
    void registrarDenuncia_publicacionNoExiste_lanzaExcepcion() {
        DenunciaDeContenidoDTO dto = new DenunciaDeContenidoDTO(10L, "spam", 1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setEmail("juan@test.com");
        usuario.setContrasena("1234");

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuario));
        when(publicacionRepo.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> servicioModeracion.registrarDenuncia(dto));

        verify(denunciaRepo, never()).save(any());
        verify(notificationService, never()).notificar();
    }

    @Test
    void registrarDenuncia_denunciaDuplicada_lanzaExcepcion() {
        DenunciaDeContenidoDTO dto = new DenunciaDeContenidoDTO(10L, "spam", 1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setEmail("juan@test.com");
        usuario.setContrasena("1234");

        Publicacion publicacion = new Publicacion();
        publicacion.setId(10L);
        publicacion.setContenido("post de prueba");
        publicacion.setOculta(false);
        publicacion.setEstado(EstadoDePublicacion.VISIBLE);

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuario));
        when(publicacionRepo.findById(10L)).thenReturn(Optional.of(publicacion));
        when(denunciaRepo.existeByUsuarioIdAndPublicacionIdAndMotivo(1L, 10L, "spam"))
                .thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicioModeracion.registrarDenuncia(dto));

        assertEquals("Denuncia duplicada", ex.getMessage());

        verify(publicacionRepo, never()).save(any());
        verify(denunciaRepo, never()).save(any());
        verify(notificationService, never()).notificar();
    }

    @Test
    void registrarDenuncia_casoValido_guardaDenunciaYPonePublicacionEnRevision() {
        DenunciaDeContenidoDTO dto = new DenunciaDeContenidoDTO(10L, "spam", 1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setEmail("juan@test.com");
        usuario.setContrasena("1234");

        Publicacion publicacion = new Publicacion();
        publicacion.setId(10L);
        publicacion.setContenido("post de prueba");
        publicacion.setOculta(false);
        publicacion.setEstado(EstadoDePublicacion.VISIBLE);

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuario));
        when(publicacionRepo.findById(10L)).thenReturn(Optional.of(publicacion));
        when(denunciaRepo.existeByUsuarioIdAndPublicacionIdAndMotivo(1L, 10L, "spam"))
                .thenReturn(false);

        when(denunciaRepo.save(any(Denuncia.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Denuncia resultado = servicioModeracion.registrarDenuncia(dto);

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuarioDenunciante());
        assertEquals(publicacion, resultado.getPublicacion());
        assertEquals("spam", resultado.getMotivo());
        assertEquals(EstadoDenuncia.PENDIENTE, resultado.getEstado());

        // Comprobamos que la publicación se toca como toca
        assertTrue(publicacion.isOculta());
        assertEquals(EstadoDePublicacion.EN_REVISION, publicacion.getEstado());

        verify(publicacionRepo, times(1)).save(publicacion);
        verify(notificationService, times(1)).notificar();
        verify(denunciaRepo, times(1)).save(any(Denuncia.class));
    }
}