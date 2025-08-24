package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.SolicitanteRequest;
import co.com.pragma.api.dto.SolicitanteResponse;
import co.com.pragma.model.solicitante.Solicitante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolicitanteMapper {
    Solicitante toDomain(SolicitanteRequest request);
    SolicitanteResponse toResponse(Solicitante domain);
}