package com.example.demo.service; 

import com.example.demo.dto.SolicitudDeAmistadDTO; 
import com.example.demo.entity.EstadoSolicitudAmistad; 
import com.example.demo.entity.SolicitudAmistad; 
import com.example.demo.entity.Usuario; 
import com.example.demo.repository.amistades; 
import com.example.demo.repository.solicitudesAmistad; 
import com.example.demo.repository.usuarios; 

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test; 
import org.junit.jupiter.api.extension.ExtendWith; 
import org.mockito.InjectMocks; 
import org.mockito.Mock; 
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioSocialTest {

    @Mock
    private usuarios usuarioRepository;

    @Mock
    private solicitudesAmistad solicitudRepository;

    @Mock
    private amistades amistadRepository;

    @Mock
    private ServicioNotificacion notificacionService;

    @InjectMocks
    private ServicioSocial servicioSocial;

    @Test
    void gestionarSolicitudDeAmistad_emisorNoExiste_lanzaExcepcion() {
        SolicitudDeAmistadDTO dto = new SolicitudDeAmistadDTO(1L, 2L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicioSocial.gestionarSolicitudDeAmistad(dto));

        assertEquals("Emisor no existe", ex.getMessage());

        // Aquí no debería seguir con nada más, porque ya ha petado al principio
        verify(solicitudRepository, never()).save(any());
        verify(notificacionService, never()).notificar();
    }

    @Test
    void gestionarSolicitudDeAmistad_receptorNoExiste_lanzaExcepcion() {
        SolicitudDeAmistadDTO dto = new SolicitudDeAmistadDTO(1L, 2L);

        Usuario emisor = new Usuario();
        emisor.setId(1L);
        emisor.setNombre("Juan");
        emisor.setEmail("juan@test.com");
        emisor.setContrasena("1234");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(emisor));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicioSocial.gestionarSolicitudDeAmistad(dto));

        assertEquals("Receptor no existe", ex.getMessage());

        verify(solicitudRepository, never()).save(any());
        verify(notificacionService, never()).notificar();
    }

    @Test
    void gestionarSolicitudDeAmistad_yaSonAmigos_lanzaExcepcion() {
        SolicitudDeAmistadDTO dto = new SolicitudDeAmistadDTO(1L, 2L);

        Usuario emisor = new Usuario();
        emisor.setId(1L);
        emisor.setNombre("Juan");
        emisor.setEmail("juan@test.com");
        emisor.setContrasena("1234");

        Usuario receptor = new Usuario();
        receptor.setId(2L);
        receptor.setNombre("Ana");
        receptor.setEmail("ana@test.com");
        receptor.setContrasena("abcd");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(emisor));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(receptor));
        when(amistadRepository.existsByEmisorIdAndReceptorId(1L, 2L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicioSocial.gestionarSolicitudDeAmistad(dto));

        assertEquals("Ya existe amistad", ex.getMessage());

        verify(solicitudRepository, never()).save(any());
        verify(notificacionService, never()).notificar();
    }

    @Test
    void gestionarSolicitudDeAmistad_yaExisteSolicitud_lanzaExcepcion() {
        SolicitudDeAmistadDTO dto = new SolicitudDeAmistadDTO(1L, 2L);

        Usuario emisor = new Usuario();
        emisor.setId(1L);
        emisor.setNombre("Juan");
        emisor.setEmail("juan@test.com");
        emisor.setContrasena("1234");

        Usuario receptor = new Usuario();
        receptor.setId(2L);
        receptor.setNombre("Ana");
        receptor.setEmail("ana@test.com");
        receptor.setContrasena("abcd");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(emisor));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(receptor));
        when(amistadRepository.existsByEmisorIdAndReceptorId(1L, 2L)).thenReturn(false);
        when(solicitudRepository.existsByEmisorIdAndReceptorId(1L, 2L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicioSocial.gestionarSolicitudDeAmistad(dto));

        assertEquals("Solicitud ya existe", ex.getMessage());

        verify(solicitudRepository, never()).save(any());
        verify(notificacionService, never()).notificar();
    }

    @Test
    void gestionarSolicitudDeAmistad_casoValido_guardaSolicitudYNotifica() {
        SolicitudDeAmistadDTO dto = new SolicitudDeAmistadDTO(1L, 2L);

        Usuario emisor = new Usuario();
        emisor.setId(1L);
        emisor.setNombre("Juan");
        emisor.setEmail("juan@test.com");
        emisor.setContrasena("1234");

        Usuario receptor = new Usuario();
        receptor.setId(2L);
        receptor.setNombre("Ana");
        receptor.setEmail("ana@test.com");
        receptor.setContrasena("abcd");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(emisor));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(receptor));
        when(amistadRepository.existsByEmisorIdAndReceptorId(1L, 2L)).thenReturn(false);
        when(solicitudRepository.existsByEmisorIdAndReceptorId(1L, 2L)).thenReturn(false);

        // Caso feliz de toda la vida: guardamos y devolvemos la misma solicitud
        when(solicitudRepository.save(any(SolicitudAmistad.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SolicitudAmistad resultado = servicioSocial.gestionarSolicitudDeAmistad(dto);

        assertNotNull(resultado);
        assertEquals(emisor, resultado.getEmisor());
        assertEquals(receptor, resultado.getReceptor());
        assertEquals(EstadoSolicitudAmistad.PENDIENTE, resultado.getEstado());

        verify(solicitudRepository, times(1)).save(any(SolicitudAmistad.class));
        verify(notificacionService, times(1)).notificar();
    }
}