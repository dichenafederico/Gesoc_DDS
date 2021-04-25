package domain.entidades;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Empresa implements Categoria {

    @Enumerated(EnumType.ORDINAL)
    public TipoEmpresa tipo;

    public Empresa() {
    }

}
