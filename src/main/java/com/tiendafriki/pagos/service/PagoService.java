package com.tiendafriki.pagos.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.tiendafriki.pagos.Enum.EstadoPago;
import com.tiendafriki.pagos.dto.PagoRequestDTO;
import com.tiendafriki.pagos.dto.PagoResponseDTO;
import com.tiendafriki.pagos.model.Pago;
import com.tiendafriki.pagos.model.Pedido;
import com.tiendafriki.pagos.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    private PagoResponseDTO convertirResponseDTO(Pago pago) {

        return new PagoResponseDTO(
                pago.getId(),
                pago.getPedidoId(),
                pago.getMonto(),
                pago.getMetodoPago(),
                pago.getFechaPago(),
                pago.getEstado()
        );
    }

    public List<PagoResponseDTO> listar(){

        return repository.findAll()    
            .stream()                  
            .map(this::convertirResponseDTO) 
            .toList(); 
        
    }

    public PagoResponseDTO buscarPorId(Integer id){

        return repository.findById(id)  
                .map(this::convertirResponseDTO)  
                .orElseThrow(() ->
                    new NoSuchElementException
                    ( "[ERROR] Pago no encontrado [X_X] ..."));
    }

    public List<PagoResponseDTO> buscarPorFecha(LocalDate fechaPago){

        List<Pago> pagos = repository.findByFechaPago(fechaPago);   
            
        if (pagos.isEmpty()) {

            throw new NoSuchElementException(
                    "[ERROR] No se encontraron pagos en esa fecha [X_X] ...");

        }
        
        return pagos.stream()
                .map(this::convertirResponseDTO)
                .toList();
        
            
    }

    public PagoResponseDTO buscarPorIdPedido(Integer id){

        return repository.findByPedidoId(id)   
            .map(this::convertirResponseDTO)  
            .orElseThrow(() ->
                    new NoSuchElementException
                    ( "[ERROR] Pago de pedido no encontrado [X_X] ..."));
    }

    public String guardar(PagoRequestDTO pagoDTO){

        Optional<Pago> existente =
                repository.findByPedidoIdAndEstado(
                        pagoDTO.getPedidoId(),
                        EstadoPago.COMPLETADO
                );

        if(existente.isPresent()){

            throw new IllegalArgumentException(

                    "[ERROR] El pedido ya esta pagado [X_X] ...");

        }

        Pago pago = new Pago();

        pago.setPedidoId(pagoDTO.getPedidoId());

        pago.setMonto(pagoDTO.getMonto());

        pago.setMetodoPago(pagoDTO.getMetodoPago());

        pago.setFechaPago(LocalDate.now());

        pago.setEstado(EstadoPago.PENDIENTE);

        RestTemplate restTemplate = new RestTemplate();

        String url =
                "http://localhost:8085/pedidos/buscarId/"
                + pagoDTO.getPedidoId();

        try{

            Pedido pedido =
                    restTemplate.getForObject(url, Pedido.class);

            if(Double.compare(
                    pedido.getTotal(),
                    pagoDTO.getMonto()) != 0){

                pago.setEstado(EstadoPago.FALLIDO);

                repository.save(pago);

                throw new IllegalArgumentException(

                    "[ERROR] El monto ingresado no coincide con el total del pedido [X_X] ...");

            }

            repository.save(pago);

            String urlActualizar =

                    "http://localhost:8085/pedidos/"
                    + pagoDTO.getPedidoId()
                    + "/pagado";

            restTemplate.put(urlActualizar, null);

            pago.setEstado(EstadoPago.COMPLETADO);

            repository.save(pago);

            return "[+] Pago registrado correctamente ...";

        }

        catch (HttpClientErrorException.NotFound e) {

            throw new IllegalArgumentException(
        "[ERROR] El pedido ingresado es invalido o no existe [X_X] ...");

        }

        catch(IllegalArgumentException e){

            pago.setEstado(EstadoPago.FALLIDO);

            repository.save(pago);

            throw e;
        }

        catch(Exception e){

            pago.setEstado(EstadoPago.FALLIDO);

            repository.save(pago);

            throw new RuntimeException(
                    "[ERROR] Ocurrio un fallo inesperado al procesar el pago [X_X] ..."
            );
        }

    }

}
